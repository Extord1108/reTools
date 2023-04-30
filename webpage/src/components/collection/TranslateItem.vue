<template>
    <div>
        <n-space vertical class="input-box">
            <n-input @focus="emits('inputing')" @blur="emits('inputend')" @click.stop v-model:value="fromValue" type="textarea" placeholder="输入文字(仅支持中译英和其他译中)" :rows="rows1" clearable
                @update:value="handleInputChange" />
            <n-input @mousedown="emits('inputing')" @click.stop @mouseup="emits('inputend')" v-model:value="toValue" type="textarea" placeholder="" :rows="rows2" readonly>
                <template #suffix>
                    <div style="height: 80%; display: flex; justify-content: flex-end;align-items: flex-end;">
                        <n-button text color="#343438" @click="copy" style="font-weight: bold;">
                            <n-icon size="20">
                                <copy-outline />
                            </n-icon>
                        </n-button>
                    </div>
                </template>
            </n-input>
        </n-space>
    </div>
</template>
<script setup>
import { ref, computed, onMounted, defineEmits } from 'vue'
import { jsonp } from 'vue-jsonp'
import { useMessage } from 'naive-ui'
import useClipboard from 'vue-clipboard3'
import { CopyOutline } from '@vicons/ionicons5'
import keys from "@/utils/keys/keys.json"
import md5 from 'js-md5'
const fromValue = ref('')
const toValue = ref('')
const rows1 = ref(1)
const rows2 = ref(3)
const message = useMessage()
const { toClipboard } = useClipboard()

const emits = defineEmits(['inputing', 'inputend'])

const getLang = (str) => {
    if (str.match(/[\u4e00-\u9fa5]/)) {
        return 'en'
    } else {
        return 'zh'
    }
}

const handleInputChange = (str) => {
    if (str) {
        let salt = (new Date).getTime()
        let sign = keys.baidu.app_id + str + salt + keys.baidu.app_key
        sign = md5(sign)
        jsonp('https://api.fanyi.baidu.com/api/trans/vip/translate', {
            q: str,
            from: "auto",
            to: getLang(str),
            appid: keys.baidu.app_id,
            salt: salt,
            sign: sign,
        }).then((res) => {
            console.log(res)
            if (res.error_code) {
                message.error("网络错误")
            }
            else {
                toValue.value = res.trans_result[0].dst
            }
        })
    }
    else {
        toValue.value = ''
    }
}

const copy = async () => {
    if (toValue.value) {
        try {
            await toClipboard(toValue.value)
            message.success('复制成功')
        } catch (e) {
            message.warning('您的浏览器不支持复制：', e)
        }
    }
    else {
        message.warning('没有内容可复制')
    }
}

onMounted(() => {
    //实时获取屏幕大小
    rows1.value = Math.floor(window.innerHeight / 600)
    rows2.value = Math.floor(window.innerHeight / 200)
    window.addEventListener('resize', (e) => {
        rows1.value = Math.floor(window.innerHeight / 600)
        rows2.value = Math.floor(window.innerHeight / 200)
    })
})
</script>
<style lang="less" scoped>
::v-deep .n-input {
    background-color: transparent !important;
    --n-border: 2px solid black !important;
    width: 16vw;
    text-align: left;

    .n-input-wrapper {
        resize: none !important;
    }

    .n-input__placeholder {
        color: rgba(52, 52, 56, 1) !important;
    }
}
</style>