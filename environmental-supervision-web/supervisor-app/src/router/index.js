import { createRouter, createWebHistory } from 'vue-router'
import { useAlertsStore } from '../store/user.js'

const Report = () => import('../views/Report.vue')
const Records = () => import('../views/Records.vue')
const Progress = () => import('../views/Progress.vue')
const Profile = () => import('../views/Profile.vue')
const Layout = () => import('../views/Layout.vue')
const ReportDetail=()=>import('../views/ReportDetail.vue')
const routes = [
    {
        path: '/',
        component: Layout,
        redirect: '/report',
        children: [
            { path: '/report', component: Report },
            { path: '/records', component: Records },
            { path: '/progress', component: Progress },
            { path: '/profile', component: Profile},
            {path:'/report-detail/:id',component:ReportDetail}
        ]
    }
]

const router = createRouter({
    history: createWebHistory('/supervisor'),
    routes,
})
router.beforeEach(async (to,from)=>{
   const users=useAlertsStore()
   users.fetchUser()
})

export default router