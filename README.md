# Project 2 - JWT dùng Nimbus JOSE + JWT

Project này giữ cùng chức năng và luồng xử lý của Project 1, nhưng thay thư viện `io.jsonwebtoken` (JJWT) bằng `com.nimbusds:nimbus-jose-jwt`.

## Yêu cầu
- JDK 21
- Maven 3.9+
- MySQL 8+

## Database
Project mặc định dùng database `jwt_nimbus`:

```sql
CREATE DATABASE jwt_nimbus CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## Chạy project
Nếu MySQL root không có mật khẩu:

```bash
mvn spring-boot:run
```

Nếu có mật khẩu, trên PowerShell:

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
mvn spring-boot:run
```

Server: `http://localhost:8006`

## Test API

### 1. Đăng ký
POST `http://localhost:8006/auth/signup`

```json
{
  "fullName": "Nguyen Van A",
  "email": "a@gmail.com",
  "password": "123456"
}
```

### 2. Đăng nhập lấy JWT
POST `http://localhost:8006/auth/login`

```json
{
  "email": "a@gmail.com",
  "password": "123456"
}
```

### 3. Lấy user hiện tại
GET `http://localhost:8006/users/me`

```text
Authorization: Bearer <token>
```

### 4. Lấy danh sách user
GET `http://localhost:8006/users`

```text
Authorization: Bearer <token>
```

### 5. AJAX
- Login: `http://localhost:8006/login`
- Profile: `http://localhost:8006/user/profile`

## Nimbus thay JJWT ở đâu?
File `JwtService.java` dùng:
- `SignedJWT`
- `JWTClaimsSet`
- `MACSigner`
- `MACVerifier`
- `JWSAlgorithm.HS256`

Đây là phần thay cho `Jwts.builder()`, `Jwts.parser()` và `Keys.hmacShaKeyFor()` của Project 1.
