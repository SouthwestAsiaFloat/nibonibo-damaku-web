import request from './request'
import type { User } from '../types/user'
import type { Video } from '../types/video'

interface BackendPage<T> {
  records: T[]
  total: number | string
  pageNum: number | string
  pageSize: number | string
}

interface BackendVideo {
  id: string
  userId?: string
  title: string
  description?: string
  coverUrl?: string
  videoUrl?: string
  duration?: number | string
  categoryId?: string
  status?: number
  viewCount?: number | string
  likeCount?: number | string
  favoriteCount?: number | string
  commentCount?: number | string
  createdAt?: string
  updatedAt?: string
}

export interface VideoCreatePayload {
  title: string
  description?: string
  coverUrl?: string
  videoUrl?: string
  duration?: number
  categoryId?: string
}

function toNumber(value: number | string | undefined, fallback = 0) {
  const next = Number(value)
  return Number.isFinite(next) ? next : fallback
}

function formatDuration(value: number | string | undefined) {
  const seconds = toNumber(value)
  const minutes = Math.floor(seconds / 60)
  const restSeconds = seconds % 60
  return `${String(minutes).padStart(2, '0')}:${String(restSeconds).padStart(2, '0')}`
}

function buildUp(video: BackendVideo): Pick<User, 'id' | 'nickname' | 'avatar'> {
  const userId = video.userId || '0'

  return {
    id: userId,
    nickname: userId === '0' ? 'nibonibo 用户' : `UP ${userId.slice(-6)}`,
    avatar: `https://api.dicebear.com/9.x/thumbs/svg?seed=up-${userId}`,
  }
}

function toVideo(video: BackendVideo): Video {
  return {
    id: String(video.id),
    title: video.title,
    description: video.description || '',
    coverUrl:
      video.coverUrl ||
      'https://images.unsplash.com/photo-1485846234645-a62644f84728?auto=format&fit=crop&w=900&q=80',
    videoUrl: video.videoUrl || '',
    up: buildUp(video),
    playCount: toNumber(video.viewCount),
    likeCount: toNumber(video.likeCount),
    favoriteCount: toNumber(video.favoriteCount),
    coinCount: 0,
    tags: video.categoryId ? [`分区 ${video.categoryId}`] : ['视频'],
    duration: formatDuration(video.duration),
    publishedAt: video.createdAt ? video.createdAt.replace('T', ' ').slice(0, 16) : '',
  }
}

export async function getVideoList(keyword = ''): Promise<Video[]> {
  const data = (await request.get('/videos/page', {
    params: {
      pageNum: 1,
      pageSize: 24,
      keyword: keyword || undefined,
    },
  })) as BackendPage<BackendVideo>

  return data.records.map(toVideo)
}

export async function getVideoDetail(id: string): Promise<Video | undefined> {
  const data = (await request.get(`/videos/${id}`)) as BackendVideo
  return data ? toVideo(data) : undefined
}

export async function getHotVideos(limit = 10): Promise<Video[]> {
  const data = (await request.get('/videos/hot', { params: { limit } })) as BackendVideo[]
  return data.map(toVideo)
}

export async function getPublishedVideos(): Promise<Video[]> {
  const data = (await request.get('/videos/mine')) as BackendVideo[]
  return data.map(toVideo)
}

export async function getCollectedVideos(): Promise<Video[]> {
  const data = (await request.get('/favorites/videos')) as BackendVideo[]
  return data.map(toVideo)
}

export async function createVideo(payload: VideoCreatePayload): Promise<Video> {
  const data = (await request.post('/videos', payload)) as BackendVideo
  return toVideo(data)
}
