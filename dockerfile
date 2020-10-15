FROM openjdk:11.0.8-slim-buster
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
CMD ["./gradlew", "bootRun"]