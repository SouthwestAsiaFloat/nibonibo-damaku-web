import type { User } from '../types/user'
import type { Video } from '../types/video'

export const mockUser: User = {
  id: 1001,
  username: 'nibo_user',
  nickname: '妮啵放映员',
  avatar: 'https://api.dicebear.com/9.x/thumbs/svg?seed=nibonibo',
  bio: '热爱剪辑、游戏和深夜补番。这里先是一座前端小岛，后面再接后端大陆。',
  followers: 2468,
  following: 36,
}

export const mockCollectedVideoIds: Video['id'][] = ['2', '4', '6']
