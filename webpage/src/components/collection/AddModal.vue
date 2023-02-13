<template>
    <div>
        <n-modal v-model:show="showModal" style="width: 35%;" @update:show="closeModal">
            <n-card>
                <n-tabs default-value="web">
                    <n-tab-pane name="web" tab="网站">
                        <n-form ref="webFormRef" :model="webModel" :rules="rules">
                            <n-form-item path="name" label="网站名称">
                                <n-input v-model:value="webModel.name" placeholder="网站名称"></n-input>
                            </n-form-item>
                            <n-form-item path="url" label="网站地址">
                                <n-input v-model:value="webModel.url" placeholder="https://" @blur="getIcon"></n-input>
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
                            <n-button @click="handleButtonClick" tertiary block strong><span v-if="showAdd">添加</span><span v-if="showModify">修改</span></n-button>
                        </n-form>
                    </n-tab-pane>
                    <n-tab-pane name="app" tab="应用">
                        <div style="margin:10px ;">
                            <n-grid :cols="12" v-for="(item, index) in widgetOptions" :key="index">
                                <n-gi :span="1" style="font-size: 16px;">{{ item.label }}</n-gi>
                                <n-gi :span="3">
                                    <n-button @click="addWidget(item)">添加</n-button>
                                </n-gi>
                            </n-grid>
                        </div>
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
import { useMessage } from 'naive-ui';
import { useNotification } from 'naive-ui'
const message = useMessage()
const notification = useNotification()
/***************模态框开关相关**********/
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
    clear()
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
/***************************************/

/***********添加网站表单相关*************/
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
                } else if (value.length > 8) {
                    return new Error("名称不能超过8个字");
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
const webFormRef = ref(null)
const iconUrl = ref("")
const radioValue = ref("color")

//自动获取网址的图标
const getIcon = () => {
    var parser = document.createElement('a');
    let tempIconUrl = ""
    parser.href = webModel.url;
    if (parser.hostname && parser.host != window.location.host) {
        notification.info({ content: "正在自动获取图标...", duration: 1000 })
        tempIconUrl = parser.protocol + '//' + parser.host + '/favicon.ico';

        try {
            var imgObj = new Image();
            imgObj.src = tempIconUrl;
            imgObj.onerror = function (err) {
                iconUrl.value = ""
                notification.warning({ content: "获取失败，请选择文字图标", duration: 1000 })
            };
            imgObj.onload = function (res) {
                iconUrl.value = tempIconUrl
            }
        } catch {
            iconUrl.value = ""
            notification.warning({ content: "获取失败，请选择文字图标", duration: 1000 })
        }
    }
    else {
        iconUrl.value = ""
    }
}

//单选
const handleCheckRadio = (value) => {
    radioValue.value = value
}

//添加
const addWeb = () => {
    var layout = JSON.parse(localStorage.getItem('layout'))
    let maxNum = 0, maxX = 0, maxY = 0, WofMax, HofMax, maxI = 0;
    //添加到最后一个图标后面
    layout.forEach((item) => {
        console.log(item.x, item.y)
        if (item.y * 10 + item.x >= maxNum) {
            maxY = item.y
            WofMax = item.w
            HofMax = item.h
            maxX = item.x
            maxNum = item.y * 10 + item.x
        }
        if (item.i > maxI)
            maxI = item.i
    })
    console.log(maxX, maxY)
    //换行
    if (maxX + WofMax >= 9) {
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

//修改
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

//点击添加或修改按钮
const handleButtonClick = () => {
    webFormRef.value?.validate((errors) => {
        if (!errors) {
            if (props.showAdd && !props.showModify)
                addWeb()
            else if (props.showModify && !props.showAdd)
                modifyWeb()
        } else {
            console.log(errors)
        }
    })
}

const clear = () => {
    webModel.icon = ""
    webModel.url = ""
    webModel.iconColor = ""
    webModel.name = ""
    iconUrl.value = ""
}

/***************************************/

/*************添加组件相关***************/

const widgetOptions = ref([
    {
        label: "天气",
        class: "weather",
    }
])

const addWidget = (widget) => {
    var layout = JSON.parse(localStorage.getItem('layout'))
    let maxNum = 0, maxX = 0, maxY = 0, WofMax, HofMax, maxI = 0;
    //添加到最后一个图标后面
    layout.forEach((item) => {
        console.log(item.x, item.y)
        if (item.y * 10 + item.x >= maxNum) {
            maxY = item.y
            WofMax = item.w
            HofMax = item.h
            maxX = item.x
            maxNum = item.y * 10 + item.x
        }
        if (item.i > maxI)
            maxI = item.i
    })
    console.log(maxX, maxY)
    //换行
    if (maxX + WofMax + 1 >= 9) {
        maxX = 0;
        WofMax = 0;
        maxY = maxY + 1;
    }
    layout.push({ "x": maxX + WofMax, "y": maxY, "w": 2, "h": 2, "i": maxI + 1, "class": "weather", "static": false, "name": "天气" })
    localStorage.setItem("layout", JSON.stringify(layout))
    emits("add")
    message.success("添加成功")
}

</script>
<style lang="less" scoped>

</style>