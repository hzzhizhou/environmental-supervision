<script setup>
import { ref } from 'vue'
import { DataLine,DataAnalysis } from '@element-plus/icons-vue'
const city = ref('all')

const barData = ref([
    { month: '1月', value: 88, height: 88, color: '#93C5FD' },
    { month: '2月', value: 82, height: 82, color: '#93C5FD' },
    { month: '3月', value: 95, height: 95, color: '#F97316' },
    { month: '4月', value: 72, height: 72, color: '#F59E0B' },
    { month: '5月', value: 65, height: 65, color: '#10B981' },
    { month: '6月', value: 78, height: 78, color: '#F59E0B' },
])

const tableData = ref([
    { city: '沈阳', jan: 142, feb: 128, mar: 156, apr: 98, may: 85, jun: 102, avg: 118.5, trend: '↓ 改善中', trendType: 'success' },
    { city: '北京', jan: 165, feb: 148, mar: 172, apr: 112, may: 95, jun: 110, avg: 133.7, trend: '↓ 改善中', trendType: 'success' },
    { city: '广州', jan: 72, feb: 68, mar: 82, apr: 58, may: 52, jun: 60, avg: 65.3, trend: '↓ 良好', trendType: 'success' },
    { city: '深圳', jan: 55, feb: 50, mar: 62, apr: 42, may: 38, jun: 45, avg: 48.7, trend: '↓ 优良', trendType: 'success' },
    { city: '大连', jan: 68, feb: 62, mar: 75, apr: 48, may: 42, jun: 50, avg: 57.5, trend: '↓ 良好', trendType: 'success' },
])
</script>
<template>
    <div class="trend">
        <!-- 柱状图卡片 -->
        <div class="card-box">
            <div class="card-header">
                <h6><el-icon><DataLine /></el-icon> 月度AQI均值趋势（2026年）</h6>
                <div class="header-actions">
                    <el-select v-model="city" size="small" style="width:130px">
                        <el-option label="全国均值" value="all" />
                        <el-option label="辽宁省" value="辽宁" />
                        <el-option label="广东省" value="广东" />
                    </el-select>
                    <el-button size="small">对比省份</el-button>
                </div>
            </div>
            <div class="card-body">
                <div class="bar-chart">
                    <div v-for="item in barData" :key="item.month" class="bar-item">
                        <div class="bar-value">{{ item.value }}</div>
                        <div class="bar-fill" :style="{ height: item.height + 'px', background: item.color }"></div>
                        <div class="bar-month">{{ item.month }}</div>
                    </div>
                </div>
                <div class="chart-note">▲ 6月数据截至6月3日，为部分月份数据</div>
            </div>
        </div>

        <!-- 表格卡片 -->
        <div class="card-box">
            <div class="card-header">
                <h6><el-icon><DataAnalysis /></el-icon> 多城市月度趋势对比</h6>
            </div>
            <div class="card-body">
                <el-table :data="tableData" stripe style="width:100%">
                    <el-table-column prop="city" label="城市" width="80" />
                    <el-table-column prop="jan" label="1月" width="65" />
                    <el-table-column prop="feb" label="2月" width="65" />
                    <el-table-column prop="mar" label="3月" width="65" />
                    <el-table-column prop="apr" label="4月" width="65" />
                    <el-table-column prop="may" label="5月" width="65" />
                    <el-table-column prop="jun" label="6月" width="65" />
                    <el-table-column prop="avg" label="半年均值" width="85">
                        <template #default="{ row }">
                            <strong>{{ row.avg }}</strong>
                        </template>
                    </el-table-column>
                    <el-table-column prop="trend" label="趋势" width="90">
                        <template #default="{ row }">
                            <el-tag :type="row.trendType" size="small">{{ row.trend }}</el-tag>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
    </div>
</template>



<style scoped>
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

.header-actions {
    display: flex;
    gap: 8px;
    align-items: center;
}

.card-body {
    padding: 20px;
}

.bar-chart {
    display: flex;
    align-items: flex-end;
    gap: 24px;
    height: 160px;
    justify-content: center;
    padding-bottom: 8px;
    border-bottom: 2px solid #E2E8F0;
}

.bar-item {
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
    width: 36px;
    border-radius: 6px 6px 0 0;
    min-height: 4px;
    transition: height 0.3s;
}

.bar-month {
    font-size: 11px;
    color: #64748B;
    margin-top: 8px;
    font-weight: 500;
}

.chart-note {
    text-align: center;
    font-size: 11px;
    color: #94A3B8;
    margin-top: 8px;
}
</style>