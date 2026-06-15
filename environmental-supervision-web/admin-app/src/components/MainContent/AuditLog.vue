<script setup>
import { reactive, ref } from 'vue'
import { Document } from '@element-plus/icons-vue'
const filters = reactive({
    action: '',
    date: ''
})

const currentPage = ref(1)

const tableData = ref([
    {
        auditCode: 'RV-20260603-001',
        reportCode: 'AQI-20260603-002',
        auditor: 'admin',
        action: 'confirm',
        province: '广东省',
        city: '广州',
        aqi: 68,
        auditTime: '2026-06-03 10:30',
        remark: '数据合理，与官方监测站一致'
    }
])  
</script>

<template>
    <div class="audit-log">
        <!-- 表格卡片 -->
        <div class="card-box">
            <div class="card-header">
                <h6><el-icon><Document /></el-icon> 数据审核记录</h6>
                <div class="header-actions">
                    <el-select v-model="filters.action" size="small" placeholder="全部操作" clearable style="width:120px">
                        <el-option label="确认" value="confirm" />
                        <el-option label="驳回" value="reject" />
                    </el-select>
                    <el-date-picker v-model="filters.date" type="date" size="small" placeholder="选择日期" />
                    <el-button size="small">筛选</el-button>
                </div>
            </div>
            <div class="card-body">
                <el-table :data="tableData" stripe style="width:100%">
                    <el-table-column prop="auditCode" label="审核编号" width="180" />
                    <el-table-column prop="reportCode" label="数据编号" width="180" />
                    <el-table-column prop="auditor" label="审核人" width="100" />
                    <el-table-column prop="action" label="操作类型" width="100">
                        <template #default="{ row }">
                            <el-tag v-if="row.action === 'confirm'" type="success" size="small">✅ 确认</el-tag>
                            <el-tag v-else type="danger" size="small">❌ 驳回</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="province" label="省份" width="80" />
                    <el-table-column prop="city" label="城市" width="80" />
                    <el-table-column prop="aqi" label="AQI数值" width="90" />
                    <el-table-column prop="auditTime" label="审核时间" width="160" />
                    <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
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