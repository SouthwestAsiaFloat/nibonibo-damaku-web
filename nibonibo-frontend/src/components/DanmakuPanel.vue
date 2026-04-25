<script setup lang="ts">
import { ref } from 'vue'
import type { Danmaku } from '../types/danmaku'

defineProps<{
  danmakuList: Danmaku[]
  enabled: boolean
}>()

const emit = defineEmits<{
  'update:enabled': [value: boolean]
  send: [content: string]
}>()

const draft = ref('')

function submit() {
  const content = draft.value.trim()

  if (!content) {
    return
  }

  emit('send', content)
  draft.value = ''
}
</script>

<template>
  <section class="danmaku-panel panel-card">
    <div class="panel-head">
      <div>
        <h3>弹幕</h3>
        <p>{{ danmakuList.length }} 条正在屏幕里游泳</p>
      </div>
      <el-switch
        :model-value="enabled"
        active-text="开"
        inactive-text="关"
        @update:model-value="emit('update:enabled', Boolean($event))"
      />
    </div>

    <div class="send-row">
      <el-input
        v-model="draft"
        maxlength="40"
        placeholder="发一条友善弹幕吧"
        show-word-limit
        @keyup.enter="submit"
      />
      <el-button type="primary" @click="submit">发送</el-button>
    </div>

    <div class="danmaku-list">
      <div v-for="item in danmakuList" :key="item.id" class="danmaku-item">
        <span class="time">{{ item.time }}s</span>
        <span class="content">{{ item.content }}</span>
        <span class="author">{{ item.user.nickname }}</span>
      </div>
    </div>
  </section>
</template>

<style scoped>
.danmaku-panel {
  padding: 18px;
}

.panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14px;
  margin-bottom: 16px;
}

.panel-head h3 {
  margin: 0;
  font-size: 18px;
}

.panel-head p {
  margin: 4px 0 0;
  color: var(--nibo-muted);
  font-size: 13px;
}

.send-row {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 10px;
  margin-bottom: 16px;
}

.danmaku-list {
  display: grid;
  gap: 8px;
  max-height: 240px;
  overflow: auto;
  padding-right: 4px;
}

.danmaku-item {
  display: grid;
  grid-template-columns: 44px 1fr auto;
  align-items: center;
  gap: 8px;
  padding: 9px 10px;
  border-radius: 12px;
  background: #f5fbff;
  color: var(--nibo-text);
  font-size: 13px;
}

.time,
.author {
  color: var(--nibo-muted);
  font-size: 12px;
}

.content {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

@media (max-width: 520px) {
  .send-row {
    grid-template-columns: 1fr;
  }

  .danmaku-item {
    grid-template-columns: 40px 1fr;
  }

  .author {
    display: none;
  }
}
</style>
