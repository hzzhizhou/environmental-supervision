import { createRouter, createWebHistory } from 'vue-router'

const Dashboard = () => import('../views/Dashboard.vue')

const routes = [
    { path: '/', component: Dashboard }
]

const router = createRouter({
    history: createWebHistory('/decider'),
    routes,
})

export default router