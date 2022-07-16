<template>
    <n-card style="width: 35%" :bordered="false" size="huge" aria-modal="true">
        <n-tabs default-value="signin" size="large" animated style="margin: 0 -4px"
            pane-style="padding-left: 4px; padding-right: 4px; box-sizing: border-box;">
            <n-tab-pane name="signin" tab="登录">
                <n-form ref="loginFormRef" :model="loginForm" :rules="loginRules">
                    <n-form-item-row path="email" label="邮箱">
                        <n-input placeholder="" v-model:value="loginForm.email" @keydown.enter.prevent />
                    </n-form-item-row>
                    <n-form-item-row path="password" label="密码">
                        <n-input placeholder="" type="password" v-model:value="loginForm.password"
                            @keydown.enter.prevent />
                    </n-form-item-row>
                </n-form>
                <n-button @click="login" tertiary block strong>
                    登录
                </n-button>
            </n-tab-pane>
            <n-tab-pane name="signup" tab="注册">
                <n-form ref="registerFormRef" :model="registerForm" :rules="registerRules">
                    <n-form-item-row path="username" label="用户名">
                        <n-input placeholder="" v-model:value="registerForm.username" @keydown.enter.prevent />
                    </n-form-item-row>
                    <n-form-item-row path="email" label="邮箱">
                        <n-input placeholder="" v-model:value="registerForm.email" @keydown.enter.prevent />
                    </n-form-item-row>
                    <n-form-item-row path="password" label="密码">
                        <n-input placeholder="" type="password" v-model:value="registerForm.password"
                            @keydown.enter.prevent />
                    </n-form-item-row>
                    <n-form-item-row path="repassword" label="重复密码">
                        <n-input placeholder="" type="password" v-model:value="registerForm.repassword"
                            @keydown.enter.prevent />
                    </n-form-item-row>
                    <n-form-item-row path="code" label="邀请码">
                        <n-input placeholder="" v-model:value="registerForm.code" @keydown.enter.prevent />
                    </n-form-item-row>
                </n-form>
                <n-button tertiary block strong @click="register">
                    注册
                </n-button>
            </n-tab-pane>
        </n-tabs>
    </n-card>
</template>
<script setup>
import { onMounted, ref, defineEmits } from 'vue';
import { Account } from '@/api/account.js';
import { useMessage } from "naive-ui";

const emits = defineEmits(['loginSuccess', "closeModal"])//组件传事件
const loginFormRef = ref()
const message = useMessage()
const registerFormRef = ref()
const registerForm = ref({
    username: '',
    email: '',
    password: '',
    repassword: '',
    code: ''
})
const loginForm = ref({
    email: '',
    password: ''
})
const loginRules = ref({
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
    password: [
        {
            required: true,
            message: "请输入密码",
            trigger: ["input", "blur"]
        }
    ]
})

const registerRules = ref({
    username: [
        {
            required: true,
            message: "请输入用户名",
            trigger: ["input", "blur"]
        }
    ],
    email: [
        {
            required: true,
            validator(rule, value) {
                if (!value) {
                    return new Error("请输入邮箱");
                } else if (!/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)) {
                    return new Error("请输入正确的邮箱格式");
                }
                return true;
            },
            trigger: ["input", "blur"]
        }
    ],
    password: [
        {
            required: true,
            validator(rule, value) {
                if (!value) {
                    return new Error("请输入密码");
                }
                else if (value.length < 6) {
                    return new Error("密码长度不能小于6位");
                }
                return true;
            },
            trigger: ["input", "blur"]
        }
    ],
    repassword: [
        {
            required: true,
            message: "请输入重复密码",
            trigger: ["input", "blur"]
        },
        {
            validator() {
                if (registerForm.value.password !== registerForm.value.repassword) {
                    return new Error("两次密码不一致")
                }
                return true
            },
            message: "两次密码输入不一致",
            trigger: ["blur", "password-input"]
        }
    ],
    code: [
        {
            required: true,
            message: "请输入邀请码",
            trigger: ["input", "blur"]
        }
    ]
})

const validatePasswordSame = () => {
    const password = registerForm.value.password
    const repassword = registerForm.value.repassword
    if (password === repassword) {
        return true
    }
    return false
}

const login = () => {
    loginFormRef.value?.validate((errors) => {
        if (!errors) {
            Account.login(loginForm.value).then((res) => {
                if (res.code == 200) {
                    message.success("登录成功")
                    emits('loginSuccess', res.userInfo)
                }
                else {
                    message.error(res.msg)
                }
            })/*.catch(() => {
                message.error("网络错误")
            })*/
        }
    });
}
const register = () => {
    registerFormRef.value?.validate((errors) => {
        if (!errors) {
            if (validatePasswordSame()) {
                Account.register(registerForm.value).then((res) => {
                    if (res.code == 200) {
                        message.success("注册成功")
                        emits('closeModal')
                    }
                    else {
                        message.error(res.msg)
                    }
                }).catch(() => {
                    message.error("网络错误")
                })
            } else {
                message.error("两次密码输入不一致")
            }
        }
    });
}
</script>
<style lang="less" scoped>
.n-input {
    border: 1px solid rgb(224, 224, 230)
}
</style>