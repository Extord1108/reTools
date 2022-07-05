<template>
  <n-config-provider :theme-overrides="themeOverrides">
    <router-view></router-view>
  </n-config-provider>
</template>
<script setup>
import { NConfigProvider } from 'naive-ui'
//自定义主题
const themeOverrides = {
  common: {
    baseColor: "rgba(255, 255, 255, 1)",
    bodyColor: "rgba(0, 0, 0, 0.2)",
  },
  Input: {
    border: "none",
    borderHover: "0.5px solid rgba(52, 52, 56, 0.5)",
    borderFocus: "0.5px solid rgba(52, 52, 56, 0.5)",
    boxShadowFocus: "none",
    color: "rgba(255, 255, 255, 0.5)",
  },
  Dropdown: {
    color: "rgba(255, 255, 255, 0.5)",
  }
}

</script>
<style lang="less">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 0;
}

body {
  margin: 0;
  padding: 0;
  border: 0
}


#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
