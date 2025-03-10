FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY release/*.jar app.jar  

CMD ["java", "-jar", "app.jar"]
