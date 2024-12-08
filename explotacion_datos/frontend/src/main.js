import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import i18n from './i18n';
import { GAuthPlugin } from 'vue3-google-oauth2';

const gauthOption = {
  clientId: '495272050215-vul6map6pjci9s6bie9billsv2km65.apps.googleusercontent.com', // Reemplaza con tu Client ID
  scope: 'profile email',
  prompt: 'consent',
};

const app = createApp(App);

app.use(router);
app.use(i18n);
app.use(GAuthPlugin, gauthOption);

app.mount('#app');
