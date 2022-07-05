module.exports = {
    devServer: {
        proxy: {
            '/baiduapi': {
                target: 'http://suggestion.baidu.com/su',
                changeOrigin: true,
                pathRewrite: {
                    '^/baiduapi': ''
                }
            }
        }
    }
}
