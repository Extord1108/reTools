<template>
    <div>
        <n-config-provider :theme-overrides="themeOverrides">
            <div class="search-box">
                <n-input type="text" placeholder="" v-model="searchKeyword" @keyup.enter="search" autosize clearable
                    style="min-width: 35%;height: 2.5rem;">
                    <template #prefix>
                        <n-dropdown trigger="hover" :options="searchWays">
                            <n-avatar :src="baidu" round size="small" />
                        </n-dropdown>
                    </template>
                    <template #suffix>
                        <n-icon>
                            <search-icon />
                        </n-icon>
                    </template>
                </n-input>
            </div>
        </n-config-provider>

    </div>
</template>
<script setup>
import { ref, h } from "vue";
import { NConfigProvider, NAvatar } from 'naive-ui'
import { Search as SearchIcon } from '@vicons/ionicons5'
import baidu from '@/assets/search/baidu.png'
import zhihu from '@/assets/search/zhihu.png'
import bilibili from '@/assets/search/bilibili.png'
import bing from '@/assets/search/bing.png'
const themeOverrides = {
    Input: {
        border: "none",
        borderHover: "0.5px solid rgba(52, 52, 56, 0.5)",
        borderFocus: "0.5px solid rgba(52, 52, 56, 0.5)",
        boxShadowFocus: "none",
        color: "rgba(255, 255, 255, 0.5)",
    }
}
const renderButton = (url) => {
    return () => {
        h(NAvatar, {
            src: url,
            round: true,
            size: 'small'
        })
    }
}
const searchWays = ref([{
    key: 'baidu',
    type: 'render',
    render: renderButton(baidu)
}, {
    key: 'bing',
    type: 'render',
    render: renderButton(bing)
}, {
    key: 'zhihu',
    type: 'render',
    render: renderButton(zhihu)
}
    , {
    key: 'bilibili',
    type: 'render',
    render: renderButton(bilibili)
}])
</script>
<style lang="less" scoped>
.search-box {
    position: absolute;
    top: 13%;
    width: 100%;
    height: 3rem;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: left;

    .n-input {
        border: none !important;
    }
}
</style>