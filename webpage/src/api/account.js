import { Service } from "../utils/http/Service";
const url = {
    login: "/user/login",
    register: "/user/register",
    logout: "/user/logout",
    info: "/user/info",
    update: "/user/update",
    updatePassword: "/user/updatePassword",
}

export class Account {
    static async login(params) {
        return Service(url.login, {
            method: "POST",
            data: params
        })
    }
    static async register(params) {
        return Service(url.register, {
            method: "POST",
            data: params
        })
    }
    static async logout() {
        return Service(url.logout, {
            method: "POST"
        })
    }
    static async getUserInfo() {
        return Service(url.info, {
            method: "GET"
        })
    }
    static async updateUserInfo(params) {
        return Service(url.update, {
            method: "POST",
            data: params
        })
    }
    static async updatePassword(params) {
        return Service(url.updatePassword, {
            method: "POST",
            data: params
        })
    }
}