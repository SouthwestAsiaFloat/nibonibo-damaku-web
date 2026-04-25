import { mockUser } from '../mock/user'
import type { AuthResult, LoginForm, RegisterForm } from '../types/user'

const wait = (ms = 260) => new Promise((resolve) => window.setTimeout(resolve, ms))

export async function login(payload: LoginForm): Promise<AuthResult> {
  await wait()

  return {
    token: `mock-token-${Date.now()}`,
    user: {
      ...mockUser,
      username: payload.account,
      nickname: payload.account === 'nibo_user' ? mockUser.nickname : payload.account,
    },
  }
}

export async function register(payload: RegisterForm): Promise<AuthResult> {
  await wait()

  return {
    token: `mock-register-token-${Date.now()}`,
    user: {
      ...mockUser,
      id: Date.now(),
      username: payload.username,
      nickname: payload.username,
      bio: '刚刚加入 nibonibo，正在准备第一支投稿。',
      followers: 0,
      following: 0,
    },
  }
}
