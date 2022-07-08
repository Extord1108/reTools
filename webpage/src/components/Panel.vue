<template>
    <div>
        <n-el tag="div" id="panel-content" ref="panel">
            <div class="panel-back">
                <n-icon size="35" color="#ffffff80" @click="close">
                    <arrow-back-circle-outline />
                </n-icon>
            </div>
            <div class="user">
                <n-aratar round>
                    <n-icon size="40" color="#ffffff80">
                        <person-circle-sharp />
                    </n-icon>
                </n-aratar>
                <n-button strong type="tertiary">登录/注册</n-button>

            </div>
            <n-divider style="padding:0 2rem 0 2rem;"></n-divider>
            <div class="setting">
                <n-h4 prefix="bar">
                    <n-text>
                        设置
                    </n-text>
                </n-h4>
                <n-space vertical>
                    <n-text>遮罩深度</n-text>
                    <n-slider v-model:value="opacity" :max="1" :step="0.05" />
                    <n-text>背景模糊度</n-text>
                    <n-slider v-model:value="ambiguity" :max="1" :step="0.05" />
                    <n-text>壁纸</n-text>
                    <n-space align="center">
                        <n-button :loading="loading" @click="changeBg" strong type="tertiary">切换壁纸</n-button>
                        <n-upload accept=".png,.jpg,.jepg" :show-file-list="false" @before-upload="uploadPic">
                            <n-button strong type="tertiary">上传壁纸</n-button>
                        </n-upload>

                    </n-space>

                </n-space>
            </div>
            <n-divider style="padding:0 2rem 0 2rem;"></n-divider>
        </n-el>
    </div>
</template>
<script setup>
import { ArrowBackCircleOutline, PersonCircleSharp } from "@vicons/ionicons5"
import { NAvatar } from 'naive-ui'
import { watch, defineProps, defineEmits, ref, onMounted, reactive } from "@vue/runtime-core";
import { useStore } from 'vuex'
const props = defineProps({
    isPanelShow: {
        type: Object
    }
})
const emits = defineEmits(['closePanel', 'changeBg'])
const store = useStore()
const panel = ref()
const opacity = ref(0.2)
const ambiguity = ref(0)
const loading = ref(false)
const panelPos = reactive({ left: "calc(-20% - 10px)" })

const close = () => {
    emits('closePanel')
}
const changeBg = () => {
    loading.value = true
    setTimeout(() => {
        loading.value = false
    }, 5000)
    emits('changeBg')
}

const uploadPic = (file, fileList) => {
    console.log(file)
    console.log(fileList)

}

watch(props.isPanelShow, (val) => {
    if (val.value) {
        console.log(panel.value)
        panelPos.left = "0"
    } else {
        panelPos.left = "calc(-20% - 10px)"
    }
})
watch(opacity, (val) => {
    store.commit('changeBgOpacity', val)
})
watch(ambiguity, (val) => {
    store.commit('changeAmbiguity', val)
})

onMounted(() => {
    const bgOpacity = localStorage.getItem('bgOpacity')
    if (bgOpacity) {
        opacity.value = parseFloat(bgOpacity)
    }
    const bgAmbiguity = localStorage.getItem('ambiguity')
    if (ambiguity) {
        ambiguity.value = parseFloat(bgAmbiguity)
    }
})
</script>
<style lang="less" scoped>
#panel-content {
    position: absolute;
    top: 0;
    left: v-bind('panelPos.left');
    width: 20%;
    height: 100%;
    box-shadow: var(--panel-shadow);
    transition: left 0.2s;
    z-index: 1;
    background: var(--panel-color);
    backdrop-filter: blur(15px);

    .panel-back {
        padding: 5px 5px 0 0;
        text-align: right;

        .n-icon:hover {
            cursor: pointer;
            text-shadow: var(--panel-shadow);
        }
    }

    .user {
        display: flex;
        padding: 10px;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        .n-avatar {
            margin-top: 10px;
        }

        .n-button {
            width: 50%;
        }
    }

    .setting {
        text-align: left;
        padding: 0 2rem;
    }
}
</style>