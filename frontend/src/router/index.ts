import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: () => import('@/views/Home.vue') },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    { path: '/project/:id', component: () => import('@/views/ProjectDetail.vue') },
    {
      path: '/submit',
      component: () => import('@/views/Submit.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/profile',
      component: () => import('@/views/Profile.vue'),
      meta: { requiresAuth: true }
    }
  ],
  scrollBehavior: () => ({ top: 0 })
})

router.beforeEach(to => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.isLoggedIn) {
    return '/login'
  }
})

export default router
