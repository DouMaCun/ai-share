<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="auth-logo">⚡ AI<span>Share</span></div>
      <h2>创建账号</h2>
      <p class="auth-sub">加入开发者社区，分享你的 AI 项目</p>

      <form @submit.prevent="handleRegister" class="auth-form">
        <div class="form-item">
          <label>用户名</label>
          <input v-model="form.username" type="text" placeholder="2-20个字符" required minlength="2" maxlength="20" />
        </div>
        <div class="form-item">
          <label>邮箱</label>
          <input v-model="form.email" type="email" placeholder="your@email.com" required />
        </div>
        <div class="form-item">
          <label>密码</label>
          <input v-model="form.password" type="password" placeholder="至少6位" required minlength="6" />
        </div>
        <p v-if="error" class="error">{{ error }}</p>
        <button type="submit" class="btn btn-primary submit-btn" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>

      <p class="auth-footer">
        已有账号？<RouterLink to="/login">立即登录</RouterLink>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()
const loading = ref(false)
const error = ref('')

const form = reactive({ username: '', email: '', password: '' })

async function handleRegister() {
  loading.value = true
  error.value = ''
  try {
    await auth.register(form.username, form.email, form.password)
    router.push('/')
  } catch (e: any) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.auth-card {
  width: 100%;
  max-width: 400px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 40px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.auth-logo { font-size: 20px; font-weight: 700; }
.auth-logo span { color: var(--accent-light); }

h2 { font-size: 24px; font-weight: 700; }
.auth-sub { color: var(--text-muted); font-size: 14px; }

.auth-form { display: flex; flex-direction: column; gap: 16px; }

.form-item { display: flex; flex-direction: column; gap: 6px; }
.form-item label { font-size: 13px; color: var(--text-muted); font-weight: 500; }

.form-item input {
  padding: 10px 14px;
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  color: var(--text);
  font-size: 14px;
  transition: border-color 0.2s;
}
.form-item input:focus { border-color: var(--accent); }
.form-item input::placeholder { color: var(--text-muted); }

.error { font-size: 13px; color: var(--danger); }

.submit-btn { width: 100%; justify-content: center; padding: 12px; }
.submit-btn:disabled { opacity: 0.6; cursor: not-allowed; }

.auth-footer { font-size: 13px; color: var(--text-muted); text-align: center; }
.auth-footer a { color: var(--accent-light); }
</style>
