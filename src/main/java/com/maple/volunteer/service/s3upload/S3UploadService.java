package com.maple.volunteer.service.s3upload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.maple.volunteer.exception.BadRequestException;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Service
public class S3UploadService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    // 커뮤니티 이미지 업로드
    public List<String> communityUpload(List<MultipartFile> multipartFileList) {
        List<String> imgUrlList = new ArrayList<>();
        // 리스트에 있는 모든 파일에 대해
        for (MultipartFile file : multipartFileList) {
            // 이미지 업로드 후 URL을 리스트에 추가
            imgUrlList.add(uploadImage(file, "/image/community", 800, 600));
        }
        // 업로드된 모든 이미지의 URL 리스트를 반환
        return imgUrlList;
    }

    // 게시판 이미지 업로드
    public String posterUpload(MultipartFile multipartFile) {
        // 이미지 업로드 후 URL 반환
        return uploadImage(multipartFile, "/image/poster", 800, 600);
    }

    // 이미지 업로드
    private String uploadImage(MultipartFile file, String path, int targetWidth, int targetHeight) {
        // 파일 이름 생성 및 확장자 추출
        String fileName = createFileName(file.getOriginalFilename());
        String fileFormatName = getFileExtension(fileName).substring(1);

        // 이미지 리사이징 후 리사이징된 이미지를 Resource로 반환
        Resource resizedImage = resizeImage(fileName, fileFormatName, file, targetWidth, targetHeight);

        // S3에 업로드할 파일의 메타데이터 생성
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try {
            // 리사이징된 이미지의 크기와 MIME 타입을 메타데이터에 설정
            objectMetadata.setContentLength(resizedImage.contentLength());
            objectMetadata.setContentType(file.getContentType());
        } catch (IOException e) {
            // 이미지 크기 읽기 실패 시 예외 처리
            throw new BadRequestException(ErrorCode.IMAGE_SIZE_READ_FAIL);
        }

        try (InputStream inputStream = resizedImage.getInputStream()){
            // 리사이징된 이미지를 S3에 업로드하고 업로드된 이미지의 URL을 반환
            amazonS3.putObject(new PutObjectRequest(bucket + path, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            // 임시 파일 삭제
            if (resizedImage instanceof FileSystemResource) {
                Path tempFilePath = Paths.get(((FileSystemResource) resizedImage).getPath());
                deleteFileWithRetry(tempFilePath, 3);
            }

            return amazonS3.getUrl(bucket + path, fileName).toString();
        } catch (IOException e) {
            // 이미지 업로드 실패 시 예외 처리
            throw new BadRequestException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }

    // 이미지 파일명 중복 방지
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 확장자 유효성 확인
    private String getFileExtension(String fileName) {
        // 파일 확장자 추출 후 소문자로 변환
        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        // 확장자 유효성 검사
        if (!List.of(".jpg", ".jpeg", ".png").contains(extension)) {
            throw new NotFoundException(ErrorCode.FILE_EXTENSION_NOT_FOUND);
        }
        return extension;
    }

    // 이미지 리사이징
    private Resource resizeImage(String fileName, String fileFormatName, MultipartFile multipartFile, int targetWidth, int targetHeight) {
        try {
            // MultipartFile -> BufferedImage로 변환
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            int originWidth = image.getWidth();
            int originHeight = image.getHeight();

            // 원본 이미지가 리사이징될 사이즈보다 작을 경우 원본 파일 반환
            if (originWidth < targetWidth && originHeight < targetHeight)
                return multipartFile.getResource();

            // 이미지 리사이징
            BufferedImage resized = Thumbnails.of(image)
                    .size(targetWidth, targetHeight)
                    .asBufferedImage();

            // 임시 파일을 생성 후 리사이징된 이미지를 저장
            Path tempFile = Files.createTempFile(fileName, "." + fileFormatName);
            ImageIO.write(resized, fileFormatName, tempFile.toFile());

            // 임시 파일을 이용하여 FileSystemResource 인스턴스를 생성 후 반환
            return new FileSystemResource(tempFile.toFile());

        } catch (IOException e) {
            // 파일 리사이징 실패시 예외 처리
            throw new BadRequestException(ErrorCode.IMAGE_RESIZING_FAIL);
        }
    }

    // 임시 파일 삭제
    private void deleteFileWithRetry(Path filePath, int maxRetryCount) throws IOException {
        int retryCount = 0;
        while(retryCount++ < maxRetryCount) {
            boolean isDeleted = Files.deleteIfExists(filePath);
            if (isDeleted || !Files.exists(filePath)) {
                return;  // 파일이 성공적으로 삭제되었거나, 파일이 존재하지 않는 경우
            }
        }
        throw new BadRequestException(ErrorCode.FILE_DELETE_FAIL);  // 파일 삭제 실패
    }

    // 이미지 삭제
    public void deleteFile(String imageUrl) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, imageUrl));
    }
}
