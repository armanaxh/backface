#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean dependency:copy-dependencies package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/my-app-1.0-SNAPSHOT.jar /usr/local/lib/my-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-cp","/usr/local/lib/my-app.jar", "com.mycompany.app.App"]
