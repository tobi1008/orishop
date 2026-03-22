# Sử dụng image Maven có sẵn Java 17 để build code
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build ra file .jar và bỏ qua test để chạy cho nhanh
RUN mvn clean package -DskipTests

# Sang giai đoạn 2: Tạo image chạy thực tế siêu nhẹ
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy file .jar từ bước builder sang
COPY --from=builder /app/target/*.jar app.jar
# Mở port mặc định của Spring Boot
EXPOSE 8080
# Lệnh khởi động app
ENTRYPOINT ["java", "-jar", "app.jar"]
