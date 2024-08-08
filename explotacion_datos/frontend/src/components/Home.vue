<template>
  <div class="container">
    <h1>Welcome</h1>
    <div class="buttons">
      <button @click="goToLogin">Login</button>
      <button @click="goToSignup">Sign Up</button>
    </div>
  </div>     
</template>

<script>
export default {
  name: "Home",
  methods: {
    goToLogin() {
      this.$router.push('/login');
    },
    goToSignup() {
      this.$router.push('/signup');
    }
  }
}
</script>

<style scoped>
  html, body {
    margin: 0;
    padding: 0;
    height: 100%;
  }

  .container {
    width: 100%;
    height: 100vh; 
    display: flex; 
    justify-content: center; 
    align-items: center; 
    --color: rgba(114, 114, 114, 0.3);
    background-color: #191a1a;
    background-image: linear-gradient(0deg, transparent 24%, var(--color) 25%, var(--color) 26%, transparent 27%, transparent 74%, var(--color) 75%, var(--color) 76%, transparent 77%, transparent),
        linear-gradient(90deg, transparent 24%, var(--color) 25%, var(--color) 26%, transparent 27%, transparent 74%, var(--color) 75%, var(--color) 76%, transparent 77%, transparent);
    background-size: 55px 55px;
    color: white; 
    text-align: center; 
  }

.buttons {
  margin-top: 20px;
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

