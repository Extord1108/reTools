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
    },
  },
  configureWebpack: {
    plugins: [
      new webpack.ProvidePlugin({
        $: "jquery",
        jQuery: "jquery",
        "windows.jQuery": "jquery",
      }),
    ],
  },
};
