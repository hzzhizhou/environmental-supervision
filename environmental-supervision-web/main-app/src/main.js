import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import { registerMicroApps, start } from 'qiankun'

let app = null
window.__LOGOUT__ = () => {
    if(confirm('确认要退出吗')){
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    router.push('/login')
    }
  
}
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

// 挂载到 window
window.$img = (path) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return API_BASE_URL + path
}
function render(props = {}) {
    const { container } = props
    app = createApp(App)

    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
        app.component(key, component)
    }

    app.use(ElementPlus)
    app.use(router)
    app.mount(container ? container.querySelector('#app') : '#app')
}

render()

router.isReady().then(() => {
    console.log('当前路由:', router.currentRoute.value.path)
    console.log('#subapp存在:', !!document.querySelector('#subapp'))

    if (document.querySelector('#subapp')) {
        registerMicroApps([
            { name: 'admin', entry: '//localhost:5174', container: '#subapp', activeRule: '/admin' },
            {name:'supervisor',entry:'//localhost:5175',container:'#subapp',activeRule:'/supervisor'},
            {name:'grid',entry:'//localhost:5176',container:'#subapp',activeRule:'/grid'},
        ])
        start()
        console.log('qiankun已启动')
    } else {
        console.log('#subapp不存在，跳过qiankun启动')
    }
})