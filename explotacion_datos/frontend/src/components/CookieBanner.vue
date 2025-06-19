<template>
  <div v-if="showBanner" class="cookie-banner">
    <p>{{ $t('cookies.message') }}</p>
    <div class="cookie-actions">
      <button @click="acceptCookies">{{ $t('cookies.accept') }}</button>
      <button @click="rejectCookies">{{ $t('cookies.reject') }}</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showBanner: false
    };
  },
  mounted() {
    const consent = localStorage.getItem('cookieConsent');
    if (!consent) {
      this.showBanner = true;
    }
  },
  methods: {
    acceptCookies() {
      localStorage.setItem('cookieConsent', 'accepted');
      this.showBanner = false;
    },
    rejectCookies() {
      localStorage.setItem('cookieConsent', 'rejected');
      this.showBanner = false;
    }
  }
};
</script>

<style scoped>
.cookie-banner {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  background: #34495e;
  color: white;
  padding: 20px;
  text-align: center;
  z-index: 10000;
  font-family: 'Poppins', sans-serif;
}
.cookie-actions button {
  margin: 0 10px;
  background: #1abc9c;
  border: none;
  padding: 10px 20px;
  color: white;
  border-radius: 5px;
  font-family: 'Poppins', sans-serif;
}
</style>
