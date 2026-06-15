<script setup>
import { ref } from 'vue'
import img1 from '../assets/1.webp'
const visible = ref(false)
const list = ref([])

const open = (data) => {
    list.value = data || []
    console.log(data);
    visible.value = true
}

defineExpose({ open })
</script>

<template>
    <el-dialog v-model="visible" title="积分排行榜" width="420px" :close-on-click-modal="false">
        <div class="rank-list">
            <div class="rank-item" v-for="item in list" :key="item.userId">
                <div class="rank-num" :class="'top' + item.rank" v-if="item.rank <= 3">{{ item.rank }}</div>
                <div class="rank-num" v-else>{{ item.rank }}</div>
                <el-avatar :size="36" :src="img1"></el-avatar>
                <div class="rank-info">
                    <div class="rank-name">{{ item.realName }}</div>
                </div>
                <div class="rank-points">{{ item.points }} 分</div>
            </div>
            <div v-if="!list || list.length === 0" class="empty">暂无排行数据</div>
        </div>
        <template #footer>
            <el-button @click="visible = false">关闭</el-button>
        </template>
    </el-dialog>
</template>


<style scoped>
.rank-list {
    max-height: 400px;
    overflow-y: auto;
}

.rank-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid #F1F5F9;
}

.rank-num {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 13px;
    font-weight: 700;
    background: #F1F5F9;
    color: #64748B;
    flex-shrink: 0;
}

.rank-num.top1 { background: #F59E0B; color: #fff; }
.rank-num.top2 { background: #94A3B8; color: #fff; }
.rank-num.top3 { background: #B45309; color: #fff; }

.rank-info {
    flex: 1;
}

.rank-name {
    font-size: 14px;
    font-weight: 500;
    color: #334155;
}

.rank-desc {
    font-size: 12px;
    color: #94A3B8;
    margin-top: 2px;
}

.rank-points {
    font-size: 15px;
    font-weight: 700;
    color: #F59E0B;
    flex-shrink: 0;
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