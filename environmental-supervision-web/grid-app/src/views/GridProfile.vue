<script setup>
import {  DataLine,  Setting, ArrowRight } from '@element-plus/icons-vue'
import { useUserStore } from '../store/user';
import { storeToRefs } from 'pinia'
import UserInfo from '../components/UserInfo.vue';
import EditDialog from '../components/EditDialog.vue';
import img1 from '../assets/1.webp'
import { ref } from 'vue'
import { updateAvatar,updateUser,updatePassword } from '../../../supervisor-app/src/api/user.js';
import { getUser } from '../api/user.js';
const currentUser = useUserStore()
const {user,avatar,userId}=storeToRefs(currentUser)
//创建时间
const getWorkAge = (time) => {
  if (!time) return '--'
  const now = new Date()
  const start = new Date(time)
  const diffMonths = (now.getFullYear() - start.getFullYear()) * 12 + now.getMonth() - start.getMonth()
  const years = Math.floor(diffMonths / 12)
  const months = diffMonths % 12
  if (years === 0) return `${months}个月`
  if (months === 0) return `${years}年`
  return `${years}年${months}个月`
}
//退出登录
const logout=()=>{
    window.__LOGOUT__()
}
//弹窗
const nameDialog=ref(null)
const passwordDialog=ref(null)
const phoneDialog=ref(null)
const infoDialog=ref(null)
//保存
const handleSave=async(newuserInfo)=>{
    if(newuserInfo.field==='password'){
        await updatePassword({userId:userId,oldPassword:newuserInfo.oldPassword,
            newPassword:newuserInfo.newPassword
        })
    }
}
const handleAvatar=()=>{

}
</script>
<template>
    <div class="profile">
          <EditDialog ref="nameDialog" type="name" :value="user?.realName"  @save="handleSave" />
    <!-- 修改密码弹窗 -->
    <EditDialog ref="passwordDialog" type="password" @save="handleSave" :user="user"/>
    <!-- 修改电话弹窗 -->
     <EditDialog ref="phoneDialog" type="phone" :value="user?.phone" @save="handleSave" />
      <UserInfo ref="infoDialog" :avatar="avatar" :user="user" @edit-name="nameDialog.open()" @edit-password="passwordDialog.open()" @edit-phone="phoneDialog.open()" 
      @edit-avatar="handleAvatar" />
        <div class="user-header">
            <el-avatar :size="64" :src="avatar||img1"> </el-avatar>
            <div class="name">{{ user?.realName }}</div>
            <div class="role">{{ user?.roleName }}</div>
            <div class="grid">城东一网格 · 辽宁省沈阳市</div>
        </div>

        <div class="info-card">
            <div class="info-row">
                <span class="label">工号</span>
                <span class="value">{{ user?.gridId }}</span>
            </div>
            <div class="info-row">
                <span class="label">绑定手机</span>
                <span class="value">{{ user?.phone }}</span>
            </div>
            <div class="info-row">
                <span class="label">入职工龄</span>
                <span class="value">{{ getWorkAge(user?.createdAt) }}</span>
            </div>
            <div class="info-row">
                <span class="label">累计完成数量</span>
                <span class="value" style="color:#F59E0B">128次</span>
            </div>
        </div>

        <div class="menu-list">
            <div class="menu-item" @click="infoDialog.open()">
                <el-icon size="18" color="#7C3AED">
                    <DataLine />
                </el-icon>
                <span>编辑个人资料</span>
                <el-icon color="#CBD5E1">
                    <ArrowRight /> 
                </el-icon>
            </div>
            <div class="menu-item">
                <el-icon size="18" color="#64748B">
                    <Setting />
                </el-icon>
                <span>设置</span>
                <el-icon color="#CBD5E1">
                    <ArrowRight />
                </el-icon>
            </div>
        </div>

        <el-button type="danger" plain size="large" class="logout-btn" @click="logout">退出登录</el-button>
        <p class="version">东软环保监督系统</p>
    </div>
</template>



<style scoped>
.profile {
    max-width: 600px;
    margin: 0 auto;
    padding: 16px;
}

.user-header {
    text-align: center;
    padding: 20px 0;
}

.name {
    font-size: 18px;
    font-weight: 700;
    margin-top: 10px;
    color: #1E293B;
}

.role {
    font-size: 12px;
    color: #1E40AF;
    font-weight: 500;
    margin-top: 2px;
}

.grid {
    font-size: 11px;
    color: #94A3B8;
    margin-top: 4px;
}

.info-card {
    background: #EFF6FF;
    border-radius: 14px;
    padding: 16px;
    margin: 16px 0;
}

.info-row {
    display: flex;
    justify-content: space-between;
    padding: 8px 0;
    font-size: 13px;
    border-bottom: 1px solid #DBEAFE;
}

.info-row:last-child {
    border-bottom: none;
}

.label {
    color: #64748B;
}

.value {
    font-weight: 600;
    color: #1E40AF;
}

.menu-list {
    background: #fff;
    border-radius: 14px;
    border: 1px solid #E2E8F0;
    overflow: hidden;
}

.menu-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 14px 16px;
    border-bottom: 1px solid #F1F5F9;
    cursor: pointer;
}

.menu-item:last-child {
    border-bottom: none;
}

.menu-item span {
    flex: 1;
    font-size: 14px;
    font-weight: 500;
    color: #334155;
}

.logout-btn {
    width: 100%;
    margin-top: 20px;
    height: 48px;
    font-size: 15px;
}

.version {
    text-align: center;
    font-size: 11px;
    color: #94A3B8;
    margin-top: 12px;
}

@media (max-width: 768px) {
    .profile {
        padding: 12px;
    }
}
</style>