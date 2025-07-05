<template>
  <div class="page-with-menu">
    <SideMenu />

    <main class="main-content" role="main" aria-labelledby="activitiesTitle">
      <div class="groups-container">
        <h1 class="main-title" id="activitiesTitle">{{ $t('publicActivities') }}</h1>

        <section class="groups-content">
          <form class="filters" role="search" aria-label="{{ $t('filterActivities') }}">
            <label for="searchTitle" class="sr-only">{{ $t('searchByTitle') }}</label>
            <input
              id="searchTitle"
              type="text"
              v-model="searchQuery"
              :placeholder="$t('searchByTitle')"
              class="filter-select search-input"
              :aria-label="$t('searchByTitle')"
            />

            <label for="searchLocation" class="sr-only">{{ $t('location') }}</label>
            <input
              id="searchLocation"
              type="text"
              v-model="selectedLocation"
              :placeholder="$t('location')"
              class="filter-select search-input"
              :aria-label="$t('location')"
            />

            <label for="selectType" class="sr-only">{{ $t('allTypes') }}</label>
            <select id="selectType" v-model="selectedType" class="filter-select" :aria-label="$t('allTypes')">
              <option value="">{{ $t('allTypes') }}</option>
              <option value="Aventura">{{ $t('Aventura') }}</option>
              <option value="Cultural">{{ $t('Cultural') }}</option>
              <option value="Playa">{{ $t('Playa') }}</option>
              <option value="Romántica">{{ $t('Romántica') }}</option>
              <option value="Relax">{{ $t('Relax') }}</option>
              <option value="Gastronómica">{{ $t('Gastronómica') }}</option>
              <option value="Bienestar">{{ $t('Bienestar') }}</option>
              <option value="Montaña">{{ $t('Montaña') }}</option>
            </select>

            <label for="startFrom" class="sr-only">{{ $t('startDateFrom') }}</label>
            <input id="startFrom" type="date" v-model="startDateFrom" class="filter-select" :aria-label="$t('startDateFrom')" />

            <label for="startTo" class="sr-only">{{ $t('startDateTo') }}</label>
            <input id="startTo" type="date" v-model="startDateTo" class="filter-select" :aria-label="$t('startDateTo')" />
          </form>

          <div v-if="activities.length">
            <ul class="group-list">
              <li v-for="activity in filteredActivities" :key="activity.id" class="group-item" :aria-label="`${activity.title}, ${$t(activity.type)}, ${activity.location}`">
                <span class="group-title">{{ activity.title }}</span>

                <p class="group-name">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-landmark-icon" aria-hidden="true">
                    <path d="M10 18v-7" />
                    <path d="M11.12 2.198a2 2 0 0 1 1.76.006l7.866 3.847c.476.233.31.949-.22.949H3.474c-.53 0-.695-.716-.22-.949z" />
                    <path d="M14 18v-7" />
                    <path d="M18 18v-7" />
                    <path d="M3 22h18" />
                    <path d="M6 18v-7" />
                  </svg>
                  {{ $t(activity.type) }}
                </p>

                <p class="group-name">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-map-pin-icon" aria-hidden="true">
                    <path d="M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0" />
                    <circle cx="12" cy="10" r="3" />
                  </svg>
                  {{ activity.location }}
                </p>

                <div class="group-dates">
                  <p class="group-name">
                    <span class="date-icon" :title="$t('startDate')">
                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-calendar-icon" aria-hidden="true">
                        <path d="M8 2v4" />
                        <path d="M16 2v4" />
                        <rect width="18" height="18" x="3" y="4" rx="2" />
                        <path d="M3 10h18" />
                      </svg>
                      {{ new Date(activity.startDateTime).toLocaleDateString() }}
                    </span>
                  </p>

                  <p class="group-name">
                    <span class="date-icon" :title="$t('startTime')">
                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-clock-icon" aria-hidden="true">
                        <circle cx="12" cy="12" r="10" />
                        <polyline points="12 6 12 12 16 14" />
                      </svg>
                      {{ new Date(activity.startDateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) }}
                    </span>
                  </p>
                </div>

                <button class="join-button" @click="joinActivity(activity.id)" :aria-label="`${$t('joinActivity')}: ${activity.title}`">
                  {{ $t('joinActivity') }}
                </button>
              </li>
            </ul>
          </div>

          <div v-else>
            <p>{{ $t('noActivitiesAvailable') }}</p>
          </div>
        </section>
      </div>
    </main>
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
      searchQuery: '',
      selectedType: '',
      selectedLocation: '',
      startDateFrom: '',
      startDateTo: '',
    };
  },
  computed: {
    filteredActivities() {
      return this.activities.filter(activity => {
        const matchTitle = activity.title.toLowerCase().includes(this.searchQuery.toLowerCase());
        const matchType = this.selectedType ? activity.type === this.selectedType : true;
        const matchLocation = activity.location.toLowerCase().includes(this.selectedLocation.toLowerCase());

        const activityDate = new Date(activity.startDateTime);
        const fromDate = this.startDateFrom ? new Date(this.startDateFrom) : null;
        const toDate = this.startDateTo ? new Date(this.startDateTo + 'T23:59:59') : null;

        const matchDateFrom = fromDate ? activityDate >= fromDate : true;
        const matchDateTo = toDate ? activityDate <= toDate : true;

        return matchTitle && matchType && matchLocation && matchDateFrom && matchDateTo;
      });
    }
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
    joinActivity(activityId) {
      const userEmail = localStorage.getItem('email');
      if (!userEmail) {
        alert('Debes iniciar sesión para inscribirte.');
        return;
      }

      axios.post(`/api/activities/join/${activityId}`, { email: userEmail })
        .then(() => {
          alert('Te has inscrito correctamente en la actividad.');
        })
        .catch(error => {
          console.error('Error al inscribirse:', error);
          alert('No se pudo completar la inscripción.');
        });
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
  background-image: url('@/assets/mapamundi.png');
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

.filters {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 20px;
  font-family: 'Poppins', sans-serif;
}

.filter-select {
  padding: 10px;
  font-size: 1rem;
  border-radius: 10px;
  border: 2px solid #1abc9c;
  background-color: #34495e;
  color: white;
  font-family: 'Poppins', sans-serif;
  box-sizing: border-box;
}

.filter-select option {
  background-color: #2c3e50;
}

.filter-select[type="date"] {
  background-color: #ecf0f1;
  color: #34495e;
  padding: 10px;
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
