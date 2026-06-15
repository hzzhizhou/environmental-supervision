<script setup>
import { ref } from 'vue'

const visible = ref(false)
const logs = ref([])
//处理时间
const formatTime = (isoTime) => {
    if (!isoTime) return ''
    const date = new Date(isoTime)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    return `${year}-${month}-${day} ${hours}:${minutes}`
}
const open = async (logsData) => {
    logs.value = logsData.data || []
    visible.value = true   
}
defineExpose({ open })
</script>
<template>
    <el-dialog v-model="visible" title="积分明细" width="420px" :close-on-click-modal="false">
        <div class="log-list">
            <div class="log-item" v-for="item in logs" :key="item.id">
                <div class="log-left">
                    <div class="log-title">{{ item.description }}</div>
                    <div class="log-time">{{ formatTime(item.createdAt) }}</div>
                </div>
                <div class="log-right" :class="1 === 1 ? 'gain' : 'cost'">
                    + {{ item.points }}
                </div>
            </div>
            <div v-if="!logs || logs.length === 0" class="empty">暂无积分记录</div>
        </div>

        <template #footer>
            <el-button @click="visible = false">关闭</el-button>
        </template>
    </el-dialog>
</template>



<style scoped>
.log-list {
    max-height: 300px;
    overflow-y: auto;
}

.log-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #F1F5F9;
}

.log-left {
    flex: 1;
}

.log-title {
    font-size: 14px;
    color: #334155;
    font-weight: 500;
}

.log-time {
    font-size: 12px;
    color: #94A3B8;
    margin-top: 4px;
}

.log-right {
    font-size: 16px;
    font-weight: 700;
    flex-shrink: 0;
    margin-left: 12px;
}

.log-right.gain {
    color: #10B981;
}

.log-right.cost {
    color: #EF4444;
}

.empty {
    text-align: center;
    color: #94A3B8;
    padding: 40px 0;
    font-size: 14px;
}

:deep(.el-dialog) { border-radius: 14px; }
:deep(.el-dialog__header) { padding: 18px 20px 10px; font-weight: 700; font-size: 15px; }
:deep(.el-dialog__body) { padding: 10px 20px; }
</style>