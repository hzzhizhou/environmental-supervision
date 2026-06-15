<script setup>
import { ref, reactive } from 'vue'
import { useRoute,useRouter } from 'vue-router'
import { register } from '../api/auth'
import { ElMessage } from 'element-plus'
const form = reactive({
    username: '',
    password: '',
    repassword: '',
    phone: '',
    code: '',
    realName: '公共监督员'
})
const initForm=()=>{return{
    username: '',
    password: '',
    repassword: '',
    phone: '',
    code: '',
    realName: '公共监督员'
}}
const route=useRoute()
const countdown = ref(0)

const router = useRouter()

//返回
const Return = () => {
    router.go(-1)
}
//注册
const Register = async () => {
    const formData={...form}
    delete formData.repassword
    try {
        const res=await register(formData)
        Object.assign(form,initForm())
        
        ElMessage.success(res.message)
        router.replace('/login')
    } catch (err) {
        ElMessage.warning('注册失败')
    }
}
const formRef = ref()
</script>

<template>

    <div class="Register">
        <div class="register-content">
            <div class="register-header">
                <div class="icon-wrapper">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                        <circle cx="12" cy="7" r="4" />
                    </svg>
                </div>
                <h1>公众监督员注册</h1>
                <p class="subtitle">请注册您的账户</p>
            </div>
            <el-form style="width:100%;max-width: 600px" label-width="auto" class="demo-dynamic" :model="form"
                :ref="formRef">
                <el-form-item prop="username" label="账号" :rules="[
                    {
                        required: true,
                        message: '请输入账号',
                        trigger: 'blur',
                    },
                ]">
                    <el-input v-model="form.username" class="elInput" placeholder="请输入用户名或邮箱" prefix-icon="User" />
                </el-form-item>

                <el-form-item prop="password" label="密码" :rules="[
                    {
                        required: true,
                        message: '请输入密码',
                        trigger: ['blur', 'change'],
                    },
                ]">
                    <el-input v-model="form.password" class="elInput" type="password" placeholder="请输入密码"
                        prefix-icon="Lock" show-password />
                </el-form-item>
                <el-form-item prop="repassword" label="确认密码" :rules="[
                    {
                        required: true,
                        message: '请输入密码',
                        trigger: ['blur', 'change'],
                    },
                ]">
                    <el-input v-model="form.repassword" class="elInput" type="password" placeholder="请输入密码"
                        prefix-icon="Lock" show-password />
                </el-form-item>
                <el-form-item prop="phone" label="电话号码" :rules="[
                    {
                        required: true,
                        message: '请输入电话号码',
                        trigger: ['blur', 'change'],
                    },
                ]">
                    <el-input v-model="form.phone" class="elInput" placeholder="请输入电话号码" prefix-icon="Lock" />
                </el-form-item>
                <el-form-item prop="code" label="验证码" :rules="[
                    {
                        required: true,
                        message: '请输入验证码',
                        trigger: ['blur', 'change'],
                    },
                ]">
                    <div class="code-wrapper">
                        <el-input v-model="form.code" class="code-input" placeholder="请输入验证码" prefix-icon="Message"
                            maxlength="6" />
                        <span class="code-line"></span>
                        <button class="code-send-btn" :class="{ sending: countdown > 0 }"
                            :disabled="countdown > 0 || !form.phone" @click="sendCode" type="button">
                            {{ countdown > 0 ? countdown + 's后重发' : '获取验证码' }}
                        </button>
                    </div>
                </el-form-item>

                <el-form-item class="btnItem">
                    <el-button type="primary" class="btn back" @click="Return">返回</el-button>
                    <el-button type="primary" class="btn register-btn" @click="Register">注册</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>
<style scoped>
.Register {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #ffffff;
    padding: 2rem;
}

.register-content {
    width: 100%;
    max-width: 380px;
}

.register-header {
    text-align: center;
    margin-bottom: 1.5rem;
}

.icon-wrapper {
    width: 60px;
    height: 60px;
    margin: 0 auto 1.5rem;
    background: linear-gradient(135deg, #8AC1C8 0%, #6baeb6 100%);
    border-radius: 1.2rem;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    box-shadow: 0 10px 25px rgba(138, 193, 200, 0.3);
}

.icon-wrapper svg {
    width: 30px;
    height: 30px;
}

.register-header h1 {
    font-size: 1.8rem;
    font-weight: 700;
    color: #2c3e50;
    margin-bottom: 0.5rem;
    letter-spacing: -0.5px;
}

.subtitle {
    color: #7f8c8d;
    font-size: 0.9rem;
    margin-top: 0.5rem;
}

/* 表单样式优化 */
.demo-dynamic {
    width: 100%;
}

/* 输入框样式 */
.elInput :deep(.el-input__wrapper) {
    border-radius: 12px;
    border: 2px solid #e9ecef;
    background: #f8f9fa;
    transition: all 0.3s ease;
    box-shadow: none;
    padding: 0.5rem 1rem;
    margin-bottom: 0.5rem;
}

.elInput :deep(.el-input__wrapper:hover) {
    border-color: #8AC1C8;
    background: #ffffff;
}

.elInput :deep(.el-input.is-focus .el-input__wrapper) {
    border-color: #8AC1C8;
    box-shadow: 0 0 0 4px rgba(138, 193, 200, 0.1);
    background: #ffffff;
}

.elInput :deep(.el-input__inner) {
    color: #2c3e50;
    font-size: 0.95rem;
}

.elInput :deep(.el-input__inner::placeholder) {
    color: #adb5bd;
}

/* 忘记密码链接 */
.forgot-password {
    text-align: right;
    margin-top: 1rem;
    margin-bottom: 1.5rem;
}

.forgot-password a {
    color: #8AC1C8;
    text-decoration: none;
    font-size: 0.85rem;
    font-weight: 500;
    transition: color 0.3s ease;
}

.forgot-password a:hover {
    color: #6baeb6;
    text-decoration: underline;
}

/*切换角色 */
.switch a {
    margin-right: 20px;
    color: #8AC1C8;
    text-decoration: none;
    font-size: 1em;
    font-weight: 500;
    transition: color 0.3s ease;
}

.switch a:hover {
    color: #6baeb6;
    text-decoration: underline;
    cursor: pointer;
}

/* 按钮组 */
.btnItem {
    margin-top: 1rem;
}

.btnItem :deep(.el-form-item__content) {
    display: flex;
    gap: 1rem;
    justify-content: center;
}

.btn {
    flex: 1;
    border-radius: 12px !important;
    padding: 0.8rem 1.5rem !important;
    font-size: 1rem !important;
    font-weight: 600 !important;
    letter-spacing: 0.5px;
    transition: all 0.3s ease !important;
    border: 2px solid transparent !important;
}

/* 登录按钮 */
.back {
    background: linear-gradient(135deg, #8AC1C8 0%, #6baeb6 100%) !important;
    border: none !important;
    color: white !important;
    box-shadow: 0 8px 20px rgba(138, 193, 200, 0.3);
}

.back-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 25px rgba(138, 193, 200, 0.4);
    background: linear-gradient(135deg, #7ab5bc 0%, #5d9ea8 100%) !important;
}

.back-btn:active {
    transform: translateY(0);
}

/* 注册按钮 */
.register-btn {
    background: transparent !important;
    border: 2px solid #8AC1C8 !important;
    color: #8AC1C8 !important;
    box-shadow: none;
}

.register-btn:hover {
    background: #8AC1C8 !important;
    color: white !important;
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(138, 193, 200, 0.3);
}

.register-btn:active {
    transform: translateY(0);
}

/* ========== 验证码输入框组 ========== */
.code-wrapper {
    display: flex;
    align-items: center;
    width: 100%;
    border: 2px solid #e9ecef;
    border-radius: 12px;
    overflow: hidden;
    background: #f8f9fa;
    transition: all 0.3s ease;
}

.code-wrapper:focus-within {
    border-color: #8AC1C8;
    box-shadow: 0 0 0 4px rgba(138, 193, 200, 0.1);
    background: #ffffff;
}

/* 输入框部分——去掉Element Plus默认样式 */
.code-input {
    flex: 1;
}

.code-input :deep(.el-input__wrapper) {
    border: none !important;
    box-shadow: none !important;
    border-radius: 0;
    padding: 0 12px;
    background: transparent;
}

.code-input :deep(.el-input__inner) {
    color: #2c3e50;
    font-size: 0.95rem;
}

.code-input :deep(.el-input__inner::placeholder) {
    color: #adb5bd;
}

/* 中间分隔线 */
.code-line {
    width: 1px;
    height: 24px;
    background: #dee2e6;
    flex-shrink: 0;
}

/* 发送按钮 */
.code-send-btn {
    padding: 10px 16px;
    border: none;
    background: transparent;
    color: #8AC1C8;
    font-size: 13px;
    font-weight: 600;
    cursor: pointer;
    white-space: nowrap;
    flex-shrink: 0;
    transition: all 0.3s ease;
    letter-spacing: 0.5px;
}

.code-send-btn:hover:not(:disabled) {
    background: rgba(138, 193, 200, 0.08);
    color: #6baeb6;
}

.code-send-btn:active:not(:disabled) {
    background: rgba(138, 193, 200, 0.15);
}

/* 禁用/倒计时状态 */
.code-send-btn:disabled {
    color: #cbd5e0;
    cursor: not-allowed;
}

.code-send-btn.sending {
    color: #94a3b8;
    font-variant-numeric: tabular-nums;
}

@media(max-width:768px) {
    .Login {
        padding: 2rem 1.5rem;
    }

    .login-content {
        max-width: 100%;
    }
}

@media(max-width:480px) {
    .login-header h1 {
        font-size: 1.5rem;
    }

    .btnItem :deep(.el-form-item__content) {
        flex-direction: column;
        gap: 0.8rem;
    }

    .pain {
        height: 160px;
    }

}
</style>