import type { Comment } from '../types/comment'

export const mockComments: Record<string, Comment[]> = {
  '1': [
    {
      id: 'c-1-1',
      videoId: '1',
      user: {
        id: '201',
        nickname: '蓝色汽水',
        avatar: 'https://api.dicebear.com/9.x/thumbs/svg?seed=soda',
      },
      content: '这个城市光效很有味道，像凌晨下班路上的精神补给。',
      likes: 98,
      createdAt: '2026-04-21 00:32',
    },
    {
      id: 'c-1-2',
      videoId: '1',
      user: {
        id: '202',
        nickname: '没睡醒的 CSS',
        avatar: 'https://api.dicebear.com/9.x/thumbs/svg?seed=css',
      },
      content: '封面一出来我就点进来了，前端也可以很电影。',
      likes: 67,
      createdAt: '2026-04-21 08:14',
    },
  ],
  '2': [
    {
      id: 'c-2-1',
      videoId: '2',
      user: {
        id: '203',
        nickname: '路由守卫',
        avatar: 'https://api.dicebear.com/9.x/thumbs/svg?seed=router',
      },
      content: '讲得很清楚，适合拿来当项目第一版的骨架。',
      likes: 142,
      createdAt: '2026-04-19 11:20',
    },
  ],
}

export const fallbackComments: Comment[] = [
  {
    id: 'c-fallback-1',
    videoId: 'fallback',
    user: {
      id: '204',
      nickname: '首页巡逻员',
      avatar: 'https://api.dicebear.com/9.x/thumbs/svg?seed=patrol',
    },
    content: '先把前端跑起来就很舒服，后端接口以后慢慢接。',
    likes: 35,
    createdAt: '2026-04-20 19:45',
  },
  {
    id: 'c-fallback-2',
    videoId: 'fallback',
    user: {
      id: '205',
      nickname: '弹幕漂流瓶',
      avatar: 'https://api.dicebear.com/9.x/thumbs/svg?seed=bottle',
    },
    content: '这个布局已经有简化版视频社区的感觉了。',
    likes: 21,
    createdAt: '2026-04-21 13:08',
  },
]
