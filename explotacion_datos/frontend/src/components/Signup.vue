<template>
  <div class="signup-container">
    <div class="container">
      <div class="heading">{{ $t('signup') }}</div>
      <form class="form" @submit.prevent="signup">
        <input
          :placeholder="$t('name')"
          id="name"
          v-model="name"
          type="text"
          class="input"
          required
        />
        <input
          placeholder="E-mail"
          id="email"
          v-model="email"
          type="email"
          class="input"
          required
        />
        <input
          :placeholder="$t('password')"
          id="password"
          v-model="password"
          type="password"
          class="input"
          required
        />
        <button type="submit" class="signup-button">{{ $t('signup') }}</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'; 
axios.defaults.baseURL = 'http://localhost:8081';
axios.defaults.headers.common['Accept'] = 'application/json';
axios.defaults.headers.post['Content-Type'] = 'application/json';

export default {
  data() {
    return {
      name: '',
      email: '',
      password: ''
    };
  },
  methods: {
    async signup() {
      try {
        const response = await axios.post('/api/signup', {
          name: this.name,
          email: this.email,
          password: this.password
        }, {
          withCredentials: true
        });
        console.log('User signed up successfully:', response.data);
        this.$router.push('/groups');
      } catch (error) {
        console.error('Error during signup:', error.response.data.message);
      }
    }
  }
};
</script>

<style scoped>
.signup-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #2c3e50;
  text-align: center;
}

.container {
  max-width: 400px;
  width: 100%;
  background: #34495e; 
  border-radius: 40px;
  padding: 30px 40px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 10px 20px;
}

.heading {
  text-align: center;
  font-weight: 900;
  font-size: 30px;
  color: #1abc9c; 
}

.form {
  margin-top: 20px;
  width: 100%;
}

.input {
  width: 100%;
  background: white;
  border: none;
  padding: 15px 20px;
  border-radius: 20px;
  margin-top: 15px;
  box-shadow: #cff0ff 0px 10px 10px -5px;
  border-inline: 2px solid transparent;
  box-sizing: border-box;
}

.input::placeholder {
  color: rgb(170, 170, 170);
}

.input:focus {
  outline: none;
  border-inline: 2px solid #1abc9c; 
}

.signup-button {
  display: block;
  width: 100%;
  font-weight: bold;
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%); /* Verde lima */
  color: white;
  padding-block: 15px;
  margin: 20px auto;
  border-radius: 20px;
  border: none;
  transition: all 0.2s ease-in-out;
}

.signup-button:hover {
  background: #1abc9c; 
  transform: scale(1.03);
}

.signup-button:active {
  transform: scale(0.95);
}
</style>
