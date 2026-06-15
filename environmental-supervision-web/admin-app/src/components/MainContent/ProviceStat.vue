<script setup>
import { OfficeBuilding,Download,Document } from '@element-plus/icons-vue'
import { ref } from 'vue'

const month = ref('2026-06')

const barData = ref([
    { name: '广东', count: 1245, percent: 100, color: '#3B82F6' },
    { name: '辽宁', count: 1098, percent: 88, color: '#2563EB' },
    { name: '北京', count: 952, percent: 76, color: '#60A5FA' },
    { name: '浙江', count: 876, percent: 70, color: '#93C5FD' },
    { name: '江苏', count: 812, percent: 65, color: '#93C5FD' },
    { name: '上海', count: 728, percent: 58, color: '#BFDBFE' },
    { name: '山东', count: 650, percent: 52, color: '#BFDBFE' },
    { name: '四川', count: 574, percent: 46, color: '#DBEAFE' },
])

const tableData = ref([
    { province: '广东省', total: 1245, confirmed: 1180, rate: 94.8, rejected: 32, users: 456, cities: 21 },
    { province: '辽宁省', total: 1098, confirmed: 1042, rate: 94.9, rejected: 28, users: 389, cities: 14 },
    { province: '北京市', total: 952, confirmed: 905, rate: 95.1, rejected: 18, users: 312, cities: 1 },
    { province: '浙江省', total: 876, confirmed: 820, rate: 93.6, rejected: 22, users: 278, cities: 11 },
    { province: '江苏省', total: 812, confirmed: 768, rate: 94.6, rejected: 20, users: 245, cities: 13 },
])
</script>
<template>
    <div class="province-stat">
        <!-- 顶部统计 -->
        <div class="stat-row">
            <div class="stat-card">
                <div class="stat-num" style="color:#3B82F6">31</div>
                <div class="stat-label">覆盖省份数</div>
            </div>
            <div class="stat-card">
                <div class="stat-num" style="color:#10B981">342</div>
                <div class="stat-label">覆盖城市数</div>
            </div>
            <div class="stat-card">
                <div class="stat-num" style="color:#F59E0B">8,562</div>
                <div class="stat-label">本月上报总量</div>
            </div>
        </div>

        <!-- 柱状图卡片 -->
        <div class="card-box">
            <div class="card-header">
                <h6><el-icon><OfficeBuilding /></el-icon> 各省份数据上报统计（本月）</h6>
                <el-select v-model="month" size="small" style="width:120px">
                    <el-option label="2026年6月" value="2026-06" />
                    <el-option label="2026年5月" value="2026-05" />
                </el-select>
            </div>
            <div class="card-body">
                <div class="bar-chart">
                    <div v-for="item in barData" :key="item.name" class="bar-item">
                        <div class="bar-value">{{ item.count }}</div>
                        <div class="bar-fill" :style="{ height: item.percent + '%', background: item.color }"></div>
                        <div class="bar-name">{{ item.name }}</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 表格卡片 -->
        <div class="card-box">
            <div class="card-header">
                <h6><el-icon><Document /></el-icon> 各省份详细统计表</h6>
                <el-button type="success" size="small"><el-icon><Download /></el-icon> 导出Excel</el-button>
            </div>
            <div class="card-body">
                <el-table :data="tableData" stripe style="width:100%">
                    <el-table-column type="index" label="排名" width="60" />
                    <el-table-column prop="province" label="省份" width="100" />
                    <el-table-column prop="total" label="上报总量" width="100" sortable />
                    <el-table-column prop="confirmed" label="已确认" width="90" />
                    <el-table-column prop="rate" label="确认率" width="90">
                        <template #default="{ row }">
                            <el-tag :type="row.rate >= 95 ? 'success' : 'warning'" size="small">{{ row.rate }}%</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="rejected" label="驳回数" width="80" />
                    <el-table-column prop="users" label="活跃用户" width="90" />
                    <el-table-column prop="cities" label="覆盖城市" width="80" />
                </el-table>

                <div class="pagination-wrap">
                    <span class="text-muted">共 5 条数据</span>
                </div>
            </div>
        </div>
    </div>
</template>


<style scoped>
.stat-row {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;
    margin-bottom: 24px;
}

.stat-card {
    background: #fff;
    border-radius: 10px;
    padding: 20px;
    text-align: center;
    box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.stat-num {
    font-size: 28px;
    font-weight: 800;
    line-height: 1.2;
}

.stat-label {
    font-size: 12px;
    color: #64748B;
    margin-top: 4px;
}

.card-box {
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.04);
    overflow: hidden;
    margin-bottom: 20px;
}

.card-header {
    padding: 14px 20px;
    border-bottom: 1px solid #F1F5F9;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.card-header h6 {
    margin: 0;
    font-weight: 700;
    font-size: 15px;
}

.card-body {
    padding: 20px;
}

.bar-chart {
    display: flex;
    align-items: flex-end;
    gap: 20px;
    height: 200px;
    padding: 10px 0;
}

.bar-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100%;
}

.bar-value {
    font-size: 12px;
    font-weight: 700;
    margin-bottom: 4px;
}

.bar-fill {
    width: 40px;
    border-radius: 6px 6px 0 0;
    min-height: 4px;
    transition: height 0.3s;
}

.bar-name {
    font-size: 12px;
    color: #64748B;
    margin-top: 6px;
}

.pagination-wrap {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 12px;
    font-size: 13px;
}

.text-muted {
    color: #909399;
}
</style>