<script setup>
import { useTasksStore } from '../store/tasks';
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
import { UserFilled } from '@element-plus/icons-vue'
import { getTasks,acceptTask,getHasDone } from '../api/tasks';
import {ElMessage} from 'element-plus'
import { formatTime } from '../untils/formatTime';
const tasks = useTasksStore()
tasks.fetchTasks()
const { allTasks } = storeToRefs(tasks)
const noconfrimNumber=ref(null)
const Fresh=async()=>{
    try{
        const {data}=await getHasDone({status:0})        
        noconfrimNumber.value=data?.data?.records.length??0
    }catch(err){
        ElMessage.warning('获取数据失败')
    }
}
Fresh()
//这里是实现不同列表的效果
const currentTaskList = ref('all')
const changeList = async(item) => {
    if(item==='noConfirm'){
        const {data}=await getHasDone({status:0})
        currentTaskList.value = item
        currentTasks.value = data.data
    }else{
         currentTaskList.value = item
         refresh(item)
    }
   
}
//获取不同列表下的任务
const currentTasks = ref(null)
currentTasks.value = allTasks.value
const refresh= async (status) => {
    try{
        const { data } = await getTasks({status})
        currentTasks.value = data.data    
    }catch(err){
        ElMessage.warning('数据获取异常')
    }
}
//接受任务接口
const handleAccept = async (taskId) => {
  try{
    const {data}=await acceptTask(taskId)
    ElMessage.success(data?.message??'任务已接受')
     refresh('assigned')
  }catch(err){
    ElMessage.warning('任务接受失败')
  }
  
}
</script>
<template>
    <div class="tasks">
        <div class="stat-row">
            <div class="stat-box" @click="changeList('all')" :class="{ active: currentTaskList === 'all' }">
                <div class="num" style="color:#3B82F6">{{ allTasks?.statistics?.total ?? 0 }}</div>
                <div class="lbl">全部任务</div>
            </div>
            <div class="stat-box" @click="changeList('assigned')" :class="{ active: currentTaskList === 'assigned' }">
                <div class="num" style="color:#F59E0B">{{ allTasks?.statistics?.assigned ?? 0 }}</div>
                <div class="lbl">待接受</div>
            </div>
            <div class="stat-box" @click="changeList('accepted')" :class="{ active: currentTaskList === 'accepted' }">
                <div class="num" style="color:#10B981">{{ allTasks?.statistics?.accepted ?? 0 }}</div>
                <div class="lbl">已接受</div>
            </div>
            <div class="stat-box" @click="changeList('noConfirm')" :class="{ active: currentTaskList === 'noConfirm' }">
                <div class="num" style="color:#10B981">{{ noconfrimNumber }}</div>
                <div class="lbl">待审核</div>
            </div>
        </div>

        <!-- 全部任务 -->
        <div class="task-card" v-for="item in currentTasks?.records" :key="item.taskId">
            <div class="card-left">
                <span class="code">{{ item.reportCode }}</span>
                <div class="card-info">
                    <span class="worker"><el-icon><UserFilled /></el-icon>{{ item.workerName }}</span>
                    <span class="time">{{ formatTime(item.deadline) }}</span>
                </div>
            </div>
            <div class="card-right">
                <template v-if="currentTaskList === 'assigned'">
                    <el-button type="success" size="small" @click="handleAccept(item.taskId)">接受</el-button>
                </template>
                <el-button v-if="currentTaskList === 'accepted'" type="primary" size="small" @click="$router.push({path:'/confirm',state:item})">确认</el-button>
                <el-tag v-if="currentTaskList==='noConfirm'" size="small">待审核</el-tag>
            </div>
        </div>

        <!-- 死数据：全部 -->
        <template v-if="currentTaskList === 'all'">
            <div class="task-card urgent">
                <div class="card-left">
                    <span class="code">AQI-20260601-001</span>
                    <div class="card-info">
                        <span class="worker"><el-icon><UserFilled /></el-icon>网格员张三</span>
                        <span class="time">2026-06-12 18:00</span>
                    </div>
                </div>
                <div class="card-right">
                    <el-tag size="small">待指派</el-tag>
                </div>
            </div>
        </template>

        <!-- 死数据：已指派 -->
        <template v-if="currentTaskList === 'assigned'">
            <div class="task-card urgent">
                <div class="card-left">
                    <span class="code">AQI-20260602-002</span>
                    <div class="card-info">
                        <span class="worker"><el-icon><UserFilled /></el-icon>网格员李四</span>
                        <span class="time">2026-06-12 20:00</span>
                    </div>
                </div>
                <div class="card-right">
                    <el-button type="success" size="small">接受</el-button>
                </div>
            </div>
        </template>

        <!-- 死数据：已接受 -->
        <template v-if="currentTaskList === 'accepted'">
            <div class="task-card normal">
                <div class="card-left">
                    <span class="code">AQI-20260603-003</span>
                    <div class="card-info">
                        <span class="worker"><el-icon><UserFilled /></el-icon>网格员王五</span>
                        <span class="time">2026-06-13 12:00</span>
                    </div>
                </div>
                <div class="card-right">
                    <el-button type="primary" size="small">确认</el-button>
                </div>
            </div>
        </template>

        <!-- 死数据：已完成 -->

        <div v-if="currentTasks?.records?.length === 0" class="empty">暂无任务</div>
    </div>
</template>

<style scoped>
.tasks {
    max-width: 600px;
    margin: 0 auto;
    padding: 16px;
}

.stat-row {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 10px;
    margin-bottom: 20px;
}

.stat-box {
    background: #fff;
    border-radius: 14px;
    padding: 14px 10px;
    text-align: center;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    border: 1px solid #F1F5F9;
    cursor: pointer;
}

.num {
    font-size: 24px;
    font-weight: 800;
}

.lbl {
    font-size: 11px;
    color: #94A3B8;
    margin-top: 4px;
}

.task-card {
    background: #fff;
    border-radius: 14px;
    padding: 16px;
    margin-bottom: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border: 1px solid #E2E8F0;
}

.task-card.urgent {
    border-left: 4px solid #EF4444;
}

.task-card.normal {
    border-left: 4px solid #3B82F6;
}

.card-left {
    flex: 1;
}

.code {
    font-size: 13px;
    font-weight: 700;
    color: #1E293B;
    display: block;
    margin-bottom: 6px;
}

.card-info {
    display: flex;
    gap: 16px;
    font-size: 12px;
    color: #64748B;
}

.card-right {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 8px;
}

.active {
    background-color: #EFF6FF;
    border-color: #3B82F6;

}


.empty {
  text-align: center;
  padding: 40px;
  color: #94A3B8;
}

@media (max-width: 768px) {
    .tasks {
        padding: 12px;
    }

    .stat-row {
        grid-template-columns: repeat(2, 1fr);
    }

    .card-info {
        flex-direction: column;
        gap: 2px;
    }
}
</style>