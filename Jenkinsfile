pipeline {
    agent {
        kubernetes {
            yaml '''
            apiVersion: v1
            kind: Pod
            spec:
              containers:
              - name: docker-cli
                image: docker:24.0.5
                command: ["cat"]
                tty: true
              - name: dind
                image: docker:24.0.5-dind
                securityContext:
                  privileged: true
                env:
                - name: DOCKER_TLS_CERTDIR
                  value: ""
              - name: k8s-tools
                image: alpine/k8s:1.28.2
                command: ["cat"]
                tty: true
            '''
        }
    }

    environment {
        DOCKER_HOST = 'tcp://localhost:2375'
        IMAGE_NAME = "tobi1008/orishop:latest"
        DOCKER_CREDS = credentials('docker-hub-credentials')
        AWS_CREDS = credentials('aws-credentials')
        AWS_REGION = "ap-southeast-1"
        EKS_CLUSTER_NAME = "quyenlt-eks-cluster"
    }

    stages {
        stage('1. Kéo mã nguồn') {
            steps {
                checkout scm
                echo "--- Đã lấy mã nguồn com.quyenlt mới nhất ---"
            }
        }

        stage('2. Đóng gói & Đẩy Image') {
            steps {
                container('docker-cli') {
                    script {
                        echo "--- Đang đợi Docker Daemon nổ máy ---"
                        timeout(time: 2, unit: 'MINUTES') {
                            until {
                                try {
                                    sh "docker version"
                                    return true
                                } catch (Exception e) {
                                    sleep 5
                                    return false
                                }
                            }
                        }
                        sh "docker build -t ${IMAGE_NAME} ."
                        sh "echo ${DOCKER_CREDS_PSW} | docker login -u ${DOCKER_CREDS_USR} --password-stdin"
                        sh "docker push ${IMAGE_NAME}"
                    }
                }
            }
        }

        stage('3. Triển khai lên AWS EKS') {
            steps {
                container('k8s-tools') {
                    withEnv(["AWS_ACCESS_KEY_ID=${AWS_CREDS_USR}", "AWS_SECRET_ACCESS_KEY=${AWS_CREDS_PSW}"]) {
                        sh "aws eks update-kubeconfig --region ${AWS_REGION} --name ${EKS_CLUSTER_NAME}"
                        sh "kubectl apply -f orishop-eks.yaml"
                        sh "kubectl get pods"
                    }
                }
            }
        }
    }
}