import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { login as mockLogin, register as mockRegister } from '../api/user'
import type { LoginForm, RegisterForm, User } from '../types/user'

const TOKEN_KEY = 'nibonibo_token'
const USER_KEY = 'nibonibo_user'

function readStoredUser(): User | null {
  const raw = localStorage.getItem(USER_KEY)

  if (!raw) {
    return null
  }

  try {
    return JSON.parse(raw) as User
  } catch {
    localStorage.removeItem(USER_KEY)
    return null
  }
}

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem(TOKEN_KEY) ?? '')
  const currentUser = ref<User | null>(readStoredUser())
  const isLoggedIn = computed(() => Boolean(token.value))

  function persist(nextToken: string, user: User) {
    token.value = nextToken
    currentUser.value = user
    localStorage.setItem(TOKEN_KEY, nextToken)
    localStorage.setItem(USER_KEY, JSON.stringify(user))
  }

  async function login(payload: LoginForm) {
    const result = await mockLogin(payload)
    persist(result.token, result.user)
    return result.user
  }

  async function register(payload: RegisterForm) {
    const result = await mockRegister(payload)
    persist(result.token, result.user)
    return result.user
  }

  function logout() {
    token.value = ''
    currentUser.value = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  return {
    token,
    currentUser,
    isLoggedIn,
    login,
    register,
    logout,
  }
})
