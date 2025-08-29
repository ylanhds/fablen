// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import UserLogin from '../views/UserLogin.vue'
import UserDashboard from '../views/UserDashboard.vue'
import GithubCallback from '../views/GithubCallback.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: UserLogin
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: UserDashboard
  },
  {
    path: '/login/oauth2/code/github',
    name: 'GithubCallback',
    component: GithubCallback
  },
  {
    path: '/',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
