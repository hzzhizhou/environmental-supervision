<script setup>import { reactive, ref, onMounted } from 'vue';
import { DataAnalysis, Clock, Check, Close, Download, Search, UserPlus } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import AssignWorker from '../Dialog/AssignWorker.vue';
import { listReports } from '../../api/manageUser';
const filters = reactive({
 province: '',
 city: '',
 level: '',
 status: '',
 startDate: '',
 endDate: ''
});
const currentPage = ref(1);
const total = ref(0);
const tableData = ref([]);
const loading = ref(false);
const showAssignModal = ref(false);
const selectedReport = ref(null);
const adminId = ref(1);
const loadReports = async () => {
 loading.value = true;
 try {
 const params = {
 status: filters.status || undefined,
 province: filters.province || undefined,
 city: filters.city || undefined,
 pageNum: currentPage.value,
 pageSize: 10
 };
 const { data } = await listReports(params);
 tableData.value = data.data.records;
 total.value = data.data.total;
 }
 catch (error) {
 ElMessage.error('获取数据失败');
 console.error(error);
 }
 finally {
 loading.value = false;
 }
};
const handleSearch = () => {
 currentPage.value = 1;
 loadReports();
};
const handleReset = () => {
 filters.province = '';
 filters.city = '';
 filters.level = '';
 filters.status = '';
 filters.startDate = '';
 filters.endDate = '';
 currentPage.value = 1;
 loadReports();
};
const handleAssign = (row) => {
 selectedReport.value = row;
 showAssignModal.value = true;
};
const handleAssignSuccess = () => {
 loadReports();
};
onMounted(() => {
 loadReports();
});
</script>

<template>
    <div class="data-list">
        <div class="stat-row">
            <div class="stat-card">
                <div class="stat-icon" style="background:#3B82F6">	<el-icon><DataAnalysis /></el-icon></div>
                <div>
                    <div class="stat-num">2,847</div>
                    <div class="stat-label">监督数据总量</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background:#F59E0B"><el-icon><Clock /></el-icon></div>
                <div>
                    <div class="stat-num">23</div>
                    <div class="stat-label">待指派数据</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background:#10B981"><el-icon><Check /></el-icon></div>
                <div>
                    <div class="stat-num">2,756</div>
                    <div class="stat-label">已确认数据</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background:#EF4444">	<el-icon><Close /></el-icon></div>
                <div>
                    <div class="stat-num">68</div>
                    <div class="stat-label">已驳回数据</div>
                </div>
            </div>
        </div>

        <div class="card-box">
            <div class="card-header">
                <h6><el-icon><DataAnalysis /></el-icon> 公众监督数据列表</h6>
                <el-button type="success" size="small"><el-icon><Download /></el-icon> 导出Excel</el-button>
            </div>
            <div class="card-body">
                <div class="filter-bar">
                    <div class="filter-item">
                        <label>省份</label>
                        <el-select v-model="filters.province" size="small" placeholder="全部省份" clearable>
                            <el-option label="辽宁省" value="辽宁" />
                            <el-option label="广东省" value="广东" />
                            <el-option label="北京市" value="北京" />
                        </el-select>
                    </div>
                    <div class="filter-item">
                        <label>城市</label>
                        <el-select v-model="filters.city" size="small" placeholder="全部城市" clearable>
                            <el-option label="沈阳" value="沈阳" />
                            <el-option label="大连" value="大连" />
                        </el-select>
                    </div>
                    <div class="filter-item">
                        <label>质量等级</label>
                        <el-select v-model="filters.level" size="small" placeholder="全部" clearable>
                            <el-option label="优" value="优" />
                            <el-option label="良" value="良" />
                            <el-option label="轻度污染" value="轻度污染" />
                            <el-option label="中度污染" value="中度污染" />
                            <el-option label="重度污染" value="重度污染" />
                        </el-select>
                    </div>
                    <div class="filter-item">
                        <label>数据状态</label>
                        <el-select v-model="filters.status" size="small" placeholder="全部" clearable>
                            <el-option label="待指派" value="pending" />
                            <el-option label="已指派" value="assigned" />
                            <el-option label="已检测" value="detected" />
                            <el-option label="已确认" value="confirmed" />
                        </el-select>
                    </div>
                    <div class="filter-item">
                        <label>开始日期</label>
                        <el-date-picker v-model="filters.startDate" type="date" size="small" placeholder="开始日期" />
                    </div>
                    <div class="filter-item">
                        <label>结束日期</label>
                        <el-date-picker v-model="filters.endDate" type="date" size="small" placeholder="结束日期" />
                    </div>
                    <div class="filter-item filter-actions">
                        <el-button type="primary" size="small" @click="handleSearch"><el-icon><Search /></el-icon> 检索</el-button>
                        <el-button size="small" @click="handleReset">重置</el-button>
                    </div>
                </div>

                <el-table :data="tableData" stripe style="width:100%" :loading="loading">
                    <el-table-column type="selection" width="40" />
                    <el-table-column prop="reportCode" label="数据编号" width="180" />
                    <el-table-column prop="userName" label="上报人" width="100" />
                    <el-table-column prop="province" label="省份" width="80" />
                    <el-table-column prop="city" label="城市" width="80" />
                    <el-table-column prop="aqiValue" label="AQI数值" width="90">
                        <template #default="{ row }">
                            <strong>{{ row.aqiValue }}</strong>
                        </template>
                    </el-table-column>
                    <el-table-column prop="aqiLevel" label="质量等级" width="110">
                        <template #default="{ row }">
                            <el-tag :type="{
                                '优': 'success',
                                '良': 'success',
                                '轻度污染': 'warning',
                                '中度污染': 'danger',
                                '重度污染': 'danger'
                            }[row.aqiLevel]" effect="dark" size="small">
                                {{ row.aqiLevel }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="pm25" label="PM2.5" width="80" />
                    <el-table-column prop="pm10" label="PM10" width="80" />
                    <el-table-column prop="reportedAt" label="上报时间" width="160" />
                    <el-table-column prop="status" label="状态" width="90">
                        <template #default="{ row }">
                            <el-tag v-if="row.status === 'pending'" type="warning" size="small">待指派</el-tag>
                            <el-tag v-else-if="row.status === 'assigned'" type="primary" size="small">已指派</el-tag>
                            <el-tag v-else-if="row.status === 'detected'" type="info" size="small">已检测</el-tag>
                            <el-tag v-else-if="row.status === 'confirmed'" type="success" size="small">已确认</el-tag>
                            <el-tag v-else type="danger" size="small">已驳回</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="250" fixed="right">
                        <template #default="{ row }">
                            <el-button 
                                type="primary" 
                                size="small" 
                                link 
                                :disabled="row.status !== 'pending'"
                                @click="handleAssign(row)"
                            >
                                <el-icon><UserPlus /></el-icon> 指派
                            </el-button>
                            <el-button type="success" size="small" link>确认</el-button>
                            <el-button type="danger" size="small" link>驳回</el-button>
                            <el-button size="small" link>详情</el-button>
                        </template>
                    </el-table-column>
                </el-table>

                <div class="pagination-wrap">
                    <span class="text-muted">共 {{ total }} 条数据</span>
                    <el-pagination 
                        v-model:current-page="currentPage" 
                        :page-size="10" 
                        :total="total"
                        layout="prev, pager, next" 
                        size="small" 
                        background 
                        @current-change="loadReports"
                    />
                </div>
            </div>
        </div>

        <AssignWorker
            :visible="showAssignModal"
            :report-id="selectedReport?.reportId"
            :report-city="selectedReport?.city"
            :admin-id="adminId"
            @close="showAssignModal = false"
            @success="handleAssignSuccess"
        />
    </div>
</template>

<style scoped>
.stat-row {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    margin-bottom: 24px;
}
.filter-item :deep(.el-select) {
    width: 160px;
}

@media (max-width: 1400px) {
    .stat-row {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (max-width: 768px) {
    .stat-row {
        grid-template-columns: 1fr;
    }
}

.stat-card {
    background: #fff;
    border-radius: 10px;
    padding: 16px 18px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
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
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
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
    padding: 16px;
}

.filter-bar {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
    align-items: flex-end;
    margin-bottom: 16px;
}

.filter-item label {
    font-size: 11px;
    color: #64748B;
    display: block;
    margin-bottom: 4px;
}

.filter-actions {
    display: flex;
    gap: 6px;
    align-items: flex-end;
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