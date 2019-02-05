ARG VERSION=8u151

FROM openjdk:${VERSION}-jdk

COPY . /src
WORKDIR /src
RUN ./gradlew

CMD ["java","-jar","./build/libs/com.fetcher-0.0.1.jar"]