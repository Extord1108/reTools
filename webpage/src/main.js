import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import naive from 'naive-ui'
import Axios from "axios";

const app = createApp(App);
//注入axios
app.config.globalProperties.$axios = Axios;

app.use(store).use(naive).use(router).mount('#app')
