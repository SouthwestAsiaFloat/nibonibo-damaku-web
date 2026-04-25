<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Coin, Pointer, Star, UserFilled } from '@element-plus/icons-vue'
import CommentList from '../components/CommentList.vue'
import DanmakuPanel from '../components/DanmakuPanel.vue'
import { getComments, getDanmaku, sendComment, sendDanmaku } from '../api/interaction'
import { getVideoDetail } from '../api/video'
import { useUserStore } from '../stores/user'
import type { Comment as VideoComment } from '../types/comment'
import type { Danmaku } from '../types/danmaku'
import type { Video } from '../types/video'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const video = ref<Video>()
const comments = ref<VideoComment[]>([])
const danmakuList = ref<Danmaku[]>([])
const danmakuEnabled = ref(true)
const liked = ref(false)
const favored = ref(false)
const coined = ref(false)
const loading = ref(true)

const videoId = computed(() => String(route.params.id))

async function loadVideo() {
  loading.value = true
  const [detail, nextComments, nextDanmaku] = await Promise.all([
    getVideoDetail(videoId.value),
    getComments(videoId.value),
    getDanmaku(videoId.value),
  ])

  if (!detail) {
    ElMessage.error('视频不存在')
    router.replace('/')
    return
  }

  video.value = detail
  comments.value = nextComments
  danmakuList.value = nextDanmaku
  liked.value = false
  favored.value = false
  coined.value = false
  loading.value = false
}

function requireLogin() {
  if (userStore.isLoggedIn) {
    return true
  }

  ElMessage.warning('请先登录再互动')
  router.push({
    path: '/login',
    query: { redirect: route.fullPath },
  })
  return false
}

function toggleLike() {
  if (!requireLogin() || !video.value) {
    return
  }

  liked.value = !liked.value
  video.value.likeCount += liked.value ? 1 : -1
}

function toggleFavorite() {
  if (!requireLogin()) {
    return
  }

  favored.value = !favored.value
  ElMessage.success(favored.value ? '已收藏' : '已取消收藏')
}

function toggleCoin() {
  if (!requireLogin() || !video.value) {
    return
  }

  coined.value = !coined.value
  video.value.coinCount += coined.value ? 1 : -1
}

async function handleDanmakuSend(content: string) {
  if (!requireLogin() || !video.value) {
    return
  }

  const item = await sendDanmaku(video.value.id, content, userStore.currentUser ?? undefined)
  danmakuList.value = [...danmakuList.value, item]
}

async function handleCommentSend(content: string) {
  if (!requireLogin() || !video.value) {
    return
  }

  const item = await sendComment(video.value.id, content, userStore.currentUser ?? undefined)
  comments.value = [item, ...comments.value]
}

function formatNumber(value: number) {
  if (value >= 10000) {
    return `${(value / 10000).toFixed(1)}万`
  }

  return String(value)
}

watch(videoId, loadVideo)
onMounted(loadVideo)
</script>

<template>
  <div class="detail-view page-shell">
    <el-skeleton v-if="loading" :rows="12" animated />

    <template v-else-if="video">
      <section class="main-grid">
        <div class="player-column">
          <div class="player-card panel-card">
            <div class="video-stage">
              <video class="video-player" controls :poster="video.coverUrl">
                <source :src="video.videoUrl" type="video/mp4" />
              </video>

              <div v-if="danmakuEnabled" class="danmaku-overlay">
                <span
                  v-for="(item, index) in danmakuList.slice(-8)"
                  :key="item.id"
                  :style="{ top: `${10 + (index % 6) * 13}%`, color: item.color }"
                >
                  {{ item.content }}
                </span>
              </div>
            </div>

            <div class="video-info">
              <el-tag v-for="tag in video.tags" :key="tag" round>{{ tag }}</el-tag>
              <h1>{{ video.title }}</h1>
              <p>{{ video.description }}</p>
              <div class="info-meta">
                <span>{{ formatNumber(video.playCount) }} 播放</span>
                <span>{{ video.publishedAt }}</span>
              </div>
            </div>
          </div>

          <div class="action-bar panel-card">
            <button :class="{ active: liked }" type="button" @click="toggleLike">
              <el-icon><Pointer /></el-icon>
              点赞 {{ formatNumber(video.likeCount) }}
            </button>
            <button :class="{ active: favored }" type="button" @click="toggleFavorite">
              <el-icon><Star /></el-icon>
              收藏 {{ formatNumber(video.favoriteCount) }}
            </button>
            <button :class="{ active: coined }" type="button" @click="toggleCoin">
              <el-icon><Coin /></el-icon>
              投币 {{ formatNumber(video.coinCount) }}
            </button>
          </div>

          <CommentList :comments="comments" :can-comment="userStore.isLoggedIn" @send="handleCommentSend" />
        </div>

        <aside class="side-column">
          <section class="up-card panel-card">
            <img :src="video.up.avatar" :alt="video.up.nickname" />
            <div>
              <span>
                <el-icon><UserFilled /></el-icon>
                UP 主
              </span>
              <strong>{{ video.up.nickname }}</strong>
              <p>认真发布每一份喜欢，也欢迎在评论区一起补充灵感。</p>
            </div>
          </section>

          <DanmakuPanel
            v-model:enabled="danmakuEnabled"
            :danmaku-list="danmakuList"
            @send="handleDanmakuSend"
          />
        </aside>
      </section>
    </template>
  </div>
</template>

<style scoped>
.detail-view {
  min-height: 640px;
}

.main-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 360px;
  gap: 22px;
  align-items: start;
}

.player-column,
.side-column {
  display: grid;
  gap: 18px;
}

.player-card {
  overflow: hidden;
}

.video-stage {
  position: relative;
  overflow: hidden;
  border-radius: 18px 18px 0 0;
  background: #0d1722;
}

.video-player {
  display: block;
  width: 100%;
  aspect-ratio: 16 / 9;
  background: #0d1722;
}

.danmaku-overlay {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
}

.danmaku-overlay span {
  position: absolute;
  right: -30%;
  min-width: max-content;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.85);
  animation: danmaku-move 9s linear infinite;
}

.video-info {
  padding: 22px;
}

.video-info h1 {
  margin: 14px 0 10px;
  color: var(--nibo-text);
  font-size: clamp(26px, 4vw, 38px);
  line-height: 1.18;
}

.video-info p {
  margin: 0;
  color: #526176;
  line-height: 1.8;
}

.info-meta {
  display: flex;
  gap: 14px;
  margin-top: 14px;
  color: var(--nibo-muted);
  font-size: 13px;
}

.action-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding: 14px;
}

.action-bar button {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  padding: 10px 16px;
  border: 0;
  border-radius: 999px;
  background: #f2faff;
  color: var(--nibo-muted);
  cursor: pointer;
  transition: 0.2s ease;
}

.action-bar button:hover,
.action-bar button.active {
  background: var(--nibo-blue);
  color: #fff;
  transform: translateY(-2px);
}

.up-card {
  display: grid;
  grid-template-columns: 64px 1fr;
  gap: 14px;
  padding: 18px;
}

.up-card img {
  width: 64px;
  height: 64px;
  border-radius: 50%;
}

.up-card span {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  color: var(--nibo-muted);
  font-size: 12px;
}

.up-card strong {
  display: block;
  margin: 4px 0 8px;
  color: var(--nibo-text);
  font-size: 18px;
}

.up-card p {
  margin: 0;
  color: #566276;
  font-size: 13px;
  line-height: 1.7;
}

@keyframes danmaku-move {
  to {
    transform: translateX(-150vw);
  }
}

@media (max-width: 1080px) {
  .main-grid {
    grid-template-columns: 1fr;
  }
}
</style>
