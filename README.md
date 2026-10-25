# Orishop

Website thương mại điện tử viết bằng Spring Boot, kèm toàn bộ hạ tầng CI/CD để deploy lên Kubernetes.

## Ứng dụng

- Java 17, Spring Boot 3.1 (Web, Security, Data JPA, Validation, Mail), Thymeleaf
- MySQL 8, migration bằng Flyway
- Trang khách hàng: sản phẩm, giỏ hàng, đặt hàng, đánh giá, thanh toán MoMo và VNPay
- Trang quản trị: sản phẩm, danh mục, đơn hàng, cài đặt website

## Hạ tầng và CI/CD

**K3s trên VPS:** `git push` lên `main` → GitHub Actions build Docker image → đẩy lên Docker Hub → SSH vào VPS chạy `kubectl apply` và `rollout restart`.

- App chạy 2 replica, dùng chung volume upload (PVC)
- MySQL chạy trong cụm với PVC riêng
- Traefik Ingress, SSL tự động bằng cert-manager + Let's Encrypt

**AWS** (bản thử nghiệm): Jenkins pipeline build image và deploy lên EKS, kèm cấu hình CodeBuild (`buildspec.yml.aws`); database dùng AWS RDS.

| File | Vai trò |
|---|---|
| `.github/workflows/deploy.yml` | Pipeline deploy lên K3s |
| `orishop-k8s.yaml` | Deployment, Service, Ingress, PVC của app |
| `orishop-mysql.yaml` | MySQL và PVC |
| `orishop-issuer.yaml` | ClusterIssuer Let's Encrypt |
| `Jenkinsfile.aws`, `orishop-eks.yaml.aws` | Pipeline Jenkins và manifest cho EKS |
| `DEPLOYMENT_GUIDE.md` | Hướng dẫn triển khai chi tiết |

## Chạy local

```bash
# Cần Java 17 và MySQL 8
export DB_URL="jdbc:mysql://localhost:3306/orishop?createDatabaseIfNotExist=true"
export DB_USER=root
export DB_PASS=your-password
./mvnw spring-boot:run
```
