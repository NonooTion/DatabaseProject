import { createApp } from 'vue'
import App from './App.vue'

import router from './router/router'
import ElementPlus from 'element-plus' //导入 ElementPlus 组件库的所有模块和功能 
import 'element-plus/dist/index.css' //导入 ElementPlus 组件库所需的全局 CSS 样式
import axios from 'axios'
import VueAxios from 'vue-axios'
import path from './data/path'
import code from './data/code'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import moment from 'moment'
const app=createApp(App)
app.use(router)
app.use(ElementPlus)
app.config.globalProperties.$axios = axios  
app.config.globalProperties.$path = path 
app.config.globalProperties.$code = code
app.config.globalProperties.$moment = moment
app.mount('#app')
