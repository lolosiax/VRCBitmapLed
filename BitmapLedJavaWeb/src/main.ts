import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import ElementPlus from 'element-plus'
import App from './App.vue'
import router from './router'
import "./utils/app-preset"

//import theme
import './theme/index.scss'

//import unocss
import 'uno.css'

//i18n
import { setupI18n } from '@/lang'

import '@/styles/index.scss' // global css
import 'bootstrap-icons/font/bootstrap-icons.scss'
import 'vue-cropper/dist/index.css'

//svg-icon
import 'virtual:svg-icons-register'
import svgIcon from '@/icons/SvgIcon.vue'
import directive from '@/directives'

//import router intercept
import './permission'

//import element-plus
import 'element-plus/dist/index.css'
const app = createApp(App)

//router
app.use(router)

//pinia
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)

//i18n
app.use(setupI18n)
app.component('SvgIcon', svgIcon)
directive(app)

//element-plus
app.use(ElementPlus)

//import vxe-table
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'
import { useKeyboard } from '@/hooks/use-keyboard'
app.use(VXETable)

useKeyboard()

app.mount('#app')
