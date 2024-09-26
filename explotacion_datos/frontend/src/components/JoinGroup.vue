<template>
  <div class="join-container">
    <button class="create-button" @click="back">Back</button>

    <h1>Join a Group</h1>
    <form @submit.prevent="joinGroupWithPreferences">
      <div>
        <label for="group-name">Group name:</label>
        <input type="text" v-model="groupName" id="group-name" required />
      </div>

      <div>
        <label for="email">Your Email:</label>
        <input type="email" v-model="email" id="email" required />
      </div>

      <div class="options">
        <label v-for="type in vacationTypes" :key="type">
          <input 
            type="checkbox" 
            :value="type" 
            v-model="selectedTypes" 
          />
          {{ type }}
        </label>
      </div>

      <button type="submit" class="join-button">Join Group</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      groupName: '',  
      email: '',  
      vacationTypes: [
        'Cultural', 'Playa', 'Romantica', 'Relax', 
        'Aventura', 'Gastronómica', 'Bienestar', 'Montaña'
      ],
      selectedTypes: []   
    };
  },
  methods: {
    back() {
      this.$router.push('/groups');
    },
    async joinGroupWithPreferences() {
      if (this.selectedTypes.length === 0) {
        alert('Please select at least one vacation type.');
        return;
      }
      try {
        await axios.post('/api/groups/joinWithPreferences', {
          groupName: this.groupName,
          email: this.email,
          preference: this.selectedTypes[0]
        });

        this.$router.push('/groups'); 
      } catch (error) {
        console.error('Error joining group:', error);
        if (error.response) {
          alert(`Failed to join group: ${error.response.data.message}`);
        } else {
          alert('Failed to join group. Please try again.');
        }
      }
    }
  }
};
</script>

<style scoped>
.join-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #2c3e50; 
  color: white;
  text-align: center;
}

form {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

label {
  margin-right: 10px;
}

input[type="text"],
input[type="email"],
input[type="number"] {
  margin-top: 10px;
  padding: 10px;
  border-radius: 20px;
  border: 2px solid #1abc9c; 
  background-color: #34495e; 
  color: white;
  width: 100%;
}

input[type="text"]::placeholder,
input[type="email"]::placeholder,
input[type="number"]::placeholder {
  color: rgb(170, 170, 170);
}

input[type="text"]:focus,
input[type="email"]:focus,
input[type="number"]:focus {
  outline: none;
  border: 2px solid #1abc9c; 
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

.join-button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #38a169; 
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.join-button:hover {
  background-color: #2f855a; 
}
</style>
