<template>
    <div>
        <div id="avatar">
            <n-aratar round @click="showPanel">
                <n-icon size="40" color="#ffffff80">
                    <person-circle-sharp />
                </n-icon>
            </n-aratar>
        </div>
        <div id="panel">
            <Panel :isPanelShow="isPanelShow" @closePanel="isPanelShow.value = false" @changeBg="getBgImg"></Panel>
        </div>
        <div id="background">
            <img :src="bgImg" alt="背景图片加载失败">
            <div>
                <n-el tag="div" id="bg-mask"></n-el>
            </div>
        </div>
        <div id="content">
            <Date></Date>
            <Search></Search>
        </div>
    </div>
</template>
<script setup>
import { PersonCircleSharp } from "@vicons/ionicons5"
import { NAvatar } from 'naive-ui'
import { onMounted, ref, reactive } from "vue";
import Date from "@/components/Date.vue";
import Search from "@/components/Search.vue";
import Panel from "@/components/Panel.vue";
const bgImg = ref('')
const isPanelShow = reactive({ value: false })

const showPanel = () => {
    isPanelShow.value = !isPanelShow.value
}

const getBgImg = () => {
    //当前屏幕大小
    const width = document.documentElement.clientWidth;
    const height = document.documentElement.clientHeight;
    //随机生成1-3的数字
    const random = Math.floor(Math.random() * 3) + 1;
    //根据随机数选择背景图片
    switch (random) {
        case 1:
            bgImg.value = `https://picsum.photos/${width}/${height}/?random`;
            break;
        case 2:
            bgImg.value = `https://source.unsplash.com/${width}x${height}/?nature`;
            break;
        case 3:
            bgImg.value = 'https://picsum.photos/id/' + Math.floor(Math.random() * 1000) + '/' + width + '/' + height
            break;
    }
}

onMounted(() => {
    getBgImg()
})

</script>
<style lang="less" scoped>
#avatar {
    position: absolute;
    top: 0;
    left: 0;
    margin: auto;
    margin-top: 5px;
    margin-left: 5px;
}

#avatar:hover {
    cursor: pointer;
}

#background {
    height: 100vh;
    width: 100vw;
    position: absolute;
    top: 0;
    left: 0;
    z-index: -9999;

    img {
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0;
        left: 0;
    }

    #bg-mask {
        display: inline-block;
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: var(--body-color);
        backdrop-filter: blur(var(--body-ambiguity));
    }
}

#date {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: #fff;
    font-size: 2rem;
    font-weight: bold;
}
</style>