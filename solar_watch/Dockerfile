# Dockerfile
#FROM eclipse-temurin:17-jre-alpine
#WORKDIR /tmp
#COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","app.jar"]

FROM maven:3.8.1-openjdk-17 as build

WORKDIR /app
COPY . .
RUN mvn clean package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/solar_watch-0.0.1-SNAPSHOT.jar /app/solar_watch.jar
EXPOSE 8080
CMD ["java", "-jar", "solar_watch.jar"]