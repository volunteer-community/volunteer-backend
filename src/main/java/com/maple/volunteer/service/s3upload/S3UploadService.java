package com.maple.volunteer.service.s3upload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.exception.UploadException;
import com.maple.volunteer.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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

    // MultipartFile을 전달 받 File 로 변환
    public List<String> upload(List<MultipartFile> multipartFile) {

        List<String> imgUrlList = new ArrayList<>();

        for (MultipartFile file : multipartFile) {
            String fileName = createFileName(file.getOriginalFilename());
            String fileContentType = file.getContentType();
            System.out.println(fileContentType);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(fileContentType);

            try (InputStream inputStream = file.getInputStream()){
                amazonS3.putObject(new PutObjectRequest(bucket + "/image", fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                imgUrlList.add(amazonS3.getUrl(bucket+"/image", fileName).toString());
            } catch (IOException e) {
                throw new UploadException(ErrorCode.IMAGE_UPLOAD_FAIL);
            }
        }
        return imgUrlList;
    }

    // 이미지 파일명 중복 방지
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 확인
    private String getFileExtension(String fileName) {

        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));

        if (!fileValidate.contains(idxFileName)) {
            throw new NotFoundException(ErrorCode.FILE_EXTENSION_NOT_FOUND);
        }

        return idxFileName;
    }
}
