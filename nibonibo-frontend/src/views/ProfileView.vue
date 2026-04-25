<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import VideoCard from '../components/VideoCard.vue'
import { getCollectedVideos, getPublishedVideos } from '../api/video'
import { useUserStore } from '../stores/user'
import type { Video } from '../types/video'

const router = useRouter()
const userStore = useUserStore()
const publishedVideos = ref<Video[]>([])
const collectedVideos = ref<Video[]>([])
const loading = ref(true)

onMounted(async () => {
  const [published, collected] = await Promise.all([getPublishedVideos(), getCollectedVideos()])
  publishedVideos.value = published
  collectedVideos.value = collected
  loading.value = false
})
</script>

<template>
  <div class="profile-view page-shell">
    <section v-if="userStore.currentUser" class="profile-hero panel-card">
      <img :src="userStore.currentUser.avatar" :alt="userStore.currentUser.nickname" />
      <div>
        <span class="profile-label">个人中心</span>
        <h1>{{ userStore.currentUser.nickname }}</h1>
        <p>{{ userStore.currentUser.bio }}</p>
        <div class="profile-stats">
          <span>{{ userStore.currentUser.followers }} 粉丝</span>
          <span>{{ userStore.currentUser.following }} 关注</span>
          <span>{{ publishedVideos.length }} 投稿</span>
        </div>
      </div>
      <el-button type="primary" round @click="router.push('/upload')">继续投稿</el-button>
    </section>

    <el-skeleton v-if="loading" :rows="10" animated />
    <el-tabs v-else class="profile-tabs" type="border-card">
      <el-tab-pane label="我发布的视频">
        <div class="video-grid">
          <VideoCard v-for="video in publishedVideos" :key="video.id" :video="video" />
        </div>
      </el-tab-pane>
      <el-tab-pane label="我的收藏">
        <div class="video-grid">
          <VideoCard v-for="video in collectedVideos" :key="video.id" :video="video" />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style scoped>
.profile-view {
  display: grid;
  gap: 22px;
}

.profile-hero {
  display: grid;
  grid-template-columns: 112px 1fr auto;
  align-items: center;
  gap: 22px;
  padding: 30px;
}

.profile-hero > img {
  width: 112px;
  height: 112px;
  border: 5px solid #fff;
  border-radius: 34px;
  box-shadow: 0 16px 28px rgba(0, 174, 236, 0.18);
}

.profile-label {
  display: inline-flex;
  margin-bottom: 8px;
  padding: 5px 10px;
  border-radius: 999px;
  background: var(--nibo-blue-soft);
  color: var(--nibo-blue-deep);
  font-size: 12px;
  font-weight: 700;
}

.profile-hero h1 {
  margin: 0;
  color: var(--nibo-text);
  font-size: clamp(28px, 5vw, 46px);
}

.profile-hero p {
  max-width: 720px;
  margin: 10px 0 0;
  color: #536174;
  line-height: 1.8;
}

.profile-stats {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 14px;
}

.profile-stats span {
  padding: 7px 12px;
  border-radius: 999px;
  background: #f1faff;
  color: var(--nibo-muted);
  font-size: 13px;
}

.profile-tabs {
  overflow: hidden;
  border: 1px solid rgba(0, 174, 236, 0.1);
  border-radius: 18px;
  box-shadow: var(--nibo-shadow);
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
  padding: 6px;
}

@media (max-width: 900px) {
  .profile-hero {
    grid-template-columns: 90px 1fr;
  }

  .profile-hero > img {
    width: 90px;
    height: 90px;
    border-radius: 28px;
  }

  .profile-hero .el-button {
    grid-column: 1 / -1;
    width: max-content;
  }

  .video-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 560px) {
  .profile-hero {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .profile-hero > img,
  .profile-hero .el-button {
    justify-self: center;
  }

  .profile-stats {
    justify-content: center;
  }

  .video-grid {
    grid-template-columns: 1fr;
  }
}
</style>
