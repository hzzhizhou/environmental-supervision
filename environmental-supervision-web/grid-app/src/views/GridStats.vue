<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '../store/user'
import { storeToRefs } from 'pinia'
const user=useUserStore()
const { rank }=storeToRefs(user)
const rankEmoji = computed(() => {
    if (rank.value?.myRank === 1) return '🥇'
    if (rank.value?.myRank === 2) return '🥈'
    if (rank.value?.myRank === 3) return '🥉'
    return '🏅'
})
</script>
<template>
    <div class="stats">
        <div class="stat-grid">
            <div class="stat-card">
                <div class="num" style="color:#10B981">{{ rank?.myCompletedTasks??0 }}</div>
                <div class="lbl">累计确认数</div>
            </div>
            <div class="stat-card">
                <div class="num" style="color:#8B5CF6">{{ rank?.myRank??'--' }}</div>
                <div class="lbl">排名</div>
            </div>
        </div>

        <div class="rank-box">
            <h6>网格员排名</h6>
            <div class="rank-icon">{{ rankEmoji }}</div>
            <el-tag type="warning" size="large" round>第 {{ rank?.myRank ?? '--' }} 名 / {{ rank?.records.length ?? '--' }}人</el-tag>
            <div class="rank-info">已确认 {{ rank?.myCompletedTasks ?? 0 }} 条 </div>
        </div>
    </div>
</template>


<style scoped>
.stats {
    max-width: 600px;
    margin: 0 auto;
    padding: 16px;
}

.stat-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
    margin-bottom: 20px;
}

.stat-card {
    background: #fff;
    border-radius: 16px;
    padding: 20px 16px;
    text-align: center;
    box-shadow: 0 1px 3px rgba(0,0,0,0.04);
    border: 1px solid #F1F5F9;
    transition: transform 0.2s;
}

.stat-card:hover {
    transform: translateY(-2px);
}

.stat-card .icon {
    font-size: 24px;
    margin-bottom: 8px;
}

.num {
    font-size: 30px;
    font-weight: 800;
    letter-spacing: -1px;
}

.lbl {
    font-size: 12px;
    color: #94A3B8;
    margin-top: 4px;
    font-weight: 500;
}

.rank-box {
    background: linear-gradient(135deg, #EFF6FF 0%, #F0F9FF 100%);
    border-radius: 20px;
    padding: 28px 20px;
    text-align: center;
    border: 1px solid #DBEAFE;
}

.rank-box h6 {
    font-size: 14px;
    font-weight: 700;
    color: #1E40AF;
    margin: 0 0 12px;
}

.rank-icon {
    font-size: 56px;
    margin: 8px 0 12px;
    animation: bounce 2s infinite;
}

@keyframes bounce {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-8px); }
}

.rank-box .el-tag {
    font-size: 16px;
    padding: 8px 20px;
}

.rank-info {
    font-size: 13px;
    color: #64748B;
    margin-top: 14px;
    font-weight: 500;
}

@media (max-width: 768px) {
    .stats { padding: 12px; }
}
</style>