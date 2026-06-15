<script setup>
import { ref } from 'vue'
import { getHasDone } from '../api/tasks';
import { ElMessage } from 'element-plus'
import { formatTime } from '../untils/formatTime';
const finishTasks=ref(null)
const refresh=async()=>{
    try{
          const {data}=await getHasDone({status:1})
          finishTasks.value=data.data.records
    }catch(err){
        ElMessage.warning('获取任务失败')
    }
}
refresh()
</script>
<template>
  
    <div class="confirmed-list">
        <div class="record-item" v-for="item in finishTasks">
            <div class="record-icon confirm">✓</div>
            <div class="record-info">
                <span class="code">{{ item?.reportCode }}</span>
                <div>
                    <span class="aqi" style="color:#10B981">{{ item.aqiValue }}</span>
                    <span class="level" style="color:#10B981">{{ item.aqiLevel }}</span>
                </div>
                <span class="meta">{{ formatTime(item?.detectTime) }} | {{ item.workerName }}</span>
            </div>
            <div class="record-result">
                <el-tag type="success" size="small">已审核</el-tag>
                <span class="points">+20积分</span>
            </div>
        </div>
    </div>
      <div v-if="finishTasks?.length === 0" class="empty">暂无已完成任务</div>
</template>


<style scoped>
.confirmed-list {
    max-width: 600px;
    margin: 0 auto;
    padding: 16px;
}
.record-item {
    display: flex;
    align-items: center;
    gap: 12px;
    background: #fff;
    border: 1px solid #E2E8F0;
    border-radius: 12px;
    padding: 14px;
    margin-bottom: 8px;
}

.record-icon {
    width: 36px;
    height: 36px;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    font-weight: 700;
    flex-shrink: 0;
}

.record-icon.confirm {
    background: #D1FAE5;
    color: #065F46;
}

.record-icon.reject {
    background: #FEE2E2;
    color: #991B1B;
}

.record-info {
    flex: 1;
}

.code {
    font-size: 11px;
    color: #64748B;
    display: block;
}

.aqi {
    font-size: 18px;
    font-weight: 700;
    margin-right: 6px;
}

.level {
    font-size: 11px;
}

.meta {
    font-size: 11px;
    color: #64748B;
    display: block;
}

.record-result {
    text-align: right;
    flex-shrink: 0;
}

.points {
    display: block;
    font-size: 10px;
    color: #64748B;
    margin-top: 2px;
}

.note {
    display: block;
    font-size: 10px;
    color: #EF4444;
    margin-top: 2px;
}
.empty {
    text-align: center;
    padding: 40px;
    color: #94A3B8;
    font-size: 14px;
}

@media (max-width: 768px) {
    .confirmed-list {
        padding: 12px;
    }
}
</style>