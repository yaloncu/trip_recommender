<template>
  <div>
    <h1>Login Page</h1>
    <button @click="signInWithGoogle">Login with Google</button>
  </div>
</template>

<script>
import { auth, googleAuthProvider } from '../firebase';
import { signInWithPopup } from 'firebase/auth';
import axios from 'axios';

export default {
  name: 'Login',
  methods: {
    async loginWithGoogle() {
      const auth = getAuth();
      const provider = new GoogleAuthProvider();
      try {
        const result = await signInWithPopup(auth, provider);
        const user = result.user;
        await axios.post('http://localhost:8080/api/users/create', {
          id: user.uid,
          name: user.displayName,
          email: user.email
        });

        console.log('User created successfully');
      } catch (error) {
        console.error('Error during login:', error);
      }
    }
  }
}
</script>

<style scoped>
.login-container {
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
