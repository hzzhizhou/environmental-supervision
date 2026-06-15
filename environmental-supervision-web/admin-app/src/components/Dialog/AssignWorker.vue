<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { User, MapPin, AlertCircle } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getLocalGridWorkers, getCrossCityGridWorkers, checkLocalGridWorkers, assignTask } from '../../api/manageUser'

const props = defineProps({
  visible: Boolean,
  reportId: Number,
  reportCity: String,
  adminId: Number
})

const emit = defineEmits(['close', 'success'])

const localWorkers = ref([])
const crossCityWorkers = ref([])
const hasLocalWorkers = ref(true)
const selectedWorkerId = ref(null)
const isCrossCityMode = ref(false)
const loading = ref(false)

const currentWorkers = computed(() => {
  return isCrossCityMode.value ? crossCityWorkers.value : localWorkers.value
})

const titleText = computed(() => {
  return isCrossCityMode.value ? '跨省调派 - 选择外省网格员' : `本市指派 - ${props.reportCity}市网格员`
})

const canAssign = computed(() => {
  return selectedWorkerId.value !== null && !loading.value
})

const loadWorkers = async () => {
  if (!props.reportId) return
  
  loading.value = true
  try {
    const hasLocalRes = await checkLocalGridWorkers(props.reportId)
    hasLocalWorkers.value = hasLocalRes.data.data
    
    if (hasLocalWorkers.value) {
      const localRes = await getLocalGridWorkers(props.reportId)
      localWorkers.value = localRes.data.data
      isCrossCityMode.value = false
    } else {
      const crossRes = await getCrossCityGridWorkers(props.reportId)
      crossCityWorkers.value = crossRes.data.data
      isCrossCityMode.value = true
    }
  } catch (error) {
    ElMessage.error('获取网格员列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSwitchMode = async () => {
  if (!hasLocalWorkers.value) {
    ElMessage.warning('本市无可用网格员，已自动切换到跨省调派')
    return
  }
  
  isCrossCityMode.value = !isCrossCityMode.value
  
  if (isCrossCityMode.value && crossCityWorkers.value.length === 0) {
    loading.value = true
    try {
      const res = await getCrossCityGridWorkers(props.reportId)
      crossCityWorkers.value = res.data.data
    } catch (error) {
      ElMessage.error('获取外省网格员失败')
      isCrossCityMode.value = false
    } finally {
      loading.value = false
    }
  }
}

const handleAssign = async () => {
  if (!selectedWorkerId.value || loading.value) return
  
  loading.value = true
  try {
    await assignTask({
      reportId: props.reportId,
      adminId: props.adminId,
      workerId: selectedWorkerId.value
    })
    
    ElMessage.success('指派成功')
    emit('success')
    handleClose()
  } catch (error) {
    const message = error.response?.data?.message || '指派失败'
    ElMessage.error(message)
  } finally {
    loading.value = false
  }
}

const handleClose = () => {
  emit('close')
  selectedWorkerId.value = null
  isCrossCityMode.value = false
}

watch(() => props.visible, (newVal) => {
  if (newVal) {
    loadWorkers()
  }
})

onMounted(() => {
  if (props.visible) {
    loadWorkers()
  }
})
</script>

<template>
  <el-dialog
    :title="titleText"
    :visible="visible"
    :width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-if="loading" class="loading">
      <el-spinner size="medium" />
      <span>加载中...</span>
    </div>
    
    <div v-else>
      <div v-if="hasLocalWorkers" class="mode-switch">
        <el-button
          type="primary"
          :plain="!isCrossCityMode"
          @click="isCrossCityMode = false"
        >
          <el-icon><MapPin /></el-icon> 本市指派
        </el-button>
        <el-button
          type="warning"
          :plain="isCrossCityMode"
          :disabled="!hasLocalWorkers"
          @click="handleSwitchMode"
        >
          <el-icon><User /></el-icon> 跨省调派
        </el-button>
        <div v-if="!isCrossCityMode" class="tip">
          <el-icon class="warning-icon"><AlertCircle /></el-icon>
          <span>本市有可用网格员，请优先指派本市人员</span>
        </div>
        <div v-else class="tip cross-city">
          <el-icon class="warning-icon"><AlertCircle /></el-icon>
          <span>已切换到跨省调派模式</span>
        </div>
      </div>
      
      <div v-else class="no-local-warning">
        <el-alert
          type="warning"
          title="提示"
          description="本市暂无可用网格员，将自动启用跨省调派"
          show-icon
        />
      </div>
      
      <div class="worker-list">
        <el-table
          :data="currentWorkers"
          :empty-text="isCrossCityMode ? '暂无外省可用网格员' : '暂无本市可用网格员'"
          style="width: 100%"
          @row-click="(row) => selectedWorkerId = row.userId"
        >
          <el-table-column type="selection" :selectable="() => true" />
          <el-table-column prop="realName" label="网格员姓名" />
          <el-table-column prop="phone" label="联系电话" />
          <el-table-column prop="gridName" label="所属网格" />
          <el-table-column prop="statusDesc" label="状态">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
                {{ row.statusDesc }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button
        type="primary"
        :loading="loading"
        :disabled="!canAssign"
        @click="handleAssign"
      >
        {{ isCrossCityMode ? '确认跨省调派' : '确认指派' }}
      </el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  gap: 12px;
}

.mode-switch {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.tip {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #f59e0b;
  margin-left: auto;
}

.tip.cross-city {
  color: #ef4444;
}

.warning-icon {
  font-size: 14px;
}

.no-local-warning {
  margin-bottom: 16px;
}

.worker-list {
  max-height: 300px;
  overflow-y: auto;
}
</style>