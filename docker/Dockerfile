FROM gradle:8-jdk17 AS build
COPY src /home/gradle/src
COPY build.gradle /home/gradle
RUN gradle test bootJar

FROM openjdk:17 as composer
ARG JAR_FILE=/home/gradle/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:17
RUN groupadd -r spring && adduser -r spring -g spring
USER spring:spring
COPY --from=composer dependencies/ /app
COPY --from=composer spring-boot-loader/ /app
COPY --from=composer application/ /app
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher ${0} ${@}"]
