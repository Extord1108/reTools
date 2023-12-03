<template>
  <div>
    <div id="avatar">
      <n-avatar round color="#00000000" @click="showPanel" :size="45">
        <n-icon color="#ffffff80">
          <SettingsSharp />
        </n-icon>
      </n-avatar>
    </div>
    <div id="panel">
      <Panel :isPanelShow="isPanelShow" :isImgLoaded="isImgLoaded" @closePanel="isPanelShow.value = false" @changeBg="getBgImg"></Panel>
    </div>
    <div id="background">
      <img v-if="isImgLoaded.value" :src="bgImg" alt="背景图片加载失败" class="bgimg">
      <div>
        <n-el tag="div" id="bg-mask"></n-el>
      </div>
    </div>
    <div id="content" :style="{ marginTop: onlySearch ? '8rem' : 0, height: onlySearch ? 'calc(100vh - 8rem)' : '100vh', position: 'relative', transition: 'all 0.3s' }">
      <Date></Date>
      <Search></Search>
      <n-grid v-if="!onlySearch" :x-gap="24" style="position: absolute;top: 10rem;width:100%;height:calc(100vh - 10rem - 30px);" :cols="48">
        <n-gi :span="1"></n-gi>
        <n-gi :span="12">
          <n-el tag="div" class="box-bg">
            <TrendBox></TrendBox>
          </n-el>
        </n-gi>
        <n-gi :span="22">
          <n-el tag="div" class="box-bg">
            <CollectionBox></CollectionBox>
          </n-el>

        </n-gi>
        <n-gi :span="12">
          <n-el tag="div" class="box-bg">
            <ChatBox></ChatBox>
          </n-el>
        </n-gi>
        <n-gi :span="1"></n-gi>
      </n-grid>
    </div>
    <div id="bottom">
      <!--备案信息-->
      <div id="icp">
        <a href="http://beian.miit.gov.cn/" target="_blank">鲁ICP备2022027913号</a>
      </div>
    </div>
  </div>
</template>
<script setup>
import { SettingsSharp } from "@vicons/ionicons5";
import { NAvatar } from "naive-ui";
import { onMounted, ref, reactive, computed, watch } from "vue";
import { useStore } from "vuex";
import axios from "axios";
import { mediaURL } from "@/utils/http/Service.js"
import Date from "@/components/Date.vue";
import Search from "@/components/Search.vue";
import CollectionBox from "@/components/collection/CollectionBox.vue";
import TrendBox from "@/components/Trend/TrendBox.vue";
import Panel from "@/components/Panel.vue";
import ChatBox from "../components/Chat/ChatBox.vue";
const store = useStore();
const bgImg = ref("");
const isPanelShow = reactive({ value: false });
const isImgLoaded = reactive({ value: false });

const showPanel = () => {
  isPanelShow.value = !isPanelShow.value;
};

const getBgImg = () => {
  isImgLoaded.value = false;
  //当前屏幕大小
  const width = document.documentElement.clientWidth;
  const height = document.documentElement.clientHeight;
  // bgImg.value = `https://picsum.photos/${width}/${height}`;
  axios.get(`https://picsum.photos/${width}/${height}`, {}).then((res) => {
    bgImg.value = res.request.responseURL
    isImgLoaded.value = true;
  }).catch(error => {
    isImgLoaded.value = true;
    console.log(error)
  })
};

const wallpaper = computed(() => {
  if (store.getters.getWallpaper != null && store.getters.getWallpaper != "") {
    return mediaURL + store.getters.getWallpaper;
  }
  else
    return null;
})

const onlySearch = computed(() => {
  return store.getters.getOnlySearch
})

onMounted(() => {
  getBgImg();
});
</script>
<style lang="less" scoped>
#avatar {
  position: absolute;
  top: 0;
  left: 0;
  margin: auto;
  margin-top: 10px;
  margin-left: 10px;
  z-index: 1;
}

#avatar:hover {
  cursor: pointer;
}

#background {
  height: 100vh;
  width: 100vw;
  min-width: 1080px;
  position: absolute;
  top: 0;
  left: 0;
  z-index: -9999;

  .bgimg {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
  }

  #bg-mask {
    display: inline-block;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: var(--body-color);
    backdrop-filter: blur(var(--body-ambiguity));
    background-repeat: no-repeat;
  }
}

#content {
  min-width: 1080px;
  overflow: hidden;
}

#date {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  font-size: 2rem;
  font-weight: bold;
}

#bottom {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;

  #icp {
    a {
      font-size: 0.5rem;
      color: #bbb;
      text-decoration: none;
    }

    a:hover {
      color: #222;
    }
  }
}

.box-bg {
  height: 100%;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 15px;
  box-shadow: 0px 0px 20px #ffffff44;
}
</style>