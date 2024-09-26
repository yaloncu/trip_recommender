<template>
  <div class="groups-container">
    <button class="create-button" @click="createGroup">Create</button>

    <div class="profile-button-wrapper">
      <div class="profile-button-container">
        <a title="Go to profile page" @click="viewMyGroups" class="profile-button">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 profile-icon" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"></path>
          </svg>
        </a>
      </div>
    </div>

    <h1 class="main-title">Join a Group</h1>

    <div class="groups-content">
      <div v-if="groups.length">
        <h2>Available Groups:</h2>
        <ul>
          <li v-for="group in groups" :key="group.id" class="group-item">
            <span>{{ group.name }}</span>
            <button class="join-button" @click="joinGroup(group.id)">Join Group</button>
          </li>
        </ul>
      </div>
      <div v-else>
        <p>No groups available at the moment.</p>
      </div>
    </div>
    <button class="button" @click="joinSpecificGroup">Join an especific group</button>
  </div>
</template>

<script>
import axios from 'axios';
axios.defaults.baseURL = 'http://localhost:8081';
axios.defaults.headers.common['Accept'] = 'application/json';
axios.defaults.headers.post['Content-Type'] = 'application/json';

export default {
  data() {
    return {
      groups: [] 
    };
  },
  methods: {
    createGroup() {
      this.$router.push('/groups/create');
    },
    viewMyGroups() {
      this.$router.push('/groups/user');
    },
    joinSpecificGroup() {
      this.$router.push('/groups/joinWithPreferences');
    },
    async fetchGroups() {
      try {
        const response = await axios.get('/api/groups');
        this.groups = response.data;
      } catch (error) {
        console.error('Error fetching groups:', error);
      }
    },
    joinGroup(groupId) {
      console.log('Joining group:', groupId);
    }
  },
  mounted() {
    this.fetchGroups(); // Al montar el componente, obtenemos los grupos
  }
};
</script>

<style scoped>
.groups-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #2c3e50; 
  color: white; 
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
}

.create-button:hover {
  transform: scale(1.03);
}

.create-button:active {
  transform: scale(0.95);
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

.w-5, .h-5 {
  width: 2rem; 
  height: 2rem;
  fill: white; 
}

.profile-icon {
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
  margin-bottom: 30px;
}

.groups-content {
  text-align: center;
  max-width: 600px;
  width: 100%;
  background: #34495e; 
  border-radius: 30px;
  padding: 30px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 30px 30px -20px;
}

ul {
  list-style: none;
  padding: 0;
}

.group-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 15px 0;
  padding: 15px;
  background-color: #ecf0f1; 
  border-radius: 20px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 6px;
}

.join-button {
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

.join-button:hover {
  transform: scale(1.03);
}

.join-button:active {
  transform: scale(0.95);
}
</style>
