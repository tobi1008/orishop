pipeline {
    agent any

    environment {
        DOCKER_HUB_USER = "tobi1008" 
        IMAGE_NAME = "${DOCKER_HUB_USER}/orishop" 
        IMAGE_TAG = "v${env.BUILD_NUMBER}" 
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Đang tải mã nguồn từ GitHub...'
                git branch: 'main', url: 'https://github.com/tobi1008/orishop.git' 
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Đang đóng gói ứng dụng...'
                script {
                    dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo 'Đang đẩy Image lên kho chứa...'
                script {
                    docker.withRegistry('', 'docker-hub-credentials') {
                        dockerImage.push()
                        dockerImage.push('latest') 
                    }
                }
            }
        }
        
        // --- ĐÂY LÀ PHẦN MỚI THÊM VÀO ---
        stage('Deploy to Kubernetes') {
            steps {
                echo 'Đang ra lệnh cho cụm K8s triển khai ứng dụng...'
                // Gọi file Secret có ID là k8s-kubeconfig mà bạn vừa nạp
                withCredentials([file(credentialsId: 'k8s-kubeconfig', variable: 'KUBECONFIG')]) {
                    sh '''
                    # Trỏ đường dẫn KUBECONFIG và chạy lệnh apply
                    export KUBECONFIG=$KUBECONFIG
                    
                    # Ép K8s cập nhật image mới nhất
                    kubectl apply -f orishop-k8s.yaml
                    kubectl rollout restart deployment/orishop-deployment
                    '''
                }
            }
        }
    }
}