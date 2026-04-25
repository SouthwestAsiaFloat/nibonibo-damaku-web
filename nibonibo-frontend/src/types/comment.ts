import type { User } from './user'

export interface Comment {
  id: string
  videoId: string
  user: Pick<User, 'id' | 'nickname' | 'avatar'>
  content: string
  likes: number
  createdAt: string
}
