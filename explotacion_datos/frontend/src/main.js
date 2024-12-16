import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import i18n from './i18n';
import { GAuthPlugin } from 'vue3-google-oauth2';
import axios from 'axios';

const gauthOption = {
  clientId: '495272050215-vul6map6pjci9s6bie9billsv2km65.apps.googleusercontent.com', 
  scope: 'profile email',
  prompt: 'consent',
};

axios.defaults.baseURL = 'http://localhost:8081'; 
axios.defaults.headers.common['Accept'] = 'application/json';

axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken'); 
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; 
    }
    return config;
  },
  (error) => {
    return Promise.reject(error); 
  }
);

const app = createApp(App);

app.use(router);
app.use(i18n);
app.use(GAuthPlugin, gauthOption);

app.mount('#app');
