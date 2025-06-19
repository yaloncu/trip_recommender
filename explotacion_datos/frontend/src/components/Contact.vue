<template>
  <div class="page-with-menu">
    <SideMenu />
    <div class="contact-container">
      <h1 class="main-title">{{ $t('contact.title') }}</h1>
      <div class="contact-card">
        <p>{{ $t('contact.description') }}</p>
        <p><strong>yaizaalonsocuervo@gmail.com</strong></p>
      </div>
    </div>
  </div>
</template>

<script>
import SideMenu from '@/components/SideMenu.vue';

export default {
  name: 'Contact',
  components: {
    SideMenu
  }
};
</script>

<style scoped>
.page-with-menu {
  display: flex;
  min-height: 100vh;
}

.contact-container {
  padding: 100px 40px;
  min-height: 100vh;
  width: 100%;
  background-image: url('@/assets/mapamundi.png');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  background-color: #2c3e50;
  color: white;
  font-family: 'Poppins', sans-serif;
  box-sizing: border-box;
}

.main-title {
  color: #1abc9c;
  font-size: 2.2rem;
  margin-bottom: 30px;
  text-align: center;
}

.contact-card {
  background: #34495e;
  padding: 30px;
  border-radius: 20px;
  max-width: 600px;
  margin: 0 auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  font-size: 1.1rem;
  line-height: 1.6;
  text-align: center;
}
</style>
