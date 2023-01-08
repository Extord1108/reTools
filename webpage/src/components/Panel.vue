<template>
  <div>
    <n-el tag="div" id="panel-content" ref="panel">
      <div class="panel-back">
        <n-icon size="40" color="#ffffff80" @click="close">
          <arrow-back-circle-outline />
        </n-icon>
      </div>
      <div class="user">
        <n-avatar color="#00000000" :size="40" round v-if="!avatar">
          <n-icon color="#ffffff80">
            <person-circle-sharp />
          </n-icon>
        </n-avatar>
        <n-avatar v-else round :src="avatar">
        </n-avatar>
        <n-button v-if="!isLogin" strong type="tertiary" @click="openLoginModal">登录/注册</n-button>
        <n-button-group v-else>
          <n-button style="width:100%;font-size: 1rem;" text @click="showUserModal = true">{{
          userInfo.username
          }}</n-button>
          <n-button text style="width:100%;font-size: 1.5rem" @click="logout">
            <n-icon>
              <log-out-outline />
            </n-icon>
          </n-button>
        </n-button-group>
      </div>
      <n-divider style="padding:0 2rem 0 2rem;"></n-divider>
      <div class="setting">
        <n-h4 prefix="bar">
          <n-text>
            设置
          </n-text>
        </n-h4>
        <n-space vertical>
          <n-text>遮罩深度</n-text>
          <n-slider v-model:value="opacity" :max="1" :step="0.05" />
          <n-text>背景模糊度</n-text>
          <n-slider v-model:value="ambiguity" :max="1" :step="0.05" />
          <n-text>壁纸</n-text>
          <n-space align="center">
            <n-button :loading="loading" @click="changeBg" strong type="tertiary">切换壁纸</n-button>
            <!--<n-upload ref="wallpaperUploadRef" accept=".png,.jpg,.jepg" :action="uploadUrl"
              :data="{ type: 'wallpaper' }" :max="1" :show-file-list="false" :default-file-list="fileList"
              :with-credentials="true" @finish="handleUploadFinish" @error="handleUploadError">
              <n-button strong type="tertiary">上传壁纸</n-button>
            </n-upload>-->

          </n-space>

        </n-space>
      </div>
      <n-divider style="padding:0 2rem 0 2rem;"></n-divider>
    </n-el>
    <n-modal v-model:show="showLoginModal">
      <login-modal @close-modal="closeLoginModal" @login-success="loginSuccess"></login-modal>
    </n-modal>
    <n-modal v-model:show="showUserModal">
      <user-modal @close-modal="closeUserModal"></user-modal>
    </n-modal>
  </div>
</template>
<script setup>
import {
  ArrowBackCircleOutline,
  PersonCircleSharp,
  LogOutOutline,
  StorefrontSharp,
} from "@vicons/ionicons5";
import { NAvatar, useMessage } from "naive-ui";
import {
  watch,
  defineProps,
  defineEmits,
  ref,
  onMounted,
  reactive,
  computed,
} from "@vue/runtime-core";
import { useStore } from "vuex";
import LoginModal from "./account/LoginModal.vue";
import UserModal from "./account/UserModal.vue";
import { Account } from "../api/account";
import { mediaURL, ConfigBaseURL } from "@/utils/http/Service.js"
//组件传参
const props = defineProps({
  isPanelShow: {
    type: Object,
  },
});
const message = useMessage();
const emits = defineEmits(["closePanel", "changeBg"]); //组件传事件
const store = useStore(); //全局变量
const panel = ref(); //组件实例
const opacity = ref(0.2);
const ambiguity = ref(0);
const loading = ref(false); //切换壁纸时加载
const panelPos = reactive({ left: "calc(-20% - 10px)" });
const showLoginModal = ref(false); //登录模态框
const showUserModal = ref(false); //用户模态框
const isLogin = ref(false); //是否登录
const uploadUrl = ref(ConfigBaseURL + '/image/upload');
const wallpaperUploadRef = ref();
const userInfo = ref({
  avatar: "",
  username: "",
});

const close = () => {
  emits("closePanel");
};
const changeBg = () => {
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
  }, 5000);
  emits("changeBg");
};

const uploadPic = (file, fileList) => {
  console.log(file);
  console.log(fileList);
};

const openLoginModal = () => {
  message.warning("登录注册功能暂未开放");
  //showLoginModal.value = true;
};

const closeLoginModal = () => {
  showLoginModal.value = false;
};

const loginSuccess = (data) => {
  userInfo.value.username = data.username;
  localStorage.setItem("username", data.username);
  if (data.avatar != null) {
    localStorage.setItem("avatar", data.avatar);
    store.commit("changeAvatar", data.avatar);
    localStorage.setItem("wallpaper", data.wallpaper);
    store.commit("changeWallpaper", data.wallpaper);
  }
  showLoginModal.value = false;
  isLogin.value = true;
};

const logout = () => {
  localStorage.removeItem("username");
  localStorage.removeItem("avatar");
  localStorage.removeItem("wallpaper");
  store.commit("changeAvatar", "");
  store.commit("changeWallpaper", "");
  isLogin.value = false;
  userInfo.value = {};
  Account.logout().then(() => {
    message.success("退出成功");
  });
};

//上传壁纸
const handleUploadFinish = ({ file, event }) => {
  let ret = JSON.parse((event?.target).response);
  localStorage.setItem('wallpaper', ret.url)
  store.commit('changeWallpaper', ret.url)
  wallpaperUploadRef.value.clear()
  return null;
}

//上传壁纸失败
const handleUploadError = () => {
  message.error('上传失败')
}

const avatar = computed(() => {
  if (store.getters.getAvatar != null && store.getters.getAvatar != "") {
    return mediaURL + store.getters.getAvatar;
  }
  else
    return null;
})

//控制抽屉弹出
watch(props.isPanelShow, (val) => {
  if (val.value) {
    console.log(panel.value);
    panelPos.left = "0";
  } else {
    panelPos.left = "calc(-20% - 10px)";
  }
});

//监听透明度变化
watch(opacity, (val) => {
  store.commit("changeBgOpacity", val);
});

//监听模糊度变化
watch(ambiguity, (val) => {
  store.commit("changeAmbiguity", val);
});

onMounted(() => {
  const bgOpacity = localStorage.getItem("bgOpacity");
  if (bgOpacity) {
    opacity.value = parseFloat(bgOpacity);
  }
  const bgAmbiguity = localStorage.getItem("ambiguity");
  if (ambiguity) {
    ambiguity.value = parseFloat(bgAmbiguity);
  }
  const username = localStorage.getItem("username");
  if (username) {
    userInfo.value.username = username;
    isLogin.value = true;
  }
});
</script>
<style lang="less" scoped>
#panel-content {
  position: absolute;
  top: 0;
  left: v-bind("panelPos.left");
  width: 20%;
  height: 100%;
  box-shadow: var(--panel-shadow);
  transition: left 0.2s;
  z-index: 1;
  background: var(--panel-color);
  backdrop-filter: blur(15px);

  .panel-back {
    padding: 5px 5px 0 0;
    text-align: right;

    .n-icon:hover {
      cursor: pointer;
      text-shadow: var(--panel-shadow);
    }
  }

  .user {
    display: flex;
    padding: 10px;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .n-avatar {
      margin-top: 10px;
    }

    .n-button {
      width: 50%;
      margin-top: 10px;
    }
  }

  .setting {
    text-align: left;
    padding: 0 2rem;
  }
}
</style>