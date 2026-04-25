# nibonibo-backend

Spring Boot MVC 单体后端 MVP 骨架，包名为 `com.nibonibo.backend`。

## 技术栈

- Java 17
- Spring Boot 3.x
- Maven
- MyBatis-Plus
- MySQL 8
- Redis
- JWT
- MinIO
- RabbitMQ
- WebSocket
- Lombok
- Knife4j / Swagger

## 启动步骤

1. 创建 MySQL 数据库并执行 `src/main/resources/sql/init.sql`。
2. 准备 Redis、MinIO、RabbitMQ，本地默认配置见 `src/main/resources/application.yml`。
3. 按需通过环境变量覆盖 MySQL、Redis、MinIO、RabbitMQ、JWT 配置。
4. 启动服务：

```bash
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

## API 文档

服务启动后访问：

- Knife4j: `http://localhost:8080/doc.html`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## 当前说明

- 登录注册已可生成 JWT，密码使用 BCrypt 加密。
- `/api/**` 默认需要 `Authorization: Bearer <token>`，登录、注册和文档资源除外。
- 上传接口会直接写入 MinIO。
- WebSocket 弹幕入口为 `/ws/danmaku/{videoId}`，当前只做房间广播，暂不做复杂鉴权。
- RabbitMQ 已创建播放量、消息通知、视频转码三个队列，并提供示例 Producer。当前 Producer 发送失败只记录日志，不会阻断视频详情或投稿主流程，方便本地逐步启动依赖。
- 前端联调默认读取 `http://localhost:8080/api`，对应前端配置文件为 `nibonibo-frontend/.env.development`。
