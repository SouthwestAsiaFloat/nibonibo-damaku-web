import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import ProfileView from '../views/ProfileView.vue'
import RegisterView from '../views/RegisterView.vue'
import UploadView from '../views/UploadView.vue'
import VideoDetailView from '../views/VideoDetailView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/video/:id',
      name: 'video-detail',
      component: VideoDetailView,
      props: true,
    },
    {
      path: '/upload',
      name: 'upload',
      component: UploadView,
      meta: {
        requiresAuth: true,
      },
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      meta: {
        requiresAuth: true,
      },
    },
  ],
  scrollBehavior() {
    return { top: 0 }
  },
})

router.beforeEach((to) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    return {
      path: '/login',
      query: {
        redirect: to.fullPath,
      },
    }
  }

  return true
})

export default router
