import Service from "@utils/http/Service";
const url = {
    login: "/user/login",
    register: "/user/register",
    logout: "/user/logout",
}

export class Account {
    async login(params) {
        return Service(url.login, {
            method: "POST",
            data: params
        })
    }
    async register(params) {
        return Service(url.register, {
            method: "POST",
            data: params
        })
    }
    async logout() {
        return Service(url.logout, {
            method: "POST"
        })
    }
}