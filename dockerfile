FROM openjdk:11.0.8-slim-buster
COPY . /usr/src/user_service
WORKDIR /usr/src/user_service
CMD ["./gradlew", "bootRun"]