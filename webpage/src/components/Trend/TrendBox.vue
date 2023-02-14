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
            <n-scrollbar style="max-height:65vh ;margin-top: 10px;">
                <!-- <n-collapse :default-expanded-names="[0]">
                    <n-collapse-item v-for="( item, index) in trends" :key="index" v-show="item.show" :title="item.name" :name="index">
                        <template #header>
                            <div style="color: black;font-size: 14px;">{{ item.name }}</div>
                        </template>
                        <div v-for="(line, j) in item.content" :key="j" style="padding:0.2rem 0 ;">
                            <n-a :href="line.url" target="_blank" style="text-decoration:none;">
                                <n-grid :cols="12">
                                    <n-gi :span="1">{{ j+ 1 }}</n-gi>
                                    <n-gi :span="10">{{ line.title }}</n-gi>
                                    <n-gi :span="1"></n-gi>
                                </n-grid>
                            </n-a>
                        </div>
                    </n-collapse-item>
                </n-collapse> -->
                <Container orientation="vertical" @drop="onDrop">
                    <Draggable v-for="( item, index) in trends" :key="index">
                        <n-collapse :default-expanded-names="[0]">
                            <n-collapse-item v-show="item.show" :title="item.name" :name="index">
                                <template #header>
                                    <div style="color: black;font-size: 0.8rem;">{{ item.name }}</div>
                                </template>
                                <template #header-extra>
                                    <n-icon :size="20" style="cursor: move;">
                                        <ReorderThreeSharp></ReorderThreeSharp>
                                    </n-icon>
                                </template>
                                <div v-for="(line, j) in item.content" :key="j" style="padding:0.2rem 0 ;">
                                    <n-a :href="line.url" target="_blank" style="text-decoration:none;">
                                        <n-grid :cols="12" style="font-size: 1rem;">
                                            <n-gi :span="1">{{ j+ 1 }}</n-gi>
                                            <n-gi :span="10">{{ line.title }}</n-gi>
                                            <n-gi :span="1"></n-gi>
                                        </n-grid>
                                    </n-a>
                                </div>
                            </n-collapse-item>
                        </n-collapse>
                    </Draggable>
                </Container>
            </n-scrollbar>
        </div>
    </div>
</template>
<script setup>
import { ref, onMounted } from "vue"
import { RefreshSharp } from "@vicons/ionicons5"
import { useNotification } from "naive-ui";
import axios from "axios";
import { Container, Draggable } from "vue3-smooth-dnd";
import { ReorderThreeSharp } from "@vicons/ionicons5"
const notification = useNotification()
const trends = ref([
    {
        name: "知乎",
        key: "zhihuHot",
        show: true,
        content: []
    },
    {
        name: "36氪",
        key: "36Ke",
        show: true,
        content: []
    },
    {
        name: "B站",
        key: "bili",
        show: true,
        content: []
    },
    {
        name: "微博",
        key: "wbHot",
        show: true,
        content: []
    },
    {
        name: "百度",
        key: "baiduRD",
        show: true,
        content: []
    },
])

//获取热榜
const getTrendofKey = (i) => {
    return new Promise((resolve, reject) => {
        if (trends.value[i].show) {
            var url = '/vvhan?type=' + trends.value[i].key
            url = url.replace("/vvhan", "https://api.vvhan.com/api/hotlist")
            axios.get(url).then(res => {
                console.log(res)
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
        if (trends.value[i].show) {
            actArr.push(getTrendofKey(i))
            // switch (trends.value[i].key) {
            //     case "zhihu":
            //         actArr.push(getZhihuTrend(i))
            //         break
            //     case "bilibili":
            //         actArr.push(getBilibiliTrend(i))
            //         break
            //     case "weibo":
            //         actArr.push(getWeiBoTrend(i))
            //         break
            //     case "baidu":
            //         actArr.push(getBaiduTrend(i))
            //         break
            // }
        }
    }
    Promise.all(actArr).then(() => {
        localStorage.setItem("trends", JSON.stringify(trends.value))
        localStorage.setItem("trends_time", new Date().getTime())
    }).catch(() => {
        var last_trends = localStorage.getItem("trends")
        if (last_trends) {
            for (let i = 0; i < last_trends.length; i++)
                last_trends[i].content = []
        }
        trends.value = last_trends
        localStorage.setItem("trends", last_trends)
    })
}

const onDrop = (dropResult) => {
    trends.value = applyDrag(trends.value, dropResult)
    localStorage.setItem("trends", JSON.stringify(trends.value))
}
const applyDrag = (arr, dragResult) => {
    const { removedIndex, addedIndex, payload } = dragResult;

    if (removedIndex === null && addedIndex === null) return arr;
    const result = [...arr];
    let itemToAdd = payload;

    if (removedIndex !== null) {
        itemToAdd = result.splice(removedIndex, 1)[0];
    }
    if (addedIndex !== null) {
        result.splice(addedIndex, 0, itemToAdd);
    }
    return result;
}

/***************弃用*******************/
const getZhihuTrend = (i) => {
    return new Promise((resolve, reject) => {
        var url = "/zhihu/topstory/hot-list"
        url = url.replace("/zhihu", "https://api.zhihu.com")
        axios.get(url).then(res => {
            console.log(res)
            var content = []
            res.data.data.slice(0, 10).forEach(line => {
                content.push({
                    name: line.target.title,
                    url: line.target.url.replace("api", "www").replace("tions", "tion")
                })
            });
            trends.value[i].content = content
            resolve()
        }).catch(err => {
            notification.warning({
                content: "获取知乎热榜失败",
                duration: 1000
            })
            reject()
        })
    })
}

const getBilibiliTrend = (i) => {
    return new Promise((resolve, reject) => {
        var url = "/bilibili/main/hotword"
        url = url.replace("/bilibili", "https://s.search.bilibili.com")
        axios.get(url).then(res => {
            console.log(res)
            var content = []
            res.data.list.slice(0, 10).forEach(line => {
                content.push({
                    name: line.show_name,
                    url: "https://search.bilibili.com/all?keyword=" + line.show_name.replace(' ', "%20")
                })
            });
            trends.value[i].content = content
            resolve()
        }).catch(err => {
            notification.warning({
                content: "获取B站热榜失败",
                duration: 1000
            })
            reject()
        })
    })
}

const getWeiBoTrend = (i) => {
    return new Promise((resolve, reject) => {
        var url = "/weibo/ajax/statuses/hot_band"
        url = url.replace("/weibo", "https://weibo.com")
        axios.get(url).then(res => {
            console.log(res)
            var content = []
            res.data.data.band_list.slice(0, 10).forEach(line => {
                content.push({
                    name: line.word,
                    url: "https://s.weibo.com/weibo?q=%23" + line.word.replace(' ', "%20") + "%23"
                })
            });
            trends.value[i].content = content
            resolve()
        }).catch(err => {
            notification.warning({
                content: "获取微博热榜失败",
                duration: 1000
            })
            reject()
        })
    })
}

const getBaiduTrend = (i) => {
    return new Promise((resolve, reject) => {
        var url = "/baidu/board"
        url = url.replace("/baidu", "https://top.baidu.com/api")
        axios.get(url).then(res => {
            console.log(res)
            var content = []
            res.data.data.cards[0].content.slice(0, 10).forEach(line => {
                content.push({
                    name: line.word,
                    url: line.url
                })
            });
            trends.value[i].content = content
            resolve()
        }).catch(err => {
            notification.warning({
                content: "获取百度热榜失败",
                duration: 1000
            })
            reject()
        })
    })
}
/**************************************/


onMounted(() => {
    var last_time = localStorage.getItem("trends_time")
    var last_trends = JSON.parse(localStorage.getItem("trends"))
    if (last_trends != null)
        trends.value = last_trends
    if (last_time == null || new Date().getTime() - last_time > 1000 * 10 * 60) {
        getTrend(false)
    } else {
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
        background-color: #ffffff88;
        color: gray;
        text-align: left;
        border: none;
        border-radius: 10px;
        padding: 10px;
        margin: 5px 10px;
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