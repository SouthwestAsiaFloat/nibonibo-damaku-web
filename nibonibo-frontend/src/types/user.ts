export interface User {
  id: string
  username: string
  nickname: string
  avatar: string
  bio: string
  followers: number
  following: number
}

export interface LoginForm {
  account: string
  password: string
}

export interface RegisterForm {
  username: string
  password: string
  confirmPassword: string
}

export interface AuthResult {
  token: string
  user: User
}
