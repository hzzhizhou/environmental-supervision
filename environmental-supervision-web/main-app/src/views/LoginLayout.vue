<script setup>
import { computed } from 'vue';
import LoginLeft from '../components/LoginLeft.vue'
import LoginRight from '../components/LoginRight.vue';
import Register from '../components/Register.vue';
import { useRoute } from 'vue-router';
const route = useRoute()

// 根据路由自动切换右侧组件
const isRegister=computed(()=> route.path.includes('register'))
</script>

<template>
    <div class="container">
        <div class="card">
         <LoginLeft />
         <LoginRight v-show="!isRegister"></LoginRight>
         <Register v-show="isRegister"></Register>

        </div>
    </div>
</template>

<style scoped>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

.container {
    background: linear-gradient(135deg, #D2E4E4 0%, #b8d4d4 50%, #a3c8c8 100%);
    width: 100vw;
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 2rem;
    font-family: 'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif;
}

.card {
    display: flex;
    width: 85vw;
    max-width: 1000px;
    height: 65vh;
    max-height: 650px;
    background-color: #ffffff;
    border-radius: 2rem;
    overflow: hidden;
    box-shadow: 0 25px 60px rgba(0, 0, 0, 0.12), 
                0 10px 30px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
}

.card:hover {
    box-shadow: 0 30px 70px rgba(0, 0, 0, 0.15), 
                0 15px 35px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
}


/* 响应式设计 */
@media (max-width: 768px) {
    .container {
        padding: 1rem;
    }
    
    .card {
        flex-direction: column;
        width: 95vw;
        height: auto;
        max-height: none;
    }
}

@media (max-width: 480px) {
    .card {
        border-radius: 1.5rem;
    }
}
</style>