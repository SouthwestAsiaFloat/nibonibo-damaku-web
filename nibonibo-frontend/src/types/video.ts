import type { User } from './user'

export interface Video {
  id: string
  title: string
  description: string
  coverUrl: string
  videoUrl: string
  up: Pick<User, 'id' | 'nickname' | 'avatar'>
  playCount: number
  likeCount: number
  favoriteCount: number
  coinCount: number
  tags: string[]
  duration: string
  publishedAt: string
}
