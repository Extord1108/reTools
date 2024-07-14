<template>
    <div>
        <div id="header">
            <n-h3 prefix="bar" style="margin:10px 0 0 0;">
                <n-grid :cols="12">
                    <n-gi :span="3">
                        <n-text>
                            壁纸库
                        </n-text>
                    </n-gi>
                    <n-gi :span="7"></n-gi>
                    <n-gi :span="2">
                        <n-space>
                            <n-tooltip>
                                <template #trigger>
                                    <n-button text @click="uploadWallpaper">
                                        <n-icon :size="20">
                                            <CloudUploadOutline />
                                        </n-icon>
                                    </n-button>
                                </template>
                                上传
                            </n-tooltip>
                            <n-tooltip>
                                <template #trigger>
                                    <n-button text @click="updateWallpaper">
                                        <n-icon :size="20">
                                            <RefreshSharp />
                                        </n-icon>
                                    </n-button>
                                </template>
                                刷新
                            </n-tooltip>
                        </n-space>
                    </n-gi>
                </n-grid>
            </n-h3>
        </div>
        <div>
            <n-scrollbar style="max-height:calc((100vh - 11rem) - 100px) ;margin-top: 10px;">
                <n-space vertical>
                    <n-card v-for="(item, index) in wallpaperCollection" :key="index">
                        <template #cover>
                            <n-image object-fit="cover" :src="item" :preview-disabled="true">
                            </n-image>
                        </template>
                        <n-button @click="deleteWallpaper(index)" text type="error">
                            <n-icon :size="20">
                                <TrashSharp />
                            </n-icon>
                        </n-button>
                    </n-card>
                </n-space>
            </n-scrollbar>
        </div>
    </div>
</template>
<script setup>
import { ref, onMounted, defineExpose } from "vue"
import { CloudUploadOutline, RefreshSharp, TrashSharp } from "@vicons/ionicons5"
const wallpaperCollection = ref([])

const updateWallpaper = () => {
    if (localStorage.getItem("wallpaperCollection") == null) {
        localStorage.setItem("wallpaperCollection", JSON.stringify([]))
        wallpaperCollection.value = []
    }
    else {
        wallpaperCollection.value = JSON.parse(localStorage.getItem("wallpaperCollection"))
        console.log(wallpaperCollection.value)
    }
}

const uploadWallpaper = () => {

}

const deleteWallpaper = (index)=>{
    wallpaperCollection.value.splice(index, 1)
    localStorage.setItem("wallpaperCollection", JSON.stringify(wallpaperCollection.value))
}

onMounted(() => {
    updateWallpaper()
})
</script>
<style lang="less" scoped>
#header {
    padding: 10px 20px 0 20px;
    color: white;
    text-align: left;
}

.n-card {
    margin: auto;
    width: 90%;
    height: 20vh;
    background-color: #666;
    border: none;
    border-radius: 15px;
    box-shadow: none;
    position: relative;

    :deep(.n-card__content){
        padding: 0;
    }

    .n-button {
        position: absolute;
        top: 10px;
        right: 10px;
        opacity: 0;
    }

    .n-button:hover {
        opacity: 1;
    }
}

.n-image {
    border-radius: 15px;

    transition: all 0.5s;

}

.n-image:hover {
    opacity: 1;
}
</style>