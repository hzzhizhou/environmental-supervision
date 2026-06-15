import { createWebHistory, createRouter } from 'vue-router'
import { useUserStore } from '../stores/user.js'

const DataList = () => import('../components/MainContent/Data-list.vue')
const ConfirmList = () => import('../components/MainContent/ConfirmList.vue')
const AuditLog = () => import('../components/MainContent/AuditLog.vue')
const ProviceStat = () => import('../components/MainContent/ProviceStat.vue')
const DistriBution = () => import('../components/MainContent/Distribution.vue')
const Tren = () => import('../components/MainContent/Trend.vue')
const OtherStat = () => import('../components/MainContent/OtherStat.vue')
const UserManage = () => import('../components/MainContent/UserManage.vue')
const GridConfig = () => import('../components/MainContent/GridConfig.vue')
const Layout=()=>import('../views/Layout.vue')
const routes = [
    {
        path: '/',
        component: Layout,
        redirect: '/data-list',
        children: [
            { path: 'data-list', component: DataList },           // 去掉 /
            { path: 'confirm-list', component: ConfirmList },
            { path: 'audit-log', component: AuditLog },
            { path: 'province-stat', component: ProviceStat },
            { path: 'distribution', component: DistriBution },
            { path: 'trend', component: Tren },
            { path: 'other-stat', component: OtherStat },
            { path: 'user-manage', component: UserManage },
            { path: 'grid-config', component: GridConfig },
        ]
    }
]
const router = createRouter({
    history: createWebHistory('/admin'),
    routes,
})
router.beforeEach(async()=>{
  const user=useUserStore()
  user.fetchUser()
})
export default router