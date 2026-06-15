
<script setup>
import { ref, reactive, computed } from 'vue'

const visible = ref(false)
const form = reactive({
    value: '',
    password: '',
    oldPassword:'',
    confirmPassword: ''
})

const props = defineProps({
    type: { type: String, default: 'name' }, 
    value: { type: String, default: '' }
})

const emit = defineEmits(['save'])

const title = computed(() => ({ name: '修改昵称', phone: '修改手机号', password: '修改密码' }[props.type]))
const placeholder = computed(() => ({ name: '请输入新昵称', phone: '请输入新手机号' }[props.type]))

const handleSave = () => {
    if (props.type === 'password') {
        if (form.password !== form.confirmPassword) return ElMessage.warning('两次密码不一致')
        if (form.password.length < 6) return ElMessage.warning('密码至少6位')
        emit('save', { field: props.type, value: {oldPassword:form.oldPassword,newPassword:form.password} })
    } else if(props.type==='name') {
        emit('save', { field: props.type, value: form.value })
    }else{
        if(form.phone.length!==11) return ElMessage.warning('电话号码至少11位')
        emit('save',{field:props.type,value:form.phone})
    }
    visible.value = false
}

const open = () => {
    form.value = props.type === 'name' ? (props.value || '') : ''
    form.password = ''
    form.confirmPassword = ''
    form.oldPassword='',
    form.phone=props.type==='phone'?(props.phone||''):''
    visible.value = true
}

defineExpose({ open })
</script>

<template>
    <el-dialog v-model="visible" :title="title" width="360px" :close-on-click-modal="false">
        <template v-if="type === 'password'">
            <el-input v-model="form.oldPassword" type="password" placeholder="请输入旧密码" size="large" show-password  class="mb"/>
            <el-input v-model="form.password" type="password" placeholder="请输入新密码" size="large" show-password class="mb" />
            <el-input v-model="form.confirmPassword" type="password" placeholder="请确认新密码" size="large" show-password />
        </template>
        <template v-else-if="type==='name'">
            <el-input v-model="form.value" :placeholder="placeholder" size="large" />
        </template>
        <template v-else>
            <el-input v-model="form.phone" :placeholder="placeholder" size="large" />
        </template>
        <template #footer>
            <el-button @click="visible = false">取消</el-button>
            <el-button type="primary" @click="handleSave">保存</el-button>
        </template>
    </el-dialog>
</template>


<style scoped>
:deep(.el-dialog) { border-radius: 14px; }
:deep(.el-dialog__header) { padding: 18px 20px 10px; font-weight: 700; font-size: 15px; }
:deep(.el-dialog__body) { padding: 10px 20px; }
:deep(.el-input__wrapper) { border-radius: 10px; }
.mb { margin-bottom: 12px; }
</style>