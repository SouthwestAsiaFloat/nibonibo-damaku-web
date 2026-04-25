import type { Danmaku } from '../types/danmaku'

export const mockDanmaku: Record<string, Danmaku[]> = {
  '1': [
    {
      id: 'd-1-1',
      videoId: '1',
      user: {
        id: '301',
        nickname: '夜航模式',
        avatar: 'https://api.dicebear.com/9.x/thumbs/svg?seed=night',
      },
      content: '开场这个蓝色太 B 站了',
      color: '#ffffff',
      time: 12,
      createdAt: '2026-04-21 00:01',
    },
    {
      id: 'd-1-2',
      videoId: '1',
      user: {
        id: '302',
        nickname: '像素企鹅',
        avatar: 'https://api.dicebear.com/9.x/thumbs/svg?seed=pixel',
      },
      content: '前方高能！',
      color: '#00e5ff',
      time: 32,
      createdAt: '2026-04-21 00:03',
    },
  ],
  '7': [
    {
      id: 'd-7-1',
      videoId: '7',
      user: {
        id: '303',
        nickname: '弹幕同步率',
        avatar: 'https://api.dicebear.com/9.x/thumbs/svg?seed=sync',
      },
      content: '这波同步笑死',
      color: '#fff2a8',
      time: 18,
      createdAt: '2026-04-07 21:14',
    },
  ],
}

export const fallbackDanmaku: Danmaku[] = [
  {
    id: 'd-fallback-1',
    videoId: 'fallback',
    user: {
      id: '304',
      nickname: '空降成功',
      avatar: 'https://api.dicebear.com/9.x/thumbs/svg?seed=landing',
    },
    content: '打卡！这个页面看着挺清爽',
    color: '#ffffff',
    time: 8,
    createdAt: '2026-04-20 16:22',
  },
  {
    id: 'd-fallback-2',
    videoId: 'fallback',
    user: {
      id: '305',
      nickname: '一键三连',
      avatar: 'https://api.dicebear.com/9.x/thumbs/svg?seed=triple',
    },
    content: '点赞收藏投币先安排',
    color: '#72e7ff',
    time: 21,
    createdAt: '2026-04-20 16:23',
  },
]
