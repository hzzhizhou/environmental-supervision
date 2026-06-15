
<script setup>
import { reactive, ref } from 'vue'
import { Close,Document,Clock,Warning,Check   } from '@element-plus/icons-vue'
const filters = reactive({
    province: '',
    city: ''
})

const currentPage = ref(1)

const tableData = ref([
    {
        code: 'AQI-20260603-001',
        aqi: 156,
        aqiColor: '#EF4444',
        level: '中度污染',
        levelColor: '#EF4444',
        reporter: 'user_wang',
        location: '辽宁省沈阳市工业路',
        pm25: 118,
        pm10: 162,
        o3: 86,
        so2: 22,
        time: '2026-06-03 09:15',
        waitTime: '2小时',
        waitHours: 2
    }
])
</script>
<template>
    <div class="confirm-list">
        <!-- 统计卡片 -->
        <div class="stat-row">
            <div class="stat-card">
                <div class="stat-icon" style="background:#F59E0B"><el-icon><Clock /></el-icon></div>
                <div>
                    <div class="stat-num">15</div>
                    <div class="stat-label">待确认数据</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background:#3B82F6"><el-icon><Document /></el-icon></div>
                <div>
                    <div class="stat-num">8</div>
                    <div class="stat-label">今日待处理</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background:#EF4444"><el-icon><Warning /></el-icon></div>
                <div>
                    <div class="stat-num">3</div>
                    <div class="stat-label">超时未确认</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background:#10B981"><el-icon><Check /></el-icon></div>
                <div>
                    <div class="stat-num">98.2%</div>
                    <div class="stat-label">确认及时率</div>
                </div>
            </div>
        </div>

        <!-- 表格卡片 -->
        <div class="card-box">
            <div class="card-header">
                <h6><el-icon><Check /></el-icon> 待确认AQI数据列表</h6>
                <div class="header-actions">
                    <el-select v-model="filters.province" size="small" placeholder="全部省份" clearable style="width:130px">
                        <el-option label="辽宁省" value="辽宁" />
                        <el-option label="广东省" value="广东" />
                    </el-select>
                    <el-select v-model="filters.city" size="small" placeholder="全部城市" clearable style="width:130px">
                        <el-option label="沈阳" value="沈阳" />
                        <el-option label="大连" value="大连" />
                    </el-select>
                    <el-button size="small">筛选</el-button>
                    <el-button type="success" size="small">批量确认</el-button>
                </div>
            </div>
            <div class="card-body">
                <el-table :data="tableData" stripe style="width:100%">
                    <el-table-column type="selection" width="40" />
                    <el-table-column prop="code" label="数据编号" width="180" />
                    <el-table-column prop="aqi" label="AQI数值" width="100">
                        <template #default="{ row }">
                            <strong :style="{ color: row.aqiColor }">{{ row.aqi }}</strong>
                        </template>
                    </el-table-column>
                    <el-table-column prop="level" label="质量等级" width="110">
                        <template #default="{ row }">
                            <el-tag :color="row.levelColor" effect="dark" size="small">{{ row.level }}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="reporter" label="上报人" width="100" />
                    <el-table-column prop="location" label="位置" width="160" />
                    <el-table-column prop="pm25" label="PM2.5" width="80" />
                    <el-table-column prop="pm10" label="PM10" width="80" />
                    <el-table-column prop="o3" label="O₃" width="70" />
                    <el-table-column prop="so2" label="SO₂" width="70" />
                    <el-table-column prop="time" label="上报时间" width="160" />
                    <el-table-column prop="waitTime" label="等待时长" width="100">
                        <template #default="{ row }">
                            <el-tag v-if="row.waitHours >= 4" type="danger" size="small">{{ row.waitTime }}</el-tag>
                            <el-tag v-else-if="row.waitHours >= 2" type="warning" size="small">{{ row.waitTime }}</el-tag>
                            <span v-else>{{ row.waitTime }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="200" fixed="right">
                        <template #default>
                            <el-button type="success" size="small"><el-icon><Check /></el-icon> 确认</el-button>
                            <el-button type="danger" size="small">	<el-icon><Close /></el-icon> 驳回</el-button>
                            <el-button type="primary" size="small" link>详情</el-button>
                        </template>
                    </el-table-column>
                </el-table>

                <div class="pagination-wrap">
                    <span class="text-muted">共 1 条数据</span>
                    <el-pagination
                        v-model:current-page="currentPage"
                        :page-size="10"
                        :total="1"
                        layout="prev, pager, next"
                        size="small"
                        background
                    />
                </div>
            </div>
        </div>
    </div>
</template>


<style scoped>
.stat-row {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    margin-bottom: 24px;
}

@media (max-width: 1400px) {
    .stat-row { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 768px) {
    .stat-row { grid-template-columns: 1fr; }
}

.stat-card {
    background: #fff;
    border-radius: 10px;
    padding: 16px 18px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.04);
    display: flex;
    align-items: center;
    gap: 12px;
}

.stat-icon {
    width: 42px;
    height: 42px;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
    flex-shrink: 0;
}

.stat-num {
    font-size: 24px;
    font-weight: 700;
    line-height: 1.2;
}

.stat-label {
    font-size: 12px;
    color: #64748B;
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

.header-actions {
    display: flex;
    gap: 8px;
    align-items: center;
}

.card-body {
    padding: 16px;
}

.pagination-wrap {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 16px;
    font-size: 13px;
}

.text-muted {
    color: #909399;
}
</style>