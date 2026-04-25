# nibonibo-web

一个仿 Bilibili 风格的视频弹幕社区前端 MVP，当前已切到 `nibonibo-backend` 后端接口进行联调。

## 技术栈

- Vue 3
- Vite
- TypeScript
- Element Plus
- Axios
- Pinia
- Vue Router

## 页面

- `/` 首页视频网格流
- `/login` 登录页
- `/register` 注册页
- `/video/:id` 视频详情页
- `/upload` 视频上传页
- `/profile` 个人中心页

## 启动

```bash
npm install
npm run dev
```

默认访问 Vite 输出的本地地址，例如 `http://localhost:5173`。开发环境 API 地址在 `.env.development` 中配置：

```bash
VITE_API_BASE_URL=http://localhost:8080/api
```

## 构建

```bash
npm run build
```

## 说明

- `src/api/request.ts` 已封装 Axios，会自动携带 localStorage 中的 JWT token。
- 登录和注册会调用后端接口，并把 token 与当前用户保存到 localStorage。
- 首页视频、视频详情、评论、弹幕、个人中心投稿/收藏列表都来自后端接口。
- 上传功能会先调用后端 MinIO 上传接口，再调用视频发布接口创建记录。
