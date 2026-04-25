import request from './request'
import type { Comment } from '../types/comment'
import type { Danmaku } from '../types/danmaku'
import type { User } from '../types/user'

interface BackendComment {
  id: string
  videoId: string
  userId: string
  parentId?: string
  rootId?: string
  content: string
  likeCount?: number | string
  createdAt?: string
}

interface BackendDanmaku {
  id: string
  videoId: string
  userId: string
  content: string
  timeOffset?: number | string
  color?: string
  fontSize?: number
  mode?: number
  createdAt?: string
}

function buildUser(userId: string): Pick<User, 'id' | 'nickname' | 'avatar'> {
  return {
    id: userId,
    nickname: `用户 ${userId.slice(-6)}`,
    avatar: `https://api.dicebear.com/9.x/thumbs/svg?seed=user-${userId}`,
  }
}

function toNumber(value: number | string | undefined, fallback = 0) {
  const next = Number(value)
  return Number.isFinite(next) ? next : fallback
}

function toComment(comment: BackendComment): Comment {
  return {
    id: String(comment.id),
    videoId: String(comment.videoId),
    user: buildUser(String(comment.userId)),
    content: comment.content,
    likes: toNumber(comment.likeCount),
    createdAt: comment.createdAt ? comment.createdAt.replace('T', ' ').slice(0, 16) : '',
  }
}

function toDanmaku(item: BackendDanmaku): Danmaku {
  return {
    id: String(item.id),
    videoId: String(item.videoId),
    user: buildUser(String(item.userId)),
    content: item.content,
    color: item.color || '#ffffff',
    time: Math.floor(toNumber(item.timeOffset) / 1000),
    createdAt: item.createdAt ? item.createdAt.replace('T', ' ').slice(0, 16) : '',
  }
}

export async function getComments(videoId: string): Promise<Comment[]> {
  const data = (await request.get(`/comments/video/${videoId}`)) as BackendComment[]
  return data.map(toComment)
}

export async function sendComment(videoId: string, content: string): Promise<Comment> {
  const data = (await request.post('/comments', {
    videoId,
    content,
  })) as BackendComment
  return toComment(data)
}

export async function getDanmaku(videoId: string): Promise<Danmaku[]> {
  const data = (await request.get(`/danmaku/video/${videoId}`)) as BackendDanmaku[]
  return data.map(toDanmaku)
}

export async function sendDanmaku(videoId: string, content: string): Promise<Danmaku> {
  const data = (await request.post('/danmaku', {
    videoId,
    content,
    timeOffset: 0,
    color: '#ffffff',
    fontSize: 25,
    mode: 1,
  })) as BackendDanmaku
  return toDanmaku(data)
}

export async function likeVideo(videoId: string) {
  await request.post(`/interactions/videos/${videoId}/like`)
}

export async function unlikeVideo(videoId: string) {
  await request.delete(`/interactions/videos/${videoId}/like`)
}

export async function favoriteVideo(videoId: string) {
  await request.post(`/favorites/videos/${videoId}`)
}

export async function unfavoriteVideo(videoId: string) {
  await request.delete(`/favorites/videos/${videoId}`)
}
