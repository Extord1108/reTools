<template>
    <div>

        <div ref="collectionBox" class="collection-box">
            <n-scrollbar style="max-height: 70vh;padding: 5px 0;">
                <grid-layout style="min-height: 70vh;" v-model:layout="layout" :col-num="10" :is-resizable="false" :row-height="rowHeight" :is-draggable="true" :is-mirrored="false"
                    :margin="[15, 25]" :use-css-transforms="true" :vertical-compact="true" @layout-updated="handleLayoutUpdated">
                    <grid-item v-for="item in layout" :static="item.static" :x="item.x" :y="item.y" :w="item.w" :h="item.h" :i="item.i" :key="item.i" @move="moveEvent">
                        <div class="widget" @contextmenu="handleContextMenu" @mousedown="handleMouseDown" @mouseup="handleMouseUp" @click="handleClick(item)">
                            <n-dropdown trigger="manual" :show="webDropdownID == item.i" placement="bottom-start" :options="webOptions" :on-clickoutside="onClickoutside"
                                @select="handleSelect">
                                <div class="widget_inner" v-if="item.class === 'add'">
                                    <n-icon :size="35">
                                        <AddCircle />
                                    </n-icon>
                                </div>
                                <div class="widget_inner" v-if="item.class === 'web'" @click.right="webDropdownID = item.i">

                                    <n-image :width="35" :height="35" v-if="item.icon" :preview-disabled="true" :src="item.icon"></n-image>
                                    <n-avatar :size="35" v-else round :color="item.iconColor">{{ item.name }}</n-avatar>

                                </div>
                            </n-dropdown>

                        </div>
                        <div class="title">{{ item.name }}</div>
                    </grid-item>
                </grid-layout>
            </n-scrollbar>
        </div>
        <add-modal v-model:showAdd="showAddModal" v-model:showModify="showModifyModal" :webInfo="webInfo" @add="loadLayout"></add-modal>
    </div>
</template>
<script setup>
import { ref, nextTick, computed, onMounted } from 'vue'
import AddModal from './AddModal.vue';
import { AddCircle } from "@vicons/ionicons5"
import { NScrollbar, NIcon, NModal, NCard, NTabs } from 'naive-ui'
import { json } from 'body-parser';
const collectionBox = ref(null)
const boxWidth = ref(690)
const layout = ref([])
const showAddModal = ref(false)
const showModifyModal = ref(false)

/**************布局相关**************/
const rowHeight = computed(() => {
    return Math.floor((boxWidth.value - 165) / 10) + 14//行高随空间宽度变化
})

//自定义拖拽导致的布局改变
const moveEvent = (i, newX, newY) => {
    var p;
    for (p = 0; p < layout.value.length; p++) {
        //Horizontal swapping
        if (
            newX >= layout.value[p]["x"] &&
            newX < layout.value[p]["x"] + layout.value[p]["w"] &&
            newY == layout.value[p]["y"] &&
            i != layout.value[p]["i"]
        ) {
            let initialX = layout.value[i]["x"];
            let finalX = layout.value[p]["x"];
            layout.value[i]["x"] = finalX;
            layout.value[p]["x"] = initialX;
        }
        // Vertical swapping
        if (
            newY >= layout.value[p]["y"] &&
            newY < layout.value[p]["y"] + 1 &&
            newX == layout.value[p]["x"] &&
            i != layout.value[p]["i"]
        ) {
            let initialY = layout.value[i]["y"];
            let finalY = layout.value[p]["y"];
            layout.value[i]["y"] = finalY;
            layout.value[p]["y"] = initialY;
        }
    }
}

//从localStorage中取出layout
const loadLayout = () => {
    if (localStorage.getItem('layout')) {
        layout.value = JSON.parse(localStorage.getItem('layout'))
    }
    else {
        layout.value = [{ "x": 0, "y": 0, "w": 1, "h": 1, "i": 0, "class": "add", "static": false, "name": "添加组件" },]
        localStorage.setItem('layout', JSON.stringify(layout.value))
    }
}

const handleLayoutUpdated = () => {
    localStorage.setItem("layout", JSON.stringify(layout.value))
}


/*************网站图标相关**************/

const webDropdownID = ref(-1)
const webOptions = ref([{ label: "修改", key: "modify" }, { label: "删除", key: "delete" }])
const webInfo = ref(null)

//禁止原生事件
const handleContextMenu = (e) => {
    e.preventDefault()
}
//点击外面
const onClickoutside = () => {
    webDropdownID.value = -1
    webInfo.value = null;
}

//选择下拉菜单
const handleSelect = (key, option) => {
    if (key == "modify") {
        webInfo.value = layout.value[webDropdownID.value]
        showModifyModal.value = true
    }
    else if (key == "delete") {
        layout.value.splice(webDropdownID.value, 1)
        for (let i = webDropdownID.value; i < layout.value.length; i++) {
            layout.value[i].i = i;
        }
        webDropdownID.value = -1
        localStorage.setItem("layout", JSON.stringify(layout.value))
    }
}


//拖拽和点击事件冲突
var startTime, endTime, isClick = false;
const handleMouseDown = () => {
    startTime = new Date().getTime();
}
const handleMouseUp = () => {
    endTime = new Date().getTime();
    if (endTime - startTime < 200)
        isClick = true;
}
const handleClick = (item) => {
    if (isClick) {
        if (item.class == "add")
            showAddModal.value = true
        else if (item.class == "web") {
            window.open(item.url, "_blank")
        }
        isClick = false;
    }
}

onMounted(() => {
    boxWidth.value = collectionBox.value.offsetWidth
    window.addEventListener('resize', () => {
        console.log(collectionBox.value.offsetWidth)
        boxWidth.value = collectionBox.value.offsetWidth
    })
    loadLayout()
})
</script>
<style lang="less" scoped>
#header {
    padding: 10px 20px 0 20px;
    color: white;
    text-align: left;
}

.collection-box {
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: left;

    .vue-grid-layout {
        height: 100%;
        width: 100%;

        ::v-deep .vue-grid-placeholder {
            background-color: #ffffff;
            border-radius: 10px;
        }

        .vue-grid-item {
            cursor: pointer !important;

            .title {
                height: 20%;
                font-size: 8px;
                color: #ffffff;
                text-align: center;
            }

            .widget {
                height: 80%;
                background-color: #ffffff55;
                border-radius: 10px;
                box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.1);
                display: flex;
                justify-content: center;
                align-items: center;
                text-align: center;
                font-size: 1.5rem;
                color: #000;
                transition: all 0.3s;
            }

            .widget:hover {
                background-color: #ffffff;
            }

            .widget_inner {
                display: flex;
            }
        }
    }
}
</style>