<script setup>
import { ref, provide,onMounted,onUnmounted } from 'vue'

const isLoggedIn = ref(!!localStorage.getItem('token'))

// 暴露给登录组件调用
provide('loginSuccess', () => {
  isLoggedIn.value = true
})
const handlePopstate = () => {
    if (window.location.pathname === '/login') {
        isLoggedIn.value = false
    }
}

onMounted(() => {
    window.addEventListener('popstate', handlePopstate)
})

onUnmounted(() => {
    window.removeEventListener('popstate', handlePopstate)
})
</script>

<template>
   <router-view v-if="$route.path === '/login' || $route.path === '/register' || $route.path === '/decider'" />
<div v-show="isLoggedIn && $route.path !== '/decider'" id="subapp" style="width: 100%; height: 100%;"></div>
</template>