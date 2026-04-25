<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '../stores/user'
import type { RegisterForm } from '../types/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive<RegisterForm>({
  username: '',
  password: '',
  confirmPassword: '',
})

const rules = reactive<FormRules<RegisterForm>>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 18, message: '用户名长度为 2-18 个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少 6 位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (_rule, value: string, callback) => {
        if (value !== form.password) {
          callback(new Error('两次输入的密码不一致'))
          return
        }

        callback()
      },
      trigger: 'blur',
    },
  ],
})

async function submit() {
  await formRef.value?.validate()
  loading.value = true

  try {
    await userStore.register(form)
    ElMessage.success('注册成功，已自动登录')
    router.replace('/profile')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-page page-shell">
    <section class="auth-card panel-card">
      <div class="auth-visual">
        <span>JOIN</span>
        <h1>创建你的 UP 主身份</h1>
        <p>先把前端体验走通：注册 mock 成功后会写入 token 和用户信息。</p>
      </div>

      <el-form ref="formRef" class="auth-form" :model="form" :rules="rules" label-position="top">
        <h2>注册账号</h2>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="给自己取个昵称" size="large" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="至少 6 位" show-password size="large" type="password" />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            placeholder="再输入一次密码"
            show-password
            size="large"
            type="password"
          />
        </el-form-item>

        <el-button class="submit-button" type="primary" size="large" :loading="loading" @click="submit">
          注册
        </el-button>

        <p class="auth-link">
          已经有账号？
          <RouterLink to="/login">去登录</RouterLink>
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
  min-height: 540px;
  padding: 34px;
  background:
    radial-gradient(circle at 65% 20%, rgba(255, 255, 255, 0.9), transparent 8rem),
    linear-gradient(150deg, #33c7f5, var(--nibo-blue) 55%, #ffbfd3);
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
