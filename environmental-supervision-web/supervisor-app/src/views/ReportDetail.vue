<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Picture } from '@element-plus/icons-vue'
import { getReportDetail } from '../api/reports'
import { turnPhoto } from '../utils/avatar'
const route = useRoute()
const router = useRouter()
const loading = ref(false)
const detail = ref(null)

// 是否显示操作按钮（可配置）
const showActions = computed(() => {
    return detail.value?.status === 'pending' || detail.value?.status === 'assigned'
})

// 污染类型转换
const turn = (type) => {
    const map = {
        'air': '大气污染',
        'water': '水污染',
        'soil': '土壤污染',
        'noise': '噪声污染',
        'other': '其他'
    }
    return map[type] || type || '-'
}

// 污染等级颜色和文字
const getLevelColor = (level) => {
    const map = {
        '轻微污染': '#10B981',
        '轻度污染': '#F59E0B',
        '中度污染': '#F97316',
        '严重污染': '#EF4444',
        '重度污染': '#7C3AED'
    }
    return map[level] || '#94A3B8'
}

const getLevelLabel = (level) => {
    const map = {
        'mild': '轻微污染',
        'light': '轻度污染',
        'moderate': '中度污染',
        'heavy': '严重污染',
        'severe': '重度污染'
    }
    return map[level] || level || '-'
}

// AQI颜色
const getAqiColor = (aqi) => {
    if (aqi <= 50) return '#10B981'
    if (aqi <= 100) return '#F59E0B'
    if (aqi <= 150) return '#F97316'
    if (aqi <= 200) return '#EF4444'
    return '#7C3AED'
}

// 状态标签类型
const getStatusType = (status) => {
    const map = {
        'pending': 'warning',
        'assigned': 'primary',
        'detected': 'info',
        'confirmed': 'success'
    }
    return map[status] || 'info'
}

// 时间格式化
const formatTime = (time) => {
    if (!time) return '-'
    const date = new Date(time)
    return date.toLocaleString('zh-CN')
}

// 返回
const goBack = () => {
    router.back()
}

// 指派处理
const handleAssign = () => {
    router.push(`/assign-report/${detail.value?.reportId}`)
}

// 确认完成
const handleConfirm = () => {
    router.push(`/confirm-report/${detail.value?.reportId}`)
}

// 打印详情
const handlePrint = () => {
    window.print()
}

// 获取详情

const fetchDetail = async () => {
    const reportId = route.params.id
    if (!reportId) {
        ElMessage.error('缺少上报ID')
        return
    }

    loading.value = true
    try {
        const { data } = await getReportDetail(reportId)

        if (data.code === 200) {
            detail.value = data.data
        } else {
            ElMessage.error(data.message || '获取详情失败')
        }
    } catch (err) {
        ElMessage.error('获取详情失败')
    } finally {
        loading.value = false
    }
}

fetchDetail()

</script>
<template>
    <div class="report-detail">
        <div class="detail-header">
            <el-button type="primary" text @click="goBack" class="back-btn">
                <el-icon>
                    <ArrowLeft />
                </el-icon>
                返回
            </el-button>
            <h2>上报详情</h2>
            <el-tag :type="getStatusType(detail?.status)" size="large">{{ detail?.statusDesc }}</el-tag>
        </div>

        <div class="detail-content" v-loading="loading">
            <!-- 基本信息 -->
            <div class="detail-card">
                <div class="card-title">基本信息</div>
                <div class="info-grid">
                    <div class="info-item">
                        <span class="label">上报编码：</span>
                        <span class="value">{{ detail?.reportCode }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">上报人：</span>
                        <span class="value">{{ detail?.realName }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">联系电话：</span>
                        <span class="value">{{ detail?.phone }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">上报时间：</span>
                        <span class="value">{{ formatTime(detail?.reportedAt) }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">确认时间：</span>
                        <span class="value">{{ detail?.confirmedAt ? formatTime(detail.confirmedAt) : '未确认' }}</span>
                    </div>
                </div>
            </div>

            <!-- 位置信息 -->
            <div class="detail-card">
                <div class="card-title">位置信息</div>
                <div class="info-grid">
                    <div class="info-item">
                        <span class="label">省份：</span>
                        <span class="value">{{ detail?.province }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">城市：</span>
                        <span class="value">{{ detail?.city }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">详细地址：</span>
                        <span class="value">{{ detail?.address }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">所属网格：</span>
                        <span class="value">{{ detail?.gridName }}</span>
                    </div>
                </div>
            </div>

            <!-- 污染信息 -->
            <div class="detail-card">
                <div class="card-title">污染信息</div>
                <div class="info-grid">
                    <div class="info-item">
                        <span class="label">污染类型：</span>
                        <span class="value">{{ turn(detail?.pollutionType) }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">污染等级：</span>
                        <span class="value" :style="{ color: getLevelColor(detail?.pollutionLevel) }">
                            {{ getLevelLabel(detail?.pollutionLevel) }}
                        </span>
                    </div>
                    <div class="info-item full-width">
                        <span class="label">污染描述：</span>
                        <span class="value">{{ detail?.pollutionDesc || '无' }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">备注：</span>
                        <span class="value">{{ detail?.remark || '无' }}</span>
                    </div>
                </div>
            </div>

            <!-- AQI数据 -->
            <div class="detail-card" v-if="detail?.aqiValue">
                <div class="card-title">AQI数据</div>
                <div class="aqi-grid">
                    <div class="aqi-item">
                        <div class="aqi-label">AQI指数</div>
                        <div class="aqi-value" :style="{ color: getAqiColor(detail?.aqiValue) }">
                            {{ detail?.aqiValue }}
                        </div>
                        <div class="aqi-level">{{ detail?.aqiLevel }}</div>
                    </div>
                    <div class="aqi-detail">
                        <div class="detail-row"><span>PM2.5：</span>{{ detail?.pm25 }} μg/m³</div>
                        <div class="detail-row"><span>PM10：</span>{{ detail?.pm10 }} μg/m³</div>
                        <div class="detail-row"><span>O₃：</span>{{ detail?.o3 }} μg/m³</div>
                        <div class="detail-row"><span>SO₂：</span>{{ detail?.so2 }} μg/m³</div>
                        <div class="detail-row"><span>NO₂：</span>{{ detail?.no2 }} μg/m³</div>
                        <div class="detail-row"><span>CO：</span>{{ detail?.co }} mg/m³</div>
                    </div>
                </div>
            </div>

            <!-- 现场照片 -->
            <div class="detail-card" v-if="detail?.photos && detail.photos.length > 0">
                <div class="card-title">现场照片</div>
                <div class="photo-list">
                    <el-image v-for="(photo, index) in detail.photos" :key="index" :src="turnPhoto(photo)"
                        :preview-src-list="detail.photos" :initial-index="index" fit="cover" class="photo-item" lazy>
                        <template #error>
                            <div class="photo-error">
                                <el-icon>
                                    <Picture />
                                </el-icon>
                                <span>加载失败</span>
                            </div>
                        </template>
                    </el-image>
                </div>
            </div>

            <!-- 操作按钮 -->
            <div class="detail-actions" v-if="showActions">
                <el-button v-if="detail?.status === 'pending'" type="primary" @click="handleAssign">
                    指派处理
                </el-button>
                <el-button v-if="detail?.status === 'assigned'" type="success" @click="handleConfirm">
                    确认完成
                </el-button>
                <el-button type="info" @click="handlePrint">打印详情</el-button>
            </div>
        </div>
    </div>
</template>



<style scoped>
.report-detail {
    padding: 20px;
    background: #f5f7fa;
    min-height: 100vh;
}

.detail-header {
    display: flex;
    align-items: center;
    gap: 16px;
    margin-bottom: 20px;
    background: #fff;
    padding: 16px 20px;
    border-radius: 12px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.detail-header h2 {
    flex: 1;
    margin: 0;
    font-size: 20px;
    color: #2c3e50;
}

.back-btn {
    font-size: 14px;
}

.detail-content {
    max-width: 900px;
    margin: 0 auto;
}

.detail-card {
    background: #fff;
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.card-title {
    font-size: 16px;
    font-weight: 600;
    color: #2c3e50;
    padding-bottom: 12px;
    margin-bottom: 16px;
    border-bottom: 2px solid #e9ecef;
}

.info-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
}

.info-item {
    display: flex;
    align-items: baseline;
}

.info-item.full-width {
    grid-column: span 2;
}

.info-item .label {
    width: 100px;
    color: #7f8c8d;
    font-size: 14px;
    flex-shrink: 0;
}

.info-item .value {
    color: #2c3e50;
    font-size: 14px;
    word-break: break-all;
}

/* AQI 区域 */
.aqi-grid {
    display: flex;
    gap: 30px;
    flex-wrap: wrap;
}

.aqi-item {
    text-align: center;
    padding: 0 20px;
    border-right: 1px solid #e9ecef;
}

.aqi-item:last-child {
    border-right: none;
}

.aqi-label {
    font-size: 12px;
    color: #7f8c8d;
    margin-bottom: 8px;
}

.aqi-value {
    font-size: 48px;
    font-weight: 700;
}

.aqi-level {
    font-size: 14px;
    color: #7f8c8d;
    margin-top: 4px;
}

.aqi-detail {
    flex: 1;
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 12px 20px;
}

.detail-row {
    font-size: 14px;
    color: #2c3e50;
}

.detail-row span {
    color: #7f8c8d;
    margin-right: 8px;
}

/* 照片列表 */
.photo-list {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
}

.photo-item {
    width: 120px;
    height: 120px;
    border-radius: 8px;
    cursor: pointer;
    object-fit: cover;
    border: 1px solid #e9ecef;
}

.photo-error {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: #f8f9fa;
    color: #adb5bd;
    font-size: 12px;
}

.photo-error .el-icon {
    font-size: 24px;
    margin-bottom: 4px;
}

/* 操作按钮 */
.detail-actions {
    display: flex;
    justify-content: center;
    gap: 16px;
    margin-top: 20px;
}

/* 打印样式 */
@media print {

    .detail-header .back-btn,
    .detail-actions {
        display: none;
    }

    .report-detail {
        background: #fff;
        padding: 0;
    }

    .detail-card {
        box-shadow: none;
        border: 1px solid #e9ecef;
        break-inside: avoid;
    }
}
</style>