<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '../stores/user'
import type { LoginForm } from '../types/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive<LoginForm>({
  account: 'nibo_user',
  password: '123456',
})

const rules = reactive<FormRules<LoginForm>>({
  account: [{ required: true, message: '请输入手机号或用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
})

async function submit() {
  await formRef.value?.validate()
  loading.value = true

  try {
    await userStore.login(form)
    ElMessage.success('登录成功，token 已保存到 localStorage')

    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/profile'
    router.replace(redirect)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-page page-shell">
    <section class="auth-card panel-card">
      <div class="auth-visual">
        <span>LOGIN</span>
        <h1>欢迎回到 nibonibo</h1>
        <p>这里先用 mock 登录成功，后续接后端时只需要替换 API 层。</p>
      </div>

      <el-form ref="formRef" class="auth-form" :model="form" :rules="rules" label-position="top">
        <h2>账号登录</h2>
        <el-form-item label="手机号 / 用户名" prop="account">
          <el-input v-model="form.account" placeholder="请输入手机号或用户名" size="large" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" show-password size="large" type="password" />
        </el-form-item>

        <el-button class="submit-button" type="primary" size="large" :loading="loading" @click="submit">
          登录
        </el-button>

        <p class="auth-link">
          还没有账号？
          <RouterLink to="/register">去注册</RouterLink>
        </p>
      </el-form>
    </section>
  </div>
</template>

<style scoped>
.auth-page {
  display: grid;
  min-height: calc(100vh - 128px);
  place-items: center;
}

.auth-card {
  display: grid;
  grid-template-columns: 0.9fr 1fr;
  width: min(920px, 100%);
  overflow: hidden;
}

.auth-visual {
  display: grid;
  align-content: end;
  min-height: 520px;
  padding: 34px;
  background:
    radial-gradient(circle at 30% 20%, rgba(255, 255, 255, 0.92), transparent 8rem),
    linear-gradient(150deg, var(--nibo-blue), #77e4ff 55%, #fff0f6);
  color: #fff;
}

.auth-visual span {
  width: max-content;
  margin-bottom: 12px;
  padding: 6px 12px;
  border: 1px solid rgba(255, 255, 255, 0.45);
  border-radius: 999px;
  font-weight: 800;
  letter-spacing: 0.15em;
}

.auth-visual h1 {
  margin: 0;
  font-size: 42px;
  line-height: 1.1;
}

.auth-visual p {
  max-width: 320px;
  margin: 16px 0 0;
  line-height: 1.8;
}

.auth-form {
  display: grid;
  align-content: center;
  padding: 48px;
}

.auth-form h2 {
  margin: 0 0 24px;
  color: var(--nibo-text);
  font-size: 28px;
}

.submit-button {
  width: 100%;
  margin-top: 8px;
}

.auth-link {
  margin: 18px 0 0;
  color: var(--nibo-muted);
  text-align: center;
}

.auth-link a {
  color: var(--nibo-blue-deep);
  font-weight: 700;
}

@media (max-width: 760px) {
  .auth-card {
    grid-template-columns: 1fr;
  }

  .auth-visual {
    min-height: 260px;
  }

  .auth-form {
    padding: 30px;
  }
}
</style>
