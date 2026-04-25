<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { UploadFile, UploadUserFile } from 'element-plus'

const emit = defineEmits<{
  published: []
}>()

const form = reactive({
  title: '',
  description: '',
  tags: '',
})

const coverFiles = ref<UploadUserFile[]>([])
const videoFiles = ref<UploadUserFile[]>([])
const progress = ref(0)
const publishing = ref(false)

function keepLatestCover(_file: UploadFile, files: UploadUserFile[]) {
  coverFiles.value = files.slice(-1)
}

function keepLatestVideo(_file: UploadFile, files: UploadUserFile[]) {
  videoFiles.value = files.slice(-1)
}

function simulatePublish() {
  if (!form.title.trim()) {
    ElMessage.warning('先给视频起一个标题吧')
    return
  }

  if (!videoFiles.value.length) {
    ElMessage.warning('请选择一个视频文件')
    return
  }

  publishing.value = true
  progress.value = 8

  const timer = window.setInterval(() => {
    progress.value = Math.min(100, progress.value + Math.ceil(Math.random() * 16))

    if (progress.value >= 100) {
      window.clearInterval(timer)
      window.setTimeout(() => {
        publishing.value = false
        ElMessage.success('发布成功，这是前端模拟流程')
        emit('published')
      }, 300)
    }
  }, 220)
}
</script>

<template>
  <section class="upload-panel panel-card">
    <el-form label-position="top">
      <el-form-item label="视频标题">
        <el-input v-model="form.title" maxlength="40" placeholder="给你的作品取个吸引人的标题" show-word-limit />
      </el-form-item>

      <el-form-item label="视频简介">
        <el-input
          v-model="form.description"
          maxlength="240"
          placeholder="介绍一下这支视频，或写点幕后花絮"
          show-word-limit
          type="textarea"
          :rows="4"
        />
      </el-form-item>

      <el-form-item label="标签">
        <el-input v-model="form.tags" placeholder="例如：前端, Vue3, 生活" />
      </el-form-item>

      <div class="upload-grid">
        <el-form-item label="封面上传">
          <el-upload
            v-model:file-list="coverFiles"
            accept="image/*"
            action="#"
            :auto-upload="false"
            drag
            list-type="picture"
            :limit="1"
            :on-change="keepLatestCover"
          >
            <div class="upload-copy">
              <strong>拖拽封面到这里</strong>
              <span>支持 jpg/png/webp，当前只做前端选择</span>
            </div>
          </el-upload>
        </el-form-item>

        <el-form-item label="视频文件上传">
          <el-upload
            v-model:file-list="videoFiles"
            accept="video/*"
            action="#"
            :auto-upload="false"
            drag
            :limit="1"
            :on-change="keepLatestVideo"
          >
            <div class="upload-copy">
              <strong>选择视频文件</strong>
              <span>不会上传到服务器，只模拟进度条</span>
            </div>
          </el-upload>
        </el-form-item>
      </div>

      <div v-if="publishing || progress > 0" class="progress-box">
        <span>上传进度</span>
        <el-progress :percentage="progress" :stroke-width="12" striped striped-flow />
      </div>

      <div class="footer-actions">
        <el-button size="large" @click="progress = 0">重置进度</el-button>
        <el-button type="primary" size="large" :loading="publishing" @click="simulatePublish">
          发布视频
        </el-button>
      </div>
    </el-form>
  </section>
</template>

<style scoped>
.upload-panel {
  padding: 24px;
}

.upload-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18px;
}

.upload-copy {
  display: grid;
  gap: 8px;
  place-items: center;
  min-height: 132px;
  color: var(--nibo-text);
}

.upload-copy span {
  color: var(--nibo-muted);
  font-size: 13px;
}

.progress-box {
  display: grid;
  gap: 10px;
  margin-top: 4px;
  padding: 16px;
  border-radius: 16px;
  background: #f4fbff;
}

.progress-box span {
  color: var(--nibo-muted);
  font-size: 13px;
}

.footer-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

@media (max-width: 820px) {
  .upload-grid {
    grid-template-columns: 1fr;
  }
}
</style>
