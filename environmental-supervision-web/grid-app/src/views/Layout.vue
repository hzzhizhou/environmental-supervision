<script setup>
import { List, Checked, DataLine, User } from '@element-plus/icons-vue'
import { useUserStore } from '../store/user';
import { storeToRefs } from 'pinia'
import img1 from '../assets/1.webp'
const currentUser=useUserStore()
const {user,avatar}=storeToRefs(currentUser)
const menuList = [
    { name: '待确认', path: '/tasks', icon: List },
    { name: '已审核', path: '/confirmed', icon: Checked },
    { name: '统计', path: '/stats', icon: DataLine },
    { name: '我的', path: '/profile', icon: User },
]

</script>
<template>
    <div class="grid-layout">
        <!-- PC 顶部导航 -->
        <header class="top-nav">
            <div class="logo">东软环保监督</div>
            <nav class="nav-menu">
                <span
                    v-for="item in menuList"
                    :key="item.path"
                    class="nav-item"
                    :class="{ active: $route.path ===  item.path }"
                    @click="$router.push(item.path)"
                >
                    <el-icon><component :is="item.icon" /></el-icon>
                    {{ item.name }}
                    
                </span>
            </nav>
            <div class="user-info">
                <el-avatar :size="28" :src="avatar||img1"></el-avatar>
                <span>{{ user?.realName }}</span>
            </div>
        </header>

        <main class="content">
            <RouterView />
        </main>

        <footer class="bottom-nav">
            <div
                v-for="item in menuList"
                :key="item.path"
                class="bottom-item"
                :class="{ active: $route.path ===  item.path }"
                @click="$router.push(item.path)"
            >
                <el-icon :size="20"><component :is="item.icon" /></el-icon>
                <span>{{ item.name }}</span>
            </div>
        </footer>
    </div>
</template>



<style scoped>
.grid-layout {
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
    color: #1E40AF;
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
    background: #EFF6FF;
    color: #1E40AF;
}

.nav-item.active {
    background: #DBEAFE;
    color: #1E40AF;
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
    color: #1E40AF;
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