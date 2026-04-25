import { mockCollectedVideoIds } from '../mock/user'
import { mockVideos } from '../mock/videos'
import type { Video } from '../types/video'

const wait = (ms = 180) => new Promise((resolve) => window.setTimeout(resolve, ms))

export async function getVideoList(): Promise<Video[]> {
  await wait()
  return mockVideos
}

export async function getVideoDetail(id: string): Promise<Video | undefined> {
  await wait()
  return mockVideos.find((video) => video.id === id)
}

export async function getPublishedVideos(): Promise<Video[]> {
  await wait()
  return mockVideos.filter((video) => ['1', '6', '8'].includes(video.id))
}

export async function getCollectedVideos(): Promise<Video[]> {
  await wait()
  return mockVideos.filter((video) => mockCollectedVideoIds.includes(video.id))
}
