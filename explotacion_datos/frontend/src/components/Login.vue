<template>
  <div class="login-container">
    <div class="container">
      <div class="heading">{{ $t('login') }}</div>
      <form class="form" @submit.prevent="login" aria-labelledby="loginTitle">
        <label for="email" class="visually-hidden">{{ $t('email') }}</label>
        <input
          :placeholder="$t('email')"
          id="email"
          v-model="email"
          type="email"
          class="input"
          required
        />

        <label for="password" class="visually-hidden">{{ $t('password') }}</label>
        <input
          :placeholder="$t('password')"
          id="password"
          v-model="password"
          type="password"
          class="input"
          required
        />

        <button type="submit" class="login-button">{{ $t('login') }}</button>
      </form>

      <button class="google-button" @click="loginWithGoogle">
        {{ $t('login_with_google') }}
      </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { signInWithGoogle } from '@/services/authService';

axios.defaults.baseURL = 'http://localhost:8081';
axios.defaults.headers.common['Accept'] = 'application/json';
axios.defaults.headers.post['Content-Type'] = 'application/json';

export default {
  data() {
    return {
      email: '',
      password: ''
    };
  },
  methods: {
    async login() {
      try {
        const response = await axios.post('/api/login', {
          email: this.email,
          password: this.password
        });

        const { email, token } = response.data;
        if (token) {
          localStorage.setItem('email', email);
          localStorage.setItem('token', token);
          this.$router.push('/groups');
        } else {
          throw new Error('Token missing in login response');
        }
      } catch (error) {
        console.error('Error during login:', error.response?.data?.message || error.message);
      }
    },
    async loginWithGoogle() {
      try {
        const { user, idToken } = await signInWithGoogle();
        const response = await axios.post('/api/signup/google', {
          token: idToken
        });

        const { email, token } = response.data;
        if (token) {
          localStorage.setItem('email', email);
          localStorage.setItem('token', token);
          this.$router.push('/groups');
        } else {
          throw new Error('Token missing in login response');
        }
      } catch (error) {
        console.error('Error during Google login:', error.response?.data?.message || error.message);
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-image: url('../assets/mapamundi.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  text-align: center;
  font-family: 'Poppins', sans-serif;
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
  font-family: 'Poppins', sans-serif;
}

.form {
  margin-top: 20px;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 15px;
  font-family: 'Poppins', sans-serif;
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

.login-button {
  display: block;
  width: 100%;
  font-weight: bold;
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  padding-block: 15px;
  margin: 20px auto;
  border-radius: 20px;
  border: none;
  transition: all 0.2s ease-in-out;
}

.login-button:hover {
  background: #1abc9c;
  transform: scale(1.03);
}

.login-button:active {
  transform: scale(0.95);
}

.google-button {
  display: block;
  width: 100%;
  font-weight: bold;
  background: #4285F4; /* Azul oficial de Google */
  color: white;
  padding-block: 15px;
  margin: 20px auto;
  border-radius: 20px;
  border: none;
  transition: all 0.2s ease-in-out;
  font-family: 'Poppins', sans-serif;
}

.google-button:hover {
  background: #3367D6; 
  transform: scale(1.03);
}

.google-button:active {
  transform: scale(0.95);
}
:focus {
  outline: 3px solid #1abc9c;
  outline-offset: 2px;
}
input::placeholder {
  color: #bdc3c7;
}
#sendButton {
  min-width: 44px;
  min-height: 44px;
}
.visually-hidden {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0 0 0 0);
  white-space: nowrap;
  border: 0;
}

</style>
