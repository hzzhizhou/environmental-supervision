<script setup>
import Sidebar from '../components/Leftnav.vue'
import Topbar from '../components/Topbar.vue'
</script>
<template>
    <div class="layout">
        <Sidebar></Sidebar>
        <div class="main">
            <Topbar />
            <div class="content">
               <RouterView></RouterView>
            </div>
        </div>
    </div>
</template>

<style scoped>
.layout {
    display: flex;
    min-height: 100vh;
}

.main {
    flex: 1;
    margin-left: 240px;
    background: #F1F5F9;
    overflow-y: auto;
}

.content {
    padding: 24px;
}
</style>