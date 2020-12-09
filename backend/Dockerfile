FROM openjdk:11.0.8-jre-slim-buster
LABEL maintainer = a.jabrov753@gmail.com
ARG BACKEND_VERSION="1.0-SNAPSHOT"
ADD  /build/libs/backend-${BACKEND_VERSION}.jar /app/
WORKDIR /app/
CMD ["java", "-jar", "/app/backend-${BACKEND_VERSION}.jar"]
EXPOSE 7000