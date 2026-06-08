<template>
  <div class="profile-page">
    <div class="container">
      <div class="profile-header">
        <div class="profile-avatar">{{ auth.user?.username?.[0]?.toUpperCase() }}</div>
        <div class="profile-info">
          <h1>{{ auth.user?.username }}</h1>
          <p class="profile-email">{{ auth.user?.email }}</p>
          <p class="profile-bio">{{ auth.user?.bio || '还没有简介' }}</p>
        </div>
        <button class="btn btn-ghost" @click="showEdit = true">编辑资料</button>
      </div>

      <div class="section-title">我的项目</div>

      <div v-if="loading" class="loading-wrap"><div class="spinner"></div></div>

      <div v-else-if="projects.length" class="project-grid">
        <div v-for="p in projects" :key="p.id" class="project-item">
          <ProjectCard :project="p" />
          <div class="item-actions">
            <RouterLink :to="`/submit?edit=${p.id}`" class="btn btn-ghost btn-sm">编辑</RouterLink>
            <button class="btn btn-sm danger" @click="confirmDelete(p)">删除</button>
          </div>
        </div>
      </div>

      <div v-else class="empty">
        <div class="empty-icon">🚀</div>
        <p>还没有发布项目，<RouterLink to="/submit">去发布第一个</RouterLink></p>
      </div>
    </div>

    <!-- Edit Profile Modal -->
    <div v-if="showEdit" class="modal-overlay" @click.self="showEdit = false">
      <div class="modal">
        <h3>编辑资料</h3>
        <div class="form-item">
          <label>头像 URL</label>
          <input v-model="editForm.avatar" type="url" placeholder="https://..." />
        </div>
        <div class="form-item">
          <label>个人简介</label>
          <textarea v-model="editForm.bio" rows="3" placeholder="介绍一下自己..." maxlength="300"></textarea>
        </div>
        <div class="modal-actions">
          <button class="btn btn-ghost" @click="showEdit = false">取消</button>
          <button class="btn btn-primary" @click="saveProfile">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { projectApi, userApi } from '@/api'
import { useAuthStore } from '@/stores/auth'
import ProjectCard from '@/components/ProjectCard.vue'

const auth = useAuthStore()
const projects = ref<any[]>([])
const loading = ref(true)
const showEdit = ref(false)

const editForm = reactive({
  avatar: auth.user?.avatar || '',
  bio: auth.user?.bio || ''
})

async function loadProjects() {
  const res: any = await projectApi.mine()
  projects.value = res.data
  loading.value = false
}

async function confirmDelete(p: any) {
  if (!confirm(`确认删除「${p.title}」？`)) return
  await projectApi.remove(p.id)
  projects.value = projects.value.filter(x => x.id !== p.id)
}

async function saveProfile() {
  await userApi.updateProfile(editForm)
  auth.user.avatar = editForm.avatar
  auth.user.bio = editForm.bio
  localStorage.setItem('user', JSON.stringify(auth.user))
  showEdit.value = false
}

onMounted(loadProjects)
</script>

<style scoped>
.profile-page { padding: 40px 0 80px; }

.profile-header {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 48px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 32px;
}

.profile-avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--accent), var(--accent-light));
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  font-weight: 700;
  color: white;
  flex-shrink: 0;
}

.profile-info { flex: 1; }
.profile-info h1 { font-size: 22px; font-weight: 700; margin-bottom: 4px; }
.profile-email { font-size: 13px; color: var(--text-muted); margin-bottom: 6px; }
.profile-bio { font-size: 14px; color: var(--text-muted); }

.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border);
}

.project-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.project-item { display: flex; flex-direction: column; gap: 8px; }

.item-actions { display: flex; gap: 8px; }

.btn-sm { padding: 6px 12px; font-size: 12px; }

.danger {
  background: transparent;
  border: 1px solid var(--border);
  color: var(--danger);
  border-radius: var(--radius-sm);
}
.danger:hover { border-color: var(--danger); background: rgba(239,68,68,0.1); }

.loading-wrap { display: flex; justify-content: center; padding: 60px 0; }
.spinner {
  width: 36px; height: 36px;
  border: 3px solid var(--border);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

.empty { text-align: center; padding: 60px 0; color: var(--text-muted); }
.empty-icon { font-size: 40px; margin-bottom: 12px; }
.empty a { color: var(--accent-light); }

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 200;
  backdrop-filter: blur(4px);
}

.modal {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 32px;
  width: 100%;
  max-width: 440px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.modal h3 { font-size: 18px; font-weight: 600; }

.form-item { display: flex; flex-direction: column; gap: 6px; }
.form-item label { font-size: 13px; color: var(--text-muted); }

.form-item input,
.form-item textarea {
  padding: 10px 14px;
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  color: var(--text);
  font-size: 14px;
  resize: vertical;
}
.form-item input:focus,
.form-item textarea:focus { border-color: var(--accent); outline: none; }

.modal-actions { display: flex; justify-content: flex-end; gap: 12px; }
</style>
