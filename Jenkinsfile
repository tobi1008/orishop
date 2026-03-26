pipeline {
    agent {
        kubernetes {
            yaml '''
            apiVersion: v1
            kind: Pod
            spec:
              containers:
              # 1. Container Đặc vụ: Dùng để gõ lệnh Docker (Build & Push)
              - name: docker-cli
                image: docker:24.0.5
                command: ["cat"]
                tty: true
              
              # 2. Container Động cơ (Sidecar): Chạy ngầm Docker Daemon
              - name: dind
                image: docker:24.0.5-dind
                securityContext:
                  privileged: true
                env:
                - name: DOCKER_TLS_CERTDIR
                  value: ""
              
              # 3. Container Công cụ: Dùng để tương tác với AWS và EKS
              - name: k8s-tools
                image: alpine/k8s:1.28.2
                command: ["cat"]
                tty: true
            '''
        }
    }

    environment {
        // Cấu hình quan trọng: Trỏ Docker CLI sang con Sidecar chạy ở localhost
        DOCKER_HOST = 'tcp://localhost:2375'
        
        // Thông tin hình ảnh và tài khoản
        IMAGE_NAME = "tobi1008/orishop:latest"
        DOCKER_CREDS = credentials('docker-hub-credentials')
        AWS_CREDS = credentials('aws-credentials')
        
        // Thông tin hạ tầng AWS
        AWS_REGION = "ap-southeast-1"
        EKS_CLUSTER_NAME = "quyenlt-eks-cluster"
    }

    stages {
        stage('1. Kéo mã nguồn') {
            steps {
                checkout scm
                echo "--- Đã lấy mã nguồn com.quyenlt mới nhất về máy ---"
            }
        }

        stage('2. Đóng gói & Đẩy Image (Docker Hub)') {
            steps {
                container('docker-cli') {
                    script {
                        echo "--- Bắt đầu Build Docker Image ---"
                        // Sử dụng nháy kép "" để Jenkins nhận diện biến môi trường
                        sh "docker build -t ${IMAGE_NAME} ."
                        
                        echo "--- Đăng nhập vào Docker Hub ---"
                        sh "echo ${DOCKER_CREDS_PSW} | docker login -u ${DOCKER_CREDS_USR} --password-stdin"
                        
                        echo "--- Đang đẩy Image lên kho tobi1008 ---"
                        sh "docker push ${IMAGE_NAME}"
                        
                        echo "--- Đã đẩy Image lên kho an toàn! ---"
                    }
                }
            }
        }

        stage('3. Triển khai lên AWS EKS') {
            steps {
                container('k8s-tools') {
                    script {
                        echo "--- Đang kết nối tới cụm EKS: ${EKS_CLUSTER_NAME} ---"
                        
                        withEnv(["AWS_ACCESS_KEY_ID=${AWS_CREDS_USR}", "AWS_SECRET_ACCESS_KEY=${AWS_CREDS_PSW}"]) {
                            // Cập nhật cấu hình kubeconfig để nhận diện cụm EKS
                            sh "aws eks update-kubeconfig --region ${AWS_REGION} --name ${EKS_CLUSTER_NAME}"
                            
                            echo "--- Đang nạp file cấu hình orishop-eks.yaml vào hệ thống ---"
                            // Thực thi file YAML đã chỉnh sửa cổng 8091 và RDS Endpoint
                            sh "kubectl apply -f orishop-eks.yaml"
                            
                            echo "--- Kiểm tra trạng thái các Pod ---"
                            sh "kubectl get pods"
                            
                            echo "--- Triển khai lên AWS EKS thành công rực rỡ! ---"
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Chúc mừng Tobi! Hệ thống OriShop đã lên sóng Cloud-Native an toàn."
        }
        failure {
            echo "Có biến rồi Tobi ơi! Kiểm tra lại Log ở trên để bắt bệnh nhé."
        }
    }
}