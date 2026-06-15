<script setup>
import { ref } from 'vue'
import { DataAnalysis,Document } from '@element-plus/icons-vue'
const barData = ref([
    { label: '0-50', count: 911, percent: 32, color: '#10B981' },
    { label: '51-100', count: 740, percent: 26, color: '#F59E0B' },
    { label: '101-150', count: 512, percent: 18, color: '#F97316' },
    { label: '151-200', count: 399, percent: 14, color: '#EF4444' },
    { label: '201-300', count: 285, percent: 10, color: '#7C3AED' },
])

const tableData = ref([
    { city: '大连', excellent: 156, good: 98, lightPollution: 42, moderatePollution: 18, heavyPollution: 6, total: 320, rate: 79.4 },
    { city: '深圳', excellent: 142, good: 88, lightPollution: 35, moderatePollution: 12, heavyPollution: 3, total: 280, rate: 82.1 },
    { city: '广州', excellent: 108, good: 95, lightPollution: 56, moderatePollution: 32, heavyPollution: 14, total: 305, rate: 66.6 },
    { city: '沈阳', excellent: 82, good: 110, lightPollution: 78, moderatePollution: 56, heavyPollution: 38, total: 364, rate: 52.7 },
    { city: '北京', excellent: 95, good: 120, lightPollution: 68, moderatePollution: 45, heavyPollution: 28, total: 356, rate: 60.4 },
])
</script>
<template>
    <div class="distribution">
        <!-- 环形图 + 柱状图 -->
        <div class="chart-row">
            <!-- 环形图卡片 -->
            <div class="card-box">
                <div class="card-header">
                    <h6> 空气质量等级分布</h6>
                    <span class="text-muted" style="font-size:12px">本月</span>
                </div>
                <div class="card-body chart-center">
                    <div class="donut-wrap">
                        <div class="donut"></div>
                        <div class="donut-label">2,847</div>
                    </div>
                    <div class="legend-list">
                        <div class="legend-item"><span class="legend-dot" style="background:#10B981"></span>优 32%</div>
                        <div class="legend-item"><span class="legend-dot" style="background:#F59E0B"></span>良 26%</div>
                        <div class="legend-item"><span class="legend-dot" style="background:#F97316"></span>轻度污染 18%</div>
                        <div class="legend-item"><span class="legend-dot" style="background:#EF4444"></span>中度污染 14%</div>
                        <div class="legend-item"><span class="legend-dot" style="background:#7C3AED"></span>重度污染 10%</div>
                    </div>
                </div>
            </div>

            <!-- 横向柱状图卡片 -->
            <div class="card-box">
                <div class="card-header">
                    <h6><el-icon><DataAnalysis /></el-icon> AQI数值区间分布</h6>
                    <span class="text-muted" style="font-size:12px">本月</span>
                </div>
                <div class="card-body">
                    <div class="horizontal-bar" v-for="item in barData" :key="item.label">
                        <div class="bar-label">{{ item.label }}</div>
                        <div class="bar-track">
                            <div class="bar-fill" :style="{ width: item.percent + '%', background: item.color }">
                                {{ item.count }}条
                            </div>
                        </div>
                        <div class="bar-percent">{{ item.percent }}%</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 表格卡片 -->
        <div class="card-box">
            <div class="card-header">
                <h6><el-icon><Document /></el-icon> 各城市AQI等级分布明细</h6>
            </div>
            <div class="card-body">
                <el-table :data="tableData" stripe style="width:100%">
                    <el-table-column prop="city" label="城市" width="80" />
                    <el-table-column prop="excellent" label="优" width="70" />
                    <el-table-column prop="good" label="良" width="70" />
                    <el-table-column prop="lightPollution" label="轻度污染" width="90" />
                    <el-table-column prop="moderatePollution" label="中度污染" width="90" />
                    <el-table-column prop="heavyPollution" label="重度污染" width="90" />
                    <el-table-column prop="total" label="总计" width="70" />
                    <el-table-column prop="rate" label="优良率" width="90">
                        <template #default="{ row }">
                            <el-tag :type="row.rate >= 70 ? 'success' : 'warning'" size="small">{{ row.rate }}%</el-tag>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
    </div>
</template>



<style scoped>
.chart-row {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
    margin-bottom: 20px;
}

.card-box {
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.04);
    overflow: hidden;
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

.chart-center {
    display: flex;
    align-items: center;
    gap: 24px;
    justify-content: center;
}

.donut-wrap {
    position: relative;
    width: 140px;
    height: 140px;
    flex-shrink: 0;
}

.donut {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background: conic-gradient(#10B981 0% 32%, #F59E0B 32% 58%, #F97316 58% 76%, #EF4444 76% 90%, #7C3AED 90% 100%);
}

.donut-label {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 22px;
    font-weight: 800;
}

.legend-list {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.legend-item {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
}

.legend-dot {
    width: 10px;
    height: 10px;
    border-radius: 3px;
    flex-shrink: 0;
}

.horizontal-bar {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 10px;
}

.bar-label {
    width: 60px;
    text-align: right;
    font-size: 12px;
    font-weight: 600;
    flex-shrink: 0;
}

.bar-track {
    flex: 1;
    height: 24px;
    background: #F1F5F9;
    border-radius: 4px;
    overflow: hidden;
}

.bar-fill {
    height: 100%;
    border-radius: 4px;
    display: flex;
    align-items: center;
    padding-left: 8px;
    color: #fff;
    font-size: 11px;
    font-weight: 600;
}

.bar-percent {
    width: 40px;
    font-size: 12px;
    font-weight: 600;
    flex-shrink: 0;
}

.text-muted {
    color: #909399;
}
</style>