<template>
  <div class="signup-container">
    <div class="container">
      <h1 class="heading" id="signup-title">{{ $t('signup') }}</h1>

      <form class="form" @submit.prevent="signup" aria-labelledby="signup-title">
        <div class="form-group">
          <label :for="'name'">{{ $t('name') }}</label>
          <input
            id="name"
            v-model="name"
            type="text"
            class="input"
            required
            :placeholder="$t('name')"
          />
        </div>

        <div class="form-group">
          <label :for="'email'">{{ $t('email') }}</label>
          <input
            id="email"
            v-model="email"
            type="email"
            class="input"
            required
            :placeholder="$t('email')"
          />
        </div>

        <div class="form-group">
          <label :for="'password'">{{ $t('password') }}</label>
          <input
            id="password"
            v-model="password"
            type="password"
            class="input"
            required
            :placeholder="$t('password')"
          />
        </div>

        <button type="submit" class="signup-button" :aria-label="$t('signup')">
          {{ $t('signup') }}
        </button>
      </form>

      <button class="google-signup-button" @click="signupWithGoogle" :aria-label="$t('signupWithGoogle')">
        {{ $t('signupWithGoogle') }}
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
        });
        console.log('User signed up successfully:', response.data);
        localStorage.setItem('email', this.email);
        this.$router.push('/groups');
      } catch (error) {
        console.error('Error during signup:', error.response?.data?.message || error.message);
      }
    },
    async signupWithGoogle() {
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
          throw new Error('Token missing in response');
        }
      } catch (error) {
        console.error("Error during Google signup:", error.message);
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
  font-family: 'Poppins', sans-serif;
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
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
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

.google-signup-button {
  display: block;
  width: 100%;
  font-weight: bold;
  background: #4285F4; 
  color: white;
  padding-block: 15px;
  margin: 20px auto;
  border-radius: 20px;
  border: none;
  transition: all 0.2s ease-in-out;
  font-family: 'Poppins', sans-serif;
}

.google-signup-button:hover {
  background: #3367D6;
  transform: scale(1.03);
}

.google-signup-button:active {
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
.form-group {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-top: 15px;
}

label {
  font-weight: bold;
  color: #ecf0f1;
  margin-bottom: 5px;
  font-size: 0.95rem;
}

</style>
