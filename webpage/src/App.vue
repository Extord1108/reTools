<template>
  <n-config-provider :theme-overrides="themeOverrides">
    <n-dialog-provider>
      <n-message-provider>
        <n-notification-provider>
          <router-view></router-view>
        </n-notification-provider>
      </n-message-provider>
    </n-dialog-provider>
  </n-config-provider>
</template>
<script setup>
import { NConfigProvider } from 'naive-ui'
import { useStore } from 'vuex'
import { computed, onMounted, ref, watch } from 'vue'
const store = useStore()
//自定义主题
const themeOverrides = ref({
  common: {
    primaryColor: "rgba(0, 0, 0, 1)",
    baseColor: "rgba(255, 255, 255, 1)",
    bodyColor: "rgba(0, 0, 0,0.3)",
    panelColor: "rgba(255, 255, 255, 0.1)",
    panelShadow: "5px 0 5px rgba(200, 200, 200, 0.5)",
    bodyAmbiguity: "0px",
  },
  Input: {
    border: "none",
    borderHover: "0.5px solid rgba(52, 52, 56, 0.5)",
    borderFocus: "0.5px solid rgba(52, 52, 56, 0.5)",
    boxShadowFocus: "none",
    color: "rgba(255, 255, 255, 0.5)",
    caretColor: "#0",
  },
  Button: {
    textColorText: "rgba(240, 240, 240, 1)",
    textColorTextPressed: "rgba(240, 240, 240, 1)",
    textColorTextHover: "rgba(200, 200, 200, 1)",
    textColorTertiary: "rgba(240, 240, 240, 1)",
    textColorPressed: "rgba(240, 240, 240, 1)",
    textColorHover: "rgba(200, 200, 200, 1)",
    textColorFocus: "rgba(240, 240, 240, 1)",
    borderColor: "rgba(240, 240, 240, 1)",
    borderHover: "1px solid rgb(200,200,200)",
    borderPressed: "1px solid rgb(240, 240, 240)",
    borderFocus: "1px solid rgb(240, 240, 240)",
    rippleColor: "rgb(240, 240, 240)"
  },
  Divider: {
    color: "rgba(255, 255, 255, 0.5)",
  },
  Typography: {
    textColor: "rgba(240, 240, 240, 1)",
    headerBarColor: "rgba(240, 240, 240, 1)",
  },


})

const opacity = computed(() => {
  return store.getters.getBgOpacity
})

watch(opacity, (val) => {
  themeOverrides.value.common.bodyColor = `rgba(0, 0, 0,${val})`
  localStorage.setItem('bgOpacity', val)
})

const ambiguity = computed(() => {
  return store.getters.getAmbiguity
})

watch(ambiguity, (val) => {
  themeOverrides.value.common.bodyAmbiguity = val * 20 + "px"
  localStorage.setItem('ambiguity', val)
})

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
  border: 0;
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
