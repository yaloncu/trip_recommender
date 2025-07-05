<template>
  <div class="page-with-menu">
    <SideMenu />
    <div class="recommendations-container">
      <h1 class="main-title">{{ $t('recommendationsForGroup') }}</h1>

      <div class="recommendations-card">
        <p v-if="recommendations.length === 0">{{ $t('noRecommendations') }}</p>
        <ul v-else class="recommendations-list">
          <li
            v-for="(city, index) in recommendations"
            :key="index"
            class="recommendation-item"
          >
            <div class="recommendation-info">
              <strong>{{ city }}</strong>
            </div>
            <button
              class="recommend-button"
              @click="voteForCity(city)"
              :aria-label="$t('voteForCity') + ' ' + city"
            >
              {{ $t('vote') }}
            </button>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import SideMenu from '@/components/SideMenu.vue';

export default {
  components: {
    SideMenu
  },
  data() {
    return {
      recommendations: []
    };
  },
  methods: {
    async voteForCity(city) {
      const groupId = this.$route.params.groupId;
      const userId = localStorage.getItem('email');

      try {
        await axios.post('/api/vote', {
          groupId,
          userId,
          city
        });
        alert(`Votaste por ${city}!`);
      } catch (error) {
        console.error('Error al votar:', error.response?.data || error.message);
        alert('Error al votar, intenta nuevamente.');
      }
    }
  },
  mounted() {
    const groupId = this.$route.params.groupId;

    axios
      .get(`/api/recommendations/${groupId}`)
      .then(response => {
        this.recommendations = response.data;
      })
      .catch(error => {
        console.error('Error fetching recommendations:', error);
      });
  }
};
</script>

<style scoped>
.page-with-menu {
  display: flex;
  min-height: 100vh;
}

.recommendations-container {
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

.recommendations-card {
  background: #34495e;
  padding: 30px;
  border-radius: 20px;
  max-width: 600px;
  margin: 0 auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.recommendations-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.recommendation-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #1abc9c;
}

.recommendation-info {
  font-size: 1.1rem;
  color: #ecf0f1;
}

.recommend-button {
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  border: none;
  padding: 10px 18px;
  border-radius: 12px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: bold;
  transition: transform 0.2s ease-in-out;
}
.recommend-button:hover {
  transform: scale(1.05);
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
</style>
