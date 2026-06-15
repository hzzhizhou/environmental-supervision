<script setup>
import { ref } from 'vue'
import { User,Warning } from '@element-plus/icons-vue'
const activeData = ref([
    { label: '日活跃用户(DAU)', value: '1,245', change: '↑ 12%', up: true },
    { label: '周活跃用户(WAU)', value: '3,280', change: '↑ 8%', up: true },
    { label: '月活跃用户(MAU)', value: '4,520', change: '↑ 15%', up: true },
    { label: '日均上报次数', value: '95', change: '↓ 3%', up: false },
])

const channelData = ref([
    { name: 'APP', percent: 78 },
    { name: '小程序', percent: 15 },
    { name: '网页端', percent: 7 },
])

const pollutantData = ref([
    { name: 'PM2.5', count: '2,847', percent: '100%', note: '主要指标' },
    { name: 'PM10', count: '2,847', percent: '100%', note: '次要指标' },
    { name: '臭氧(O₃)', count: '2,340', percent: '82.2%', note: '夏季偏高' },
    { name: '二氧化硫(SO₂)', count: '1,890', percent: '66.4%', note: '工业区偏高' },
    { name: '二氧化氮(NO₂)', count: '2,105', percent: '73.9%', note: '交通区偏高' },
])
</script>
<template>
    <div class="other-stat">
        <!-- 顶部统计 -->
        <div class="stat-row">
            <div class="stat-card">
                <div class="stat-num" style="color:#3B82F6">4,892</div>
                <div class="stat-label">累计注册用户数</div>
            </div>
            <div class="stat-card">
                <div class="stat-num" style="color:#10B981">86</div>
                <div class="stat-label">活跃网格员数</div>
            </div>
            <div class="stat-card">
                <div class="stat-num" style="color:#F59E0B">98.2%</div>
                <div class="stat-label">数据确认及时率</div>
            </div>
        </div>

        <!-- 双卡片 -->
        <div class="chart-row">
            <!-- 用户活跃度 -->
            <div class="card-box">
                <div class="card-header"><h6><el-icon><User /></el-icon> 用户活跃度统计</h6></div>
                <div class="card-body">
                    <el-table :data="activeData" stripe>
                        <el-table-column prop="label" label="指标" />
                        <el-table-column prop="value" label="数值" />
                        <el-table-column prop="change" label="环比">
                            <template #default="{ row }">
                                <span :style="{ color: row.up ? '#10B981' : '#EF4444' }">{{ row.change }}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>

            <!-- 上报渠道 -->
            <div class="card-box">
                <div class="card-header"><h6>上报渠道分布</h6></div>
                <div class="card-body">
                    <div class="channel-bar" v-for="item in channelData" :key="item.name">
                        <div class="channel-label">{{ item.name }}</div>
                        <div class="channel-track">
                            <div class="channel-fill" :style="{ width: item.percent + '%' }"></div>
                        </div>
                        <div class="channel-percent">{{ item.percent }}%</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 污染指标统计 -->
        <div class="card-box">
            <div class="card-header"><h6><el-icon><Warning /></el-icon> 污染类型上报统计</h6></div>
            <div class="card-body">
                <el-table :data="pollutantData" stripe>
                    <el-table-column prop="name" label="污染指标" width="150" />
                    <el-table-column prop="count" label="上报次数" />
                    <el-table-column prop="percent" label="占比" />
                    <el-table-column prop="note" label="说明" />
                </el-table>
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
}

.card-header h6 {
    margin: 0;
    font-weight: 700;
    font-size: 15px;
}

.card-body {
    padding: 16px;
}

.channel-bar {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 10px;
}

.channel-label {
    width: 50px;
    font-size: 12px;
    font-weight: 600;
}

.channel-track {
    flex: 1;
    height: 22px;
    background: #F1F5F9;
    border-radius: 4px;
    overflow: hidden;
}

.channel-fill {
    height: 100%;
    background: #3B82F6;
    border-radius: 4px;
}

.channel-percent {
    width: 35px;
    font-size: 12px;
    font-weight: 600;
}
</style>