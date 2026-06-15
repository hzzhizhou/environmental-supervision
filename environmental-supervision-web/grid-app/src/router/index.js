import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user.js'
const Layout = () => import('../views/Layout.vue')
const Tasks = () => import('../views/Tasks.vue')
const Confirm = () => import('../views/Confirm.vue')
const ConfirmedList = () => import('../views/ConfirmedList.vue')
const GridStats = () => import('../views/GridStats.vue')
const GridProfile = () => import('../views/GridProfile.vue')

const routes = [
    {
        path: '/',
        component: Layout,
        redirect: '/tasks',
        children: [
            { path: '/tasks', component: Tasks },
            { path: '/confirm', component: Confirm },
            { path: '/confirmed', component: ConfirmedList },
            { path: '/stats', component: GridStats },
            { path: '/profile', component: GridProfile },
        ]
    }
]

const router = createRouter({
    history: createWebHistory('/grid'),
    routes,
})
router.beforeEach(()=>{
    const user=useUserStore()
    user.getUserMessage()
})

export default router