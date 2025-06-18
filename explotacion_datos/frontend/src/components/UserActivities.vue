<template>
  <div class="page-with-menu">
    <SideMenu />
    <div class="user-container">
      <h1 class="main-title">{{ $t('yourActivities') }}</h1>
      
      <div class="cards-container">
        <div class="form-card">
          <div v-if="activities.length" class="groups-grid">
            <div v-for="activity in activities" :key="activity.id" class="group-card">
              <h3 class="group-name">{{ activity.title }}</h3>
              <p>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-landmark-icon lucide-landmark">
                  <path d="M10 18v-7"/><path d="M11.12 2.198a2 2 0 0 1 1.76.006l7.866 3.847c.476.233.31.949-.22.949H3.474c-.53 0-.695-.716-.22-.949z"/><path d="M14 18v-7"/><path d="M18 18v-7"/><path d="M3 22h18"/><path d="M6 18v-7"/>
                </svg>
                {{ activity.type }}
              </p>
              <p>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-map-pin-icon lucide-map-pin">
                  <path d="M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0"/><circle cx="12" cy="10" r="3"/>
                </svg>
                {{ activity.location }}
              </p>
              <p>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-calendar-icon lucide-calendar">
                  <path d="M8 2v4"/><path d="M16 2v4"/><rect width="18" height="18" x="3" y="4" rx="2"/><path d="M3 10h18"/>
                </svg>
                {{ formatDate(activity.startDateTime) }}
              </p>
              <p>{{ activity.description }}</p>
              <p>
                <button @click="$router.push(`/activity-chat/${activity.id}`)" class="recommend-button">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-message-square-text-icon lucide-message-square-text">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/><path d="M13 8H7"/><path d="M17 12H7"/>
                  </svg>
                </button>
                <button @click="viewParticipants(activity.id)" class="recommend-button">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-users-icon lucide-users">
                    <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><path d="M16 3.128a4 4 0 0 1 0 7.744"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><circle cx="9" cy="7" r="4"/>
                  </svg>
                </button>
              </p>
              <p>
                <button @click="leaveActivity(activity.id)" class="leave-button">{{ $t('leaveActivity') }}</button>
              </p>
            </div>
          </div>
          <div v-else>
            <p>{{ $t('notInAnyActivity') }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import SideMenu from '@/components/SideMenu.vue';

export default {
  data() {
    return {
      activities: [],
      userEmail: ''
    };
  },
  components: {
    SideMenu
  },
  methods: {
    async fetchUserActivities() {
      try {
        const email = localStorage.getItem('email');
        this.userEmail = email;
        const response = await axios.get(`/api/activities/user?email=${email}`);
        this.activities = response.data;
      } catch (error) {
        console.error('Error fetching activities:', error);
        alert('Error al cargar tus actividades.');
      }
    },
    async leaveActivity(activityId) {
      try {
        await axios.delete(`/api/activities/leave`, {
          data: { email: this.userEmail, activityId }
        });
        alert('Has abandonado la actividad.');
        this.fetchUserActivities();
      } catch (error) {
        console.error('Error leaving activity:', error);
        alert('Error al abandonar la actividad.');
      }
    },
    formatDate(datetimeStr) {
        if (!datetimeStr) return '';
        const date = new Date(datetimeStr);
        return date.toLocaleString('es-ES', {
        dateStyle: 'medium',
        timeStyle: 'short'
        });
    },
    viewParticipants(activityId) {
      this.$router.push(`/activity-participants/${activityId}`);
    },
  },
  mounted() {
    this.fetchUserActivities();
  }
};
</script>

<style scoped>
.page-with-menu {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex-grow: 1;
  margin-left: 220px; 
  box-sizing: border-box;
}

.user-container {
  position: relative;
  padding: 100px 40px 100px 40px; 
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


.join-button-header {
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
  font-family: 'Poppins', sans-serif;
  z-index: 1000;
}

.join-button-header:hover {
  transform: scale(1.03);
}

.join-button-header:active {
  transform: scale(0.95);
}

.create-button-header {
  position: absolute;
  top: 20px;
  right: 20px;
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  font-size: 1.5rem;
  padding: 20px 40px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 20px 10px -15px;
  font-family: 'Poppins', sans-serif;
  z-index: 1000;
}

.create-button-header:hover {
  transform: scale(1.03);
}

.create-button-header:active {
  transform: scale(0.95);
}

.profile-button-wrapper {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 1000; /* Ensure it's above other content */
}

.profile-button-container {
  background-color: #16a085;
  padding: 20px;
  border-radius: 50%;
  transition: 0.2s ease-in-out;
}

.profile-button {
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  cursor: pointer;
}

.profile-icon {
  width: 2rem;
  height: 2rem;
  fill: white;
}

.main-title {
  color: #1abc9c;
  font-size: 2.2rem;
  margin-bottom: 20px;
  text-align: center;
}

.cards-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  width: 100%;
  max-width: 1200px;
  gap: 30px;
  margin-top: 50px; /* Adjusted margin-top to account for header buttons */
}

.form-card { /* Renamed from .card to .form-card for consistency */
  background: #34495e;
  padding: 30px;
  border-radius: 30px; /* Increased border-radius for consistency */
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  flex: 1;
  min-width: 360px;
  box-sizing: border-box; /* Ensures padding doesn't increase total width */
}

.groups-grid,
.invited-groups-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.group-card {
  background: #2c3e50;
  padding: 20px;
  border-radius: 15px;
  color: white;
  box-shadow: 0 6px 16px rgba(0,0,0,0.15);
}

.group-name {
  font-size: 1.5rem; /* Increased font size for group name */
  font-weight: bold;
  color: #1abc9c;
  margin-bottom: 10px;
}

.group-actions {
  margin-top: 15px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

button.leave-button,
button.close-button,
button.recommend-button,
button.final-destination-button,
button.invite-button {
  padding: 10px 20px; /* Adjusted padding */
  border-radius: 20px;
  font-size: 1rem; /* Adjusted font size */
  border: none;
  cursor: pointer;
  background-color: #1abc9c;
  color: white;
  transition: background-color 0.3s ease;
}

button.leave-button:hover,
button.close-button:hover,
button.recommend-button:hover,
button.final-destination-button:hover,
button.invite-button:hover {
  background-color: #16a085;
}

.input {
  width: 100%;
  background: white;
  border: none;
  padding: 15px 20px;
  border-radius: 15px;
  margin-top: 10px;
  margin-bottom: 20px; /* Adjusted margin-bottom */
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
  font-family: 'Poppins', sans-serif;
  box-sizing: border-box;
}

.admin-actions {
  margin-top: 10px;
}

.title { /* Added title class for consistency */
  color: #1abc9c;
  font-size: 1.2rem;
  margin-bottom: 10px;
  margin-top: 20px;
}

.checkbox-column {
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: flex-start;
  margin-bottom: 20px;
}

.radio-box {
  display: flex;
  align-items: center;
  gap: 8px;
  color: white;
  font-weight: bold;
  cursor: pointer;
}

.compact-date-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.icon-date-group {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  min-width: 150px;
}

.icon-small {
  width: 18px;
  height: 18px;
  min-width: 18px;
  fill: #16a085; 
}

.compact-date {
  flex: 1;
  padding: 10px 15px;
  border-radius: 12px;
  border: none; 
  background: white; 
  box-shadow: 0 2px 8px rgba(0,0,0,0.15); 
  font-family: 'Poppins', sans-serif;
}

.range-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  width: 100%; 
  gap: 10px; 
}

.range-btn {
  padding: 10px 20px;
  border-radius: 10px;
  font-weight: bold;
  border: none;
  cursor: pointer;
  flex: 1; 
}

.range-btn.add {
  background-color: #2980b9;
  color: white;
}

.range-btn.delete {
  background-color: #e74c3c;
  color: white;
}

.create-group-button { /* Used for the accept invitation button */
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  font-size: 1.2rem;
  padding: 15px 30px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  margin-top: 20px;
  font-family: 'Poppins', sans-serif;
  transition: 0.2s ease-in-out;
  width: 100%; /* Make button span full width */
}

.create-group-button:hover {
  transform: scale(1.03);
}
.recommend-button {
  border: 2px solid red;
}
</style>
