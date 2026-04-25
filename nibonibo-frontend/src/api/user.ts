import request from './request'
import type { AuthResult, LoginForm, RegisterForm, User } from '../types/user'

interface BackendUserVO {
  id: string
  username: string
  nickname: string
  avatar?: string
  bio?: string
  token?: string
}

function toUser(vo: BackendUserVO): User {
  return {
    id: vo.id,
    username: vo.username,
    nickname: vo.nickname || vo.username,
    avatar: vo.avatar || `https://api.dicebear.com/9.x/thumbs/svg?seed=${vo.username}`,
    bio: vo.bio || '这个人很神秘，还没有写简介。',
    followers: 0,
    following: 0,
  }
}

export async function login(payload: LoginForm): Promise<AuthResult> {
  const data = (await request.post('/users/login', {
    username: payload.account,
    password: payload.password,
  })) as BackendUserVO

  return {
    token: data.token || '',
    user: toUser(data),
  }
}

export async function register(payload: RegisterForm): Promise<AuthResult> {
  const data = (await request.post('/users/register', {
    username: payload.username,
    password: payload.password,
    nickname: payload.username,
  })) as BackendUserVO

  return {
    token: data.token || '',
    user: toUser(data),
  }
}

export async function getMe(): Promise<User> {
  const data = (await request.get('/users/me')) as BackendUserVO
  return toUser(data)
}
