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
    component: () => import('../views/Tab.vue'),
    meta: {
      title: 'reTools-新标签页'
    }
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})


router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
});

export default router
