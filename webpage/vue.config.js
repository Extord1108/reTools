const webpack = require("webpack");
module.exports = {
  devServer: {
    proxy: {
      "/test": {
        target: "http://localhost:8081",
        pathRewrite: { "^/test": "" },
        ws: true,
        changeOrigin: true,
      },
      "/api": {
        target: "http://localhost:4000",
        pathRewrite: { "^/api": "" },
        ws: true,
        changeOrigin: true,
      },
      /**********以下不用了***********/
      "/itapi": {
        target: "https://api.itapi.cn/api",
        pathRewrite: { "^/itapi": "" },
        ws: true,
        changeOrigin: true,
      },
      "/zhihu": {
        target: "https://api.zhihu.com",
        pathRewrite: { "^/zhihu": "" },
        ws: true,
        changeOrigin: true,
      },
      "/bilibili": {
        target: "https://s.search.bilibili.com",
        pathRewrite: { "^/bilibili": "" },
        ws: true,
        changeOrigin: true,
      },
      "/weibo": {
        target: "https://weibo.com",
        pathRewrite: { "^/weibo": "" },
        ws: true,
        changeOrigin: true,
      },
      "/baidu": {
        target: "https://top.baidu.com/api",
        pathRewrite: { "^/baidu": "" },
        ws: true,
        changeOrigin: true,
      },
      /**********以上不用了***********/
      "/vvhan": {
        target: "https://api.vvhan.com/api/hotlist",
        pathRewrite: { "^/vvhan": "" },
        ws: true,
        changeOrigin: true,
      },
    },
  },
};
