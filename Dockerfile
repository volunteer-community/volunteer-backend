# base-image
FROM openjdk:11
# 변수 설정 (빌드 파일 경로)
ARG JAR_FILE=build/libs/Distribution_Test-0.0.1-SNAPSHOT.jar
# 환경 변수 설정
ENV AWS_ACCESS_KEY=${AWS_ACCESS_KEY} \
AWS_SECRET_KEY=${AWS_SECRET_KEY} \
AWS_BUCKET=${AWS_BUCKET} \
DATABASE_URL=${DATABASE_URL} \
DATABASE_USERNAME=${DATABASE_USERNAME} \
DATABASE_PASSWORD=${DATABASE_PASSWORD} \
DATABASE_NAME=${DATABASE_NAME}
# 빌드 파일 컨테이너로 복사
COPY ${JAR_FILE} app.jar
# jar 파일 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]