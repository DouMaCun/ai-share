<template>
  <div class="submit-page">
    <div class="container">
      <div class="page-header">
        <h1>发布项目</h1>
        <p>分享你的 AI 项目，让更多人发现它</p>
      </div>

      <div class="form-card">
        <form @submit.prevent="handleSubmit">
          <div class="form-grid">
            <div class="form-section">
              <div class="form-item">
                <label>项目名称 <span class="required">*</span></label>
                <input v-model="form.title" type="text" placeholder="给你的项目起个好名字" required maxlength="200" />
              </div>

              <div class="form-item">
                <label>项目描述 <span class="md-hint">（支持 Markdown）</span></label>
                <div class="md-toolbar">
                  <button type="button" @click="insertMd('**','**')" title="加粗">B</button>
                  <button type="button" @click="insertMd('*','*')" title="斜体"><em>I</em></button>
                  <button type="button" @click="insertMd('`','`')" title="行内代码">&lt;/&gt;</button>
                  <button type="button" @click="insertMd('[','](url)')" title="链接">🔗</button>
                  <button type="button" @click="insertMd('\n- ','')" title="列表">-</button>
                  <button type="button" @click="insertMd('\n```\n','\n```')" title="代码块">{ }</button>
                </div>
                <textarea
                  ref="descTextarea"
                  v-model="form.description"
                  placeholder="详细介绍你的项目：功能特性、使用方法、技术栈...&#10;&#10;支持 Markdown：**加粗** *斜体* `代码` [链接](url)&#10;```&#10;代码块&#10;```&#10;- 列表"
                  rows="10"
                  maxlength="5000"
                ></textarea>
                <span class="char-count">{{ form.description.length }}/5000</span>
              </div>

              <div class="form-item">
                <label>封面图</label>
                <div class="cover-upload" @click="triggerUpload" @dragover.prevent @drop.prevent="handleDrop">
                  <input
                    ref="fileInput"
                    type="file"
                    accept="image/jpeg,image/png,image/gif,image/webp"
                    style="display:none"
                    @change="handleFileChange"
                  />
                  <div v-if="uploadingCover" class="cover-uploading">
                    <span class="spinner"></span>
                    <span>上传中...</span>
                  </div>
                  <div v-else-if="coverPreview" class="cover-preview">
                    <img :src="coverPreview" alt="封面预览" />
                    <button type="button" class="cover-remove" @click.stop="removeCover">&times;</button>
                  </div>
                  <div v-else class="cover-placeholder">
                    <span class="cover-plus">+</span>
                    <span>点击或拖拽上传封面图</span>
                    <span class="cover-hint">JPG / PNG / GIF / WebP，最大 5MB</span>
                  </div>
                </div>
              </div>

              <div class="form-item">
                <label>项目截图 <span class="md-hint">（选填，最多5张）</span></label>
                <div class="screenshot-grid" v-if="screenshotPreviews.length > 0">
                  <div v-for="(preview, i) in screenshotPreviews" :key="i" class="screenshot-item">
                    <img :src="preview" alt="截图" />
                    <button type="button" class="cover-remove" @click="removeScreenshot(i)">&times;</button>
                    <span v-if="screenshotUploading[i]" class="screenshot-loading"><span class="spinner"></span></span>
                  </div>
                </div>
                <label v-if="screenshotPreviews.length < 5" class="screenshot-add" @click="triggerScreenshot">
                  <input
                    ref="screenshotInput"
                    type="file"
                    accept="image/jpeg,image/png,image/gif,image/webp"
                    style="display:none"
                    multiple
                    @change="handleScreenshotChange"
                  />
                  <span class="cover-plus">+</span>
                </label>
              </div>
            </div>

            <div class="form-section">
              <div class="form-item">
                <label>项目链接</label>
                <input v-model="form.projectUrl" type="url" placeholder="https://your-project.com" />
              </div>

              <div class="form-item">
                <label>GitHub 链接</label>
                <input v-model="form.githubUrl" type="url" placeholder="https://github.com/..." />
              </div>

              <div class="form-item">
                <label>技术标签</label>
                <div class="tag-select">
                  <button
                    v-for="tag in tags"
                    :key="tag.id"
                    type="button"
                    class="tag-option"
                    :class="{ selected: form.tagIds.includes(tag.id) }"
                    :style="form.tagIds.includes(tag.id) ? { borderColor: tag.color, color: tag.color, background: tag.color + '15' } : {}"
                    @click="toggleTag(tag.id)"
                  >{{ tag.name }}</button>
                </div>
              </div>

              <div class="form-item">
                <label>发布状态</label>
                <div class="status-toggle">
                  <button
                    type="button"
                    class="status-btn"
                    :class="{ active: form.status === 1 }"
                    @click="form.status = 1"
                  >公开发布</button>
                  <button
                    type="button"
                    class="status-btn"
                    :class="{ active: form.status === 0 }"
                    @click="form.status = 0"
                  >保存草稿</button>
                </div>
              </div>
            </div>
          </div>

          <p v-if="error" class="error">{{ error }}</p>

          <div class="form-actions">
            <RouterLink to="/" class="btn btn-ghost">取消</RouterLink>
            <button type="submit" class="btn btn-primary" :disabled="loading">
              {{ loading ? '发布中...' : (form.status === 1 ? '公开发布' : '保存草稿') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { projectApi, tagApi, uploadApi } from '@/api'

const router = useRouter()
const loading = ref(false)
const uploadingCover = ref(false)
const error = ref('')
const tags = ref<any[]>([])
const fileInput = ref<HTMLInputElement | null>(null)
const coverFile = ref<File | null>(null)
const coverPreview = ref('')
const descTextarea = ref<HTMLTextAreaElement | null>(null)
const screenshotInput = ref<HTMLInputElement | null>(null)
const screenshotFiles = ref<File[]>([])
const screenshotPreviews = ref<string[]>([])
const screenshotUploading = ref<boolean[]>([])

function insertMd(before: string, after: string) {
  const ta = descTextarea.value
  if (!ta) return
  const start = ta.selectionStart
  const end = ta.selectionEnd
  const text = form.description
  const selected = text.substring(start, end)
  form.description = text.substring(0, start) + before + selected + after + text.substring(end)
  setTimeout(() => {
    ta.focus()
    ta.selectionStart = start + before.length
    ta.selectionEnd = start + before.length + selected.length
  })
}

const form = reactive({
  title: '',
  description: '',
  coverUrl: '',
  projectUrl: '',
  githubUrl: '',
  screenshots: [] as string[],
  tagIds: [] as number[],
  status: 1
})

function toggleTag(id: number) {
  const idx = form.tagIds.indexOf(id)
  if (idx >= 0) form.tagIds.splice(idx, 1)
  else if (form.tagIds.length < 5) form.tagIds.push(id)
}

function triggerUpload() {
  fileInput.value?.click()
}

function handleFileChange(e: Event) {
  const input = e.target as HTMLInputElement
  if (input.files && input.files[0]) {
    setFile(input.files[0])
  }
}

function handleDrop(e: DragEvent) {
  const file = e.dataTransfer?.files?.[0]
  if (file) setFile(file)
}

function setFile(file: File) {
  if (!file.type.startsWith('image/')) {
    error.value = '仅支持图片格式'
    return
  }
  if (file.size > 5 * 1024 * 1024) {
    error.value = '图片大小不能超过5MB'
    return
  }
  error.value = ''
  coverFile.value = file
  coverPreview.value = URL.createObjectURL(file)
}

function removeCover() {
  coverFile.value = null
  coverPreview.value = ''
  form.coverUrl = ''
  if (fileInput.value) fileInput.value.value = ''
}

function triggerScreenshot() { screenshotInput.value?.click() }

function handleScreenshotChange(e: Event) {
  const input = e.target as HTMLInputElement
  if (!input.files) return
  for (let i = 0; i < input.files.length && screenshotFiles.value.length < 5; i++) {
    const file = input.files[i]
    if (!file.type.startsWith('image/')) continue
    if (file.size > 5 * 1024 * 1024) continue
    screenshotFiles.value.push(file)
    screenshotPreviews.value.push(URL.createObjectURL(file))
    screenshotUploading.value.push(false)
  }
  input.value = ''
}

function removeScreenshot(i: number) {
  screenshotFiles.value.splice(i, 1)
  screenshotPreviews.value.splice(i, 1)
  screenshotUploading.value.splice(i, 1)
}

async function handleSubmit() {
  loading.value = true
  error.value = ''
  try {
    if (coverFile.value) {
      uploadingCover.value = true
      const uploadRes: any = await uploadApi.image(coverFile.value)
      form.coverUrl = uploadRes.data.url
      uploadingCover.value = false
    }
    if (screenshotFiles.value.length > 0) {
      const urls: string[] = []
      for (let i = 0; i < screenshotFiles.value.length; i++) {
        screenshotUploading.value[i] = true
        const uploadRes: any = await uploadApi.image(screenshotFiles.value[i])
        urls.push(uploadRes.data.url)
        screenshotUploading.value[i] = false
      }
      form.screenshots = urls as any
    }
    const res: any = await projectApi.create(form)
    router.push(`/project/${res.data.id}`)
  } catch (e: any) {
    error.value = e.message
  } finally {
    loading.value = false
    uploadingCover.value = false
  }
}

onMounted(async () => {
  const res: any = await tagApi.list()
  tags.value = res.data
})
</script>

<style scoped>
.submit-page { padding: 40px 0 80px; }

.page-header { margin-bottom: 32px; }
.page-header h1 { font-size: 28px; font-weight: 700; margin-bottom: 8px; }
.page-header p { color: var(--text-muted); }

.form-card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 40px;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
}

@media (max-width: 768px) { .form-grid { grid-template-columns: 1fr; } }

.form-section { display: flex; flex-direction: column; gap: 24px; }

.form-item { display: flex; flex-direction: column; gap: 8px; position: relative; }

.form-item label {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-muted);
}

.required { color: var(--danger); }
.md-hint { font-weight: 400; color: var(--accent-light); font-size: 11px; }

.md-toolbar {
  display: flex;
  gap: 4px;
}
.md-toolbar button {
  padding: 4px 10px;
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: 4px;
  color: var(--text-muted);
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.15s;
}
.md-toolbar button:hover { border-color: var(--accent); color: var(--text); background: var(--bg-card); }

.form-item input,
.form-item textarea {
  padding: 10px 14px;
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  color: var(--text);
  font-size: 14px;
  transition: border-color 0.2s;
  resize: vertical;
}

.form-item input:focus,
.form-item textarea:focus { border-color: var(--accent); }

.form-item input::placeholder,
.form-item textarea::placeholder { color: var(--text-muted); }

.char-count {
  align-self: flex-end;
  font-size: 11px;
  color: var(--text-muted);
}

.cover-upload {
  border: 2px dashed var(--border);
  border-radius: var(--radius-sm);
  aspect-ratio: 16/9;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  overflow: hidden;
  transition: border-color 0.2s;
  position: relative;
}

.cover-upload:hover { border-color: var(--accent); }

.cover-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  color: var(--text-muted);
  font-size: 13px;
}

.cover-plus {
  font-size: 32px;
  font-weight: 300;
  color: var(--text-muted);
}

.cover-hint { font-size: 11px; opacity: 0.6; }

.cover-uploading {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: var(--text-muted);
  font-size: 13px;
}

.spinner {
  width: 24px;
  height: 24px;
  border: 2px solid var(--border);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin { to { transform: rotate(360deg); } }

.cover-preview {
  width: 100%;
  height: 100%;
  position: relative;
}

.cover-preview img { width: 100%; height: 100%; object-fit: cover; }

.cover-remove {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: rgba(0,0,0,0.7);
  color: #fff;
  border: none;
  font-size: 18px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
}

.cover-remove:hover { background: var(--danger); }

.screenshot-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 8px;
}

.screenshot-item {
  position: relative;
  aspect-ratio: 16/10;
  border-radius: var(--radius-sm);
  overflow: hidden;
  border: 1px solid var(--border);
}

.screenshot-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.screenshot-item .cover-remove {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 22px;
  height: 22px;
  font-size: 14px;
}

.screenshot-loading {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.screenshot-add {
  border: 2px dashed var(--border);
  border-radius: var(--radius-sm);
  aspect-ratio: 16/10;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.2s;
}

.screenshot-add:hover { border-color: var(--accent); }

.tag-select { display: flex; flex-wrap: wrap; gap: 8px; }

.tag-option {
  padding: 6px 12px;
  border-radius: 100px;
  font-size: 13px;
  color: var(--text-muted);
  background: var(--bg);
  border: 1px solid var(--border);
  cursor: pointer;
  transition: all 0.2s;
}

.tag-option:hover { border-color: var(--text-muted); color: var(--text); }
.tag-option.selected { font-weight: 500; }

.status-toggle { display: flex; gap: 8px; }

.status-btn {
  flex: 1;
  padding: 10px;
  border-radius: var(--radius-sm);
  font-size: 13px;
  background: var(--bg);
  border: 1px solid var(--border);
  color: var(--text-muted);
  cursor: pointer;
  transition: all 0.2s;
}

.status-btn.active {
  background: rgba(124, 58, 237, 0.1);
  border-color: var(--accent);
  color: var(--accent-light);
}

.error { color: var(--danger); font-size: 13px; margin-top: 16px; }

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid var(--border);
}
</style>
