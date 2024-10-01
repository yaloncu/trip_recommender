<template>
  <div class="user-container">
    <button class="join-button" @click="joinGroup">{{ $t('join') }}</button>
    <button class="create-button" @click="createGroup">{{ $t('create') }}</button>

    <h1 class="main-title">Your groups</h1>
    <div class="card">
      <input v-model="userEmail" type="email" :placeholder="$t('enterYourEmail')" class="input email-input" />
      <button @click="fetchUserGroups" class="create-group-button">{{ $t('fetchGroups') }}</button>

      <div v-if="groups.length">
        <ul>
          <li v-for="group in groups" :key="group.groupId" class="group-item">
            {{ group.groupName }} - {{ $t('preference') }}: {{ group.preference || 'No preference' }}
            <button @click="viewRecommendation(group.groupId)" class="recommend-button">{{ $t('recomendation') }}</button>
            <button @click="leaveGroup(group.groupName)" class="leave-button">{{ $t('leaveGroup') }}</button>
            <button v-if="isAdmin(group.email)" @click="closeGroup(group.groupName)" class="close-button">{{ $t('closeGroup') }}</button>
          </li>
        </ul>
      </div>
      <div v-else>
        <p>{{ $t('notBelongGroup') }}</p>
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
    fetchGroups() {
      axios.get('/api/groups')
          .then(response => {
              this.groups = response.data; 
          })
          .catch(error => {
              console.error("Error fetching groups:", error);
          });
    },
    joinGroup() {
      this.$router.push('/groups');
    },
    createGroup() {
      this.$router.push('/groups/create');
    },
    isAdmin(groupEmail) {
      console.log("Admin Email:", groupEmail);
      console.log("User Email:", this.userEmail);
      return groupEmail === this.userEmail;
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
    async closeGroup(groupName) {
      if (!confirm(`Are you sure you want to close the group ${groupName}?`)) {
        return;
      }
      try {
        const response = await axios.put(`/api/groups/close/${groupName}`);
        alert(response.data.message);
        this.fetchUserGroups(); 
      } catch (error) {
        console.error('Error closing group:', error);
        alert('Error al cerrar el grupo.');
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
    },
    async leaveGroup(groupName) {
      if (!confirm(`Are you sure you want to leave the group ${groupName}?`)) {
        return;
      }
      try {
        const response = await axios.delete(`/api/groups/leave`, {
          data: { email: this.userEmail, groupName: groupName }
        });
        alert(response.data.message);
        this.fetchUserGroups();
      } catch (error) {
        console.error('Error leaving group:', error);
        alert('Error al salir del grupo.');
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

.leave-button {
  margin-left: 10px;
  padding: 5px 10px;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.leave-button:hover {
  background-color: #c0392b;
}

</style>
