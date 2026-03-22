pipeline {
    agent any

    environment {
        // Tên tài khoản Docker Hub của bạn
        DOCKER_HUB_USER = "tobi1008" 
        // Tên image sẽ được tạo ra
        IMAGE_NAME = "${DOCKER_HUB_USER}/orishop" 
        // Tag version (dùng số build của Jenkins cho dễ quản lý)
        IMAGE_TAG = "v${env.BUILD_NUMBER}" 
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Đang tải mã nguồn từ GitHub...'
                // Điền đúng link repo GitHub của bạn vào đây
                git branch: 'main', url: 'https://github.com/tobi1008/orishop.git' 
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Đang đóng gói ứng dụng Spring Boot thành Docker Image...'
                script {
                    // Lệnh build sử dụng Dockerfile bạn vừa tạo
                    dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo 'Đang đẩy Image lên Docker Hub...'
                script {
                    // Sử dụng ID Credentials bạn vừa tạo ở Bước 1
                    docker.withRegistry('', 'docker-hub-credentials') {
                        dockerImage.push()
                        // Push thêm một tag 'latest' để dễ gọi
                        dockerImage.push('latest') 
                    }
                }
            }
        }
    }
}