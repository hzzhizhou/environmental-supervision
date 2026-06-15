<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { addUser, updateUser } from '../../api/manageUser'
const emit = defineEmits(['success'])
const dialogVisible = ref(false)
const formRef = ref()
const { isEdit, userId } = defineProps({
    isEdit: Boolean,
    userId:Number
})
const form = reactive({
    username: '',
    password: '',
    realName: '',
    phone: '',
    roleId: null,
    gridId: null,
    status: 1
})

const handleSubmit = () => {
    formRef.value.validate(async (valid) => {
        if (!valid) return
        const submitData = { ...form }
        if (submitData.roleId !== 2) delete submitData.gridId
        if (isEdit) {
            delete submitData.username
            delete submitData.password
        } else {
            delete submitData.status
        }
        submitData.roleId = +submitData.roleId
        try {
            if (isEdit) {
                await updateUser(userId, submitData)
                ElMessage.success('修改成功')
                dialogVisible.value = false
            } else {
                await addUser(submitData)
                ElMessage.success('新增成功')
                dialogVisible.value = false
            }

            emit('success')
        } catch {
            ElMessage.error('操作失败')
        }
    })
}
const open = (row) => {
    dialogVisible.value = true
    if (isEdit && row) {
        Object.assign(form, {
            realName: row.realName || '',
            phone: row.phone || '',
            roleId: row.roleId || null,
            gridId: row.gridId || null,
            status: row.status || 1
        })
    } else {
        Object.assign(form, { username: '', password: '', realName: '', phone: '', roleId: null, gridId: null })
    }

}
const rules = {
    username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
    realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
    phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1\d{10}$/, message: '手机号格式错误', trigger: 'blur' }
    ],
    roleId: [{ required: true, message: '请选择角色', trigger: 'change' }],
    status: [{ required: true, message: '请选择状态', trigger: 'blur' }]
}
defineExpose({ open })
</script>

<template>
    <!-- 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑用户':'添加用户'" width="460px" :close-on-click-modal="false">
        <el-form ref="formRef" :model="form" label-width="80px" :rules="rules">
            <el-form-item label="账号" prop="username" v-if="!isEdit">
                <el-input v-model="form.username" placeholder="请输入登录账号" />
            </el-form-item>
            <el-form-item label="密码" prop="password" v-if="!isEdit">
                <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
            </el-form-item>
            <el-form-item label="姓名" prop="realName">
                <el-input v-model="form.realName" placeholder="请输入真实姓名" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" placeholder="请输入11位手机号" />
            </el-form-item>
            <el-form-item label="角色" prop="roleId">
                <el-select v-model="form.roleId" placeholder="请选择角色" style="width:100%">
                    <el-option label="系统管理员" :value="1" />
                    <el-option label="网格员" :value="2" />
                    <el-option label="公众监督员" :value="3" />
                    <el-option label="决策者" :value="4" />
                </el-select>
            </el-form-item>
            <el-form-item v-if="form.roleId === 2" label="网格" prop="gridId">
                <el-input v-model="form.gridId" placeholder="请输入网格ID" />
            </el-form-item>
            <el-form-item label="状态" prop="status" v-if="isEdit">
                <el-select v-model="form.status" style="width:100%" placeholder="请输入状态">
                    <el-option label="启用" :value="1" />
                    <el-option label="禁用" :value="0" />
                </el-select>
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmit">确定</el-button>
        </template>
    </el-dialog>
</template>