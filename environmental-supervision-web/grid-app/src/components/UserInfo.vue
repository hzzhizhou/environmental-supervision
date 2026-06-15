<script setup>
import { ref } from 'vue'
import { Camera } from '@element-plus/icons-vue'
import img1 from '../assets/1.webp'
const visible = ref(false)

defineProps({
    user: Object,
    avatar:String
})
//修改头像
const editAvatar = () => {
    visible.value = false
    Input.value.click()
}

//选择头像
const Input=ref(null)
const handleChange=(e)=>{
    const file=e.target.files[0]
    if(file){
        emit('editAvatar',file)
    }
    e.target.value=''
    
}
//修改昵称
const editNickName = () => {
    visible.value = false
    emit('editName')
}
//修改密码
const editPassword = () => {
    visible.value = false
    emit('editPassword')
}
//修改电话
const editPhone = () => {
    visible.value = false
    emit('editPhone')
}


const emit = defineEmits(['editName', 'editPassword', 'editPhone', 'editAvatar'])

const open = () => { visible.value = true }

defineExpose({ open })
</script>

<template>
    <el-dialog v-model="visible" title="个人信息" width="360px" :close-on-click-modal="false">
        <!-- 头像区域 -->
         <input ref="Input" type="file" accept="image/*" @change="handleChange" style="display: none;" />
         <div class="avatar-box">
        <div class="avatar-section" @click="editAvatar">
            <el-avatar :size="70" :src="avatar||img1" class="avatar">
                <img :src="img1" />
            </el-avatar>
            <div class="avatar-edit">
                <el-icon><Camera /></el-icon>
                <span>更换头像</span>
            </div>
        </div>
        </div>

        <!-- 只读展示区 -->
        <div class="info-section">
            <div class="info-item">
                <span class="label">昵称</span>
                <span class="value">{{ user?.realName || '未设置' }}</span>
            </div>
            <div class="info-item">
                <span class="label">手机号</span>
                <span class="value">{{ user?.phone || '未绑定' }}</span>
            </div>
        </div>

        <template #footer>
            <div class="footer-btns">
                <el-button @click="visible = false">取消</el-button>
                <el-button type="warning" @click="editNickName">修改昵称</el-button>
                <el-button type="primary" @click="editPhone">修改电话</el-button>
            </div>
            <div class="footer-btns">
                <el-button type="primary" @click="editPassword">修改密码</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<style scoped>
.avatar-box{
    display: flex;
    justify-content: center;
}
.avatar-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 0 0 8px;
    cursor: pointer;
    transition: all 0.2s;
    width: 100px;
}

.avatar-section:hover {
    opacity: 0.8;
}

.avatar {
    margin-bottom: 8px;
    border: 2px solid #F1F5F9;
}

.avatar-edit {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: #94A3B8;
}

.avatar-edit:hover {
    color: #F59E0B;
}

.info-section {
    padding: 8px 0;
}

.info-item {
    display: flex;
    align-items: center;
    padding: 14px 0;
    border-bottom: 1px solid #F1F5F9;
}

.info-item:last-child {
    border-bottom: none;
}

.label {
    width: 60px;
    font-size: 13px;
    color: #64748B;
    flex-shrink: 0;
}

.value {
    flex: 1;
    font-size: 14px;
    color: #334155;
    font-weight: 500;
}

.footer-btns {
    display: flex;
    justify-content: center;
    gap: 12px;
    margin-bottom: 12px;
}

.footer-btns:last-child {
    margin-bottom: 20px;
}

:deep(.el-dialog) {
    border-radius: 14px;
}

:deep(.el-dialog__header) {
    padding: 18px 20px 10px;
    font-weight: 700;
    font-size: 15px;
}

:deep(.el-dialog__body) {
    padding: 10px 20px;
}
</style>