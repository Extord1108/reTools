<template>
    <div>
        <div id="header">
            <n-h3 prefix="bar" style="margin:10px 0 0 0;">
                <n-grid :cols="12">
                    <n-gi :span="2">
                        <n-text>
                            热榜
                        </n-text>
                    </n-gi>
                    <n-gi :span="9"></n-gi>
                    <n-gi :span="1">
                        <n-tooltip>
                            <template #trigger>
                                <n-button text>
                                    <n-icon :size="20" @click="getTrend(true)">
                                        <RefreshSharp />
                                    </n-icon>
                                </n-button>
                            </template>
                            刷新
                        </n-tooltip>
                    </n-gi>
                </n-grid>
            </n-h3>
        </div>
        <div>
            <n-scrollbar style="max-height:65vh ;">
                <n-collapse :default-expanded-names="[0]">
                    <n-collapse-item v-for="( item, index) in trends" :key="index" v-show="item.show" :title="item.name" :name="index">
                        <template #header>
                            <div style="color: black;font-size: 14px;">{{ item.name }}</div>
                        </template>
                        <div v-for="(line, j) in item.content" :key="j" style="padding:0.2rem 0 ;">
                            <n-a :href="line.url" target="_blank" style="text-decoration:none;">
                                <n-grid :cols="12">
                                    <n-gi :span="1">{{ j+ 1 }}</n-gi>
                                    <n-gi :span="10">{{ line.name }}</n-gi>
                                    <n-gi :span="1"></n-gi>
                                </n-grid>
                            </n-a>
                        </div>
                    </n-collapse-item>
                </n-collapse>
            </n-scrollbar>
        </div>
    </div>
</template>
<script setup>
import { ref, onMounted } from "vue"
import { RefreshSharp } from "@vicons/ionicons5"
import { useNotification } from "naive-ui";
import axios from "axios";
const notification = useNotification()
const trends = ref([
    {
        name: "知乎",
        key: "zhihu",
        show: true,
        content: []
    },
    {
        name: "B站",
        key: "bilibili",
        show: true,
        content: []
    },
    {
        name: "微博",
        key: "weibo",
        show: true,
        content: []
    },
    {
        name: "百度",
        key: "baidu",
        show: true,
        content: []
    },
])

const getTrendofKey = (i) => {
    return new Promise((resolve, reject) => {
        if (trends.value[i].show) {
            var url = '/itapi/hotnews/' + trends.value[i].key + '?key=qlkhD15DMJuQqX0AVattPZgDRu'
            axios.get(url).then(res => {
                trends.value[i].content = res.data.data.slice(0, 10)
                resolve()
            }).catch(err => {
                notification.warning({
                    content: "获取" + trends.value[i].name + "热榜失败",
                    duration: 1000
                })
                reject()
            })
        }
    })
}

const getTrend = (refresh) => {
    if (refresh)
        notification.info({
            content: "正在获取热榜...",
            duration: 1000
        })
    let actArr = []
    for (let i = 0; i < trends.value.length; i++) {
        actArr.push(getTrendofKey(i))
    }
    Promise.all(actArr).then(() => {
        localStorage.setItem("trends", JSON.stringify(trends.value))
        localStorage.setItem("trends_time", new Date().getTime())
    })
}

onMounted(() => {
    var last_time = localStorage.getItem("trends_time")
    if (last_time == null || new Date().getTime() - last_time > 1000 * 10 * 60) {
        getTrend(false)
    } else {
        trends.value = JSON.parse(localStorage.getItem("trends"))
        console.log(trends.value)
    }
})


</script>
<style lang="less" scoped>
#header {
    padding: 10px 20px 0 20px;
    color: white;
    text-align: left;
}

.n-collapse {
    width: 95%;
    color: white;
    border-radius: 10px;
    margin: auto;

    .n-collapse-item {
        background-color: #ffffffaa;
        color: gray;
        text-align: left;
        border: none;
        border-radius: 10px;
        padding: 10px;
        margin: 10px;
        transition: all 0.3s;

        .n-a:hover {
            color: #19F;
            transition: all 0s;
        }
    }

    .n-collapse-item:hover {
        background-color: #ffffff;
    }

    ::v-deep .n-collapse-item__header {
        padding: 0;
    }
}
</style>