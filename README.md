# AI Share — AI 项目分享平台

<div align="center">

⚡ **发现、分享、连接——AI 时代开发者的项目社区**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-6DB33F?logo=springboot)](https://spring.io)
[![Vue](https://img.shields.io/badge/Vue-3.x-4FC08D?logo=vuedotjs)](https://vuejs.org)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql)](https://www.mysql.com)
[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)

</div>

---

> **AI Share** 是一个面向 AI 开发者的项目分享社区。在这里，你可以展示自己的 AI 作品、发现优秀的开源项目、与其他开发者交流讨论。极简深色 UI，流畅交互，全栈分离架构。

### ✨ 亮点

- 🎨 **极简深色美学** — 毛玻璃导航、暗黑系配色，专注阅读体验
- ⚡ **本地文件上传** — 拖拽上传封面图，实时预览，零门槛发布
- 💬 **评论互动** — 项目下方直接讨论，支持即时删除
- ❤️ **点赞 & 收藏** — 双维度互动，发现热门项目
- 🏷️ **标签系统** — 10 大类 AI 标签，精准分类检索
- 🔐 **JWT 鉴权** — 注册/登录，接口粒度的权限控制
- 📱 **响应式布局** — 桌面/移动端自适应

### 🛠 技术架构

```
Vue 3 + TypeScript         Spring Boot 3
    Vite 5       ←→    MyBatis Plus 3.5
   Pinia                   MySQL 8 + JJWT
   前端 :5173              后端 :60001
```

### 📦 功能一览

| 模块 | 功能 |
|------|------|
| 用户 | 注册、登录、JWT 认证、个人资料 |
| 项目 | 发布、编辑、删除、搜索、分页、标签过滤、草稿/发布 |
| 互动 | 点赞/取消点赞、收藏/取消收藏、评论、删除评论 |
| 上传 | 图片上传（拖拽 + 点击）、格式校验、本地存储 |

## 项目结构

```
ai-share/
├── backend/          # Spring Boot 3 + MyBatis Plus
└── frontend/         # Vue 3 + Vite + Pinia
```

## 快速启动

### 1. 初始化数据库

在 MySQL 中创建数据库并执行建表脚本：

```sql
CREATE DATABASE IF NOT EXISTS `alspd-ai` DEFAULT CHARSET utf8mb4;
USE `alspd-ai`;
source backend/src/main/resources/schema.sql;
```

### 2. 配置数据库连接

编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/alspd-ai?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: 你的密码
```

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
# 运行在 http://localhost:60001
```

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
# 运行在 http://localhost:5173
```

## API 文档

### 认证

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| POST | /api/auth/register | 注册 | 否 |
| POST | /api/auth/login | 登录 | 否 |

### 项目

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | /api/projects | 项目列表（搜索/分页/标签/排序） | 否 |
| GET | /api/projects/:id | 项目详情 | 否 |
| POST | /api/projects | 发布项目 | 是 |
| PUT | /api/projects/:id | 更新项目 | 是 |
| DELETE | /api/projects/:id | 删除项目 | 是 |
| GET | /api/projects/mine | 我的项目 | 是 |

### 点赞 & 收藏

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| POST | /api/projects/:id/like | 点赞/取消点赞 | 是 |
| POST | /api/projects/:id/favorite | 收藏/取消收藏 | 是 |

### 评论

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | /api/projects/:id/comments | 评论列表 | 否 |
| POST | /api/projects/:id/comments | 发表评论 | 是 |
| DELETE | /api/comments/:id | 删除评论（本人） | 是 |

### 标签 & 用户 & 上传

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | /api/tags | 所有标签 | 否 |
| GET | /api/user/me | 当前用户信息 | 是 |
| PUT | /api/user/me | 更新个人资料 | 是 |
| POST | /api/upload/image | 上传图片（封面等） | 是 |

## 技术栈

**后端**
- Spring Boot 3.3
- MyBatis Plus 3.5
- JWT（jjwt 0.12）
- MySQL 8
- BCrypt 密码加密

**前端**
- Vue 3 + TypeScript
- Vite 5
- Pinia（状态管理）
- Vue Router 4
- Axios
