# Sử dụng kho Public ECR của AWS thay vì Docker Hub để né Rate Limit
FROM public.ecr.aws/docker/library/maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build ra file .jar và bỏ qua test để chạy cho nhanh
RUN mvn clean package -DskipTests

# Sang giai đoạn 2: Tạo image chạy thực tế siêu nhẹ (Cũng qua AWS Public ECR)
FROM public.ecr.aws/docker/library/eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy file .jar từ bước builder sang
COPY --from=builder /app/target/*.jar app.jar
# Mở port mặc định của Spring Boot
EXPOSE 8091
# Lệnh khởi động app
ENTRYPOINT ["java", "-jar", "app.jar"]