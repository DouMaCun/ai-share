<template>
  <div class="detail-page">
    <div class="container" v-if="project">
      <div class="breadcrumb">
        <RouterLink to="/">发现</RouterLink>
        <span>/</span>
        <span>{{ project.title }}</span>
      </div>

      <div class="detail-layout">
        <div class="detail-main">
          <div class="cover-wrap" v-if="project.coverUrl">
            <img :src="project.coverUrl" :alt="project.title" />
          </div>

          <div class="detail-card">
            <div class="detail-header">
              <div>
                <h1>{{ project.title }}</h1>
                <div class="tags">
                  <span
                    v-for="tag in project.tags"
                    :key="tag.id"
                    class="tag"
                    :style="{ color: tag.color, borderColor: tag.color + '33', background: tag.color + '15' }"
                  >{{ tag.name }}</span>
                </div>
              </div>
              <div class="actions" v-if="auth.isLoggedIn">
                <button class="action-btn" :class="{ liked: project.liked }" @click="handleLike">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                  </svg>
                  {{ project.likeCount }}
                </button>
                <button class="action-btn" :class="{ favorited: project.favorited }" @click="handleFavorite">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M17 3H7c-1.1 0-1.99.9-1.99 2L5 21l7-3 7 3V5c0-1.1-.9-2-2-2z"/>
                  </svg>
                  {{ project.favoriteCount }}
                </button>
              </div>
            </div>

            <div class="description" v-if="project.description">
              <pre>{{ project.description }}</pre>
            </div>
          </div>

          <!-- 评论区 -->
          <div class="comment-section">
            <h3>评论 ({{ comments.length }})</h3>

            <div class="comment-input" v-if="auth.isLoggedIn">
              <textarea
                v-model="commentText"
                placeholder="写下你的想法..."
                rows="3"
                maxlength="2000"
              ></textarea>
              <div class="comment-input-footer">
                <span class="char-count">{{ commentText.length }}/2000</span>
                <button class="btn btn-primary" :disabled="!commentText.trim() || posting" @click="handlePostComment">
                  {{ posting ? '发布中...' : '发布评论' }}
                </button>
              </div>
            </div>

            <div v-if="comments.length > 0" class="comment-list">
              <div v-for="c in comments" :key="c.id" class="comment-item">
                <div class="comment-avatar">{{ c.author?.username?.[0]?.toUpperCase() }}</div>
                <div class="comment-body">
                  <div class="comment-meta">
                    <span class="comment-author">{{ c.author?.username }}</span>
                    <span class="comment-time">{{ formatDate(c.createdAt) }}</span>
                  </div>
                  <p class="comment-content">{{ c.content }}</p>
                </div>
                <button
                  v-if="auth.user?.id === c.author?.id"
                  class="comment-delete"
                  @click="handleDeleteComment(c.id)"
                >删除</button>
              </div>
            </div>

            <div v-else class="comment-empty">
              <p v-if="auth.isLoggedIn">暂无评论，来发表第一条吧</p>
              <p v-else>暂无评论</p>
            </div>
          </div>
        </div>

        <aside class="detail-sidebar">
          <div class="sidebar-card">
            <div class="author-info">
              <div class="author-avatar-lg">{{ project.author?.username?.[0]?.toUpperCase() }}</div>
              <div>
                <div class="author-name">{{ project.author?.username }}</div>
                <div class="author-bio">{{ project.author?.bio || '这个人很神秘' }}</div>
              </div>
            </div>
          </div>

          <div class="sidebar-card links-card">
            <h4>项目链接</h4>
            <a v-if="project.projectUrl" :href="project.projectUrl" target="_blank" class="link-btn">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/>
                <polyline points="15 3 21 3 21 9"/><line x1="10" y1="14" x2="21" y2="3"/>
              </svg>
              访问项目
            </a>
            <a v-if="project.githubUrl" :href="project.githubUrl" target="_blank" class="link-btn github">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 0C5.374 0 0 5.373 0 12c0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23A11.509 11.509 0 0 1 12 5.803c1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576C20.566 21.797 24 17.3 24 12c0-6.627-5.373-12-12-12z"/>
              </svg>
              GitHub
            </a>
            <span v-if="!project.projectUrl && !project.githubUrl" class="no-links">暂无链接</span>
          </div>

          <div class="sidebar-card stats-card">
            <div class="stat-row">
              <span class="stat-label">浏览</span>
              <span class="stat-val">{{ project.viewCount }}</span>
            </div>
            <div class="stat-row">
              <span class="stat-label">点赞</span>
              <span class="stat-val">{{ project.likeCount }}</span>
            </div>
            <div class="stat-row">
              <span class="stat-label">收藏</span>
              <span class="stat-val">{{ project.favoriteCount }}</span>
            </div>
            <div class="stat-row">
              <span class="stat-label">发布时间</span>
              <span class="stat-val">{{ formatDate(project.createdAt) }}</span>
            </div>
          </div>
        </aside>
      </div>
    </div>

    <div v-else-if="loading" class="loading-wrap">
      <div class="spinner"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { projectApi, commentApi } from '@/api'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const auth = useAuthStore()
const project = ref<any>(null)
const loading = ref(true)
const comments = ref<any[]>([])
const commentText = ref('')
const posting = ref(false)

async function fetchProject() {
  const res: any = await projectApi.getById(Number(route.params.id))
  project.value = res.data
  loading.value = false
}

async function fetchComments() {
  const res: any = await commentApi.list(Number(route.params.id))
  comments.value = res.data
}

async function handleLike() {
  await projectApi.like(project.value.id)
  project.value.liked = !project.value.liked
  project.value.likeCount += project.value.liked ? 1 : -1
}

async function handleFavorite() {
  await projectApi.favorite(project.value.id)
  project.value.favorited = !project.value.favorited
  project.value.favoriteCount += project.value.favorited ? 1 : -1
}

async function handlePostComment() {
  if (!commentText.value.trim()) return
  posting.value = true
  try {
    await commentApi.create(Number(route.params.id), { content: commentText.value })
    commentText.value = ''
    await fetchComments()
  } finally {
    posting.value = false
  }
}

async function handleDeleteComment(id: number) {
  await commentApi.remove(id)
  comments.value = comments.value.filter(c => c.id !== id)
}

function formatDate(d: string) {
  return new Date(d).toLocaleDateString('zh-CN')
}

onMounted(() => { fetchProject(); fetchComments() })
</script>

<style scoped>
.detail-page { padding: 32px 0 80px; }

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: var(--text-muted);
  margin-bottom: 24px;
}
.breadcrumb a:hover { color: var(--text); }

.detail-layout {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 24px;
  align-items: start;
}

@media (max-width: 900px) { .detail-layout { grid-template-columns: 1fr; } }

.detail-main { display: flex; flex-direction: column; gap: 16px; }

.cover-wrap {
  border-radius: var(--radius);
  overflow: hidden;
  border: 1px solid var(--border);
  aspect-ratio: 16/9;
}
.cover-wrap img { width: 100%; height: 100%; object-fit: cover; }

.detail-card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 28px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 20px;
}

h1 { font-size: 24px; font-weight: 700; margin-bottom: 12px; }

.tags { display: flex; gap: 8px; flex-wrap: wrap; }

.actions { display: flex; gap: 8px; flex-shrink: 0; }

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: var(--radius-sm);
  font-size: 14px;
  background: var(--bg);
  border: 1px solid var(--border);
  color: var(--text-muted);
  cursor: pointer;
  transition: all 0.2s;
}
.action-btn:hover { border-color: var(--text-muted); color: var(--text); }
.action-btn.liked { color: #ef4444; border-color: #ef4444; background: rgba(239,68,68,0.1); }
.action-btn.favorited { color: #f59e0b; border-color: #f59e0b; background: rgba(245,158,11,0.1); }

.description pre {
  white-space: pre-wrap;
  word-break: break-word;
  font-family: inherit;
  font-size: 14px;
  color: var(--text-muted);
  line-height: 1.8;
}

.detail-sidebar { display: flex; flex-direction: column; gap: 12px; }

.sidebar-card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 20px;
}

.author-info { display: flex; gap: 12px; align-items: center; }

.author-avatar-lg {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: var(--accent);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 700;
  color: white;
  flex-shrink: 0;
}

.author-name { font-weight: 600; margin-bottom: 4px; }
.author-bio { font-size: 12px; color: var(--text-muted); }

.links-card h4 { font-size: 13px; color: var(--text-muted); margin-bottom: 12px; }

.link-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 14px;
  border-radius: var(--radius-sm);
  font-size: 13px;
  background: var(--bg);
  border: 1px solid var(--border);
  color: var(--text);
  transition: all 0.2s;
  margin-bottom: 8px;
}
.link-btn:hover { border-color: var(--accent); color: var(--accent-light); }
.link-btn.github:hover { border-color: #6b7280; color: var(--text); }

.no-links { font-size: 13px; color: var(--text-muted); }

.stat-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid var(--border);
  font-size: 13px;
}
.stat-row:last-child { border-bottom: none; padding-bottom: 0; }
.stat-label { color: var(--text-muted); }
.stat-val { font-weight: 500; }

.loading-wrap { display: flex; justify-content: center; padding: 120px 0; }
.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--border);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

.comment-section {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 28px;
}

.comment-section h3 { font-size: 16px; font-weight: 600; margin-bottom: 20px; }

.comment-input { margin-bottom: 24px; }

.comment-input textarea {
  width: 100%;
  padding: 12px 14px;
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  color: var(--text);
  font-size: 14px;
  resize: vertical;
  transition: border-color 0.2s;
}

.comment-input textarea:focus { border-color: var(--accent); outline: none; }

.comment-input textarea::placeholder { color: var(--text-muted); }

.comment-input-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.comment-list { display: flex; flex-direction: column; gap: 16px; }

.comment-item {
  display: flex;
  gap: 12px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border);
}

.comment-item:last-child { border-bottom: none; padding-bottom: 0; }

.comment-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--accent);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
  flex-shrink: 0;
}

.comment-body { flex: 1; min-width: 0; }

.comment-meta { display: flex; align-items: center; gap: 8px; margin-bottom: 4px; }

.comment-author { font-size: 14px; font-weight: 500; }

.comment-time { font-size: 12px; color: var(--text-muted); }

.comment-content { font-size: 14px; color: var(--text-muted); line-height: 1.6; word-break: break-word; }

.comment-delete {
  align-self: flex-start;
  padding: 4px 10px;
  border-radius: var(--radius-sm);
  font-size: 12px;
  background: transparent;
  border: 1px solid var(--border);
  color: var(--text-muted);
  cursor: pointer;
  transition: all 0.2s;
}

.comment-delete:hover { color: #ef4444; border-color: #ef4444; }

.comment-empty {
  text-align: center;
  padding: 32px 0;
  color: var(--text-muted);
  font-size: 14px;
}
</style>
