<template>
  <div class="page-with-menu">
    <SideMenu />
    <div class="groups-container">

      <h1 class="main-title">{{ $t('joinAGroup') }}</h1>

      <div class="groups-content">
        <div v-if="groups.length">
          <div class="filters">
            <div class="search-input-wrapper">
              <svg xmlns="http://www.w3.org/2000/svg" class="search-icon" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
              </svg>
              <input
                type="text"
                v-model="searchQuery"
                :placeholder="$t('searchGroupByName')"
                class="search-input" />
            </div>

            <select v-model="selectedType" class="filter-select">
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

            <select v-model="selectedAudience" class="filter-select">
              <option value="">{{ $t('allAudiences') }}</option>
              <option value="Adultos">{{ $t('adults') }}</option>
              <option value="Jóvenes">{{ $t('youth') }}</option>
              <option value="Familias">{{ $t('families') }}</option>
              <option value="Tercera edad">{{ $t('3age') }}</option>
            </select>

            <input type="date" v-model="departureFrom" class="filter-select" :placeholder="$t('departureDate')" />
            <input type="date" v-model="departureTo" class="filter-select" :placeholder="$t('returnDate')" />
          </div>

          <ul class="group-list">
            <li v-for="group in filteredGroups" :key="group.name" class="group-item">
              <span class="group-name group-title">{{ group.name }}</span>
              <span class="group-name group-audience">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                  viewBox="0 0 24 24" fill="none" stroke="currentColor"
                  stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                  class="lucide lucide-users-icon">
                  <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
                  <path d="M16 3.128a4 4 0 0 1 0 7.744"/>
                  <path d="M22 21v-2a4 4 0 0 0-3-3.87"/>
                  <circle cx="9" cy="7" r="4"/>
                </svg>
                {{ $t(group.audience) }}
              </span>
              <p class="group-name">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                  viewBox="0 0 24 24" fill="none" stroke="currentColor"
                  stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                  class="lucide lucide-compass-icon">
                  <path d="m16.24 7.76-1.804 5.411a2 2 0 0 1-1.265 1.265L7.76 16.24l1.804-5.411a2 2 0 0 1 1.265-1.265z"/>
                  <circle cx="12" cy="12" r="10"/>
                </svg>
                {{ group.tripType ? $t(group.tripType) : $t('unknownTripType') }}
              </p>

              <div class="group-dates">
                <span class="date-icon" :title="$t('departureDate')">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                    viewBox="0 0 24 24" fill="none" stroke="currentColor"
                    stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                    class="lucide lucide-plane-takeoff-icon">
                    <path d="M2 22h20"/>
                    <path d="M6.36 17.4 4 17l-2-4 1.1-.55a2 2 0 0 1 1.8 0l.17.1a2 2 0 0 0 1.8 0L8 12 5 6l.9-.45a2 2 0 0 1 2.09.2l4.02 3a2 2 0 0 0 2.1.2l4.19-2.06a2.41 2.41 0 0 1 1.73-.17L21 7a1.4 1.4 0 0 1 .87 1.99l-.38.76c-.23.46-.6.84-1.07 1.08L7.58 17.2a2 2 0 0 1-1.22.18Z"/>
                  </svg>
                  {{ group.departureDate ? new Date(group.departureDate).toLocaleDateString() : 'N/A' }}
                </span>
                <span class="date-icon" :title="$t('returnDate')">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                    viewBox="0 0 24 24" fill="none" stroke="currentColor"
                    stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                    class="lucide lucide-plane-landing-icon">
                    <path d="M2 22h20"/>
                    <path d="M3.77 10.77 2 9l2-4.5 1.1.55c.55.28.9.84.9 1.45s.35 1.17.9 1.45L8 8.5l3-6 1.05.53a2 2 0 0 1 1.09 1.52l.72 5.4a2 2 0 0 0 1.09 1.52l4.4 2.2c.42.22.78.55 1.01.96l.6 1.03c.49.88-.06 1.98-1.06 2.1l-1.18.15c-.47.06-.95-.02-1.37-.24L4.29 11.15a2 2 0 0 1-.52-.38Z"/>
                  </svg>
                  {{ group.returnDate ? new Date(group.returnDate).toLocaleDateString() : 'N/A' }}
                </span>
              </div>

              <button class="join-button" @click="viewGroupDetails(group.name)">
                {{ $t('viewGroup') }}
              </button>
            </li>
          </ul>
        </div>
        <div v-else>
          <p>{{ $t('noGroupsAvailable') }}</p>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import axios from 'axios';
import SideMenu from '@/components/SideMenu.vue';
axios.defaults.baseURL = 'http://localhost:8081';
axios.defaults.headers.common['Accept'] = 'application/json';
axios.defaults.headers.post['Content-Type'] = 'application/json';

export default {
  data() {
    return {
      groups: [],
      isChatbotVisible: false,
      selectedType: '',
      selectedAudience: '',
      departureFrom: '',
      departureTo: '',
      searchQuery: '',
    };
  },
  methods: {
    toggleChatbot() {
      this.isChatbotVisible = !this.isChatbotVisible;
    },
    createGroup() {
      this.$router.push('/groups/create');
    },
    viewMyGroups() {
      this.$router.push('/groups/user');
    },
/*************  ✨ Windsurf Command ⭐  *************/
/**
 * Navigates the user to the join with preferences page where they can join a specific group
 * by providing preferences such as trip type, audience, and dates.
 */

/*******  9069768d-bf68-4ef5-b8c9-219307a482f7  *******/
    joinSpecificGroup() {
      this.$router.push('/groups/joinWithPreferences');
    },
    viewGroupDetails(groupName) {
      this.$router.push({ name: 'GroupDetails', params: { groupName } });
    },
    async fetchGroups() {
      try {
        const response = await axios.get('/api/groups/public');
        console.log('Groups fetched:', response.data); 
        this.groups = response.data;
      } catch (error) {
        console.error('Error fetching groups:', error);
      }
    },
    joinGroup(groupName) {
      let userEmail = prompt("Please enter your email:");
      if (!userEmail || !this.validateEmail(userEmail)) {
        alert('Invalid email. Please enter a valid email address.');
        return;
      }
      axios.post('/api/groups/joinWithPreferences', {
        groupName: groupName,
        email: userEmail,
        preference: 'default' 
      })
      .then(response => {
        alert(`Successfully joined group: ${groupName}`);
      })
      .catch(error => {
        console.error('Error joining group:', error);
        alert('Failed to join group');
      });
    },
    validateEmail(email) {
      const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return re.test(String(email).toLowerCase());
    }
  },
  computed: {
    filteredGroups() {
      const lowerCaseSearchQuery = this.searchQuery.toLowerCase();
      return this.groups.filter(group => {
        const matchName = group.name.toLowerCase().includes(lowerCaseSearchQuery);
        if (this.searchQuery && !matchName) {
          return false;
        }

        const matchType = this.selectedType ? group.tripType === this.selectedType : true;
        const matchAudience = this.selectedAudience ? group.audience === this.selectedAudience : true;

        const matchDepartureDates = (() => {
          const groupDepartureDate = group.departureDate ? new Date(group.departureDate) : null;
          const groupReturnDate = group.returnDate ? new Date(group.returnDate) : null;

          const filterDepartureFrom = this.departureFrom ? new Date(this.departureFrom) : null;
          const filterDepartureTo = this.departureTo ? new Date(this.departureTo) : null;
          if (filterDepartureTo) {
            filterDepartureTo.setHours(23, 59, 59, 999);
          }

          let passDepartureFrom = true;
          if (filterDepartureFrom) {
            passDepartureFrom = groupDepartureDate && groupDepartureDate >= filterDepartureFrom;
          }

          let passDepartureTo = true;
          if (filterDepartureTo) {
            passDepartureTo = groupReturnDate && groupReturnDate <= filterDepartureTo;
          }
          
          if ((filterDepartureFrom && !groupDepartureDate) || (filterDepartureTo && !groupReturnDate)) {
              return false;
          }

          return passDepartureFrom && passDepartureTo;
        })();

        return matchName && matchType && matchAudience && matchDepartureDates;
      });
    }
  },
  mounted() {
    this.fetchGroups(); 
  },
  components: {
    SideMenu,
  },
};
</script>


<style scoped>
.page-with-menu {
  display: flex;
  width: 100vw;
  overflow-x: hidden;
}

.main-content {
  margin-left: 220px;
  width: calc(100% - 220px);
}


.groups-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #2c3e50;
  background-image: url('@/assets/mapamundi.png');
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  color: white;
  font-family: 'Poppins', sans-serif;
  min-height: 100vh;
  padding-bottom: 100px;
  overflow-y: auto;
  width: 100%;
  overflow-x: hidden;
}


.create-button {
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
}

.create-button:hover {
  transform: scale(1.03);
}

.create-button:active {
  transform: scale(0.95);
}

.group-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); 
  gap: 20px;
  padding: 0;
  margin: 0 auto;
  width: 100%;
  list-style: none;
}

.group-item {
  display: flex;
  flex-direction: column;
  margin: 0;
  padding: 15px;
  background-color: #ecf0f1;
  border-radius: 20px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 6px;
  color: #34495e;
  font-family: 'Poppins', sans-serif;
}

.group-dates {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  gap: 10px;
  font-weight: bold;
  color: #34495e;
  font-family: 'Poppins', sans-serif;
}

.date-icon {
  display: flex;
  align-items: center;
  gap: 5px;
}

.date-icon .icon {
  width: 20px;
  height: 20px;
  fill: #16a085;
}

.icon-group {
  width: 18px;
  height: 18px;
  margin-right: 5px;
  vertical-align: middle;
}

.button {
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  font-size: 1.5rem;
  padding: 20px 40px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 20px 10px -15px;
  margin-top: 20px;
  font-family: 'Poppins', sans-serif;
}

.button:hover {
  transform: scale(1.03);
}

.button:active {
  transform: scale(0.95);
}

.profile-button-wrapper {
  position: absolute;
  top: 20px;
  right: 20px;
}

.profile-button-container {
  background-color: #16a085;
  padding: 20px;
  border-radius: 50%;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 8px;
  transition: all 0.2s ease-in-out;
}

.profile-button {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent;
  border: none;
  cursor: pointer;
  border-radius: 50%;
  width: 100%;
  height: 100%;
  transition: background-color 0.2s ease-in-out;
}


.w-5, .h-5, .profile-icon {
  width: 2rem;
  height: 2rem;
  fill: white;
}

.profile-button-container:hover {
  background-color: #1abc9c;
}

.profile-button-container:active {
  background-color: #148f77;
  transform: scale(0.95);
  box-shadow: inset 0px 6px 8px rgba(0, 0, 0, 0.15);
}

.main-title {
  color: #1abc9c;
  font-size: 2rem;
  margin: 80px 0 30px 0;
  font-family: 'Poppins', sans-serif;
}

.groups-content {
  text-align: center;
  max-width: 1200px;
  width: 100%;
  background: #34495e;
  border-radius: 30px;
  padding: 30px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 30px 30px -20px;
  font-family: 'Poppins', sans-serif;
  box-sizing: border-box;
  margin: 0 auto;
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.group-item {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 15px 0;
  padding: 15px;
  background-color: #ecf0f1;
  border-radius: 20px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 6px;
  color: #34495e;
  font-family: 'Poppins', sans-serif;
}

.group-name {
  color: #34495e;
  font-weight: bold;
}

.group-title {
  margin-bottom: 10px;
}

.group-audience {
  margin-bottom: 6px;
}


.join-button {
  align-self: flex-end;
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  font-size: 1rem;
  padding: 10px 20px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  margin-top: 10px;
  transition: all 0.2s ease-in-out;
  font-family: 'Poppins', sans-serif;
}

.join-button:hover {
  transform: scale(1.03);
}

.join-button:active {
  transform: scale(0.95);
}

.button {
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  font-size: 1.2rem;
  padding: 15px 30px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  margin-top: 30px;
  transition: all 0.2s ease-in-out;
  font-family: 'Poppins', sans-serif;
}

.button:hover {
  transform: scale(1.03);
}

.button:active {
  transform: scale(0.95);
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

.search-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 15px;
  width: 20px;
  height: 20px;
  fill: #34495e;
  pointer-events: none;
  z-index: 1;
}

.search-input {
  padding-left: 45px; 
  padding-right: 15px; 
  background-color: #ecf0f1;
  color: #34495e;
  border-radius: 10px;
  border: 2px solid #1abc9c;
  font-size: 1rem;
  height: 40px;
  box-sizing: border-box;
  width: 100%; 
}

.chatBtn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 55px;
  height: 55px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  border: none;
  background-color: #B9FBC0;
  background-image: linear-gradient(147deg, #A7FF83, #16a085, #1abc9c, #A7FF83);
  cursor: pointer;
  padding-top: 3px;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.164);
  background-size: 300%;
  background-position: left;
  transition-duration: 1s;
  z-index: 1000;
}

.chatBtn:hover {
  background-position: right;
  transition-duration: 1s;
}

.tooltip {
  position: absolute;
  top: -40px;
  opacity: 0;
  background-color: rgb(255, 180, 82);
  color: white;
  padding: 5px 10px;
  border-radius: 5px;
  font-family: 'Poppins', sans-serif;
  display: flex;
  align-items: center;
  justify-content: center;
  transition-duration: .5s;
  pointer-events: none;
  letter-spacing: 0.5px;
}

.chatBtn:hover .tooltip {
  opacity: 1;
  transition-duration: .5s;
}

button,
.join-button,
.create-button,
.button {
  font-family: 'Poppins', sans-serif !important;
}

</style>
