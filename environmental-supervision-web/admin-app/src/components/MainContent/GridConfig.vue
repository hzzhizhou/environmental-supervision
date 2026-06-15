<script setup>
import { ref } from 'vue'
import { OfficeBuilding,MapLocation,User,DataAnalysis,CircleCheck } from '@element-plus/icons-vue'
const gridData = ref([
    { id: 1, name: '城东一网格', location: '辽宁省 · 沈阳市 · 沈河区', user: 'grid_zhang', count: 156, rate: 96 },
])
</script>
<template>
    <div class="grid-config">
        <div class="card-box">
            <div class="card-header">
                <h6><el-icon><MapLocation /></el-icon> 网格区域配置</h6>
                <el-button type="success" size="small">+ 新增网格</el-button>
            </div>
            <div class="card-body">
                <div class="grid-list">
                    <div class="grid-item" v-for="item in gridData" :key="item.id">
                        <div class="grid-info">
                            <div class="grid-icon"><el-icon><OfficeBuilding /></el-icon></div>
                            <div class="grid-detail">
                                <strong>{{ item.name }}</strong>
                                <span class="text-muted">{{ item.location }}</span>
                                <div class="grid-meta">
                                    <span><el-icon><User /></el-icon> {{ item.user }}</span>
                                    <span><el-icon><DataAnalysis /></el-icon> 本月: {{ item.count }}条</span>
                                    <span><el-icon><CircleCheck /></el-icon> 确认率: {{ item.rate }}%</span>
                                </div>
                            </div>
                        </div>
                        <div class="grid-actions">
                            <el-button size="small">编辑</el-button>
                            <el-button type="danger" size="small">删除</el-button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>



<style scoped>
.card-box { background: #fff; border-radius: 12px; box-shadow: 0 1px 3px rgba(0,0,0,0.04); overflow: hidden; }
.card-header { padding: 14px 20px; border-bottom: 1px solid #F1F5F9; display: flex; justify-content: space-between; align-items: center; }
.card-header h6 { margin: 0; font-weight: 700; font-size: 15px; }
.card-body { padding: 16px; }

.grid-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
    border: 1px solid #E2E8F0;
    border-radius: 10px;
    margin-bottom: 10px;
}

.grid-item:hover {
    border-color: #3B82F6;
}

.grid-info {
    display: flex;
    align-items: center;
    gap: 14px;
}

.grid-icon {
    width: 44px;
    height: 44px;
    border-radius: 10px;
    background: #EFF6FF;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
}

.grid-detail strong {
    font-size: 14px;
}

.grid-meta {
    display: flex;
    gap: 16px;
    font-size: 12px;
    color: #64748B;
    margin-top: 4px;
}

.grid-actions {
    display: flex;
    gap: 8px;
}

.text-muted {
    color: #909399;
    font-size: 12px;
    display: block;
}
</style>