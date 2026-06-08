<template>
  <nav class="navbar">
    <div class="container nav-inner">
      <RouterLink to="/" class="logo">
        <span class="logo-icon">⚡</span>
        <span class="logo-text">AI<span class="accent">Share</span></span>
      </RouterLink>

      <div class="nav-links">
        <RouterLink to="/" class="nav-link">发现</RouterLink>
        <RouterLink v-if="auth.isLoggedIn" to="/submit" class="btn btn-primary">
          + 发布项目
        </RouterLink>
        <template v-if="auth.isLoggedIn">
          <RouterLink to="/profile" class="avatar-btn">
            <img v-if="auth.user?.avatar" :src="auth.user.avatar" alt="" />
            <span v-else class="avatar-placeholder">{{ auth.user?.username?.[0]?.toUpperCase() }}</span>
          </RouterLink>
          <button class="btn btn-ghost" @click="handleLogout">退出</button>
        </template>
        <template v-else>
          <RouterLink to="/login" class="nav-link">登录</RouterLink>
          <RouterLink to="/register" class="btn btn-primary">注册</RouterLink>
        </template>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

function handleLogout() {
  auth.logout()
  router.push('/')
}
</script>

<style scoped>
.navbar {
  position: sticky;
  top: 0;
  z-index: 100;
  height: 64px;
  border-bottom: 1px solid var(--border);
  background: rgba(10, 10, 15, 0.85);
  backdrop-filter: blur(12px);
}

.nav-inner {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: 700;
  letter-spacing: -0.5px;
}

.logo-icon { font-size: 22px; }
.accent { color: var(--accent-light); }

.nav-links {
  display: flex;
  align-items: center;
  gap: 16px;
}

.nav-link {
  color: var(--text-muted);
  font-size: 14px;
  transition: color 0.2s;
}
.nav-link:hover, .nav-link.router-link-active { color: var(--text); }

.avatar-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-card);
  border: 1px solid var(--border);
  cursor: pointer;
}

.avatar-btn img { width: 100%; height: 100%; object-fit: cover; }

.avatar-placeholder {
  font-size: 14px;
  font-weight: 600;
  color: var(--accent-light);
}
</style>
