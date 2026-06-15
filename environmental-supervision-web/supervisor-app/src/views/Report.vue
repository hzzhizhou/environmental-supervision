<script setup>
import { reactive, ref } from 'vue'
import { Plus, Picture } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useReportStore } from '../store/report'
import { storeToRefs } from 'pinia'
import { postReports,uploadPhoto } from '../api/reports'
import { useAlertsStore } from '../store/user'
const users=useAlertsStore()
const {userId}=storeToRefs(users)
const report = useReportStore()
const { provinces, cities } = storeToRefs(report)
report.refresh()
//暴露出绝对的路径
const showPhoto=(url)=>{
   return window.$img(url)
}
// 污染类型选项
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

// 污染程度选项
const severityLevels = [
     {value:'verygood',label:'优',color:'#10B981'},
     {value:'good',label:'良好',color:'#3B82F6'},
    { value: 'mild', label: '轻微', color: '#F59E0B' },
    { value: 'moderate', label: '中等', color: '#F97316' },
    {value:'serious',label:'重度',color:'#DC2626'},
    { value: 'severe', label: '严重', color: '#EF4444' } 
]

const initForm = () => ({
    pollutionType: '',      
    pollutionLevel: '',           
    pollutionDesc: '',        
    province: '',
    city: '',
    address: '',
    photos: []            
})

const form = reactive(initForm())
const uploading = ref(false)

// 校验表单
const validateForm = () => {
    if (!form.pollutionType) {
        ElMessage.warning('请选择污染类型')
        return false
    }
    if (!form.pollutionLevel) {
        ElMessage.warning('请选择污染程度')
        return false
    }
    if (!form.pollutionDesc) {
        ElMessage.warning('请描述污染情况')
        return false
    }
    if (!form.province) {
        ElMessage.warning('请选择省份')
        return false
    }
    if (!form.city) {
        ElMessage.warning('请选择城市')
        return false
    }
    return true
}

// 图片上传
const handleImageUpload = async(file) => {
  try{
    const formData=new FormData()
    formData.append('file',file.raw)
    const {data}=await uploadPhoto(formData)
    form.photos.push(data.data?.url) 
  }catch(err){
    ElMessage.warning('上传失败')
  }
}

const removeImage = (index) => {
    form.photos.splice(index, 1)
}

const submit = async () => {
    if (!validateForm()) return
    uploading.value = true
    
    // 构建提交数据
    const submitData = {
        userId:userId.value,
        pollutionType: form.pollutionType,
        pollutionLevel: form.pollutionLevel,
        pollutionDesc: form.pollutionDesc,
        province: form.province,
        city: form.city,
        address: form.address || '',
        photos: form.photos.join(',')
    }
    try {
        await postReports(submitData)
        ElMessage.success('举报提交成功！感谢您的参与')
        // 重置表单
        Object.assign(form, initForm())
    } catch (error) {
        console.error('提交失败', error)
        ElMessage.error('提交失败，请重试')
    } finally {
        uploading.value = false
    }
}

const handleCity = (provinceName) => {
    form.city = ''
    report.refreshCity(provinceName)
}
</script>

<template>
    <div class="report">
        <!-- 标题 -->
        <div class="header">
            <h2>环境监督举报</h2>
            <p>您的每一次举报，都在为环保贡献力量</p>
        </div>

        <!-- 污染类型 -->
        <div class="section-title">
            <span class="required">*</span> 污染类型
        </div>
        <div class="type-grid">
            <div 
                v-for="item in pollutionTypes" 
                :key="item.value"
                class="type-item"
                :class="{ active: form.pollutionType === item.value }"
                @click="form.pollutionType = item.value"
            >
                {{ item.label }}
            </div>
        </div>

        <!-- 污染程度 -->
        <div class="section-title">
            <span class="required">*</span> 污染程度
        </div>
        <div class="severity-group">
            <div 
                v-for="item in severityLevels" 
                :key="item.value"
                class="severity-item"
                :class="{ active: form.pollutionLevel === item.value }"
                :style="{ '--active-color': item.color }"
                @click="form.pollutionLevel = item.value"
            >
                {{ item.label }}
            </div>
        </div>

        <!-- 污染描述 -->
        <div class="section-title">
            <span class="required">*</span> 污染描述
        </div>
        <el-input 
            v-model="form.pollutionDesc" 
            type="textarea" 
            :rows="4" 
            placeholder="请详细描述您看到的污染情况，例如：哪个工厂在排放黑烟、垃圾堆的具体位置等"
            maxlength="500"
            show-word-limit
        />

        <!-- 位置信息 -->
        <div class="section-title">
            <span class="required">*</span> 位置信息
        </div>
        <div class="location-row">
            <el-select v-model="form.province" placeholder="省份" size="large" @change="handleCity">
                <el-option :label="item.provinceName" :value="item.provinceName" :key="item.provinceId" v-for="item in provinces"/>
            </el-select>
            <el-select v-model="form.city" placeholder="城市" size="large">
                <el-option :label="item.cityName" :value="item.cityName" v-if="cities" :key="item.cityId" v-for="item in cities"/>
            </el-select>
        </div>
        <el-input v-model="form.address" size="large" placeholder="详细地址（选填，如街道、小区、地标等）" class="address-input" />

        <!-- 现场照片 -->
        <div class="section-title">
            <span class="required">*</span> 现场照片
        </div>
        <div class="upload-area">
            <div class="image-list" v-if="form.photos.length > 0">
                <div v-for="(img, index) in form.photos" :key="index" class="image-item">
                    <img :src="showPhoto(img)" alt="现场照片" />
                    <div class="image-remove" @click="removeImage(index)">×</div>
                </div>
            </div>
            <el-upload
                :auto-upload="false"
                :show-file-list="false"
                :on-change="handleImageUpload"
                accept="image/jpeg,image/png,image/jpg"
                multiple
            >
                <div class="upload-box">
                    <el-icon :size="36" color="#94A3B8"><Plus /></el-icon>
                    <span>点击拍照或选择图片</span>
                    <small>最多上传9张，支持jpg/png</small>
                </div>
            </el-upload>
        </div>

        <!-- 提示信息 -->
        <div class="tips">
            <el-icon><Picture /></el-icon>
            <span>拍照时请尽量清晰展示污染源和周边环境</span>
        </div>

        <el-button 
            type="success" 
            size="large" 
            class="submit-btn" 
            round 
            @click="submit"
            :loading="uploading"
        >
            提交举报
        </el-button>
    </div>
</template>

<style scoped>
.report {
    max-width: 600px;
    margin: 0 auto;
    padding: 16px;
}

.header {
    text-align: center;
    margin-bottom: 24px;
}

.header h2 {
    font-size: 24px;
    color: #1E293B;
    margin-bottom: 8px;
}

.header p {
    font-size: 13px;
    color: #64748B;
}

.section-title {
    font-size: 15px;
    font-weight: 700;
    color: #1E293B;
    margin: 20px 0 12px;
}

.required {
    color: #EF4444;
    margin-right: 4px;
}

.type-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 12px;
}

.type-item {
    background: #F8FAFC;
    border: 1px solid #E2E8F0;
    border-radius: 12px;
    padding: 10px 8px;
    text-align: center;
    font-size: 13px;
    color: #475569;
    cursor: pointer;
    transition: all 0.2s;
}

.type-item.active {
    background: #10B981;
    border-color: #10B981;
    color: white;
}

.severity-group {
    display: flex;
    gap: 16px;
}

.severity-item {
    flex: 1;
    background: #F8FAFC;
    border: 1px solid #E2E8F0;
    border-radius: 12px;
    padding: 12px;
    text-align: center;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s;
}

.severity-item.active {
    background: var(--active-color);
    border-color: var(--active-color);
    color: white;
}

.location-row {
    display: flex;
    gap: 10px;
}

.location-row .el-select {
    flex: 1;
}

.address-input {
    margin-top: 10px;
}

.upload-area {
    background: #F8FAFC;
    border-radius: 12px;
    padding: 16px;
}

.image-list {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    margin-bottom: 16px;
}

.image-item {
    position: relative;
    width: 100px;
    height: 100px;
    border-radius: 8px;
    overflow: hidden;
}

.image-item img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.image-remove {
    position: absolute;
    top: 4px;
    right: 4px;
    width: 20px;
    height: 20px;
    background: rgba(0,0,0,0.6);
    color: white;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    cursor: pointer;
}

.upload-box {
    border: 2px dashed #CBD5E1;
    border-radius: 12px;
    padding: 24px;
    text-align: center;
    color: #94A3B8;
    cursor: pointer;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    transition: all 0.2s;
}

.upload-box:hover {
    border-color: #10B981;
    color: #10B981;
}

.upload-box span {
    font-size: 13px;
}

.upload-box small {
    font-size: 11px;
}

.tips {
    display: flex;
    align-items: center;
    gap: 8px;
    background: #EFF6FF;
    padding: 12px;
    border-radius: 8px;
    margin-top: 16px;
    font-size: 12px;
    color: #3B82F6;
}

.submit-btn {
    width: 100%;
    margin-top: 24px;
    height: 48px;
    font-size: 16px;
    font-weight: 700;
}

@media (max-width: 640px) {
    .type-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}
</style>