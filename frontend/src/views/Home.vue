<template>
  <div class="home">
    <!-- Hero -->
    <section class="hero">
      <div class="container hero-inner">
        <div class="hero-badge">✦ AI 项目分享平台</div>
        <h1 class="hero-title">
          发现、分享<br />
          <span class="gradient-text">优秀的 AI 项目</span>
        </h1>
        <p class="hero-sub">汇聚全球开发者的 AI 创意，找到你的下一个灵感</p>
        <div class="hero-actions">
          <RouterLink to="/submit" class="btn btn-primary">发布项目</RouterLink>
          <a href="#discover" class="btn btn-ghost">浏览发现 ↓</a>
        </div>
        <div class="hero-stats">
          <div class="stat-item"><span class="stat-num">{{ stats.projects }}+</span><span>开源项目</span></div>
          <div class="stat-divider"></div>
          <div class="stat-item"><span class="stat-num">{{ stats.users }}+</span><span>活跃开发者</span></div>
          <div class="stat-divider"></div>
          <div class="stat-item"><span class="stat-num">{{ stats.tags }}+</span><span>技术标签</span></div>
        </div>
      </div>
    </section>

    <!-- Discover -->
    <section id="discover" class="discover">
      <div class="container">
        <div class="toolbar">
          <!-- Search -->
          <div class="search-wrap">
            <svg class="search-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/>
            </svg>
            <input
              v-model="query.keyword"
              class="search-input"
              placeholder="搜索项目..."
              @input="debouncedFetch"
            />
          </div>

          <!-- Sort -->
          <div class="sort-tabs">
            <button
              v-for="s in sorts"
              :key="s.value"
              class="sort-tab"
              :class="{ active: query.sort === s.value }"
              @click="setSort(s.value)"
            >{{ s.label }}</button>
          </div>
        </div>

        <!-- Tags -->
        <div class="tag-filter">
          <button class="filter-tag" :class="{ active: !query.tagId }" @click="setTag(null)">全部</button>
          <button
            v-for="tag in tags"
            :key="tag.id"
            class="filter-tag"
            :class="{ active: query.tagId === tag.id }"
            :style="query.tagId === tag.id ? { borderColor: tag.color, color: tag.color, background: tag.color + '15' } : {}"
            @click="setTag(tag.id)"
          >{{ tag.name }}</button>
        </div>

        <!-- Grid -->
        <Transition name="fade">
          <div v-if="loading" class="loading-grid">
            <div v-for="i in 6" :key="i" class="skeleton-card"></div>
          </div>
          <div v-else-if="projects.length" class="project-grid">
            <Transition name="slide-up" v-for="(p, i) in projects" :key="p.id" :style="{ transitionDelay: i * 30 + 'ms' }">
              <ProjectCard :project="p" />
            </Transition>
          </div>
          <div v-else class="empty">
            <div class="empty-icon">🔭</div>
            <p>还没有项目，<RouterLink to="/submit">来发布第一个？</RouterLink></p>
          </div>
        </Transition>

        <!-- Pagination -->
        <div v-if="total > query.size" class="pagination">
          <button class="btn btn-ghost" :disabled="query.page === 1" @click="changePage(-1)">← 上一页</button>
          <span class="page-info">第 {{ query.page }} 页 / 共 {{ Math.ceil(total / query.size) }} 页</span>
          <button class="btn btn-ghost" :disabled="query.page * query.size >= total" @click="changePage(1)">下一页 →</button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { projectApi, tagApi } from '@/api'
import ProjectCard from '@/components/ProjectCard.vue'

const projects = ref<any[]>([])
const tags = ref<any[]>([])
const total = ref(0)
const loading = ref(false)

const query = reactive({
  keyword: '',
  tagId: null as number | null,
  sort: 'newest',
  page: 1,
  size: 12
})

const stats = reactive({ projects: 0, users: 128, tags: 10 })

const sorts = [
  { label: '最新', value: 'newest' },
  { label: '最热', value: 'hottest' },
  { label: '最多点赞', value: 'mostLiked' }
]

let debounceTimer: any

function debouncedFetch() {
  clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => { query.page = 1; fetchProjects() }, 400)
}

function setSort(s: string) {
  query.sort = s
  query.page = 1
  fetchProjects()
}

function setTag(id: number | null) {
  query.tagId = id
  query.page = 1
  fetchProjects()
}

function changePage(delta: number) {
  query.page += delta
  fetchProjects()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

async function fetchProjects() {
  loading.value = true
  try {
    const res: any = await projectApi.list(query)
    projects.value = res.data.list
    total.value = res.data.total
    stats.projects = res.data.total
  } finally {
    loading.value = false
  }
}

async function fetchTags() {
  const res: any = await tagApi.list()
  tags.value = res.data
}

onMounted(() => {
  fetchTags()
  fetchProjects()
})
</script>

<style scoped>
.hero {
  padding: 80px 0 60px;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.hero::before {
  content: '';
  position: absolute;
  top: -200px;
  left: 50%;
  transform: translateX(-50%);
  width: 800px;
  height: 800px;
  background: radial-gradient(circle, rgba(124, 58, 237, 0.12) 0%, transparent 70%);
  pointer-events: none;
}

.hero-inner { display: flex; flex-direction: column; align-items: center; gap: 24px; }

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  border: 1px solid var(--border);
  border-radius: 100px;
  font-size: 12px;
  color: var(--text-muted);
  background: var(--bg-card);
}

.hero-title {
  font-size: clamp(32px, 5vw, 56px);
  font-weight: 800;
  line-height: 1.15;
  letter-spacing: -1px;
}

.gradient-text {
  background: linear-gradient(135deg, #7c3aed, #a855f7, #06b6d4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.hero-sub { font-size: 16px; color: var(--text-muted); max-width: 460px; }

.hero-actions { display: flex; gap: 12px; flex-wrap: wrap; justify-content: center; }

.hero-stats {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-top: 8px;
  padding: 20px 32px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
}

.stat-item { display: flex; flex-direction: column; align-items: center; gap: 4px; }
.stat-num { font-size: 24px; font-weight: 700; color: var(--accent-light); }
.stat-item span:last-child { font-size: 12px; color: var(--text-muted); }
.stat-divider { width: 1px; height: 40px; background: var(--border); }

.discover { padding: 40px 0 80px; }

.toolbar {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.search-wrap {
  flex: 1;
  min-width: 200px;
  position: relative;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--text-muted);
}

.search-input {
  width: 100%;
  padding: 10px 14px 10px 38px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  color: var(--text);
  font-size: 14px;
  transition: border-color 0.2s;
}

.search-input:focus { border-color: var(--accent); }
.search-input::placeholder { color: var(--text-muted); }

.sort-tabs { display: flex; gap: 4px; }

.sort-tab {
  padding: 8px 14px;
  border-radius: var(--radius-sm);
  font-size: 13px;
  color: var(--text-muted);
  background: transparent;
  border: 1px solid transparent;
  transition: all 0.2s;
}

.sort-tab:hover { color: var(--text); }
.sort-tab.active { color: var(--accent-light); border-color: var(--accent); background: rgba(124, 58, 237, 0.1); }

.tag-filter {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 32px;
}

.filter-tag {
  padding: 6px 14px;
  border-radius: 100px;
  font-size: 13px;
  color: var(--text-muted);
  background: var(--bg-card);
  border: 1px solid var(--border);
  transition: all 0.2s;
  cursor: pointer;
}

.filter-tag:hover { color: var(--text); border-color: var(--text-muted); }
.filter-tag.active { color: var(--accent-light); border-color: var(--accent); background: rgba(124, 58, 237, 0.1); }

.project-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.loading-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.skeleton-card {
  height: 280px;
  border-radius: var(--radius);
  background: var(--bg-card);
  border: 1px solid var(--border);
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.empty {
  text-align: center;
  padding: 80px 0;
  color: var(--text-muted);
}

.empty-icon { font-size: 48px; margin-bottom: 16px; }
.empty a { color: var(--accent-light); }

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  margin-top: 48px;
}

.page-info { font-size: 14px; color: var(--text-muted); }
</style>
