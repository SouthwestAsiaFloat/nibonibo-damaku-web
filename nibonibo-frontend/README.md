# nibonibo-web

一个仿 Bilibili 风格的视频弹幕社区前端 MVP，当前只包含前端页面、mock 数据和模拟交互，不连接后端或数据库。

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

默认访问 Vite 输出的本地地址，例如 `http://localhost:5173`。

## 构建

```bash
npm run build
```

## 说明

- `src/api/request.ts` 已封装 Axios，`baseURL` 为 `http://localhost:8080/api`，并会自动携带 localStorage 中的 mock token。
- 登录和注册当前为 mock 成功，会把 token 与当前用户保存到 localStorage。
- 首页视频、详情、评论、弹幕、个人中心列表都来自本地 mock 数据。
- 上传功能只做 UI 与进度条模拟，不会真正上传文件。
