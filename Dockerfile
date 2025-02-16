
#Chỉ định image nền là OpenJDK 17 (phiên bản Java 17).
#Image này cung cấp môi trường chạy ứng dụng Java bên trong container.
FROM openjdk:17

# Định nghĩa một biến môi trường chỉ dùng trong build-time của Docker.
ARG FILE_JAR=target/*.jar  

# Sao chép file .jar từ thư mục target/ vào container và đặt tên là api-service.jar
ADD ${FILE_JAR} api-service.jar

# Chỉ định lệnh chạy chính của container khi khởi động.
ENTRYPOINT ["java", "-jar", "api-service.jar"]

# Chỉ định cổng để truy xuất vào
EXPOSE 8080







