<template>
    <div style="">
        <div id="time-now">{{ timeNow }}</div>
        <div id="date-now">{{ dateNow }}</div>
    </div>
</template>
<script setup>
import { onMounted, ref, onBeforeUnmount } from "vue";
const timeNow = ref('')
const dateNow = ref('')
const timer = ref(null)
//获取当前时间，格式为"00:00"
const time = () => {
    const date = new Date()
    const hour = date.getHours()
    const minute = date.getMinutes()
    const time = `${hour < 10 ? '0' + hour : hour}:${minute < 10 ? '0' + minute : minute}`
    timeNow.value = time
}

//获取当前日期，自动补0
const date = () => {
    const date = new Date();
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const week = date.getDay();
    const weekArr = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
    dateNow.value = `${year}年${month}月${day}日 ${weekArr[week]}`;
}

onMounted(() => {
    timer.value = setInterval(() => {
        time();
    }, 1000);
    date()
})

onBeforeUnmount(() => {
    clearInterval(timer.value)
})

</script>
<style lang="less" scoped>
#time-now {
    position: absolute;
    top: 5%;
    left: 50%;
    font-size: 4rem;
    color: #fff;
    transform: translate(-50%, -50%);
    user-select: none;
}

#date-now {
    position: absolute;
    top: 10%;
    left: 50%;
    font-size: 1rem;
    color: #fff;
    transform: translate(-50%, -50%);
    user-select: none;
}
</style>