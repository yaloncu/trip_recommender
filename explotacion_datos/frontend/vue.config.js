module.exports = {
  publicPath: process.env.NODE_ENV === 'production'
    ? '/frontend/'
    : '/',

  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
};
