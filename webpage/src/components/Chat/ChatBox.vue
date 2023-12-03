<template>
    <div>
        <div id="header">
            <n-h3 prefix="bar" style="margin:10px 0 0 0;">
                <n-grid :cols="12">
                    <n-gi :span="2">
                        <n-text>
                            聊天
                        </n-text>
                    </n-gi>
                    <n-gi :span="9"></n-gi>
                    <n-gi :span="1">
                        <n-tooltip>
                            <template #trigger>
                                <n-button text @click="clean">
                                    <n-icon :size="20">
                                        <TrashOutline />
                                    </n-icon>
                                </n-button>
                            </template>
                            清空
                        </n-tooltip>
                    </n-gi>
                </n-grid>
            </n-h3>
        </div>
        <div id="content">
            <n-scrollbar ref="scrollbarRef" style="max-height:calc((100vh - 15rem) - 100px) ;height:calc((100vh - 15rem) - 100px);margin-top: 10px;">
                <div v-if="messages.length > 0">
                    <div v-for="(item, index) in messages" :key="index">
                        <div class="chat">
                            <n-grid :cols="48">
                                <n-gi :span="2"></n-gi>
                                <n-gi :span="4">
                                    <n-avatar class="chat-avatar-dark" :size="35" v-if="item.role == 'user'">YOU</n-avatar>
                                    <n-avatar class="chat-avatar-light" :size="35" v-else>BOT</n-avatar>
                                </n-gi>
                                <n-gi :span="1"></n-gi>
                                <n-gi :span="39">
                                    <n-spin :show="item.role == 'assistant' && item.content == ''" size="small">
                                        <div class="chat-content">
                                            <div v-html="markdown.render(item.content)"></div>
                                            <div style="text-align: right;">
                                                <n-button v-if="item.role == 'assistant' && item.content != ''" text size="small" @click="copy(item.content)" color="black">
                                                    <n-icon>
                                                        <CopyOutline />
                                                    </n-icon>
                                                </n-button>
                                            </div>
                                        </div>
                                    </n-spin>
                                </n-gi>
                                <n-gi :span="2"></n-gi>
                            </n-grid>
                        </div>
                    </div>
                </div>
                <div v-else>
                    <div style="text-align: center;font-size: 1.5rem;color: white;padding-top:calc((50vh - 9rem) - 50px);">
                        开启一段对话吧！
                    </div>
                </div>
            </n-scrollbar>
            <div class="query">
                <n-grid :cols="48">
                    <n-gi :span="1"></n-gi>
                    <n-gi :span="46">
                        <n-input type="textarea" :rows="2" maxlength="50" placeholder="" autosize v-model:value="queryContent" @focus="isQueryIconShow = true"
                            @blur="isQueryIconShow = false" @keydown.enter="query" @keyup.enter="queryContent = ''" clearable>
                            <template #suffix v-if="isQueryIconShow">
                                <n-button :disabled="isReceiving" text size="small" @click="query" color="black">
                                    <n-icon>
                                        <ChatboxEllipsesOutline />
                                    </n-icon>
                                </n-button>
                            </template>
                        </n-input>
                    </n-gi>
                    <n-gi :span="1"></n-gi>
                </n-grid>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { ChatboxEllipsesOutline, TrashOutline, CopyOutline } from "@vicons/ionicons5"
import { useMessage } from "naive-ui";
import useClipboard from 'vue-clipboard3'
import MarkdownIt from "markdown-it";
import MarkdownItHighlightjs from "markdown-it-highlightjs";
import MarkdownItKatex from 'markdown-it-katex'
const hljs = require('highlight.js')
import 'highlight.js/styles/atom-one-dark.css'
import axios from "axios";
const { toClipboard } = useClipboard()
const message = useMessage()
const markdown = new MarkdownIt({
    html: true,
    linkify: true,
    typographer: true,
    breaks: true,
    highlight: function (str, lang) {
        // 如果指定了语言，就尝试用highlight.js高亮代码
        if (lang && hljs.getLanguage(lang)) {
            try {
                return hljs.highlight(lang, str).value
            } catch (__) {
                console.log("error")
            }
        }
        // 否则，就用默认的方式转义代码
        return markdown.utils.escapeHtml(str)
    }
})
markdown.use(MarkdownItHighlightjs).use(MarkdownItKatex)
const isQueryIconShow = ref(false)
const isReceiving = ref(false)
const queryContent = ref("")
const scrollbarRef = ref(null)
const messages = ref([])
const query = () => {
    if (isReceiving.value || !checkTimes() || queryContent.value.length < 1)
        return
    messages.value = messages.value.concat([
        {
            "role": "user",
            "content": queryContent.value
        },
        {
            "role": "assistant",
            "content": ""
        }
    ])
    let tempMessages = messages.value.slice(0, messages.value.length - 1)
    queryContent.value = ""
    isReceiving.value = true
    scrollbarRef.value.scrollTo({ left: 0, top: scrollbarRef.value.scrollbarInstRef.contentRef.clientHeight })
    axios.post("https://chat.extord.space/api/ernie", {
        messages: tempMessages
    }).then((res) => {
        console.log(res.data)
        if (!res.data.result) {
            isReceiving.value = false
            messages.value.splice(0, messages.value.length - 1)
            saveChat()
            if (res.data.message) {
                message.error(res.data.message)
            }
            else {
                message.error("服务器错误，请稍后再试")
            }
            return
        }
        messages.value[messages.value.length - 1].content = res.data.result
        saveChat()
        scrollbarRef.value.scrollTo({ left: 0, top: scrollbarRef.value.scrollbarInstRef.contentRef.clientHeight })
        isReceiving.value = false
    }).catch(() => {
        isReceiving.value = false
        message.error("服务器错误，请稍后再试")
    })
}

const checkTimes = () => {
    if (!localStorage.getItem("chat_time")) {//第一次使用
        localStorage.setItem("chat_times", 0)
        localStorage.setItem("chat_time", new Date().getTime())
    }
    else {
        let now = new Date().getTime()
        let last = localStorage.getItem("chat_time")
        let gap = parseInt(now - last) / 1000
        if (gap >= 60 * 60 * 24) {//超过一天
            localStorage.setItem("chat_times", 0)
            localStorage.setItem("chat_time", new Date().getTime())
        }
        else {//在当天
            if (parseInt(localStorage.getItem("chat_times")) >= 50) {
                message.warning("使用次数已耗尽，请明天再来~")
                return false;
            }
            else {
                localStorage.setItem("chat_times", parseInt(localStorage.getItem("chat_times")) + 1)
            }
        }
    }
    return true;
}

const saveChat = () => {
    localStorage.setItem("chat_data", JSON.stringify(messages.value))
}

const loadChat = () => {
    if (localStorage.getItem("chat_data")) {
        messages.value = JSON.parse(localStorage.getItem("chat_data"))
    }
}

const clean = () => {
    isReceiving.value = false
    messages.value = []
    saveChat()
}

const copy = async (content) => {
    if (content) {
        try {
            await toClipboard(content)
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
    loadChat()
})

</script>

<style scoped lang="less">
#header {
    padding: 10px 20px 0 20px;
    color: white;
    text-align: left;
}

#content {
    color: gray;
    text-align: left;

    .chat {
        padding: 4px 0px;

        .chat-content {
            min-height: 24px;
            font-size: 14px;
            color: #333;
            padding: 10px 20px 10px;
            border-radius: 10px;
            background-color: #ffffff88;
            transition: all 0.3s;

        }

        .chat-content:hover {
            background-color: #ffffff;
        }

        .chat-avatar-dark {
            background-color: #00000088;
            color: #ffffff;
        }

        .chat-avatar-light {
            background-color: #ffffff88;
            color: #000000;
        }

        ::v-deep img {
            height: 14px;
        }

        ::v-deep .katex-html {
            display: none;
        }
    }

    .query {
        padding: 1.2rem 0 0 0;

        .n-input {
            width: 100%;
            max-height: 48px;

            .n-icon {
                cursor: pointer;
            }
        }
    }
}
</style>