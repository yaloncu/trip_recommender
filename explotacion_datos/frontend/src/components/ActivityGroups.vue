<template>
  <div class="page-with-menu">
    <SideMenu />

    <div class="main-content">
      <div class="groups-container">
        <h1 class="main-title">{{ $t('publicActivities') }}</h1>

        <div class="groups-content">
          <div v-if="activities.length">
            <ul class="group-list">
              <li v-for="activity in activities" :key="activity.id" class="group-item">
                <span class="group-title">{{ activity.title }}</span>
                <p class="group-name">
                  <strong>{{ $t('theme') }}:</strong> {{ activity.type }}
                </p>
                <p class="group-name">
                  <strong>{{ $t('place') }}:</strong> {{ activity.location }}
                </p>
                <div class="group-dates">
                  <span class="date-icon" title="Start date">
                    📅 {{ new Date(activity.startDateTime).toLocaleDateString() }}
                  </span>
                  <span class="date-icon" title="Start time">
                    ⏰ {{ new Date(activity.startDateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) }}
                  </span>
                </div>
                <button class="join-button" @click="viewActivityDetails(activity.id)">
                  {{ $t('viewActivity') }}
                </button>
              </li>
            </ul>
          </div>
          <div v-else>
            <p>{{ $t('noActivitiesAvailable') }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import SideMenu from '@/components/SideMenu.vue';
axios.defaults.baseURL = 'http://localhost:8081';

export default {
  components: {
    SideMenu
  },
  data() {
    return {
      activities: [],
    };
  },
  methods: {
    async fetchActivities() {
      try {
        const response = await axios.get('/api/activities/all');
        this.activities = response.data;
      } catch (error) {
        console.error('Error fetching activities:', error);
      }
    },
    viewActivityDetails(id) {
      this.$router.push({ name: 'ActivityDetails', params: { id } });
    }
  },
  mounted() {
    this.fetchActivities();
  }
};
</script>

<style scoped>
.page-with-menu {
  display: flex;
  min-height: 100vh;
  background-color: #2c3e50;
  background-image: url('@/assets/aviones.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: 100px;
}

.groups-container {
  color: white;
  font-family: 'Poppins', sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.main-title {
  color: #1abc9c;
  font-size: 2rem;
  margin: 80px 0 30px 0;
}

.groups-content {
  background: #34495e;
  border-radius: 30px;
  padding: 30px;
  width: 90%;
  max-width: 1200px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 30px 30px -20px;
}

.group-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.group-item {
  background-color: #ecf0f1;
  color: #34495e;
  padding: 20px;
  border-radius: 20px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
}

.group-title {
  font-weight: bold;
  font-size: 1.2rem;
  margin-bottom: 5px;
}

.date-icon {
  display: flex;
  align-items: center;
  gap: 5px;
}

.join-button {
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  padding: 10px 20px;
  border-radius: 20px;
  border: none;
  margin-top: 10px;
  cursor: pointer;
  transition: 0.2s ease;
}

.join-button:hover {
  transform: scale(1.03);
}
</style>
