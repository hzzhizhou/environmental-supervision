<script setup>
import { useRouter } from 'vue-router'
import { HomeFilled } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user';
import { storeToRefs } from 'pinia';
const user=useUserStore()
const { userMessage } =storeToRefs(user)
const router = useRouter()
const edit = () => {
    window.__LOGOUT__()
}
</script>

<template>
    <div class="topbar">
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>
                <el-icon>
                    <HomeFilled />
                </el-icon>
                <span>首页</span>
            </el-breadcrumb-item>
            <el-breadcrumb-item>公众监督数据管理</el-breadcrumb-item>
            <el-breadcrumb-item>公众监督数据列表</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="user-area">
            <el-badge :value="23" type="danger">
                <span class="badge-text">待审核</span>
            </el-badge>
            <el-avatar size="small">A</el-avatar>
            <span class="username">{{ userMessage?.realName??'系统管理员' }}</span>
            <el-button type="danger" link @click="edit">退出</el-button>
        </div>
    </div>
</template>

<style scoped>
.topbar {
    background: #fff;
    padding: 14px 24px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #E2E8F0;
}

.user-area {
    display: flex;
    align-items: center;
    gap: 12px;
}

.badge-text {
    color: #606266;
    font-size: 13px;
}

.username {
    font-size: 13px;
    color: #303133;
}
</style>