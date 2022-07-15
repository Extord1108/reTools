import axios from 'axios'
// const ConfigBaseURL = 'http://43.138.77.8/dz/' //默认路径
const ConfigBaseURL = '/test' //默认路径

//使用create方法创建axios实例
export const Service = axios.create({
    timeout: 10000, // 请求超时时间
    baseURL: ConfigBaseURL,
})
// 添加请求拦截器
Service.interceptors.request.use(
    function (error) {
        // 对请求错误做些什么
        return Promise.reject(error)
    }
)
// 添加响应拦截器
Service.interceptors.response.use(response => {
    const data = response.data
    console.log(data)
    if (data) {
        return Promise.resolve(data)
    } else {
        // message.info(data.msg);
        return Promise.reject(data)
    }
}, error => {
    return Promise.reject(error)
})
