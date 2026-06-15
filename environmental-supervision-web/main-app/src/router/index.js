import { createRouter, createWebHistory } from 'vue-router'
const DeciderFrame = () => import('../views/DeciderFrame.vue')
const Login = () => import('../views/LoginLayout.vue')

const routes = [
  { path: '/', redirect: '/login?role=1' },
  { path: '/login', component: Login },
  { path: '/register', component: Login },
  // 不需要 Layout
  // 不需要匹配子应用路由
 { path: '/decider', component: DeciderFrame },
  { path: '/admin/:path(.*)*', component: { template: '<div></div>' } },
    { path: '/supervisor/:path(.*)*', component: { template: '<div></div>' } },
    { path: '/grid/:path(.*)*', component: { template: '<div></div>' } },

]

const router = createRouter({
  history: createWebHistory(),
  routes
})
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    const publicPaths = ['/login', '/register']
     if (to.path === '/login' && !to.query.role) {
    next('/login?role=1')
    return 
  } 
    if (token || publicPaths.includes(to.path)) {
        next()
    } else {
        next('/login?role=1')
    }
})
export default router