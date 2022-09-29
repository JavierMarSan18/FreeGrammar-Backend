ARG APP_NAME=FreeGrammar-Backend

FROM openjdk:17-jdk-alpine as builder

WORKDIR /$APP_NAME

COPY ./.mvn ./.mvn
COPY ./mvnw ./mvnw
COPY ./pom.xml ./pom.xml


RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /$APP_NAME

COPY --from=builder ./target/freegrammar-0.0.1.jar .

EXPOSE 8001

ENTRYPOINT ["java","-jar","freegrammar-0.0.1.jar"]