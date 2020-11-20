#https://hub.docker.com/r/adoptopenjdk/openjdk8
FROM adoptopenjdk/openjdk8:x86_64-alpine-jdk8u272-b10

EXPOSE 8080

#add useful utils case need exec into running container
RUN apk add --no-cache curl netcat-openbsd nss bash bind-tools

#drop the springboot jar and version file into image
COPY build/libs/*.jar app.jar

#run with optimised java8 settings
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:MinRAMPercentage=50.0", "-XX:MaxRAMPercentage=80.0", "-jar", "app.jar"]