<script setup>
import { reactive, ref,nextTick } from 'vue'
import { User } from '@element-plus/icons-vue'
import { getUsers, deleteUser } from '../../api/manageUser'
import AddUser from '../Dialog/AddUser.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
const filters = reactive({ role: '', status: '' })
//页面进行刷新
const fetchCurrentUsers = async () => {
    const filter = { roleId: filters.role, status: filters.status }
    const { data } = await getUsers(filter)
    currentUsers.value = data.data
}
fetchCurrentUsers()
//格式化时间
const formatTime = (time) => {
    if (!time) return ''
    const date = new Date(time)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hour = String(date.getHours()).padStart(2, '0')
    const minute = String(date.getMinutes()).padStart(2, '0')
    return `${year}-${month}-${day} ${hour}:${minute}`
}
const currentUsers = ref([])
const addUser = ref(null)
const isEdit = ref(false)
const userId = ref(null)
const open = async(item, userid, row) => {
    isEdit.value = item
    userId.value = userid
    await nextTick()
    addUser.value.open(row)
}
//删除用户
const handledelete = async (userId) => {
    try {
        await ElMessageBox.confirm('确定要删除吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        await deleteUser(userId)
        fetchCurrentUsers()
        ElMessage.success('删除成功')
    } catch(err){
        if (err?.response) {
            ElMessage.warning('删除失败')
        }

    }
} 
</script>
<template>
    <div class="user-manage">
        <div class="card-box">
            <div class="card-header">
                <h6><el-icon>
                        <User />
                    </el-icon> 用户权限管理</h6>
                <div class="header-actions">
                    <el-select v-model="filters.role" size="small" placeholder="全部角色" clearable style="width:130px">
                        <el-option label="系统管理员" value="1" />
                        <el-option label="网格员" value="2" />
                        <el-option label="公众监督员" value="3" />
                        <el-option label="决策者" value="4" />
                    </el-select>
                    <el-select v-model="filters.status" size="small" placeholder="全部状态" clearable style="width:120px">
                        <el-option label="启用" value="1" />
                        <el-option label="禁用" value="0" />
                    </el-select>
                    <el-button size="small" @click="fetchCurrentUsers">筛选</el-button>
                    <el-button type="success" size="small" @click="open(false)">+ 新增用户</el-button>
                    <AddUser ref="addUser" @success="fetchCurrentUsers" :isEdit="isEdit" :userId="userId" />
                </div>
            </div>
            <div class="card-body">
                <el-table :data="currentUsers?.records" stripe style="width:100%">
                    <el-table-column prop="userId" label="用户ID" min-width="80" />
                    <el-table-column prop="realName" label="用户名" min-width="100" />
                    <el-table-column prop="phone" label="手机号" min-width="120" />
                    <el-table-column prop="roleName" label="角色" min-width="100">
                        <template #default="{ row }">
                            <el-tag type="primary" size="small">{{ row.roleName }}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="createdAt" label="注册时间" min-width="150">
                        <template #default="{ row }">
                            {{ formatTime(row?.createdAt) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="status" label="状态" min-width="80">
                        <template #default="{ row }">
                            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">{{
                                row.status === 1 ? '启用' : '禁用' }}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" min-width="150" fixed="right">
                        <template #default="{ row }">
                            <el-button type="primary" size="small" link
                                @click="open(true, row.userId, row)">编辑</el-button>
                            <el-button type="danger" size="small" link @click="handledelete(row.userId)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
    </div>
</template>



<style scoped>
.user-manage {
    padding: 20px;
    width: 100%;
}

.card-box {
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
    overflow: hidden;
    width: 100%;
}

.card-header {
    padding: 14px 20px;
    border-bottom: 1px solid #F1F5F9;
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;
}

.card-header h6 {
    margin: 0;
    font-weight: 700;
    font-size: 15px;
    display: flex;
    align-items: center;
    gap: 6px;
}

.header-actions {
    display: flex;
    gap: 8px;
    align-items: center;
    flex-wrap: wrap;
}

.card-body {
    padding: 16px;
    overflow-x: auto;
}

/* 表格字体 */
:deep(.el-table) {
    font-size: 14px;
}

:deep(.el-table th) {
    font-size: 13px;
    font-weight: 600;
    background: #F8FAFC;
}

:deep(.el-table td) {
    font-size: 13px;
}

/* 移动端 */
@media (max-width: 768px) {
    .user-manage {
        padding: 12px;
    }

    .card-header {
        flex-direction: column;
        align-items: flex-start;
    }

    .header-actions {
        width: 100%;
    }

    .header-actions .el-select,
    .header-actions .el-button {
        flex: 1;
        min-width: 0;
    }

    .card-body {
        padding: 10px;
    }
}
</style>