<template>
  <div class="signup-container">
    <h1>Sign Up</h1>
    <button @click="signupWithGoogle">Sign Up with Google</button>
  </div>
</template>

<script>
import { auth, googleProvider } from '../firebase';
import { signInWithPopup } from 'firebase/auth';

export default {
  name: 'Signup',
  methods: {
    async signupWithGoogle() {
      try {
        await signInWithPopup(auth, googleProvider);
        this.$router.push('/home'); // Redirigir a la página principal después de registrarse
      } catch (error) {
        console.error("Error during sign up:", error);
      }
    }
  }
}
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
