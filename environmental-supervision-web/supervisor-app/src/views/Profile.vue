<script setup>
import { User, Trophy, DataLine, Setting, ArrowRight } from '@element-plus/icons-vue'
import { useAlertsStore } from '../store/user'
import EditDialog from '../components/EditDialog.vue'
import UserInfo from '../components/UserInfo.vue'
import PointsLog from '../components/PointsLog.vue'
import Ranking from '../components/Ranking.vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { updateAvatar, updatePassword,updateUser } from '../api/user.js'
import { storeToRefs } from 'pinia'
import img1 from '../assets/1.webp'
const nameDialog = ref(null)
const passwordDialog=ref(null)
const phoneDialog=ref(null)
const infoDialog=ref(null)

const users=useAlertsStore()
const {point,userInfo,userId,pointL,rankD,avatar}=storeToRefs(users)
users.fetchUser()
//保存
const handleSave=async(data)=>{
   if(data.field==='name'){
   await updateUser({userId:userId.value,realName:data.value})
   }
   else if(data.field==='phone'){
   await updateUser({userId:userId.value,phone:data.value})
   }else{
    await updatePassword({userId:userId.value,oldPassword:data.value.oldPassword,newPassword:data.value.newPassword})
   }
   users.fetchUser()   
}
//退出登录
const edit=()=>{
   window.__LOGOUT__()
}
//积分明细
const pointDialog=ref(null)
const pointopen=()=>{
    pointDialog.value.open(pointL.value)
}
//排行榜
const rankDialog=ref(null)
const rankingopen=()=>{
    rankDialog.value.open(rankD.value)
}
//修改头像
const handleAvatar=async (file)=>{
    const formDate=new FormData()
    formDate.append('userId',userId.value)    
    formDate.append('file',file)
    try{
        const res=await updateAvatar(formDate)
        if(res.data.code===200){
            ElMessage.success('头像上传成功') 
            users.fetchUser()  
        }
    }catch(err){
        ElMessage.error('头像上传失败')
    }
}



</script>

<template>
      <EditDialog ref="nameDialog" type="name" :value="userInfo?.realName"  @save="handleSave" />
    <!-- 修改密码弹窗 -->
    <EditDialog ref="passwordDialog" type="password" @save="handleSave" :userInfo="userInfo"/>
    <!-- 修改电话弹窗 -->
     <EditDialog ref="phoneDialog" type="phone" :value="userInfo?.phone" @save="handleSave" />
      <UserInfo ref="infoDialog" :avatar="avatar" :user-info="userInfo" @edit-name="nameDialog.open()" @edit-password="passwordDialog.open()" @edit-phone="phoneDialog.open()" 
      @edit-avatar="handleAvatar" />
      <PointsLog ref="pointDialog" />
      <Ranking ref="rankDialog" />
    <div class="profile">
        <div class="user-header">
            <el-avatar :size="64" :src="avatar||img1"></el-avatar>
            <div class="name">{{ userInfo?.realName}}</div>
            <div class="role">公众监督员</div>
            <div class="location">辽宁省沈阳市</div>
        </div>

        <div class="points-card">
            <div class="points-num">{{ point }}</div>
            <div class="points-label">环保积分</div>
            <div class="points-rank">排名：第 128 位 | 已上报 {{ pointL?pointL.data.length:0 }} 次</div>
        </div>

        <div class="menu-list">
            <div class="menu-item" @click="infoDialog.open()">
                <el-icon size="18" color="#3B82F6"><User /></el-icon>
                <span>个人信息</span>
                <el-icon color="#CBD5E1"><ArrowRight /></el-icon>
            </div>
            <div class="menu-item" @click="pointopen">
                <el-icon size="18" color="#F59E0B"><Trophy /></el-icon>
                <span>积分明细</span>
                <el-icon color="#CBD5E1"><ArrowRight /></el-icon>
            </div>
            <div class="menu-item" @click="rankingopen">
                <el-icon size="18" color="#7C3AED"><DataLine /></el-icon>
                <span>排行榜</span>
                <el-icon color="#CBD5E1"><ArrowRight /></el-icon>
            </div>
            <div class="menu-item">
                <el-icon size="18" color="#64748B"><Setting /></el-icon>
                <span>设置</span>
                <el-icon color="#CBD5E1"><ArrowRight /></el-icon>
            </div>
        </div>

        <el-button type="danger" plain size="large" class="logout-btn" @click="edit">退出登录</el-button>
        <p class="version">东软环保监督系统 v2.1.0</p>
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
    color: #059669;
    font-weight: 500;
    margin-top: 2px;
}

.location {
    font-size: 11px;
    color: #94A3B8;
    margin-top: 4px;
}

.points-card {
    background: linear-gradient(135deg, #FEF3C7, #FDE68A);
    border-radius: 14px;
    padding: 20px;
    text-align: center;
    margin: 16px 0;
}

.points-num {
    font-size: 36px;
    font-weight: 800;
    color: #92400E;
}

.points-label {
    font-size: 13px;
    color: #92400E;
    opacity: 0.8;
}

.points-rank {
    font-size: 11px;
    color: #92400E;
    margin-top: 4px;
    opacity: 0.7;
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
    .profile { padding: 12px; }
}
</style>