<script setup>
import { useRecordsStore } from '../store/records';
import { storeToRefs } from 'pinia';
import { ref } from 'vue'
const records=useRecordsStore()
const {reports}=storeToRefs(records)
records.fetchRecords()
//转中文污染程度
const severityLevels = [
     {value:'verygood',label:'优',color:'#10B981'},
     {value:'good',label:'良好',color:'#3B82F6'},
    { value: 'mild', label: '轻微', color: '#F59E0B' },
    { value: 'moderate', label: '中等', color: '#F97316' },
    {value:'serious',label:'重度',color:'#DC2626'},
    { value: 'severe', label: '严重', color: '#EF4444' } 
]
const TurnC=(item)=>{
    const level=severityLevels.find(t=>t.value===item)
    return level
}
//状态颜色
const allColor=[
    {value:'待指派',color:'#10B981'},
    {value:'已指派',color:'#F59E0B'},
    {value:'已检测',color:'#8B5CF6'},
    {value:'已确认',color:'#EF4444'},
]
const status=(item)=>{
    const c=allColor.find(t=>t.value===item)
    return c
}
</script>
<template>
    <div class="progress">
        <div class="progress-card" v-for="item in reports?.content">
            <div class="header">
                <strong>{{ item.reportCode}}</strong>
                <el-tag type="warning" size="small" round :style="{color:status(item.statusDesc)?.color}">{{ status(item.statusDesc)?.value }}</el-tag>
            </div>
            <div class="aqi-info" :style="{ color:TurnC(item.pollutionLevel).color }">{{ TurnC(item.pollutionLevel)?.label }}</div>

            <div class="steps">
                <div class="step done">1</div>
                <div class="line" :class="{done:item.status!=='pending'}"></div>
                <div class="step" :class="{done:item.status!=='assign'&&item.status!=='pending',active:item.status==='assign'}" >2</div>
                <div class="line" :class="{done:item.status==='detected'||item.status==='confirmed'}"></div>
                <div class="step" :class="{done:item.status==='detected'||item.status==='confirmed',active:item.status==='detected'}">3</div>
                <div class="line" :class="{done:item.status==='confirmed'}"></div>
                <div class="step" :class="{done:item.status==='confirmed'}">4</div>
            </div>
            <div class="step-labels">
                <span>提交上报</span>
                <span>管理员受理</span>
                <span>网格员确认</span>
                <span>办结</span>
            </div>
            <div class="estimate">{{ item.confirmAt?'完成':'预计24小时内完成确认'}}</div>
        </div>
    </div>
</template>

<style scoped>
.progress {
    max-width: 600px;
    margin: 0 auto;
    padding: 16px;
}

.progress-card {
    background: #fff;
    border: 1px solid #E2E8F0;
    border-radius: 14px;
    padding: 16px;
    margin-bottom: 12px;
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 6px;
}

.header strong {
    font-size: 14px;
}

.aqi-info {
    font-size: 13px;
    color: #475569;
    margin-bottom: 14px;
}

.steps {
    display: flex;
    align-items: center;
    gap: 0;
    margin: 12px 0 4px;
}

.step {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    font-weight: 700;
    background: #E2E8F0;
    color: #94A3B8;
    flex-shrink: 0;
}

.step.done { background: #10B981; color: #fff; }
.step.active { background: #3B82F6; color: #fff; box-shadow: 0 0 0 4px rgba(59,130,246,0.2); }

.line {
    flex: 1;
    height: 3px;
    background: #E2E8F0;
}

.line.done { background: #10B981; }

.step-labels {
    display: flex;
    justify-content: space-between;
    font-size: 10px;
    color: #64748B;
    padding: 0 4px;
}

.step-labels span {
    text-align: center;
    flex: 1;
}

.estimate {
    background: #F0FDF4;
    border-radius: 8px;
    padding: 10px;
    text-align: center;
    font-size: 13px;
    color: #065F46;
    font-weight: 600;
    margin-top: 12px;
}

.done-text {
    background: #D1FAE5;
}

@media (max-width: 768px) {
    .progress { padding: 12px; }
}
</style>