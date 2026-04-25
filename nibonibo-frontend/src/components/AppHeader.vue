<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, SwitchButton, UploadFilled, UserFilled } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const keyword = ref(typeof route.query.keyword === 'string' ? route.query.keyword : '')

const displayName = computed(() => userStore.currentUser?.nickname ?? '登录')

watch(
  () => route.query.keyword,
  (nextKeyword) => {
    keyword.value = typeof nextKeyword === 'string' ? nextKeyword : ''
  },
)

function handleSearch() {
  const nextKeyword = keyword.value.trim()
  router.push({
    path: '/',
    query: nextKeyword ? { keyword: nextKeyword } : {},
  })
}

function handleLogout() {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/')
}
</script>

<template>
  <header class="app-header">
    <RouterLink class="brand" to="/">
      <span class="brand-mark">n</span>
      <span class="brand-text">nibonibo</span>
      <span class="brand-subtitle">视频小岛</span>
    </RouterLink>

    <nav class="nav-links">
      <RouterLink to="/">首页</RouterLink>
      <RouterLink to="/upload">投稿</RouterLink>
      <RouterLink to="/profile">个人中心</RouterLink>
    </nav>

    <div class="header-search">
      <el-input
        v-model="keyword"
        clearable
        placeholder="搜索视频、UP 主或标签"
        size="large"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
        <template #append>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </template>
      </el-input>
    </div>

    <div class="header-actions">
      <el-button class="upload-button" type="primary" round @click="router.push('/upload')">
        <el-icon><UploadFilled /></el-icon>
        投稿
      </el-button>

      <el-dropdown v-if="userStore.isLoggedIn" trigger="click">
        <button class="user-entry" type="button">
          <img :src="userStore.currentUser?.avatar" alt="用户头像" />
          <span>{{ displayName }}</span>
        </button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="router.push('/profile')">
              <el-icon><UserFilled /></el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item divided @click="handleLogout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

      <el-button v-else round @click="router.push('/login')">登录</el-button>
    </div>
  </header>
</template>

<style scoped>
.app-header {
  position: sticky;
  top: 0;
  z-index: 20;
  display: grid;
  grid-template-columns: auto auto minmax(240px, 1fr) auto;
  align-items: center;
  gap: 18px;
  padding: 14px max(20px, calc((100vw - 1240px) / 2));
  border-bottom: 1px solid rgba(0, 174, 236, 0.12);
  background: rgba(255, 255, 255, 0.82);
  box-shadow: 0 10px 28px rgba(54, 121, 170, 0.08);
  backdrop-filter: blur(20px);
}

.brand {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  min-width: max-content;
  font-weight: 800;
}

.brand-mark {
  display: grid;
  width: 38px;
  height: 38px;
  place-items: center;
  border-radius: 13px;
  background: linear-gradient(135deg, var(--nibo-blue), #79e2ff);
  color: #fff;
  font-size: 24px;
  line-height: 1;
  box-shadow: 0 10px 20px rgba(0, 174, 236, 0.25);
}

.brand-text {
  color: var(--nibo-text);
  font-size: 20px;
  letter-spacing: -0.03em;
}

.brand-subtitle {
  padding: 3px 8px;
  border-radius: 999px;
  background: var(--nibo-blue-soft);
  color: var(--nibo-blue-deep);
  font-size: 12px;
}

.nav-links {
  display: inline-flex;
  gap: 6px;
  padding: 5px;
  border: 1px solid rgba(0, 174, 236, 0.1);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.7);
}

.nav-links a {
  padding: 8px 14px;
  border-radius: 999px;
  color: var(--nibo-muted);
  font-size: 14px;
  transition: 0.2s ease;
}

.nav-links a:hover,
.nav-links a.router-link-active {
  background: var(--nibo-blue-soft);
  color: var(--nibo-blue-deep);
}

.header-search {
  min-width: 220px;
}

.header-actions {
  display: inline-flex;
  align-items: center;
  justify-content: flex-end;
  gap: 10px;
}

.upload-button {
  box-shadow: 0 10px 22px rgba(0, 174, 236, 0.24);
}

.user-entry {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 5px 11px 5px 5px;
  border: 1px solid rgba(0, 174, 236, 0.14);
  border-radius: 999px;
  background: #fff;
  color: var(--nibo-text);
  cursor: pointer;
}

.user-entry img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
}

@media (max-width: 1060px) {
  .app-header {
    grid-template-columns: 1fr auto;
  }

  .nav-links {
    order: 3;
    grid-column: 1 / -1;
    justify-content: center;
  }

  .header-search {
    order: 4;
    grid-column: 1 / -1;
  }
}

@media (max-width: 640px) {
  .app-header {
    gap: 12px;
    padding: 12px;
  }

  .brand-subtitle,
  .upload-button span {
    display: none;
  }

  .header-actions {
    gap: 6px;
  }
}
</style>
