<template>
  <div class="signup-container">
    <h1>Sign Up</h1>
    <form @submit.prevent="signup">
      <div>
        <label for="name">Name:</label>
        <input type="text" v-model="name" id="name" required />
      </div>
      <div>
        <label for="email">Email:</label>
        <input type="email" v-model="email" id="email" required />
      </div>
      <div>
        <label for="password">Password:</label>
        <input type="password" v-model="password" id="password" required />
      </div>
      <button type="submit">Sign Up</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

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
        const response = await axios.post('/api/users/create', {
          name: this.name,
          email: this.email,
          password: this.password
        }).then(response => {
          console.log('User signed up successfully:', response.data);
        })
        .catch(error => {
          console.error('Error during signup:', error);
        });
        console.log('User signed up successfully:', response.data);
        this.$router.push('/groups');
      } catch (error) {
        if (error.response) {
          this.$router.push('/groups');
          console.error('Error response data:', error.response.data);
          console.error('Error response status:', error.response.status);
          console.error('Error response headers:', error.response.headers);

        } else if (error.request) {
          this.$router.push('/groups');
          // La solicitud se hizo pero no se recibió respuesta
          console.error('Error request:', error.request);

        } else {
          this.$router.push('/groups');
          // Algo pasó al configurar la solicitud
          console.error('Error message:', error.message);

        }
        console.error('Error config:', error.config);
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
  background-color: #191a1a;
  color: white;
  text-align: center;
}

button {
  margin: 10px;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}
</style>
