FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "app.jar"]

# FROM eclipse-temurin:21-jdk-alpine as build
# WORKDIR /app
# COPY mvnw .
# COPY .mvn .mvn
# COPY pom.xml .
# COPY src src
# RUN ./mvnw clean package -DskipTests

# FROM eclipse-temurin:21-jre-alpine
# WORKDIR /app
# COPY --from=build /app/target/*.jar app.jar
# EXPOSE 9001
# ENTRYPOINT ["java", "-jar", "app.jar"]
