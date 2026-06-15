<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useRecordsStore } from '../store/records';
import { storeToRefs } from 'pinia';
import { ElMessage } from 'element-plus'

const router = useRouter()
const records = useRecordsStore()
records.fetchRecords()
const { assignedReports, pendingReports, confirmedReports, detectedReports, reports } = storeToRefs(records)
const filter = ref('all')

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)

// 拿到每个导航的数据
const currentList = computed(() => {
    let list = []
    switch (filter.value) {
        case 'all': 
            list = reports.value?.content || []
            break
        case 'pending': 
            list = pendingReports.value?.content || []
            break
        case 'assign': 
            list = assignedReports.value?.content || []
            break
        case 'detected': 
            list = detectedReports.value?.content || []
            break
        case 'confirmed': 
            list = confirmedReports.value?.content || []
            break
        default: 
            list = reports.value?.content || []
    }
    // 分页处理
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    return list.slice(start, end)
})

// 总条数
const total = computed(() => {
    switch (filter.value) {
        case 'all': return reports.value?.totalElements || 0
        case 'pending': return pendingReports.value?.totalElements || 0
        case 'assign': return assignedReports.value?.totalElements || 0
        case 'detected': return detectedReports.value?.totalElements || 0
        case 'confirmed': return confirmedReports.value?.totalElements || 0
        default: return reports.value?.totalElements || 0
    }
})

// 污染程度
const severityLevels = [
    { value: 'verygood', label: '优', color: '#10B981' },
    { value: 'good', label: '良好', color: '#3B82F6' },
    { value: 'mild', label: '轻微', color: '#F59E0B' },
    { value: 'moderate', label: '中等', color: '#F97316' },
    { value: 'serious', label: '重度', color: '#DC2626' },
    { value: 'severe', label: '严重', color: '#EF4444' }
]

const TurnC = (item) => {
    const level = severityLevels.find(t => t.value === item)
    return level || { label: item || '未知', color: '#94A3B8' }
}

// 转换时间
const formatTime = (time) => {
    if (!time) return ''
    const date = new Date(time)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hour = String(date.getHours()).padStart(2, '0')
    const minute = String(date.getMinutes()).padStart(2, '0')
    return `${year}-${month}-${day} ${hour}:${minute}`
}

// 污染类型
const pollutionTypes = [
    { value: 'smoke', label: '工厂烟尘' },
    { value: 'odor', label: '异味/恶臭' },
    { value: 'dust', label: '工地扬尘' },
    { value: 'burn', label: '露天焚烧' },
    { value: 'wastewater', label: '黑臭水体' },
    { value: 'garbage', label: '垃圾堆放' },
    { value: 'noise', label: '噪音污染' },
    { value: 'other', label: '其他' }
]

const turn = (item) => {
    const type = pollutionTypes.find(t => t.value === item)
    return type?.label ?? '未知类型'
}

// 跳转详情页
const goToDetail = (reportId) => {
    router.push(`/report-detail/${reportId}`)
}

// 分页切换
const handlePageChange = (page) => {
    currentPage.value = page
}

const handleSizeChange = (size) => {
    pageSize.value = size
    currentPage.value = 1
}

// 筛选切换时重置分页
const handleFilterChange = (value) => {
    filter.value = value
    currentPage.value = 1
}
</script>

<template>
    <div class="records">
        <div class="stat-row">
            <div class="stat-mini">
                <div class="num" style="color:#3B82F6">{{ reports?.totalElements ?? 0 }}</div>
                <div class="lbl">总上报</div>
            </div>
            <div class="stat-mini">
                <div class="num" style="color:#10B981">{{ pendingReports?.totalElements ?? 0 }}</div>
                <div class="lbl">待指派</div>
            </div>
            <div class="stat-mini">
                <div class="num" style="color:#F59E0B">{{ assignedReports?.totalElements ?? 0 }}</div>
                <div class="lbl">已指派</div>
            </div>
            <div class="stat-mini">
                <div class="num" style="color:#8B5CF6">{{ detectedReports?.totalElements ?? 0 }}</div>
                <div class="lbl">已检测</div>
            </div>
            <div class="stat-mini">
                <div class="num" style="color:#EF4444">{{ confirmedReports?.totalElements ?? 0 }}</div>
                <div class="lbl">已确认</div>
            </div>
        </div>

        <el-radio-group v-model="filter" size="small" class="filter-tabs" @change="handleFilterChange">
            <el-radio-button value="all">全部</el-radio-button>
            <el-radio-button value="pending">待指派</el-radio-button>
            <el-radio-button value="assign">已指派</el-radio-button>
            <el-radio-button value="detected">已检测</el-radio-button>
            <el-radio-button value="confirmed">已确认</el-radio-button>
        </el-radio-group>

        <div class="record-card" v-for="item in currentList" :key="item.reportId">
            <div class="card-top">
                <span class="code">{{ item.reportCode }}</span>
                <el-tag size="small" round>{{ item.statusDesc }}</el-tag>
            </div>
            <div class="pollution-type">{{ turn(item.pollutionType) }}</div>
            <div class="level" :style="{ color: TurnC(item.pollutionLevel)?.color }">
                {{ TurnC(item.pollutionLevel)?.label }}
            </div>
            <div class="meta">
                <span>{{ item.province }}{{ item.city }}</span>
                <span>{{ formatTime(item.reportedAt) }}</span>
            </div>
            <!-- 详情按钮 -->
            <div class="card-footer">
                <el-button type="primary" text size="small" @click="goToDetail(item.reportId)">
                    查看详情
                </el-button>
            </div>
        </div>

        <!-- 分页 -->
        <div class="pagination-wrap" v-if="total > 0">
            <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[5, 10, 20, 50]"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handlePageChange"
            />
        </div>

        <div class="record-card nocontent" v-if="reports && currentList.length === 0">暂无内容</div>
    </div>
</template>

<style scoped>
.records {
    max-width: 600px;
    margin: 0 auto;
    padding: 16px;
}

.stat-row {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 8px;
    margin-bottom: 16px;
}

.stat-mini {
    background: #F8FAFC;
    border-radius: 10px;
    padding: 12px;
    text-align: center;
}

.num {
    font-size: 20px;
    font-weight: 800;
}

.lbl {
    font-size: 10px;
    color: #64748B;
    margin-top: 2px;
}

.card-top {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 6px;
}

.pollution-type {
    font-size: 13px;
    color: #475569;
    margin-bottom: 4px;
}

.filter-tabs {
    display: flex;
    width: 100%;
    margin-bottom: 14px;
}

.filter-tabs :deep(.el-radio-group) {
    width: 100%;
}

.filter-tabs :deep(.el-radio-button) {
    flex: 1;
}

.record-card {
    background: #fff;
    border: 1px solid #E2E8F0;
    border-radius: 12px;
    padding: 14px;
    margin-bottom: 10px;
}

.code {
    font-size: 11px;
    color: #64748B;
    display: block;
    margin-bottom: 2px;
}

.level {
    font-size: 22px;
    font-weight: 800;
    margin-right: 6px;
}

.meta {
    font-size: 11px;
    color: #64748B;
    display: flex;
    gap: 12px;
}

/* 卡片底部 */
.card-footer {
    margin-top: 12px;
    padding-top: 10px;
    border-top: 1px solid #E2E8F0;
    display: flex;
    justify-content: flex-end;
}

/* 分页样式 */
.pagination-wrap {
    display: flex;
    justify-content: center;
    margin-top: 20px;
    padding: 10px 0;
}

.reject-reason {
    font-size: 11px;
    color: #EF4444;
    background: #FEF2F2;
    padding: 8px;
    border-radius: 6px;
    margin-top: 8px;
}

.nocontent {
    text-align: center;
}

@media (max-width: 768px) {
    .records {
        padding: 12px;
    }
    
    .stat-mini {
        padding: 8px;
    }
    
    .num {
        font-size: 16px;
    }
    
    .lbl {
        font-size: 9px;
    }
    
    .level {
        font-size: 18px;
    }
}
</style>