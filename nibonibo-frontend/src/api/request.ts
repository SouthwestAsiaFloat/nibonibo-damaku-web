import axios from 'axios'
import { ElMessage } from 'element-plus'

interface BackendResult<T> {
  code: number
  message: string
  data: T
}

const TOKEN_KEY = 'nibonibo_token'
const USER_KEY = 'nibonibo_user'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 15000,
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY)

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

request.interceptors.response.use(
  (response) => {
    const payload = response.data as BackendResult<unknown>

    if (payload && typeof payload.code === 'number') {
      if (payload.code !== 0) {
        ElMessage.error(payload.message || '请求失败')
        return Promise.reject(new Error(payload.message || '请求失败'))
      }

      return payload.data
    }

    return response.data
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem(TOKEN_KEY)
      localStorage.removeItem(USER_KEY)
    }

    const message = error.response?.data?.message || error.message || '请求失败，请稍后再试'
    ElMessage.error(message)
    return Promise.reject(error)
  },
)

export default request
