# base-image
FROM openjdk:11
# 변수 설정 (빌드 파일 경로)
ARG JAR_FILE=build/libs/Volunteer-0.0.1-SNAPSHOT.jar
# 환경 변수 설정
ENV AWS_ACCESS_KEY=${AWS_ACCESS_KEY} \
AWS_SECRET_KEY=${AWS_SECRET_KEY} \
AWS_BUCKET=${AWS_BUCKET} \
DATABASE_URL=${DATABASE_URL} \
DATABASE_USERNAME=${DATABASE_USERNAME} \
DATABASE_PASSWORD=${DATABASE_PASSWORD} \
DATABASE_NAME=${DATABASE_NAME} \
GOOGLE_ID=${GOOGLE_ID} \
GOOGLE_SECRET=${GOOGLE_SECRET} \
GOOGLE_URL=${GOOGLE_URL} \
NAVER_ID=${NAVER_ID} \
NAVER_SECRET=${NAVER_SECRET} \
NAVER_URL=${NAVER_URL} \
KAKAO_ID=${KAKAO_ID} \
KAKAO_URL=${KAKAO_URL} \
JWT_SECRET=${JWT_SECRET}
# 빌드 파일 컨테이너로 복사
COPY ${JAR_FILE} app.jar
# jar 파일 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]
