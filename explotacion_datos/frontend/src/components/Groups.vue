<template>
  <div class="groups-container">
    <button class="create-button" @click="createGroup">{{ $t('create') }}</button>

    <div class="profile-button-wrapper">
      <div class="profile-button-container">
        <a title="Go to profile page" @click="viewMyGroups" class="profile-button">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 profile-icon" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"></path>
          </svg>
        </a>
      </div>
    </div>

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

          <input type="date" v-model="departureFrom" class="filter-select" :placeholder="$t('departureFrom')" />
          <input type="date" v-model="departureTo" class="filter-select" :placeholder="$t('returnTo')" />
        </div>


        <ul class="group-list">
          <li v-for="group in filteredGroups" :key="group.name" class="group-item">
            <span class="group-name group-title">{{ group.name }}</span>
            <span class="group-name group-audience">
              <svg xmlns="http://www.w3.org/2000/svg" class="icon-group" viewBox="0 0 24 24" fill="#16a085" width="20" height="20" style="vertical-align: middle; margin-right: 4px;">
                <path d="M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5s-3 1.34-3 3 1.34 3 3 3zm-8 0c1.66 0 3-1.34 3-3S9.66 5 8 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z"/>
              </svg>
              {{ group.audience }}
            </span>
            <p class="group-name">
              <svg xmlns="http://www.w3.org/2000/svg" class="icon-group" viewBox="0 0 24 24" fill="#16a085" width="20" height="20" style="vertical-align: middle; margin-right: 4px;">
                <path d="M12 2a10 10 0 100 20 10 10 0 000-20zm3.5 7.5l-2 5.5-5.5 2 2-5.5 5.5-2z"/>
              </svg>
              {{ group.tripType }}
            </p>

            <div class="group-dates">
              <span class="date-icon" title="Departure date">
                <svg xmlns="http://www.w3.org/2000/svg" class="icon" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M2.5 19h19v2h-19zM21.4 9.5l-9-3.5-3-7h-2l2.3 6-5.7-2.2-.8 2 7.6 3-2.2 5.8 1.9.7 2.2-5.8 8.1 3.1z"/>
                </svg>
                {{ group.departureDate ? new Date(group.departureDate).toLocaleDateString() : 'N/A' }}
              </span>
              <span class="date-icon" title="Return date">
                <svg xmlns="http://www.w3.org/2000/svg" class="icon" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M2.5 19h19v2h-19zM21.4 9.5l-9 3.5-3 7h-2l2.3-6-5.7 2.2-.8-2 7.6-3-2.2-5.8 1.9-.7 2.2 5.8 8.1-3.1z"/>
                </svg>
                {{ group.returnDate ? new Date(group.returnDate).toLocaleDateString() : 'N/A' }}
              </span>
            </div>
            <button class="join-button" @click="viewGroupDetails(group.name)">{{ $t('viewGroup') }}</button>
          </li>
        </ul>
      </div>
      <div v-else>
        <p>{{ $t('noGroupsAvailable') }}</p>
      </div>
    </div>
    <button class="button" @click="joinSpecificGroup">{{ $t('joinSpecificGroup') }}</button>
    <button class="chatbot-button" @click="toggleChatbot">
      💬
    </button>

    <div v-if="isChatbotVisible" class="chatbot-container">
      <Chatbot />
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import Chatbot from "@/components/Chatbot.vue";
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
    Chatbot,
  },
};
</script>


<style scoped>
.chatbot-button {
  display: none; 
}

.chatbot-container {
  position: fixed;
  bottom: 100px;
  right: 20px;
  width: 400px;
  height: 500px;
  background: white;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  overflow: hidden;
  z-index: 1000;
}

.groups-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #2c3e50;
  background-image: url('@/assets/aviones.png');
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  color: white;
  font-family: 'Poppins', sans-serif;
  min-height: 100vh;
  padding-bottom: 100px;
  overflow-y: auto;
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
  width: 90%;
  background: #34495e;
  border-radius: 30px;
  padding: 30px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 30px 30px -20px;
  font-family: 'Poppins', sans-serif;
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
  padding-left: 45px; /* Asegura el espacio para la lupa */
  padding-right: 15px; /* Buen padding a la derecha */
  background-color: #ecf0f1;
  color: #34495e;
  border-radius: 10px;
  border: 2px solid #1abc9c;
  font-size: 1rem;
  height: 40px;
  box-sizing: border-box;
  width: 100%; /* Ajusta el ancho según tu diseño */
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
