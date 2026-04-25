<script setup lang="ts">
import { Pointer, StarFilled, VideoCameraFilled } from '@element-plus/icons-vue'
import type { Video } from '../types/video'

defineProps<{
  video: Video
}>()

function formatNumber(value: number) {
  if (value >= 10000) {
    return `${(value / 10000).toFixed(value >= 100000 ? 0 : 1)}万`
  }

  return String(value)
}
</script>

<template>
  <RouterLink class="video-card" :to="`/video/${video.id}`">
    <div class="cover-wrap">
      <img :src="video.coverUrl" :alt="video.title" loading="lazy" />
      <span class="duration">{{ video.duration }}</span>
    </div>

    <div class="card-body">
      <h3>{{ video.title }}</h3>
      <div class="up-row">
        <img :src="video.up.avatar" :alt="video.up.nickname" />
        <span>{{ video.up.nickname }}</span>
      </div>
      <div class="meta-row">
        <span>
          <el-icon><VideoCameraFilled /></el-icon>
          {{ formatNumber(video.playCount) }}
        </span>
        <span>
          <el-icon><Pointer /></el-icon>
          {{ formatNumber(video.likeCount) }}
        </span>
        <span>
          <el-icon><StarFilled /></el-icon>
          {{ video.publishedAt }}
        </span>
      </div>
    </div>
  </RouterLink>
</template>

<style scoped>
.video-card {
  display: block;
  overflow: hidden;
  border: 1px solid rgba(0, 174, 236, 0.1);
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 12px 28px rgba(61, 126, 170, 0.08);
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease,
    border-color 0.2s ease;
}

.video-card:hover {
  border-color: rgba(0, 174, 236, 0.32);
  box-shadow: 0 22px 45px rgba(0, 174, 236, 0.18);
  transform: translateY(-6px);
}

.cover-wrap {
  position: relative;
  aspect-ratio: 16 / 10;
  overflow: hidden;
  background: linear-gradient(135deg, #c5efff, #fff1f6);
}

.cover-wrap img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.35s ease;
}

.video-card:hover .cover-wrap img {
  transform: scale(1.05);
}

.duration {
  position: absolute;
  right: 10px;
  bottom: 10px;
  padding: 3px 8px;
  border-radius: 999px;
  background: rgba(14, 23, 33, 0.74);
  color: #fff;
  font-size: 12px;
}

.card-body {
  padding: 14px;
}

.card-body h3 {
  display: -webkit-box;
  min-height: 46px;
  margin: 0 0 12px;
  overflow: hidden;
  color: var(--nibo-text);
  font-size: 16px;
  line-height: 1.45;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

.up-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  color: var(--nibo-muted);
  font-size: 13px;
}

.up-row img {
  width: 24px;
  height: 24px;
  border-radius: 50%;
}

.meta-row {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  color: var(--nibo-muted);
  font-size: 12px;
}

.meta-row span {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
</style>
