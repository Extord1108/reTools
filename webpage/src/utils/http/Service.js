import axios from 'axios'
// const ConfigBaseURL = 'http://43.138.77.8/dz/' //默认路径
export const ConfigBaseURL = 'test' //默认路径
export const mediaURL = 'http://localhost:8081/static/image/' //媒体文件路径

//使用create方法创建axios实例
export const Service = axios.create({
    timeout: 10000, // 请求超时时间
    baseURL: ConfigBaseURL,
    withCredentials: true, // 是否允许带cookie这些
})
// 添加请求拦截器
Service.interceptors.request.use(

)
// 添加响应拦截器
Service.interceptors.response.use(response => {
    const data = response.data
    const status = response.status
    console.log(response)
    console.log(status)
    if (data) {
        return Promise.resolve(data)
    }
    else {
        // message.info(data.msg);
        return Promise.reject(data)
    }
}, error => {
    if (error.response.status == 401) {
        window.localStorage.removeItem('username');
        window.localStorage.removeItem('avatar');
        window.location.reload();
        return Promise.reject(error)
    }
    return Promise.reject(error)
})
