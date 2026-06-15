<script setup>
import { House, List, DataLine, User } from '@element-plus/icons-vue'
import { useAlertsStore } from '../store/user';
import { storeToRefs } from 'pinia'
import img1 from '../assets/1.webp'
const users=useAlertsStore()
const {avatar,userInfo} =storeToRefs(users)
const menuList = [
    { name: '数据上报', path: '/report', icon: House },
    { name: '上报记录', path: '/records', icon: List },
    { name: '确认进度', path: '/progress', icon: DataLine },
    { name: '个人中心', path: '/profile', icon: User },
]


</script>
<template>
    <div class="supervisor-layout">
        <!-- PC 顶部导航 -->
        <header class="top-nav">
            <div class="logo">东软环保监督</div>
            <nav class="nav-menu">
                <span
                    v-for="item in menuList"
                    :key="item.path"
                    class="nav-item"
                    :class="{ active: $route.path === item.path }"
                    @click="$router.push(item.path)"
                >
                    <el-icon><component :is="item.icon" /></el-icon>
                    {{ item.name }}
                </span>
            </nav>
            <div class="user-info">
                <el-avatar :size="28" :src="avatar||img1"></el-avatar>
                <span>{{ userInfo?.realName||'默认用户' }}</span>
            </div>
        </header>

        <!-- 内容区 -->
        <main class="content">
            <RouterView />
        </main>

        <!-- 手机底部导航 -->
        <footer class="bottom-nav">
            <div
                v-for="item in menuList"
                :key="item.path"
                class="bottom-item"
                :class="{ active: $route.path === item.path }"
                @click="$router.push(item.path)"
            >
                <el-icon :size="20"><component :is="item.icon" /></el-icon>
                <span>{{ item.name }}</span>
            </div>
        </footer>
    </div>
</template>



<style scoped>
.supervisor-layout {
    min-height: 100vh;
    background: #F1F5F9;
    display: flex;
    flex-direction: column;
}

.top-nav {
    background: #fff;
    padding: 0 24px;
    height: 56px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1px solid #E2E8F0;
    flex-shrink: 0;
}

.logo {
    font-size: 16px;
    font-weight: 700;
    color: #059669;
}

.nav-menu {
    display: flex;
    gap: 4px;
}

.nav-item {
    padding: 8px 16px;
    border-radius: 8px;
    font-size: 13px;
    cursor: pointer;
    color: #475569;
    display: flex;
    align-items: center;
    gap: 6px;
    transition: all 0.2s;
}

.nav-item:hover {
    background: #F0FDF4;
    color: #059669;
}

.nav-item.active {
    background: #D1FAE5;
    color: #065F46;
    font-weight: 600;
}

.user-info {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 13px;
}

.content {
    flex: 1;
    padding: 24px;
    max-width: 800px;
    margin: 0 auto;
    width: 100%;
}

.bottom-nav {
    background: #fff;
    border-top: 1px solid #E2E8F0;
    display: none;
    justify-content: space-around;
    padding: 8px 0;
    flex-shrink: 0;
}

.bottom-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 2px;
    font-size: 10px;
    color: #94A3B8;
    cursor: pointer;
    padding: 4px 12px;
}

.bottom-item.active {
    color: #059669;
    font-weight: 600;
}

@media (max-width: 768px) {
    .top-nav { display: none; }
    .bottom-nav { display: flex; }
    .content {
        padding: 12px;
        max-width: 100%;
    }
}
</style>