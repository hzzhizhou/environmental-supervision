import { createApp } from 'vue'
import { renderWithQiankun, qiankunWindow } from 'vite-plugin-qiankun/dist/helper'
import './style.css'
import App from './App.vue'
import router from './router/index.js'
import { createPinia } from 'pinia'
let app = null
const pinia=createPinia()
function render(props = {}) {
    const { container } = props
    app = createApp(App)
    app.use(pinia)
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