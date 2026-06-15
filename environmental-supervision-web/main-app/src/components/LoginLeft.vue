<script setup>
import Pain from '../assets/loginPain.png'
</script>

<template>
    <div class="pain">
        <img :src="Pain" alt="">
        <div class="overlay"></div>
        <div class="pain-text">
            <h2>Welcome Back</h2>
            <p>登录您的账户，开启精彩旅程</p>
        </div>
    </div>
</template>
<style scoped>
.pain {
    flex: 1;
    position: relative;
    overflow: hidden;
    background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%);
}

.pain img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.6s ease;
}

.pain:hover img {
    transform: scale(1.05);
}

.overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(to bottom,
            rgba(44, 62, 80, 0.3) 0%,
            rgba(52, 152, 219, 0.5) 100%);
}

.pain-text {
    position: absolute;
    bottom: 3rem;
    left: 2.5rem;
    right: 2.5rem;
    color: white;
    z-index: 2;
}

.pain-text h2 {
    font-size: 2rem;
    font-weight: 700;
    margin-bottom: 0.8rem;
    text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
    letter-spacing: 0.5px;
}

.pain-text p {
    font-size: 1rem;
    opacity: 0.95;
    text-shadow: 0 1px 5px rgba(0, 0, 0, 0.3);
    line-height: 1.5;
}

@media(max-width:768px) {
    .pain {
        height: 200px;
        flex: none;
    }

    .pain-text {
        bottom: 1.5rem;
        left: 1.5rem;
        right: 1.5rem;
    }

    .pain-text h2 {
        font-size: 1.5rem;
    }
}

@media(max-width:480px) {}
</style>
