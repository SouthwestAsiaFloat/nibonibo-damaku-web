<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import VideoCard from '../components/VideoCard.vue'
import { getVideoList } from '../api/video'
import type { Video } from '../types/video'

const route = useRoute()
const videos = ref<Video[]>([])
const loading = ref(true)

const keyword = computed(() => (typeof route.query.keyword === 'string' ? route.query.keyword.trim() : ''))
const filteredVideos = computed(() => {
  const query = keyword.value.toLowerCase()

  if (!query) {
    return videos.value
  }

  return videos.value.filter((video) => {
    return [video.title, video.description, video.up.nickname, ...video.tags]
      .join(' ')
      .toLowerCase()
      .includes(query)
  })
})

const totalPlays = computed(() => videos.value.reduce((sum, video) => sum + video.playCount, 0))

function formatNumber(value: number) {
  if (value >= 10000) {
    return `${(value / 10000).toFixed(1)}万`
  }

  return String(value)
}

onMounted(async () => {
  videos.value = await getVideoList()
  loading.value = false
})
</script>

<template>
  <div class="home-view page-shell">
    <section class="hero panel-card">
      <div class="hero-copy">
        <span class="eyebrow">nibonibo-web MVP</span>
        <h1>今天也在 <span class="nibo-gradient-text">蓝色弹幕海</span> 里相遇</h1>
        <p>
          一个简化版的视频弹幕社区前端壳子：有首页瀑布流、登录注册、详情弹幕、投稿和个人中心。
        </p>
        <div class="hero-actions">
          <el-button type="primary" size="large" round>看看推荐</el-button>
          <el-button size="large" round>本地 mock 数据</el-button>
        </div>
      </div>

      <div class="hero-card">
        <div class="hero-orbit"></div>
        <strong>{{ videos.length }}</strong>
        <span>支精选视频</span>
        <small>{{ formatNumber(totalPlays) }} 次播放在这里预热</small>
      </div>
    </section>

    <section class="feed">
      <div class="section-title">
        <div>
          <h2>{{ keyword ? `搜索：${keyword}` : '推荐视频' }}</h2>
          <p class="muted">像简化版 B 站一样浏览封面、标题、UP 主和互动数据。</p>
        </div>
        <el-tag effect="light" round>{{ filteredVideos.length }} 个结果</el-tag>
      </div>

      <el-skeleton v-if="loading" :rows="8" animated />
      <div v-else-if="filteredVideos.length" class="video-grid">
        <VideoCard v-for="video in filteredVideos" :key="video.id" :video="video" />
      </div>
      <el-empty v-else description="没有找到相关视频，换个关键词试试" />
    </section>
  </div>
</template>

<style scoped>
.home-view {
  display: grid;
  gap: 28px;
}

.hero {
  position: relative;
  display: grid;
  grid-template-columns: minmax(0, 1.4fr) minmax(260px, 0.6fr);
  gap: 24px;
  overflow: hidden;
  padding: 38px;
}

.hero::before {
  position: absolute;
  inset: auto -10% -42% 42%;
  height: 260px;
  border-radius: 999px;
  background: rgba(0, 174, 236, 0.13);
  content: "";
  filter: blur(20px);
}

.hero-copy {
  position: relative;
  z-index: 1;
}

.eyebrow {
  display: inline-flex;
  margin-bottom: 12px;
  padding: 6px 12px;
  border-radius: 999px;
  background: var(--nibo-blue-soft);
  color: var(--nibo-blue-deep);
  font-size: 13px;
  font-weight: 700;
}

.hero h1 {
  max-width: 760px;
  margin: 0;
  color: var(--nibo-text);
  font-size: clamp(34px, 5vw, 62px);
  line-height: 1.05;
  letter-spacing: -0.06em;
}

.hero p {
  max-width: 660px;
  margin: 18px 0 0;
  color: #536174;
  font-size: 17px;
  line-height: 1.8;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 26px;
}

.hero-card {
  position: relative;
  z-index: 1;
  display: grid;
  min-height: 260px;
  place-items: center;
  align-content: center;
  overflow: hidden;
  border-radius: 28px;
  background:
    linear-gradient(145deg, rgba(0, 174, 236, 0.9), rgba(117, 224, 255, 0.85)),
    linear-gradient(45deg, #fff, #b3f0ff);
  color: #fff;
  text-align: center;
  box-shadow: 0 24px 55px rgba(0, 174, 236, 0.28);
}

.hero-card strong {
  font-size: 86px;
  line-height: 1;
}

.hero-card span {
  font-size: 18px;
  font-weight: 700;
}

.hero-card small {
  margin-top: 10px;
  opacity: 0.86;
}

.hero-orbit {
  position: absolute;
  width: 160px;
  height: 160px;
  border: 1px solid rgba(255, 255, 255, 0.45);
  border-radius: 50%;
  animation: orbit 7s linear infinite;
}

.hero-orbit::after {
  position: absolute;
  top: 14px;
  right: 18px;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #fff;
  content: "";
}

.feed {
  min-height: 420px;
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 18px;
}

@keyframes orbit {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 1120px) {
  .video-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (max-width: 820px) {
  .hero {
    grid-template-columns: 1fr;
    padding: 28px;
  }

  .video-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 560px) {
  .video-grid {
    grid-template-columns: 1fr;
  }
}
</style>
