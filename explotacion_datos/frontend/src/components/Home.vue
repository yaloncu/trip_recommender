<template>
  <div class="container">
    <!-- Language Selector in the top-right corner -->
    <div class="language-selector">
      <span>{{ $t('lang.change') }}</span>:
      <select id="locales" v-model="selectedLocale" @change="changeLocale">
        <option value="en">{{ $t('lang.eng') }}</option>
        <option value="fr">{{ $t('lang.fr') }}</option>
        <option value="es">{{ $t('lang.es') }}</option>
        <option value="de">{{ $t('lang.de') }}</option>
      </select>
    </div>

    <!-- Welcome container -->
    <div class="welcome-container">
      <div class="heading">{{ $t('Welcome') }}</div>
      <div class="buttons">
        <button class="login-button" @click="goToLogin">{{ $t('login') }}</button>
        <button class="signup-button" @click="goToSignup">{{ $t('signup') }}</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      selectedLocale: this.$i18n.locale // Locale actual
    };
  },
  methods: {
    goToLogin() {
      this.$router.push('/login');
    },
    goToSignup() {
      this.$router.push('/signup');
    },
    changeLocale() {
      if (this.selectedLocale) {
        this.$i18n.locale = this.selectedLocale; // Cambio de idioma
      }
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
  position: relative;
}

.language-selector {
  position: absolute;
  top: 20px;
  right: 20px;
  font-size: 16px;
  color: #ecf0f1; 
}

.language-selector select {
  padding: 5px;
  border-radius: 5px;
  border: 1px solid #1abc9c;
  background-color: #34495e;
  color: #ecf0f1;
}

.language-selector select:focus {
  outline: none;
  border-color: #1abc9c;
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
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
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
