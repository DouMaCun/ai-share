# AI Share — AI 项目分享平台

## 项目结构

```
ai-share/
├── backend/          # Spring Boot 3 + MyBatis Plus
└── frontend/         # Vue 3 + Vite + Pinia
```

## 快速启动

### 1. 初始化数据库

```sql
-- 在 MySQL 中执行
source backend/src/main/resources/schema.sql
```

### 2. 配置数据库连接

编辑 `backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_share?...
    username: root
    password: 你的密码
```

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
# 运行在 http://localhost:8080
```

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
# 运行在 http://localhost:5173
```

## API 文档

| 方法 | 路径 | 说明 | 是否需要登录 |
|------|------|------|------------|
| POST | /api/auth/register | 注册 | 否 |
| POST | /api/auth/login | 登录 | 否 |
| GET | /api/projects | 项目列表（支持搜索/分页/标签/排序） | 否 |
| GET | /api/projects/:id | 项目详情 | 否 |
| POST | /api/projects | 发布项目 | 是 |
| PUT | /api/projects/:id | 更新项目 | 是 |
| DELETE | /api/projects/:id | 删除项目 | 是 |
| GET | /api/projects/mine | 我的项目 | 是 |
| POST | /api/projects/:id/like | 点赞/取消点赞 | 是 |
| POST | /api/projects/:id/favorite | 收藏/取消收藏 | 是 |
| GET | /api/tags | 所有标签 | 否 |
| GET | /api/user/me | 当前用户信息 | 是 |
| PUT | /api/user/me | 更新个人资料 | 是 |

## 技术栈

**后端**
- Spring Boot 3.3
- MyBatis Plus 3.5
- JWT（jjwt 0.12）
- MySQL 8

**前端**
- Vue 3 + TypeScript
- Vite 5
- Pinia（状态管理）
- Vue Router 4
- Axios
