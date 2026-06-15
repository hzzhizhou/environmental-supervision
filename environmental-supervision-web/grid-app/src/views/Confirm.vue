<script setup>
import { reactive,computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { postTask } from '../api/tasks'
import { formatTime } from '../untils/formatTime'
const aqiLevels = ['优', '良', '轻度污染', '中度污染', '重度污染', '严重污染']

const router=useRouter()
const report=history.state
const form = reactive({
    reportId:null,
    aqiValue: null,
    aqiLevel: '',
    pm25: null,
    pm10: null,
    o3: null,
    so2: null,
    no2: null,
    co: null,
    location: '',
    remark: '',
})

const handleSubmit =async() => {
    form.reportId=+report?.reportId??null
    if (!form.aqiValue||!form.aqiLevel||!form.pm25||
        !form.pm10||!form.o3||!form.so2||!form.no2||!form.co
        ||!form.location||!form.reportId
    ) {
        ElMessage.warning('请输入完整数据')
        return
    }
   try{
        const {data}=await postTask(form)
        ElMessage.success(data.message)
        router.replace('/grid/tasks')
   }catch(err){
    ElMessage.warning('提交失败')
   }
}
//等级颜色变换
const aqiColorMap = {
  '优': { bg: '#F0FDF4', num: '#10B981', text: '#065F46' },
  '良': { bg: '#F0F9FF', num: '#3B82F6', text: '#1E40AF' },
  '轻度污染': { bg: '#FFFBEB', num: '#F59E0B', text: '#92400E' },
  '中度污染': { bg: '#FFF7ED', num: '#F97316', text: '#9A3412' },
  '重度污染': { bg: '#FEF2F2', num: '#DC2626', text: '#991B1B' },
  '严重污染': { bg: '#FEF2F2', num: '#991B1B', text: '#7F1D1D' }
}

const aqiStyle = computed(() => {
  return aqiColorMap[form.aqiLevel] || { bg: '#F8FAFC', num: '#64748B', text: '#475569' }
})
</script>

<template>
    <div class="confirm">
        <div class="aqi-highlight" :style="{ background: aqiStyle.bg }">
            <div class="big-num":style="{ color: aqiStyle.num }">{{ form.aqiValue || '--' }}</div>
            <div class="level" :style="{ color: aqiStyle.text }">{{ form.aqiLevel || '请选择等级' }}</div>
        </div>

        <div class="section">
            <h6>上报信息</h6>
            <div class="info-row"><span class="label">数据编号</span><span class="value">{{ report?.reportCode??'无' }}</span>
            </div>
            <div class="info-row"><span class="label">上报人</span><span class="value">{{ report?.workerName??'网格员' }}</span></div>
            <div class="info-row"><span class="label">上报时间</span><span class="value">{{ report?formatTime(report?.assignTime):'无' }}</span>
            </div>
            <div class="info-row"><span class="label">截止时间</span><span class="value">{{ report?formatTime(report?.deadline):'无' }}</span></div>
        </div>

        <div class="section">
            <h6>污染物指标（实测数据）</h6>
            <div class="input-row">
                <span class="label">AQI数值</span>
                <el-input-number v-model="form.aqiValue" :min="0" size="small" controls-position="right" />
            </div>
            <div class="input-row">
                <span class="label">AQI等级</span>
                <el-select v-model="form.aqiLevel" size="small" style="width:160px">
                    <el-option v-for="item in aqiLevels" :key="item" :label="item" :value="item" />
                </el-select>
            </div>
            <div class="input-row">
                <span class="label">PM2.5</span>
                <el-input-number v-model="form.pm25" :min="0" :precision="1" size="small" controls-position="right" />
                <span class="unit">μg/m³</span>
            </div>
            <div class="input-row">
                <span class="label">PM10</span>
                <el-input-number v-model="form.pm10" :min="0" :precision="1" size="small" controls-position="right" />
                <span class="unit">μg/m³</span>
            </div>
            <div class="input-row">
                <span class="label">O₃</span>
                <el-input-number v-model="form.o3" :min="0" :precision="1" size="small" controls-position="right" />
                <span class="unit">μg/m³</span>
            </div>
            <div class="input-row">
                <span class="label">SO₂</span>
                <el-input-number v-model="form.so2" :min="0" :precision="1" size="small" controls-position="right" />
                <span class="unit">μg/m³</span>
            </div>
            <div class="input-row">
                <span class="label">NO₂</span>
                <el-input-number v-model="form.no2" :min="0" :precision="1" size="small" controls-position="right" />
                <span class="unit">μg/m³</span>
            </div>
            <div class="input-row">
                <span class="label">CO</span>
                <el-input-number v-model="form.co" :min="0" :precision="1" size="small" controls-position="right" />
                <span class="unit">mg/m³</span>
            </div>
        </div>

        <div class="section">
            <h6>检测信息</h6>
            <div class="input-row">
                <span class="label">检测地点</span>
                <el-input v-model="form.location" size="small" placeholder="请输入检测地点" style="width:200px" />
            </div>
        </div>

        <div class="section">
            <h6>核查结论</h6>
            <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入核查备注..." class="remark" />
        </div>

        <div class="btn-row">
            <el-button type="danger" size="large" round @click="$router.go(-1)">返回</el-button>
            <el-button type="success" size="large" round @click="handleSubmit">确认</el-button>
        </div>
    </div>
</template>


<style scoped>
.confirm {
    max-width: 600px;
    margin: 0 auto;
    padding: 16px;
}

.aqi-highlight {
    text-align: center;
    padding: 20px;
    background: #FEF2F2;
    border-radius: 12px;
    margin-bottom: 14px;
}

.big-num {
    font-size: 42px;
    font-weight: 800;
    color: #EF4444;
}

.level {
    font-size: 14px;
    color: #991B1B;
    font-weight: 600;
}

.section {
    background: #F8FAFC;
    border-radius: 12px;
    padding: 14px;
    margin-bottom: 12px;
}

.section h6 {
    font-size: 13px;
    font-weight: 700;
    color: #475569;
    margin: 0 0 8px;
}

.info-row {
    display: flex;
    justify-content: space-between;
    padding: 6px 0;
    font-size: 13px;
    border-bottom: 1px solid #F1F5F9;
}

.info-row:last-child {
    border-bottom: none;
}

.label {
    color: #64748B;
}

.value {
    font-weight: 600;
}

.input-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 0;
    font-size: 13px;
    border-bottom: 1px solid #F1F5F9;
}

.input-row .label {
    width: 80px;
    flex-shrink: 0;
    color: #64748B;
}

.input-row .el-input-number,
.input-row .el-select,
.input-row .el-input {
    width: 160px !important;
}

.input-row:last-child {
    border-bottom: none;
}

.unit {
    color: #64748B;
    font-size: 12px;
    margin-left: 4px;
}

.remark {
    margin-bottom: 10px;
}

.btn-row {
    display: flex;
    gap: 12px;
    margin-top: 16px;
}

.btn-row .el-button {
    flex: 1;
    height: 48px;
    font-size: 15px;
    font-weight: 700;
}

@media (max-width: 768px) {
    .confirm {
        padding: 12px;
    }
}
</style>