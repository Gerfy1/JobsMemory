FROM maven:3.9.7-amazoncorretto-17 AS build

WORKDIR /app

COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080

CMD ["sh", "-c", "java -jar app.jar"]
