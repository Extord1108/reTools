<template>
    <div>
        <div class="search-box">
            <n-dropdown :show="showSearchSuggest && searchSuggest.length > 0" :options="searchSuggest" placement="bottom-start" :on-select="handleSuggestSelected"
                @clickoutside="showSearchSuggest = false" style="width: 35vw;">
                <n-input type="text" placeholder="" autosize v-model:value="searchKeyword" @input="handleInputChange" @focus="handleInputFocus" @click="showSearchSuggest = true"
                    @blur="isSearchIconShow = false" @keydown.tab="changeEngine" @keyup.enter="search" clearable style="max-width: 35vw;min-width: 35vw;height: 5vh;">
                    <template #prefix>
                        <n-dropdown id="icon-menu" :render-label="renderButton" :render-icon="null" trigger="hover" :options="searchWays" :on-select="handleSearchSelected">
                            <n-avatar :src="selectedNow.src" size="small" color="rbga(255,255,255,0)" />
                        </n-dropdown>
                    </template>
                    <template #suffix v-if="isSearchIconShow">
                        <n-icon @click="search">
                            <search-icon />
                        </n-icon>
                    </template>
                </n-input>
            </n-dropdown>
        </div>
    </div>
</template>
<script setup>
import { ref, h } from "vue";
import { jsonp } from 'vue-jsonp';
import { NConfigProvider, NAvatar } from 'naive-ui'
import { Search as SearchIcon } from '@vicons/ionicons5'
import axios from 'axios'
import baidu from '@/assets/search/baidu.png'
import zhihu from '@/assets/search/zhihu.png'
import bilibili from '@/assets/search/bilibili.png'
import bing from '@/assets/search/bing.png'
import google from '@/assets/search/google.png'
const searchKeyword = ref(null)
const searchSuggest = ref([])
const showSearchSuggest = ref(false)
const isSearchIconShow = ref(false)
//渲染选择搜索引擎的按钮
const renderButton = (option) => {
    return h(NAvatar, {
        src: option.label,
        size: 'small',
        color: "rgba(255, 255, 255, 0)",
        style: {
            marginTop: '3px',
        }
    })
}
//搜索引擎列表
const searchWays = ref([{
    key: 'baidu',
    label: baidu,
}, {
    key: 'google',
    label: google,
}, {
    key: 'bing',
    label: bing,
}, {
    key: 'zhihu',
    label: zhihu,
}, {
    key: 'bilibili',
    label: bilibili,
}])
//选择搜索引擎
const selectedList = ref({
    baidu: {
        src: baidu,
        url: 'https://www.baidu.com/s?wd=',
    },
    google: {
        src: google,
        url: 'https://www.google.com/search?q=',
    },
    bing: {
        src: bing,
        url: 'https://www.bing.com/search?q=',
    },
    zhihu: {
        src: zhihu,
        url: 'https://www.zhihu.com/search?type=content&q=',
    },
    bilibili: {
        src: bilibili,
        url: 'https://www.bilibili.com/search?keyword=',
    }
})
//选中的搜索引擎
const selectedNow = ref({
    src: baidu,
    url: 'https://www.baidu.com/s?cb=window.bdsug.sug&wd=',
    key: 'baidu'
})
//处理选择事件
const handleSearchSelected = (key) => {
    selectedNow.value = selectedList.value[key]
    selectedNow.value.key = key
}

const handleInputFocus = () => {
    isSearchIconShow.value = true
    showSearchSuggest.value = searchSuggest.value.length > 0 ? true : false
}

//输入变化时更新下拉框内容
const handleInputChange = () => {
    if (searchKeyword.value == '') {
        searchSuggest.value.length = 0
        showSearchSuggest.value = false
    }
    jsonp('https://suggestion.baidu.com/su', {
        callbackQuery: 'cb',
        callbackName: 'callback',
        wd: searchKeyword.value,
        age: 100
    }).then((res) => {
        searchSuggest.value.length = 0
        for (let i = 0; i < res.s.length; i++) {
            searchSuggest.value.push({
                key: i,
                label: res.s[i]
            })
        }
        if (searchSuggest.value.length > 0) {
            showSearchSuggest.value = true
        } else {
            showSearchSuggest.value = false
        }
    }).catch((err) => {
        console.log(err)
    })
}
const callback = (res) => {
    console.log(res)
}
//选择下拉框内容时搜索
const handleSuggestSelected = (key, option) => {
    searchKeyword.value = option.label
    search()
}
//搜索
const search = () => {
    window.open(selectedNow.value.url + searchKeyword.value.replace(/%/g, "%25").replace(/\+/g, "%2B").replace(/#/g, "%23").replace(/&/g, "%26").replace(/\?/g, "%3F").replace(/=/g, "%3D").replace(/ /g, "+"))
    searchKeyword.value = ''
    searchSuggest.value.length = 0
}

//切换搜索引擎
const changeEngine = (event) => {
    event.preventDefault()
    console.log(event)
    let index = searchWays.value.findIndex((item) => {
        return item.key == selectedNow.value.key
    })
    if (index == searchWays.value.length - 1) {
        index = 0
    } else {
        index++
    }
    selectedNow.value.key = searchWays.value[index].key
    selectedNow.value.src = selectedList.value[selectedNow.value.key].src
    selectedNow.value.url = selectedList.value[selectedNow.value.key].url
    event.target.focus()
}
</script>
<style lang="less" scoped>
.search-box {
    position: absolute;
    top: 6rem;
    width: 100%;
    height: 3rem;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: left;

    .n-input {
        .n-icon {
            cursor: pointer;
        }

        .n-avatar {
            cursor: default;
        }
    }
}
</style>
<style>
#icon-menu {
    background-color: rgba(255, 255, 255, 0.5) !important;
}
</style>