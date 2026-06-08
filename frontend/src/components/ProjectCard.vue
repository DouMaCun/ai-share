<template>
  <RouterLink :to="`/project/${project.id}`" class="card">
    <div class="card-cover">
      <img v-if="project.coverUrl" :src="project.coverUrl" :alt="project.title" />
      <div v-else class="cover-placeholder">
        <span>{{ project.title[0] }}</span>
      </div>
    </div>

    <div class="card-body">
      <div class="card-header">
        <h3 class="card-title">{{ project.title }}</h3>
        <div class="card-tags">
          <span
            v-for="tag in project.tags?.slice(0, 2)"
            :key="tag.id"
            class="tag"
            :style="{ color: tag.color, borderColor: tag.color + '33', background: tag.color + '15' }"
          >{{ tag.name }}</span>
        </div>
      </div>

      <p class="card-desc">{{ project.description }}</p>

      <div class="card-footer">
        <div class="author">
          <div class="author-avatar">{{ project.author?.username?.[0]?.toUpperCase() }}</div>
          <span class="author-name">{{ project.author?.username }}</span>
        </div>
        <div class="stats">
          <span class="stat" :class="{ active: project.liked }">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
            </svg>
            {{ project.likeCount }}
          </span>
          <span class="stat">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
              <circle cx="12" cy="12" r="3"/>
            </svg>
            {{ project.viewCount }}
          </span>
        </div>
      </div>
    </div>
  </RouterLink>
</template>

<script setup lang="ts">
defineProps<{ project: any }>()
</script>

<style scoped>
.card {
  display: block;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  overflow: hidden;
  transition: all 0.25s;
}

.card:hover {
  border-color: var(--accent);
  transform: translateY(-2px);
  box-shadow: 0 8px 32px rgba(124, 58, 237, 0.15);
}

.card-cover {
  aspect-ratio: 16/9;
  overflow: hidden;
  background: var(--bg-hover);
}

.card-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.card:hover .card-cover img { transform: scale(1.03); }

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--bg-hover), rgba(124, 58, 237, 0.1));
  font-size: 48px;
  font-weight: 700;
  color: var(--accent-light);
  opacity: 0.6;
}

.card-body {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.card-header { display: flex; flex-direction: column; gap: 8px; }

.card-title {
  font-size: 15px;
  font-weight: 600;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-tags { display: flex; gap: 6px; flex-wrap: wrap; }

.card-desc {
  font-size: 13px;
  color: var(--text-muted);
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 8px;
  border-top: 1px solid var(--border);
}

.author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: var(--accent);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 600;
  color: white;
}

.author-name { font-size: 12px; color: var(--text-muted); }

.stats { display: flex; gap: 12px; }

.stat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: var(--text-muted);
}

.stat.active { color: #ef4444; }
</style>
