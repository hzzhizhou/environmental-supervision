import { createApp } from 'vue'
import { renderWithQiankun, qiankunWindow } from 'vite-plugin-qiankun/dist/helper'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
let app = null
const pinia=createPinia()
function render(props = {}) {
    const { container } = props
    app = createApp(App)

    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
        app.component(key, component)
    }
    app.use(pinia)
    app.use(ElementPlus)
    app.use(router)
    app.mount(container ? container.querySelector('#app') : '#app')
}

renderWithQiankun({
    mount(props) { render(props) },
    bootstrap() {},
    unmount() { app.unmount(); app = null },
    update() {}
})

if (!qiankunWindow.__POWERED_BY_QIANKUN__) {
    render()
}