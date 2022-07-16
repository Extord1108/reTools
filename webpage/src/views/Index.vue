<template>
    <div>
        <div>这里空空如也</div>
        <router-link to="/tab">新建标签页</router-link>
    </div>
</template>
<script setup>
import { useMessage } from 'naive-ui'
window.$message = useMessage()
</script>
<style lang="less" scoped>
</style>