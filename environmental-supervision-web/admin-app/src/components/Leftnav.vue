<script setup>
import { useRoute, useRouter } from 'vue-router';
import { menuList } from '../config/menu' 
const router=useRouter()
const route=useRoute()
const navigate=(url)=>{
    router.push({path:url})
}
</script>
<template>
    <div class="sidebar">
        <!-- Logo -->
        <div class="sidebar-logo">
            <div class="logo-icon">🌿</div>
            <div>
                <h5>东软环保监督</h5>
                <small>公众监督系统</small>
            </div>
        </div>
        <!-- 菜单 -->
        <div class="nav-section" v-for="item in menuList">
            <div class="nav-title">{{ item.title }}</div>
            <div class="nav-item" :class="{active:route.path===children.path}" @click="navigate(children.path)" v-for="children in item.children">
                <el-icon><component :is="children.icon" /></el-icon>
                {{ children.name}}
                <span class="badge-count" v-if="children.badge">{{ children.badge }}</span>
            </div>
        </div>
    </div>
</template>

<style scoped>
.sidebar {
    width: 240px;
    background: #0F172A;
    color: #CBD5E1;
    position: fixed;
    height: 100vh;
    display: flex;
    flex-direction: column;
    z-index: 100;
}

.sidebar-logo {
    padding: 20px;
    border-bottom: 1px solid #1E293B;
    display: flex;
    align-items: center;
    gap: 10px;
}

.logo-icon {
    width: 38px;
    height: 38px;
    background: linear-gradient(135deg, #059669, #10B981);
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    flex-shrink: 0;
}

.sidebar-logo h5 {
    color: #fff;
    margin: 0;
    font-size: 14px;
    line-height: 1.3;
}

.sidebar-logo small {
    color: #64748B;
    font-size: 11px;
}

.nav-section {
    padding: 12px 0;
}

.nav-title {
    padding: 8px 20px;
    color: #64748B;
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 1.5px;
    font-weight: 600;
}

.nav-item {
    padding: 10px 20px;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 13px;
    border-left: 3px solid transparent;
    transition: all 0.15s;
}

.nav-item:hover {
    background: #1E293B;
    color: #fff;
}

.nav-item.active {
    background: rgba(37, 99, 235, 0.15);
    color: #60A5FA;
    border-left-color: #3B82F6;
    font-weight: 600;
}

.badge-count {
    background: #EF4444;
    color: #fff;
    font-size: 10px;
    padding: 2px 7px;
    border-radius: 10px;
    margin-left: auto;
}
</style>