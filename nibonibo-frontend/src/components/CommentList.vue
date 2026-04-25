<script setup lang="ts">
import { ref } from 'vue'
import { ChatDotRound, Pointer } from '@element-plus/icons-vue'
import type { Comment } from '../types/comment'

defineProps<{
  comments: Comment[]
  canComment: boolean
}>()

const emit = defineEmits<{
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
  <section class="comment-list panel-card">
    <div class="section-title">
      <h2>
        <el-icon><ChatDotRound /></el-icon>
        评论区
      </h2>
      <span class="muted">{{ comments.length }} 条评论</span>
    </div>

    <div class="comment-editor">
      <el-input
        v-model="draft"
        :disabled="!canComment"
        maxlength="160"
        placeholder="登录后一起讨论这支视频"
        show-word-limit
        type="textarea"
        @keyup.ctrl.enter="submit"
      />
      <el-button type="primary" :disabled="!canComment" @click="submit">发表评论</el-button>
    </div>

    <div class="items">
      <article v-for="comment in comments" :key="comment.id" class="comment-item">
        <img :src="comment.user.avatar" :alt="comment.user.nickname" />
        <div>
          <div class="comment-meta">
            <strong>{{ comment.user.nickname }}</strong>
            <span>{{ comment.createdAt }}</span>
          </div>
          <p>{{ comment.content }}</p>
          <button type="button">
            <el-icon><Pointer /></el-icon>
            {{ comment.likes }}
          </button>
        </div>
      </article>
    </div>
  </section>
</template>

<style scoped>
.comment-list {
  padding: 22px;
}

.section-title h2 {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.comment-editor {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 12px;
  margin-bottom: 22px;
}

.items {
  display: grid;
  gap: 16px;
}

.comment-item {
  display: grid;
  grid-template-columns: 46px 1fr;
  gap: 12px;
  padding: 14px;
  border: 1px solid rgba(0, 174, 236, 0.08);
  border-radius: 16px;
  background: rgba(245, 251, 255, 0.75);
}

.comment-item img {
  width: 46px;
  height: 46px;
  border-radius: 50%;
}

.comment-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 6px;
}

.comment-meta strong {
  color: var(--nibo-text);
}

.comment-meta span {
  color: var(--nibo-muted);
  font-size: 12px;
}

.comment-item p {
  margin: 0 0 10px;
  color: #415066;
  line-height: 1.7;
}

.comment-item button {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 0;
  border: 0;
  background: transparent;
  color: var(--nibo-muted);
  cursor: pointer;
}

@media (max-width: 640px) {
  .comment-editor {
    grid-template-columns: 1fr;
  }
}
</style>
