import axios from 'axios';

axios.defaults.baseURL = 'http://backend:8081';
axios.defaults.headers.common['Accept'] = 'application/json';
axios.defaults.headers.post['Content-Type'] = 'application/json'; 

console.log('Default Axios headers:', axios.defaults.headers);

export default axios;
