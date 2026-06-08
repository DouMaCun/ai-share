import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref<any>(JSON.parse(localStorage.getItem('user') || 'null'))

  const isLoggedIn = computed(() => !!token.value)

  async function login(account: string, password: string) {
    const res: any = await authApi.login({ account, password })
    if (!res?.data || !res.data.token) {
      throw new Error(res?.message || '登录失败')
    }
    setAuth(res.data)
  }

  async function register(username: string, email: string, password: string) {
    const res: any = await authApi.register({ username, email, password })
    if (!res?.data || !res.data.token) {
      throw new Error(res?.message || '注册失败')
    }
    setAuth(res.data)
  }

  function setAuth(data: any) {
    token.value = data.token
    user.value = data.user
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(data.user))
  }

  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  return { token, user, isLoggedIn, login, register, logout }
})
