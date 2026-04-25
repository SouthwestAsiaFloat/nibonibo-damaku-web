import { fallbackComments, mockComments } from '../mock/comments'
import { fallbackDanmaku, mockDanmaku } from '../mock/danmaku'
import { mockUser } from '../mock/user'
import type { Comment } from '../types/comment'
import type { Danmaku } from '../types/danmaku'
import type { User } from '../types/user'

const wait = (ms = 160) => new Promise((resolve) => window.setTimeout(resolve, ms))

export async function getComments(videoId: string): Promise<Comment[]> {
  await wait()
  return (mockComments[videoId] ?? fallbackComments).map((comment) => ({
    ...comment,
    videoId,
  }))
}

export async function sendComment(videoId: string, content: string, user?: User): Promise<Comment> {
  await wait()

  return {
    id: `c-${Date.now()}`,
    videoId,
    user: user ?? mockUser,
    content,
    likes: 0,
    createdAt: new Date().toLocaleString(),
  }
}

export async function getDanmaku(videoId: string): Promise<Danmaku[]> {
  await wait()
  return (mockDanmaku[videoId] ?? fallbackDanmaku).map((item) => ({
    ...item,
    videoId,
  }))
}

export async function sendDanmaku(videoId: string, content: string, user?: User): Promise<Danmaku> {
  await wait()

  return {
    id: `d-${Date.now()}`,
    videoId,
    user: user ?? mockUser,
    content,
    color: '#ffffff',
    time: Math.floor(Math.random() * 120),
    createdAt: new Date().toLocaleString(),
  }
}
