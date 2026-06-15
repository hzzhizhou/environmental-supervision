<script setup>
import { ref, reactive, computed,watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { inject } from 'vue'
import { login } from '../api/auth'
import { ElMessage } from 'element-plus'
const loginSuccess = inject('loginSuccess')

const router = useRouter()
const route = useRoute()
const form = reactive({
    username: '',
    password: '',
    roleId: 3,

})
const maplist = ['公众监督员', '系统管理员', '网格员','决策者']
const rolelist = {
    '公众监督员': '3',
    '网格员':'2',
    '系统管理员': '1',
    '决策者':'4'
}
//切换角色
const urlRole = computed(() => route.query.role || '3')
const Switch = (data) => {
    const role = rolelist[data] || '3'
    router.push({ path: '/login', query: { role: role } })
    form.roleId = +role
}
watch(urlRole,(val)=>{
    for(const [key,value] of Object.entries(rolelist)){
        if(value===val){
            form.roleId=+value
            break
        }
    }
},{immediate:true})
//登录
const Login = () => {
    formRef.value.validate(async(valid) => {
        try{
              if (valid) {
            const role={
                1: '/admin/data-list',
                3: '/supervisor/report',
                2: '/grid/tasks',
                4: '/decider'
            }
            const targetPath = role[form.roleId]
            if(!targetPath){
                ElMessage.warning('角色路径未配置')
                return 
            }
            const message={
                username:form.username,
                password:form.password,
                roleId:form.roleId
            }
            const {data}=await login(message)
            
            localStorage.setItem('token',data.token)
            localStorage.setItem('userId',data.userId)
            ElMessage.success('登录成功')
            
           
            loginSuccess()  // 显示子应用容器
            // 跳转子应用
             router.push(targetPath)
        }
        }catch(error){
           alert('账号密码错误')
        }
      
    })
}
//注册跳转
const Register = () => {
    router.push("/register")
}
const formRef = ref()
</script>
<template>
    <div class="Login">
        <div class="login-content">
            <div class="login-header">
                <div class="icon-wrapper">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                        <circle cx="12" cy="7" r="4" />
                    </svg>
                </div>
                <div class='Switch'>
                    <div :class="{ active: urlRole === rolelist[item] }" v-for="item in maplist" @click="Switch(item)">
                        {{ item }}
                    </div>
                </div>
                <p class="subtitle">请登录您的账户</p>
            </div>
            <el-form style="width:100%;max-width: 600px" label-width="70px" class="demo-dynamic" :model="form"
                ref="formRef">
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

                <div class="forgot-password">
                    <a href="#">忘记密码？</a>
                </div>

                <el-form-item class="btnItem">
                    <el-button type="primary" class="btn login-btn" @click="Login">登录</el-button>
                    <el-button type="primary" class="btn register-btn" @click="Register"
                        v-if="urlRole === '3'">注册</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>
<style scoped>
.Login {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #ffffff;
    padding: 2rem;
}

.login-content {
    width: 100%;
    max-width: 380px;
}

.login-header {
    text-align: center;
    margin-bottom: 2.5rem;
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

.Switch {
    display: flex;
    width: 100%;
    gap: 20px;
    justify-content: center;
    margin-bottom: 30px;
}

.Switch div {
    width: 110px;
    height: 50px;
    background-color: #6baeb6;
    line-height: 50px;
    border-radius: 10px;
    text-align: center;
    color: white;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;
    user-select: none;
}

/* 悬停效果 */
.Switch div:hover {
    background-color: #5a9da5;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(107, 174, 182, 0.4);
}

/* 激活状态 */
.Switch div.active {
    background-color: #4a8d95;
    box-shadow: 0 4px 15px rgba(74, 141, 149, 0.5);
    transform: translateY(-2px);
    font-weight: 600;
}

/* 点击效果 */
.Switch div:active {
    transform: translateY(0);
    box-shadow: 0 2px 8px rgba(107, 174, 182, 0.3);
}

/* 添加底部指示器 */
.Switch div.active::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 60%;
    height: 3px;
    background-color: white;
    border-radius: 2px 2px 0 0;
}

.login-header h1 {
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
    margin-bottom: 1rem;
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
.login-btn {
    background: linear-gradient(135deg, #8AC1C8 0%, #6baeb6 100%) !important;
    border: none !important;
    color: white !important;
    box-shadow: 0 8px 20px rgba(138, 193, 200, 0.3);
}

.login-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 25px rgba(138, 193, 200, 0.4);
    background: linear-gradient(135deg, #7ab5bc 0%, #5d9ea8 100%) !important;
}

.login-btn:active {
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