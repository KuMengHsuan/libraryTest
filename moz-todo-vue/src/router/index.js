import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../components/library/HomeView.vue'
import LoginView from '../components/library/LoginView.vue'
import RecordView from '../components/library/RecordView.vue'
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/record',
    name: 'record',
    component: RecordView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router