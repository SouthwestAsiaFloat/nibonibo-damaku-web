<script setup lang="ts">
import { useRouter } from 'vue-router'
import UploadPanel from '../components/UploadPanel.vue'

const router = useRouter()

function handlePublished() {
  router.push('/profile')
}
</script>

<template>
  <div class="upload-view page-shell">
    <section class="upload-hero panel-card">
      <div>
        <span>CREATOR STUDIO</span>
        <h1>发布你的下一支视频</h1>
        <p>上传页目前只做前端 UI 和进度条模拟，不会请求后端，也不会写入真实数据库。</p>
      </div>
    </section>

    <UploadPanel @published="handlePublished" />
  </div>
</template>

<style scoped>
.upload-view {
  display: grid;
  gap: 22px;
}

.upload-hero {
  padding: 32px;
  background:
    linear-gradient(135deg, rgba(0, 174, 236, 0.88), rgba(104, 222, 255, 0.88)),
    #fff;
  color: #fff;
}

.upload-hero span {
  display: inline-flex;
  margin-bottom: 12px;
  padding: 6px 12px;
  border: 1px solid rgba(255, 255, 255, 0.42);
  border-radius: 999px;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.16em;
}

.upload-hero h1 {
  margin: 0;
  font-size: clamp(30px, 5vw, 48px);
  line-height: 1.12;
}

.upload-hero p {
  max-width: 680px;
  margin: 14px 0 0;
  line-height: 1.8;
}
</style>
