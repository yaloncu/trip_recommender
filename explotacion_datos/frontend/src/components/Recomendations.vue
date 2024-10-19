<template>
    <div class="recommendations-container">
      <button class="back-button" @click="goBack">{{ $t('back') }}</button>
      <h1 class="main-title">{{ $t('recommendationsForGroup') }}</h1>
  
      <div class="cards-container">
        <div class="card" v-for="(city, index) in recommendations" :key="index">
            <h2>{{ city }}</h2>
        </div>
      </div>
    </div>
</template>
  
  <script>
  export default {
    data() {
      return {
        recommendations: [] 
      };
    },
    methods: {
      goBack() {
        this.$router.push('/groups');
      }
    },
    mounted() {
      const recommendations = this.$route.query.recommendations;
      this.recommendations = recommendations ? JSON.parse(recommendations) : [];
    }
  };
  </script>
  
  <style scoped>
.recommendations-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  min-height: 100vh; 
  background-color: #2c3e50;
  color: white; 
  padding: 20px;
}

.main-title {
  color: #1abc9c;
  font-size: 2rem;
  margin-top: 20px; 
  margin-bottom: 20px;
}

.cards-container {
  display: flex; 
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  max-width: 1200px;
}

.card {
  max-width: 300px; 
  width: 100%;
  background: #34495e; 
  border-radius: 20px;
  padding: 30px 20px; 
  box-shadow: rgba(0, 0, 0, 0.1) 0px 10px 20px;
  text-align: center; 
}

.back-button {
  position: absolute;
  top: 20px;
  left: 20px;
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  font-size: 1.5rem;
  padding: 20px 40px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 20px 10px -15px;
}
.back-button:hover {
  transform: scale(1.03);
}
</style>
