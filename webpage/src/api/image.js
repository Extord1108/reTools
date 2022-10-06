import { Service } from "../utils/http/Service";
const url = {
    upload: "/image/upload",
}

export async function uploadImage(params) {
    return Service(url.upload, {
        method: "POST",
        data: params
    })
}