<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import chinaJson from '../../public/china.json'

const currentTime = ref('')
let timer = null

const provinceData = [
    { name: '北京', value: 205 }, { name: '天津', value: 88 }, { name: '河北', value: 102 },
    { name: '山西', value: 95 }, { name: '内蒙古', value: 55 }, { name: '辽宁', value: 118 },
    { name: '吉林', value: 62 }, { name: '黑龙江', value: 58 }, { name: '上海', value: 88 },
    { name: '江苏', value: 95 }, { name: '浙江', value: 105 }, { name: '安徽', value: 72 },
    { name: '福建', value: 55 }, { name: '江西', value: 68 }, { name: '山东', value: 82 },
    { name: '河南', value: 92 }, { name: '湖北', value: 78 }, { name: '湖南', value: 85 },
    { name: '广东', value: 156 }, { name: '广西', value: 65 }, { name: '海南', value: 32 },
    { name: '重庆', value: 88 }, { name: '四川', value: 75 }, { name: '贵州', value: 52 },
    { name: '云南', value: 38 }, { name: '西藏', value: 28 }, { name: '陕西', value: 72 },
    { name: '甘肃', value: 58 }, { name: '青海', value: 35 }, { name: '宁夏', value: 48 },
    { name: '新疆', value: 42 }, { name: '台湾', value: 50 }, { name: '香港', value: 68 }, { name: '澳门', value: 55 }
]

const excellent = computed(() => provinceData.filter(d => d.value <= 50).length)
const good = computed(() => provinceData.filter(d => d.value > 50 && d.value <= 100).length)
const polluted = computed(() => provinceData.filter(d => d.value > 100).length)
const avgAqi = computed(() => Math.round(provinceData.reduce((s, d) => s + d.value, 0) / provinceData.length))

const rankData = computed(() => [...provinceData].sort((a, b) => b.value - a.value).slice(0, 10))

function getColor(aqi) {
    if (aqi <= 50) return '#10B981'
    if (aqi <= 100) return '#F59E0B'
    if (aqi <= 150) return '#F97316'
    if (aqi <= 200) return '#EF4444'
    return '#7C3AED'
}

function getLevel(aqi) {
    if (aqi <= 50) return '优'
    if (aqi <= 100) return '良'
    if (aqi <= 150) return '轻度污染'
    if (aqi <= 200) return '中度污染'
    return '重度污染'
}

function updateTime() {
    const now = new Date()
    currentTime.value = now.getFullYear() + '-' +
        String(now.getMonth() + 1).padStart(2, '0') + '-' +
        String(now.getDate()).padStart(2, '0') + ' ' +
        String(now.getHours()).padStart(2, '0') + ':' +
        String(now.getMinutes()).padStart(2, '0') + ':' +
        String(now.getSeconds()).padStart(2, '0')
}

const mapRef = ref(null)
let chart = null

onMounted(() => {
    updateTime()
    timer = setInterval(updateTime, 1000)
    nextTick(() => initChart())
})

onUnmounted(() => {
    clearInterval(timer)
    if (chart) chart.dispose()
})

function initChart() {
    echarts.registerMap('china', chinaJson)
    chart = echarts.init(mapRef.value)
    chart.setOption({
        backgroundColor: 'transparent',
        tooltip: { trigger: 'item', formatter: p => '<strong>' + p.name + '</strong><br/>AQI: ' + p.value },
        series: [{
            type: 'map',
            map: 'china',
            roam: false,
            itemStyle: { borderColor: '#0F172A', borderWidth: 1 },
            emphasis: { label: { show: true, color: '#fff', fontSize: 13 }, itemStyle: { areaColor: '#60A5FA', borderColor: '#fff', borderWidth: 2 } },
            data: provinceData.map(d => ({ name: d.name, value: d.value, itemStyle: { areaColor: getColor(d.value) } }))
        }]
    })
    window.addEventListener('resize', () => chart.resize())
}
// 在 initChart 函数后面添加
function initPieAndLine() {
    // 饼图
    const pieChart = echarts.init(document.getElementById('pieChart'))
    pieChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {d}%' },
        legend: { orient: 'vertical', left: 'left', textStyle: { color: '#fff' } },
        series: [{
            type: 'pie',
            radius: '55%',
            data: [
                { name: '优', value: 43, itemStyle: { color: '#10B981' } },
                { name: '良', value: 35, itemStyle: { color: '#F59E0B' } },
                { name: '轻度污染', value: 15, itemStyle: { color: '#F97316' } },
                { name: '中度污染', value: 5, itemStyle: { color: '#EF4444' } },
                { name: '重度污染', value: 2, itemStyle: { color: '#7C3AED' } }
            ]
        }]
    })

    // 折线图
    const lineChart = echarts.init(document.getElementById('lineChart'))
    lineChart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'] },
        yAxis: { type: 'value', name: '超标天数' },
        series: [{ type: 'line', data: [12, 10, 15, 18, 22, 25, 20, 18, 14, 11, 9, 8], smooth: true, lineStyle: { color: '#EF4444', width: 3 } }]
    })
}

// 在 onMounted 里调用
onMounted(() => {
    updateTime()
    timer = setInterval(updateTime, 1000)
    nextTick(() => {
        initChart()
        initPieAndLine()
    })
})
</script>
<template>
    <div class="dashboard">
        <div class="header">
            <div class="header-left">
                <h2>东森航空质量环境综合质量平台</h2>
                <span class="subtitle">Air Quality Integrated Monitoring Platform</span>
            </div>
            <div class="header-right">
                <span class="datetime">{{ currentTime }}</span>
                <el-tag type="success" size="large">系统运行中</el-tag>
            </div>
        </div>

        <!-- 两个覆盖率卡片 -->
        <div class="stats-row">
            <div class="stat-card">
                <div class="stat-value">47<span class="stat-unit">%</span></div>
                <div class="stat-label">全国省会城市网络覆盖率</div>
            </div>
            <div class="stat-card">
                <div class="stat-value">16<span class="stat-unit">%</span></div>
                <div class="stat-label">全国大城市网络覆盖率</div>
            </div>
        </div>

        <div class="main-area">
            <!-- 左侧地图 -->
            <div class="map-panel">
                <div class="panel-title">全国AQI实时分布</div>
                <div id="chinaMap" ref="mapRef" class="map-container"></div>
            </div>

            <!-- 右侧面板 -->
            <div class="right-panels">
                <!-- 空气质量指数级别分布（饼图） -->
                <div class="panel">
                    <div class="panel-title">空气质量指数级别分布</div>
                    <div id="pieChart" class="pie-chart"></div>
                </div>

                <!-- 12个月内空气质量超标趋势（折线图） -->
                <div class="panel">
                    <div class="panel-title">12个月内空气质量超标趋势</div>
                    <div id="lineChart" class="line-chart"></div>
                </div>

                <!-- 空气质量检测实时统计 -->
                <div class="panel">
                    <div class="panel-title">空气质量检测实时统计</div>
                    <div class="realtime-stats">
                        <div class="stat-item">
                            <span class="stat-label">正常监测站</span>
                            <span class="stat-value green">41</span>
                        </div>
                        <div class="stat-item">
                            <span class="stat-label">预警监测站</span>
                            <span class="stat-value red">2</span>
                        </div>
                        <div class="stat-item total">
                            <span class="stat-label">总监测站</span>
                            <span class="stat-value">43</span>
                        </div>
                    </div>
                </div>

                <!-- 右侧底部区域：省份排名 + 汇总 -->
                <div class="bottom-row">
                    <!-- 省份AQI排名 TOP10 -->
                    <div class="panel rank-panel">
                        <div class="panel-title">省份AQI排名 TOP10</div>
                        <div class="rank-list">
                            <div v-for="(item, index) in rankData" :key="item.name" class="rank-item">
                                <span class="rank-num" :class="'t' + (index + 1)">{{ index + 1 }}</span>
                                <span class="rank-name">{{ item.name }}</span>
                                <span class="rank-aqi" :style="{ color: getColor(item.value) }">{{ item.value }}</span>
                                <span class="rank-level">{{ getLevel(item.value) }}</span>
                            </div>
                        </div>
                    </div>

                    <!-- 全国汇总数据 -->
                    <div class="panel summary-panel">
                        <div class="panel-title">全国汇总数据</div>
                        <div class="summary-grid">
                            <div class="summary-item">
                                <span class="summary-label">今日上报量</span>
                                <span class="summary-value">1,247</span>
                            </div>
                            <div class="summary-item">
                                <span class="summary-label">全国均AQI</span>
                                <span class="summary-value" :style="{ color: getColor(avgAqi) }">{{ avgAqi }}</span>
                            </div>
                            <div class="summary-item">
                                <span class="summary-label">确认率</span>
                                <span class="summary-value green">92.8%</span>
                            </div>
                            <div class="summary-item">
                                <span class="summary-label">活跃网格员</span>
                                <span class="summary-value">86</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="watermark">东森航空 · 环境监测</div>
    </div>
</template>

<style scoped>
.dashboard {
    background: #0F172A;
    min-height: 100vh;
    padding: 20px;
    position: relative;
}

/* 头部 */
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 16px;
    border-bottom: 1px solid #1E293B;
    margin-bottom: 20px;
    flex-wrap: wrap;
    gap: 10px;
}

.header-left h2 {
    margin: 0;
    font-size: 22px;
    color: #fff;
    font-weight: 600;
}

.subtitle {
    font-size: 12px;
    color: #64748B;
    letter-spacing: 2px;
}

.header-right {
    display: flex;
    align-items: center;
    gap: 16px;
    flex-wrap: wrap;
}

.datetime {
    font-size: 16px;
    font-weight: 600;
    color: #60A5FA;
}

/* 两个覆盖率卡片 */
.stats-row {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
    margin-bottom: 20px;
}

.stat-card {
    background: linear-gradient(135deg, #1E293B 0%, #0F172A 100%);
    border-radius: 12px;
    padding: 20px;
    text-align: center;
    border: 1px solid #334155;
}

.stat-value {
    font-size: 48px;
    font-weight: 800;
    color: #60A5FA;
}

.stat-unit {
    font-size: 20px;
    margin-left: 4px;
}

.stat-label {
    font-size: 14px;
    color: #94A3B8;
    margin-top: 8px;
}

/* 主区域 */
.main-area {
    display: flex;
    gap: 20px;
    min-height: calc(100vh - 280px);
}

/* 左侧地图 */
.map-panel {
    flex: 1.2;
    background: #1E293B;
    border-radius: 12px;
    padding: 16px;
    display: flex;
    flex-direction: column;
    min-width: 0;
}

.map-container {
    flex: 1;
    width: 100%;
    min-height: 400px;
    border-radius: 8px;
}

/* 右侧面板 */
.right-panels {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 16px;
    min-width: 0;
}

.panel {
    background: #1E293B;
    border-radius: 12px;
    padding: 16px;
}

.panel-title {
    font-size: 14px;
    font-weight: 700;
    color: #E2E8F0;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid #334155;
}

/* 饼图 - 修复挤在一起的问题 */
.pie-chart {
    height: 260px;
    width: 100%;
}

/* 强制调整饼图图例位置和样式 */
.pie-chart :deep(.echarts-legend),
.pie-chart :deep(div[class*="legend"]) {
    display: flex !important;
    flex-wrap: wrap !important;
    justify-content: center !important;
    gap: 8px 16px !important;
    width: 100% !important;
    left: 0 !important;
    bottom: 0 !important;
    top: auto !important;
    padding: 8px 0 0 0 !important;
}

.pie-chart :deep(.echarts-legend-item),
.pie-chart :deep(div[class*="legend-item"]) {
    display: inline-flex !important;
    align-items: center !important;
    gap: 4px !important;
}

.pie-chart :deep(canvas) {
    max-width: 100%;
    height: 200px !important;
}

/* 折线图 */
.line-chart {
    height: 220px;
    width: 100%;
}

/* 实时统计 */
.realtime-stats {
    display: flex;
    justify-content: space-around;
    padding: 16px 0;
}

.stat-item {
    text-align: center;
}

.stat-item .stat-label {
    font-size: 12px;
    color: #94A3B8;
    display: block;
    margin-bottom: 8px;
}

.stat-item .stat-value {
    font-size: 32px;
    font-weight: 800;
}

.stat-item.total .stat-value {
    color: #60A5FA;
}

.green {
    color: #10B981;
}

.red {
    color: #EF4444;
}

/* 底部行：排名 + 汇总 */
.bottom-row {
    display: flex;
    gap: 16px;
    flex: 1;
    min-height: 280px;
}

.rank-panel {
    flex: 1.5;
    overflow-y: auto;
}

.summary-panel {
    flex: 1;
}

.rank-list {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.rank-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 8px 10px;
    background: rgba(255, 255, 255, 0.02);
    border-radius: 6px;
}

.rank-num {
    width: 24px;
    height: 24px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    font-weight: 700;
    background: #334155;
    color: #94A3B8;
}

.rank-num.t1 { background: #F59E0B; color: #fff; }
.rank-num.t2 { background: #94A3B8; color: #fff; }
.rank-num.t3 { background: #B45309; color: #fff; }

.rank-name {
    flex: 1;
    font-size: 13px;
    color: #E2E8F0;
}

.rank-aqi {
    font-size: 18px;
    font-weight: 800;
}

.rank-level {
    font-size: 11px;
    color: #64748B;
    width: 60px;
}

.summary-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 12px;
}

.summary-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px;
    background: rgba(255, 255, 255, 0.03);
    border-radius: 8px;
}

.summary-label {
    font-size: 12px;
    color: #94A3B8;
}

.summary-value {
    font-size: 18px;
    font-weight: 700;
    color: #fff;
}

.watermark {
    position: fixed;
    bottom: 20px;
    right: 30px;
    font-size: 12px;
    color: rgba(255, 255, 255, 0.05);
    pointer-events: none;
}

/* ========== 手机端适配 ========== */
@media (max-width: 768px) {
    .dashboard {
        padding: 12px;
    }
    
    .header-left h2 {
        font-size: 16px;
    }
    
    .subtitle {
        font-size: 10px;
    }
    
    .datetime {
        font-size: 12px;
    }
    
    .stat-value {
        font-size: 28px;
    }
    
    .stat-unit {
        font-size: 14px;
    }
    
    .stat-label {
        font-size: 11px;
    }
    
    .main-area {
        flex-direction: column;
        min-height: auto;
    }
    
    .map-panel {
        min-height: 300px;
    }
    
    .map-container {
        min-height: 250px;
    }
    
    .right-panels {
        width: 100%;
    }
    
    .pie-chart {
        height: 240px;
    }
    
    .pie-chart :deep(canvas) {
        height: 180px !important;
    }
    
    .line-chart {
        height: 200px;
    }
    
    .bottom-row {
        flex-direction: column;
        gap: 16px;
    }
    
    .rank-panel {
        max-height: 300px;
    }
    
    .rank-aqi {
        font-size: 14px;
    }
    
    .rank-level {
        width: 50px;
        font-size: 10px;
    }
    
    .summary-value {
        font-size: 14px;
    }
    
    .watermark {
        font-size: 10px;
        bottom: 10px;
        right: 15px;
    }
}

/* 小手机端 */
@media (max-width: 480px) {
    .stats-row {
        gap: 10px;
    }
    
    .stat-card {
        padding: 12px;
    }
    
    .stat-value {
        font-size: 24px;
    }
    
    .rank-item {
        padding: 6px 8px;
    }
    
    .rank-name {
        font-size: 11px;
    }
    
    .rank-aqi {
        font-size: 12px;
    }
    
    .summary-grid {
        gap: 8px;
    }
    
    .summary-item {
        padding: 8px;
    }
    
    .summary-label {
        font-size: 10px;
    }
    
    .summary-value {
        font-size: 12px;
    }
}
</style>
