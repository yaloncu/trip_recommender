module.exports = {
  publicPath: '/',
  devServer: {
    proxy: {
      '/webhooks/rest/': {
        target: 'http://localhost:5005',
        changeOrigin: true,
      },
      '/': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
    host: '0.0.0.0', 
    port: 8080, 
    client: {
      webSocketURL: 'ws://0.0.0.0:8080/ws', 
    },
    hot: false, 
    liveReload: false, 
  },
};