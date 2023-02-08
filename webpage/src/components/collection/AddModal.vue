<template>
    <div>
        <n-modal v-model:show="showModal" style="width: 50%;" @update:show="closeModal">
            <n-card>
                <n-tabs default-value="web">
                    <n-tab-pane name="web" tab="网站">
                        <n-form ref="webFormRef" :model="webModel" :rules="rules">
                            <n-form-item path="name" label="网站名称">
                                <n-input v-model:value="webModel.name" placeholder="网站名称"></n-input>
                            </n-form-item>
                            <n-form-item path="url" label="网站地址">
                                <n-input v-model:value="webModel.url" placeholder="http(s)://" @blur="getIcon"></n-input>
                            </n-form-item>
                            <n-form-item path="icon" label="网站图标">
                                <n-radio-group :value="radioValue" @update:value="handleCheckRadio">
                                    <n-radio value="color">

                                        <n-avatar :color="webModel.iconColor">{{ webModel.name }}</n-avatar>
                                    </n-radio>
                                    <n-radio value="url" v-if="iconUrl">
                                        <n-avatar :src="iconUrl"></n-avatar>
                                    </n-radio>
                                </n-radio-group>
                            </n-form-item>
                            <n-form-item path="iconColor" label="图标颜色">
                                <n-color-picker :show-alpha="false" :modes="['hex']" v-model:value="webModel.iconColor"></n-color-picker>
                            </n-form-item>
                            <n-button v-if="showAdd" @click="addWeb" tertiary block strong>添加</n-button>
                            <n-button v-if="showModify" @click="modifyWeb" tertiary block strong>修改</n-button>
                        </n-form>
                    </n-tab-pane>
                    <n-tab-pane name="app" tab="应用">
                        暂无
                    </n-tab-pane>
                </n-tabs>
            </n-card>
        </n-modal>
    </div>
</template>
<script setup>
import { ref, watch, defineProps, computed, defineEmits, reactive } from 'vue'
import { AddCircle } from '@vicons/ionicons5'
import { Service as axios } from '@/utils/http/Service.js'
import $ from "jquery";
import { useMessage } from 'naive-ui';
import { useNotification } from 'naive-ui'
const message = useMessage()
const notification = useNotification()
//模态框开关相关
const props = defineProps({
    showAdd: {
        type: Boolean,
        required: true
    },
    showModify: {
        type: Boolean,
        required: true
    },
    webInfo: {
        type: Object,
        default: null
    }
})
const emits = defineEmits(['update:showAdd', 'update:showModify', 'add'])
const closeModal = (value) => {
    emits('update:showAdd', value)
    emits('update:showModify', value)
}
const showModal = computed(() => props.showAdd || props.showModify)
const selectedWeb = computed(() => {
    return props.webInfo
})

watch(selectedWeb, (newVal, oldVal) => {
    if (newVal) {
        webModel.name = newVal.name;
        webModel.url = newVal.url;
        webModel.icon = newVal.icon;
        webModel.iconColor = newVal.iconColor;
        webModel.i = newVal.i;
        iconUrl.value = newVal.icon
        if (newVal.icon == "") {
            radioValue.value = "color"
            iconUrl.value = getIcon()
        }
        else {
            radioValue.value = "url"
            iconUrl.value = newVal.icon
        }
    }
})


//添加网站表单相关
const webModel = reactive({
    name: '',
    url: '',
    iconColor: '',
    icon: '',
    i: '',
})
const rules = reactive({
    name: [
        {
            required: true,
            trigger: ['blur', 'input'],
            validator(rule, value) {
                if (!value) {
                    return new Error("请输入网站名称");
                } else if (value.length > 6) {
                    return new Error("名称不能超过6个字");
                }
                return true;
            },
        }
    ],
    url: [
        {
            required: true,
            validator(rule, value) {
                if (!value) {
                    return new Error("请输入网站地址");
                }
                else if (!/^http(s)?:\/\/.*/.test(value)) {
                    return new Error("请输入正确的网站地址");
                }

                return true;
            },
            trigger: ['blur', 'input'],
        }
    ],
})

const iconUrl = ref("")
const radioValue = ref("color")

//自动获取网址的图标
const getIcon = () => {
    notification.info({ content: "正在自动获取图标...", duration: 3000 })
    var parser = document.createElement('a');
    let tempIconUrl = ""
    parser.href = webModel.url;
    if (parser.hostname && parser.host != window.location.host) {
        tempIconUrl = parser.protocol + '//' + parser.host + '/favicon.ico';

        try {
            var imgObj = new Image();
            imgObj.src = tempIconUrl;
            imgObj.onerror = function (err) {
                iconUrl.value = ""
                notification.warning({ content: "获取失败，请选择文字图标", duration: 3000 })
            };
            imgObj.onload = function (res) {
                iconUrl.value = tempIconUrl
            }
        } catch {
            iconUrl.value = ""
            notification.warning({ content: "获取失败，请选择文字图标", duration: 3000 })
        }
    }
    else {
        iconUrl.value = ""
        notification.warning({ content: "获取失败，请选择文字图标", duration: 3000 })
    }
}

//单选
const handleCheckRadio = (value) => {
    radioValue.value = value
}

const addWeb = () => {
    var layout = JSON.parse(localStorage.getItem('layout'))
    let maxX = 0, maxY = 0, WofMax, HofMax, maxI = 0;
    //添加到最后一个图标后面
    layout.forEach((item) => {
        if (item.y >= maxY)
            maxY = item.y
        if (item.x >= maxX) {
            WofMax = item.w
            HofMax = item.h
            maxX = item.x
        }
        if (item.i > maxI)
            maxI = item.i
    })
    //换行
    if (maxX + WofMax == 10) {
        maxX = 0;
        WofMax = 0;
        maxY = maxY + 1;
    }
    //确定图标
    if (radioValue.value == "color") {
        webModel.icon = "";
    }
    else if (radioValue.value == "url") {
        webModel.icon = iconUrl.value
    }
    layout.push({ "x": maxX + WofMax, "y": maxY, "w": 1, "h": 1, "i": maxI + 1, "class": "web", "static": false, "name": webModel.name, "icon": webModel.icon, "url": webModel.url, "iconColor": webModel.iconColor })
    localStorage.setItem("layout", JSON.stringify(layout))
    emits("add")
    clear()
    message.success("添加成功")
}

const modifyWeb = () => {
    var layout = JSON.parse(localStorage.getItem('layout'))
    var i = webModel.i
    //确定图标
    if (radioValue.value == "color") {
        webModel.icon = "";
    }
    else if (radioValue.value == "url") {
        webModel.icon = iconUrl.value
    }
    layout[i].name = webModel.name
    layout[i].url = webModel.url
    layout[i].icon = webModel.icon
    layout[i].iconColor = webModel.iconColor
    localStorage.setItem("layout", JSON.stringify(layout))
    emits("add")
    clear()
    closeModal(false)
    message.success("修改成功")
}

const clear = () => {
    webModel.icon = ""
    webModel.url = ""
    webModel.iconColor = ""
    webModel.name = ""
    iconUrl.value = ""
}

</script>
<style lang="less" scoped>

</style>