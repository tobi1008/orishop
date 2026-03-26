pipeline {
    agent {
        kubernetes {
            yaml '''
            apiVersion: v1
            kind: Pod
            spec:
              containers:
              # 1. Container dùng để nhào nặn Docker Image (Docker-in-Docker)
              - name: docker
                image: docker:24.0.5-dind
                securityContext:
                  privileged: true
                env:
                - name: DOCKER_TLS_CERTDIR
                  value: ""
              
              # 2. Container chứa vũ khí AWS CLI và kubectl để tấn công EKS
              - name: k8s-tools
                image: alpine/k8s:1.28.2
                command:
                - cat
                tty: true
            '''
        }
    }

    environment {
        // Lấy chìa khóa từ két sắt Jenkins
        DOCKER_CREDS = credentials('docker-hub-credentials')
        AWS_CREDS = credentials('aws-credentials')
        
        // Gắn Access Key vào biến môi trường chuẩn của AWS
        AWS_ACCESS_KEY_ID = "${AWS_CREDS_USR}"
        AWS_SECRET_ACCESS_KEY = "${AWS_CREDS_PSW}"
        AWS_DEFAULT_REGION = "ap-southeast-1"
        
        // Tên cụm EKS (Lát nữa Terraform tạo ra tên gì thì bạn sửa lại cho khớp nhé)
        EKS_CLUSTER_NAME = "quyenlt-eks-cluster" 
        IMAGE_NAME = "tobi1008/orishop:latest"
    }

    stages {
        stage('1. Kéo Code từ GitHub') {
            steps {
                checkout scm
                echo "Đã lấy mã nguồn com.quyenlt mới nhất về máy!"
            }
        }

        stage('2. Đóng gói & Đẩy Image (Docker Hub)') {
            steps {
                container('docker') {
                    // VPS của bạn thường là chip Intel/AMD, nên sẽ tự ra build chuẩn linux/amd64 cho EKS
                    sh "docker build -t ${IMAGE_NAME} ."
                    sh "echo ${DOCKER_CREDS_PSW} | docker login -u ${DOCKER_CREDS_USR} --password-stdin"
                    sh "docker push ${IMAGE_NAME}"
                    echo "Đã đẩy Image lên kho an toàn!"
                }
            }
        }

        stage('3. Triển khai lên AWS EKS') {
            steps {
                container('k8s-tools') {
                    // Đưa hộ chiếu AWS cho EKS xem để lấy quyền điều khiển (Kubeconfig)
                    sh "aws eks update-kubeconfig --region ${AWS_DEFAULT_REGION} --name ${EKS_CLUSTER_NAME}"
                    
                    // Ném file cấu hình K8s vào cụm EKS để chạy App
                    sh "kubectl apply -f orishop-eks.yaml"
                    
                    // Ép EKS phải kéo Image mới nhất về (Restart Pods)
                    sh "kubectl rollout restart deployment orishop-app"
                    echo "Triển khai lên AWS EKS thành công rực rỡ!"
                }
            }
        }
    }
}