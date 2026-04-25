import type { User } from './user'

export interface Danmaku {
  id: string
  videoId: string
  user: Pick<User, 'id' | 'nickname' | 'avatar'>
  content: string
  color: string
  time: number
  createdAt: string
}
