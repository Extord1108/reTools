<template>
  <div>
    <div id="avatar">
      <n-avatar
        round
        color="#00000000"
        v-if="!avatar"
        @click="showPanel"
        :size="40"
      >
        <n-icon color="#ffffff80">
          <person-circle-sharp />
        </n-icon>
      </n-avatar>
      <n-avatar
        v-else
        round
        :src="avatar"
        @click="showPanel"
      >
      </n-avatar>
    </div>
    <div id="panel">
      <Panel
        :isPanelShow="isPanelShow"
        @closePanel="isPanelShow.value = false"
        @changeBg="getBgImg"
      ></Panel>
    </div>
    <div id="background">
      <img
        :src="bgImg"
        alt="背景图片加载失败"
      >
      <div>
        <n-el
          tag="div"
          id="bg-mask"
        ></n-el>
      </div>
    </div>
    <div id="content">
      <Date></Date>
      <Search></Search>
    </div>
    <div id="bottom">
      <!--备案信息-->
      <div id="icp">
        <a
          href="http://beian.miit.gov.cn/"
          target="_blank"
        >鲁ICP备2022027913号</a>
      </div>
    </div>
  </div>
</template>
<script setup>
import { PersonCircleSharp } from "@vicons/ionicons5";
import { NAvatar } from "naive-ui";
import { onMounted, ref, reactive, computed } from "vue";
import Date from "@/components/Date.vue";
import Search from "@/components/Search.vue";
import Panel from "@/components/Panel.vue";
const bgImg = ref("");
const avatar = ref("");
const isPanelShow = reactive({ value: false });

const showPanel = () => {
  isPanelShow.value = !isPanelShow.value;
};

const getBgImg = () => {
  //当前屏幕大小
  const width = document.documentElement.clientWidth;
  const height = document.documentElement.clientHeight;
  bgImg.value = `https://source.unsplash.com/${width}x${height}/?nature`;
};

const getAvatar = () => {
  if (localStorage.getItem("avatar")) {
    console.log("avatar");
    avatar.value =
      "http://localhost:8081/static/image/" + localStorage.getItem("avatar");
  } else avatar.value = null;
};

onMounted(() => {
  getBgImg();
  getAvatar();
});
</script>
<style lang="less" scoped>
#avatar {
  position: absolute;
  top: 0;
  left: 0;
  margin: auto;
  margin-top: 5px;
  margin-left: 5px;
}

#avatar:hover {
  cursor: pointer;
}

#background {
  height: 100vh;
  width: 100vw;
  position: absolute;
  top: 0;
  left: 0;
  z-index: -9999;

  img {
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
  }
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
      font-size: 12px;
      color: #bbb;
      text-decoration: none;
    }
    a:hover {
      color: #222;
    }
  }
}
</style>