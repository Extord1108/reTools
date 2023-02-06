<template>
    <div>
        <div ref="collectionBox" class="collection-box">
            <n-scrollbar style="max-height: 70vh;padding: 5px 0;">
                <grid-layout style="min-height: 70vh;" v-model:layout="layout" :col-num="10" :is-resizable="false" :row-height="rowHeight" :is-draggable="true" :is-mirrored="false"
                    :margin="[10, 10]" :use-css-transforms="true">
                    <grid-item v-for="item in layout" :static="item.static" :x="item.x" :y="item.y" :w="item.w" :h="item.h" :i="item.i" :key="item.i" @move="moveEvent">
                        <div class="widget" v-if="item.class === 'add'">
                            <n-icon size="30">
                                <AddCircle />
                            </n-icon>
                        </div>
                        <div class="title">{{ item.name }}</div>
                    </grid-item>
                </grid-layout>
            </n-scrollbar>
        </div>
    </div>
</template>
<script setup>
import { ref, watch, computed, onMounted } from 'vue'
import { AddCircle } from "@vicons/ionicons5"
import { NScrollbar, NIcon } from 'naive-ui'
const collectionBox = ref(null)
const boxWidth = ref(690)
const rowHeight = computed(() => {
    return Math.floor((boxWidth.value - 110) / 10) + 14//行高随空间宽度变化
})

//自定义拖拽导致的布局改变
const moveEvent = (i, newX, newY) => {
    console.log(layout.value)
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

onMounted(() => {
    boxWidth.value = collectionBox.value.offsetWidth
    window.addEventListener('resize', () => {
        console.log(collectionBox.value.offsetWidth)
        boxWidth.value = collectionBox.value.offsetWidth
    })
    if (localStorage.getItem('layout')) {
        layout.value = JSON.parse(localStorage.getItem('layout'))
    }
    else {
        layout.value = [{ "x": 0, "y": 0, "w": 1, "h": 1, "i": "0", "class": "add", "static": true, "name": "添加组件" },]
    }
})

const layout = ref([])
</script>
<style lang="less" scoped>
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
                font-size: 12px;
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
            }
        }
    }
}
</style>