<template>
  <div class="user-container">
    <button class="join-button" @click="joinGroup">Join</button>
    <button class="create-button" @click="createGroup">Create</button>

    <h1 class="main-title">Your groups</h1>
    <div class="card">
      <input v-model="userEmail" type="email" placeholder="Enter your email" class="input email-input" />
      <button @click="fetchUserGroups" class="create-group-button">Fetch Groups</button>

      <div v-if="groups.length">
        <ul>
          <li v-for="group in groups" :key="group.groupId" class="group-item">
            {{ group.groupName }} - Preference: {{ group.preference }}
            <button @click="viewRecommendation(group.groupId)" class="recommend-button">Ver Recomendación</button>
          </li>
        </ul>
      </div>
      <div v-else>
        <p>You don't belong to any group</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      userEmail: '', 
      groups: [] 
    };
  },
  methods: {
    joinGroup() {
      this.$router.push('/groups');
    },
    createGroup() {
      this.$router.push('/groups/create');
    },
    async fetchUserGroups() {
      if (!this.userEmail) {
        return;
      }
      
      try {
        const response = await axios.post(`/api/groups/user`, {
          email: this.userEmail 
        });
        this.groups = response.data;
      } catch (error) {
        console.error('Error fetching groups:', error);
        alert('Error loading your groups.');
      }
    },
    async viewRecommendation(groupId) {
      try {
        const response = await axios.get(`/api/recommendations/${groupId}`);
        const recommendations = response.data;

        if (Array.isArray(recommendations) && recommendations.length > 0) {
          alert(`Recomendaciones: ${recommendations.join(', ')}`);
        } else {
          alert('No hay recomendaciones disponibles.');
        }
      } catch (error) {
        console.error('Error fetching recommendations:', error);
        alert('Error al obtener las recomendaciones.');
      }
    }
  },
  mounted() {
    this.fetchUserGroups();
  }
};
</script>

<style scoped>
.user-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #2c3e50; 
  color: white;
  text-align: center;
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

.create-group-button{
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

.create-button {
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
}

.create-button:hover {
  transform: scale(1.03);
}

.create-button:active {
  transform: scale(0.95);
}

.main-title {
  color: #1abc9c;
  font-size: 2rem;
  margin-top: 60px; 
  margin-bottom: 20px;
}

.card {
  max-width: 400px;
  width: 100%;
  background: #34495e;
  border-radius: 20px;
  padding: 30px 40px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 10px 20px;
}

.input {
  width: 100%;
  background: white;
  border: none;
  padding: 15px 20px;
  border-radius: 20px;
  margin-top: 15px;
  box-shadow: #cff0ff 0px 10px 10px -5px;
  border-inline: 2px solid transparent;
  box-sizing: border-box;
}

.input:focus {
  outline: none;
  border-inline: 2px solid #1abc9c;
}

ul {
  list-style-type: none;
  padding: 0;
  margin-top: 20px;
}

.group-item {
  margin-bottom: 10px;
  padding: 10px;
  background-color: #34495e;
  border-radius: 20px;
}

.recommend-button {
  margin-left: 10px;
  padding: 5px 10px;
  background-color: #38a169;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.recommend-button:hover {
  background-color: #2f855a;
}
</style>
