<template>
    <n-card style="width: 35%;min-height: 50%;" :bordered="false" size="huge" aria-modal="true">
        <n-tabs default-value="userInfo" size="large" animated style="margin: 0 -4px"
            pane-style="padding-left: 4px; padding-right: 4px; box-sizing: border-box;">
            <n-tab-pane name="userInfo" tab="个人信息">
                <n-upload :action="uploadUrl" :data="{ type: 'avatar' }" :max="1" :show-file-list="false"
                    :with-credentials="true" @finish="handleUploadFinish">
                    <n-aratar v-if="!avatar" style="cursor: pointer;" round>
                        <n-icon size="60">
                            <person-circle-sharp />
                            <div class="avatar-mask">
                                <n-text>修改头像</n-text>
                            </div>
                        </n-icon>
                    </n-aratar>
                    <n-avatar v-else :size="60" style="cursor: pointer;" round :src="avatar">
                    </n-avatar>
                </n-upload>
                <n-form ref="userinfoFormRef" :model="userinfoForm" :rules="userinfoRules">
                    <n-form-item-row path="username" label="用户名">
                        <n-input placeholder="" v-model:value="userinfoForm.username" @keydown.enter.prevent />
                    </n-form-item-row>
                    <n-form-item-row path="email" label="邮箱">
                        <n-input placeholder="" v-model:value="userinfoForm.email" @keydown.enter.prevent />
                    </n-form-item-row>
                    <n-form-item-row path="phone" label="手机">
                        <n-input placeholder="" v-model:value="userinfoForm.phone" @keydown.enter.prevent />
                    </n-form-item-row>
                </n-form>
                <n-space>
                    <n-button @click="updateUserInfo" tertiary block strong>
                        修改
                    </n-button>
                </n-space>
            </n-tab-pane>
            <n-tab-pane name="password" tab="修改密码">
                <n-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules">
                    <n-form-item-row path="oldPassword" label="旧密码">
                        <n-input placeholder="" v-model:value="passwordForm.oldPassword" @keydown.enter.prevent />
                    </n-form-item-row>
                    <n-form-item-row path="newPassword" label="新密码">
                        <n-input placeholder="" v-model:value="passwordForm.newPassword" @keydown.enter.prevent />
                    </n-form-item-row>
                    <n-form-item-row path="rePassword" label="确认密码">
                        <n-input placeholder="" v-model:value="passwordForm.rePassword" @keydown.enter.prevent />
                    </n-form-item-row>
                </n-form>
                <n-space>
                    <n-button @click="updatePassword" tertiary block strong>
                        修改
                    </n-button>
                </n-space>
            </n-tab-pane>
        </n-tabs>
    </n-card>
</template>
<script setup>
import { PersonCircleSharp } from "@vicons/ionicons5"
import { onMounted, ref, defineEmits, computed } from 'vue';
import { Account } from '@/api/account.js';
import { uploadImage } from '@/api/image.js';
import { mediaURL } from '@/utils/http/Service.js';
import { useMessage } from "naive-ui";
import { useStore } from 'vuex';

const uploadUrl = ref('http://10.193.25.120:8080/test/image/upload');
const fileList = ref([])
const message = useMessage()
const store = useStore()
const userinfoFormRef = ref()
const passwordFormRef = ref()
const userinfoForm = ref({
    username: '',
    email: '',
    phone: '',
    avatar: '',
})

const passwordForm = ref({
    oldPassword: '',
    newPassword: '',
    rePassword: ''
})

const userinfoRules = ref({
    email: [
        {
            required: true,
            validator(rule, value) {
                if (!value) {
                    return new Error("请输入邮箱");
                } else if (!/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
                    .test(value)) {
                    return new Error("请输入正确的邮箱格式");
                }
                return true;
            },
            trigger: ["input", "blur"]
        }
    ],
    username: [
        {
            required: true,
            message: "请输入用户名",
            trigger: ["input", "blur"]
        }
    ],
    phone: [
        {
            required: true,
            validator(rule, value) {
                if (!value) {
                    return true
                }
                else if (!/^1[3456789]\d{9}$/.test(value)) {
                    return new Error("请输入正确的手机号码");
                }
                return true;
            },
            trigger: ["input", "blur"]
        }
    ]
})

const passwordRules = ref({
    oldPassword: [
        {
            required: true,
            validator(rule, value) {
                if (!value) {
                    return new Error("请输入旧密码");
                }
                else if (value.length < 6) {
                    return new Error("密码长度不能小于6位");
                }
                return true;
            },
            trigger: ["input", "blur"]
        }
    ],
    newPassword: [
        {
            required: true,
            validator(rule, value) {
                if (!value) {
                    return new Error("请输入新密码");
                }
                else if (value.length < 6) {
                    return new Error("密码长度不能小于6位");
                }
                return true;
            },
            trigger: ["input", "blur"]
        }
    ],
    rePassword: [
        {
            required: true,
            validator(rule, value) {
                if (!value) {
                    return new Error("请输入新密码");
                }
                else if (value != passwordForm.value.newPassword) {
                    return new Error("密码错误");
                }
                return true;
            },
            trigger: ["input", "blur"]
        }
    ]
})

const getUserInfo = () => {

    Account.getUserInfo().then(res => {
        if (res.code == 200) {
            userinfoForm.value = res.userInfo;
        }
        else
            message.error(res.msg)
    }).catch(() => {
        message.error("网络错误")
    })
}

const avatar = computed(() => {
    if (store.getters.getAvatar != null && store.getters.getAvatar != "") {
        return mediaURL + store.getters.getAvatar;
    }
    else
        return null;
})

const updateUserInfo = () => {
    userinfoFormRef.value?.validate((error) => {
        if (!error) {
            Account.updateUserInfo(userinfoForm.value).then(res => {
                if (res.code == 200)
                    message.success("修改成功")
                else
                    message.error(res.msg)
            }).catch(() => {
                message.error("网络错误")
            })
        }
        else {
            message.error(error)
        }
    })
}

const updatePassword = () => {
    passwordFormRef.value?.validate((error) => {
        if (!error) {
            Account.updatePassword(passwordForm.value).then(res => {
                if (res.code == 200)
                    message.success("修改成功")
                else
                    message.error(res.msg)
            }).catch(() => {
                message.error("网络错误")
            }).finally(() => {
                passwordForm.value = {
                    oldPassword: '',
                    newPassword: '',
                }
            })
        }
        else {
            message.error(error)
        }
    })
}

const handleUploadFinish = ({ file, event }) => {
    console.log(event)
    let ret = JSON.parse((event?.target).response);
    console.log(ret)
    localStorage.setItem('avatar', ret.url)
    store.commit('changeAvatar', ret.url)
    return file;
}
onMounted(() => {
    getUserInfo()
})
</script>
<style lang="less" scoped>
.n-input {
    border: 1px solid rgb(224, 224, 230)
}

.avatar-mask {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background-color: rgba(0, 0, 0, 0.5);
    font-size: 0.5rem;
    transition: opacity 0.2s ease-in-out;
    opacity: 0;
}

.avatar-mask:hover {
    opacity: 1;
}
</style>