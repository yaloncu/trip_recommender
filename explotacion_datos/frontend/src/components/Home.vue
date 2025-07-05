<template>
  <div class="home-container" role="mail"> 
    <!-- Selector de idioma -->
    <div class="language-selector">
      <label for="locales" class="visually-hidden">
        {{ $t('lang.change') }}
      </label>
      <span>{{ $t('lang.change') }}</span>:
      <select id="locales" v-model="selectedLocale" @change="changeLocale">
        <option value="en">{{ $t('lang.eng') }}</option>
        <option value="fr">{{ $t('lang.fr') }}</option>
        <option value="es">{{ $t('lang.es') }}</option>
        <option value="de">{{ $t('lang.de') }}</option>
      </select>
    </div>

    <!-- Contenedor de bienvenida -->
    <div class="welcome-container">
      <div class="heading">{{ $t('Welcome') }}</div>
      <div class="buttons">
        <button class="login-button" @click="goToLogin">{{ $t('login') }}</button>
        <button class="signup-button" @click="goToSignup">{{ $t('signup') }}</button>
      </div>
    </div>
  </div>

  <div id="app">
    <router-view />
    <CookieBanner />
  </div>
</template>

<script>
import CookieBanner from './CookieBanner.vue';

export default {
  name: "Home",
  components: {
    CookieBanner
  },
  data() {
    return {
      selectedLocale: this.$i18n.locale
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
        this.$i18n.locale = this.selectedLocale;
      }
    }
  }
}
</script>

<style scoped>
.home-container {
  font-family: 'Poppins', sans-serif;
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('../assets/mapamundi.png');
  background-size: cover;
  background-position: center;
  position: relative;
}



.language-selector {
  position: absolute;
  top: 20px;
  right: 20px;
  color: #ecf0f1;
  font-size: 16px;
}

.language-selector select {
  padding: 5px;
  border-radius: 8px;
  border: 1px solid #1abc9c;
  background-color: #34495e;
  color: #ecf0f1;
  font-family: 'Poppins', sans-serif;
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
  font-weight: 900;
  font-size: 30px;
  color: #1abc9c;
  margin-bottom: 30px;
}

.buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.login-button, .signup-button {
  font-weight: bold;
  font-family: 'Poppins', sans-serif;
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  padding: 15px 25px;
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
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}

</style>
