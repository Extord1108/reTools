<template>
    <div>
        <div>
            <n-grid :cols="12" style="text-align: center;">
                <n-gi :span="5">
                    <div style="font-size: 24px;font-weight: bold;">{{ weatherInfo.temperature }}℃</div>
                    <div style="line-height: 14px;display: flex;">
                        <n-icon :size="14">
                            <LocationSharp></LocationSharp>
                        </n-icon>
                        <span style="font-size: 12px;">{{ weatherInfo.city }}</span>
                    </div>
                </n-gi>
                <n-gi :span="3"></n-gi>
                <n-gi :span="4">
                    <i class="qi-sunny-fill" v-if="weatherInfo.weather == '晴'"></i>
                    <i class="qi-overcast-fill" v-else-if="weatherInfo.weather == '阴天'"></i>
                    <i class="qi-foggy-fill" v-else-if="weatherInfo.weather.indexOf('雾') != -1"></i>
                    <i class="qi-cloudy-fill" v-else-if="weatherInfo.weather.indexOf('多云') != -1"></i>
                    <i class="qi-rain-and-snow-fill" v-else-if="weatherInfo.weather == '雨夹雪'"></i>
                    <i class="qi-thundershower-fill" v-else-if="weatherInfo.weather == '雷阵雨'"></i>
                    <i class="qi-light-rain-fill" v-else-if="weatherInfo.weather == '小雨' || weatherInfo.weather == '雨'"></i>
                    <i class="qi-moderate-rain-fill" v-else-if="weatherInfo.weather == '中雨'"></i>
                    <i class="qi-heavy-rain-fill" v-else-if="weatherInfo.weather == '大雨'"></i>
                    <i class="qi-extreme-rain-fill" v-else-if="weatherInfo.weather == '暴雨'"></i>
                    <i class="qi-light-snow-fill" v-else-if="weatherInfo.weather == '小雪' || weatherInfo.weather == '雪'"></i>
                    <i class="qi-moderate-snow-fill" v-else-if="weatherInfo.weather == '中雪'"></i>
                    <i class="qi-heavy-snow-fill" v-else-if="weatherInfo.weather == '大雪'"></i>
                    <i class="qi-snowstorm-fill" v-else-if="weatherInfo.weather == '暴雪'"></i>
                    <i class="qi-hail-fill" v-else-if="weatherInfo.weather == '冰雹'"></i>

                    <div style="font-size: 12px;line-height: 14px;text-align: center;">{{ weatherInfo.weather }}</div>
                </n-gi>
            </n-grid>
            <n-grid>
                <n-gi style="height: 10px;"></n-gi>
            </n-grid>
            <n-grid :cols="12" style="line-height: 18px;">
                <n-gi :span="4">
                    <i class="qi-high-temperature" style="font-size: 18px;"></i>
                    <div style="font-size: 14px;">{{ weatherInfo.forecasts[0].dayTemp }}℃</div>

                </n-gi>
                <n-gi :span="4">
                    <i class="qi-low-temperature" style="font-size: 18px;"></i>
                    <div style="font-size: 14px;">{{ weatherInfo.forecasts[0].nightTemp }}℃</div>

                </n-gi>
                <n-gi :span="4">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="20" fill="currentColor" class="qi-1011" viewBox="0 0 16 16">

                        <path
                            d="M11.079 8.375A2.5 2.5 0 0 1 16 9c0 1.397-1.24 2.5-2.5 2.5H.5a.5.5 0 0 1 0-1h13c.74 0 1.5-.688 1.5-1.5a1.5 1.5 0 0 0-2.953-.375.5.5 0 1 1-.968-.25ZM2.5 12.5A.5.5 0 0 1 3 12h8a2 2 0 1 1-1.886 2.667.5.5 0 1 1 .943-.334A1 1 0 1 0 11 13H3a.5.5 0 0 1-.5-.5Z" />
                    </svg>
                    <div style="font-size: 14px;">{{ weatherInfo.windPower }}级</div>
                </n-gi>
            </n-grid>
            <n-grid>
                <n-gi style="height: 10px;"></n-gi>
            </n-grid>
            <n-grid :cols="12">
                <n-gi :span="4">
                    <div style="font-size: 8px;">明天</div>
                    <div style="font-size: 8px;">{{ weatherInfo.forecasts[1].nightTemp }}/{{ weatherInfo.forecasts[1].dayTemp }}℃</div>
                </n-gi>
                <n-gi :span="4">
                    <div style="font-size: 8px;">后天</div>
                    <div style="font-size: 8px;">{{ weatherInfo.forecasts[2].nightTemp }}/{{ weatherInfo.forecasts[2].dayTemp }}℃</div>
                </n-gi>
                <n-gi :span="4">
                    <div style="font-size: 8px;">大后天</div>
                    <div style="font-size: 8px;">{{ weatherInfo.forecasts[3].nightTemp }}/{{ weatherInfo.forecasts[3].dayTemp }}℃</div>
                </n-gi>
            </n-grid>
        </div>
        <n-modal v-model:show="showModal">

            <n-card style="background-color: #0000;border: none;width: 450px;">
                <div id="he-plugin-standard"></div>
            </n-card>

        </n-modal>
    </div>
</template>
<script setup>
import AMapLoader from '@amap/amap-jsapi-loader';
import { onMounted, ref, defineExpose } from 'vue';
import { useNotification } from 'naive-ui';
import { LocationSharp } from "@vicons/ionicons5"
import keys from "@/utils/keys/keys.json"
const weatherInfo = ref({
    city: "",
    forecasts: [{ dayTemp: "", nightTemp: "" }, { dayTemp: "", nightTemp: "" }, { dayTemp: "", nightTemp: "" }, { dayTemp: "", nightTemp: "" }],
    temperature: "",
    weather: "",
    windPower: ""
})
const notification = useNotification()

/********************高德地图天气API相关********************/
const getForecast = (weather, city) => {
    return new Promise((resolve, reject) => {
        weather.getForecast(city, function (err, data) {
            if (err) {
                reject()
            }
            else {
                console.log(data)
                weatherInfo.value.forecasts = data.forecasts
                resolve()
            }
        });
    })
}

const getLive = (weather, city) => {
    return new Promise((resolve, reject) => {
        weather.getLive(city, function (err, data) {
            if (err) {
                reject()
            }
            else {
                console.log(data)
                weatherInfo.value.temperature = data.temperature
                weatherInfo.value.weather = data.weather
                weatherInfo.value.windPower = data.windPower
                resolve()
            }
        });
    })
}

const initMap = () => {
    AMapLoader.load({
        key: keys.amap.location_key,             // 申请好的Web端开发者Key，首次调用 load 时必填
        version: "2.0",      // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins: ['AMap.CitySearch', 'AMap.Weather'],       // 需要使用的的插件列表，如比例尺'AMap.Scale'等
    }).then((AMap) => {

        AMap.plugin('AMap.CitySearch', function () {
            var citySearch = new AMap.CitySearch()
            citySearch.getLocalCity(function (status, result) {
                if (status === 'complete' && result.info === 'OK') {
                    console.log(result)
                    AMap.plugin('AMap.Weather', function () {
                        var weather = new AMap.Weather()
                        if (result.adcode == 371200) {
                            result.adcode = 370100
                            result.city = "济南市"
                        }
                        weatherInfo.value.city = result.city
                        let actArr = []
                        actArr.push(getForecast(weather, result.adcode))
                        actArr.push(getLive(weather, result.adcode))

                        Promise.all(actArr).then(() => {
                            localStorage.setItem("weather", JSON.stringify(weatherInfo.value))
                            localStorage.setItem("weather_time", new Date().getTime())
                        }).catch(() => {
                            var last_weather = localStorage.getItem("weather")
                            if (last_weather) {
                                weatherInfo.value = last_weather
                            }
                            notification.warning({ content: "获取天气失败", duration: 1000 })
                        })


                    })
                }
                else {
                    var lastinfo = localStorage.getItem("weather")
                    if (lastinfo)
                        weatherInfo.value = lastinfo
                    else {
                        notification.warning("获取天气信息失败")
                    }
                }
            })
        })
    }).catch(e => {
        console.log(e);
    })
}

/***********************************************/
const showModal = ref(false)

/*********************和风天气API相关*********************/
const initWeatherPanel = () => {
    showModal.value = true
    //和风天气插件调用
    window.WIDGET = {
        "CONFIG": {
            "layout": "1",
            "width": "450",
            "height": "150",
            "background": "1",
            "dataColor": "FFFFFF",
            "key": "0d0bc76e64d849cb8316d370664db9ff",
            "borderRadius": "10",
        }
    };
    (function (d) {
        var c = d.createElement('link');
        c.rel = 'stylesheet';
        c.href = 'https://widget.qweather.net/standard/static/css/he-standard.css?v=1.4.0';
        var s = d.createElement('script');
        s.src = 'https://widget.qweather.net/standard/static/js/he-standard.js?v=1.4.0'
        var sn = d.getElementsByTagName('script')[0];
        sn.parentNode.insertBefore(c, sn);
        sn.parentNode.insertBefore(s, sn);
    })(document);
}

defineExpose({
    initWeatherPanel,
})

onMounted(() => {
    var last_time = localStorage.getItem("weather_time")
    if (last_time == null || new Date().getTime() - last_time > 1000 * 10 * 60) {
        initMap();
    } else {
        weatherInfo.value = JSON.parse(localStorage.getItem("weather"))
        console.log(weatherInfo.value)
    }
    //initWeatherPanel()
})


</script>
<style lang="less" scoped>

</style>