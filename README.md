# Project 1 - JWT theo bài giảng (JJWT 0.12.6)

Project này bám theo ví dụ trong bài giảng: Entity -> Models/DTO -> Repository/Service -> JWT Service -> Filter -> SecurityConfig -> Controller -> Test.

Thư viện JWT sử dụng đúng bộ dependency trong slide: `jjwt-api`, `jjwt-impl`, `jjwt-jackson` phiên bản `0.12.6`.

## Yêu cầu
- JDK 21
- Maven 3.9+
- MySQL 8+

## Database
Project mặc định dùng database `jwt_springboot3`. Có thể tạo trước:

```sql
CREATE DATABASE jwt_springboot3 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
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

Server: `http://localhost:8005`

## Test API

### 1. Đăng ký
POST `http://localhost:8005/auth/signup`

```json
{
  "fullName": "Nguyen Van A",
  "email": "a@gmail.com",
  "password": "123456"
}
```

### 2. Đăng nhập lấy JWT
POST `http://localhost:8005/auth/login`

```json
{
  "email": "a@gmail.com",
  "password": "123456"
}
```

### 3. Lấy user hiện tại
GET `http://localhost:8005/users/me`

Header:
```text
Authorization: Bearer <token>
```

### 4. Lấy danh sách user
GET `http://localhost:8005/users`

Header:
```text
Authorization: Bearer <token>
```

### 5. AJAX
- Login: `http://localhost:8005/login`
- Profile: `http://localhost:8005/user/profile`
- Token được lưu ở `localStorage` và gửi lại bằng `Authorization: Bearer <token>`.
