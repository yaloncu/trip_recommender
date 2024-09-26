<template>
  <div class="container">
    <div class="welcome-container">
      <div class="heading">Welcome</div>
      <div class="buttons">
        <button class="login-button" @click="goToLogin">Login</button>
        <button class="signup-button" @click="goToSignup">Sign Up</button>
      </div>
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
.container {
  font-family: 'Domine', serif;
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #2c3e50; 
}

.welcome-container {
  max-width: 400px;
  width: 100%;
  background: #34495e; 
  border-radius: 40px;
  padding: 30px 40px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 10px 20px;
}

.heading {
  font-weight: 700;
  font-size: 30px;
  color: #1abc9c; 
  margin-bottom: 20px;
}

.buttons {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.login-button, .signup-button {
  font-weight: 700;
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%); /* Verde lima */
  color: white;
  padding-block: 15px;
  width: 100px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 10px 20px;
}

.login-button:hover, .signup-button:hover {
  background: #1abc9c; 
  transform: scale(1.03);
}

.login-button:active, .signup-button:active {
  transform: scale(0.95);
}
</style>
