import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/index',
    name: 'Index',
    component: () => import('../views/Index.vue')
  },
  {
    path: '/',
    redirect: '/index',
    component: () => import('../views/Index.vue')
  },
  {
    path: '/tab',
    name: 'Tab',
    component: () => import('../views/Tab.vue')
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
