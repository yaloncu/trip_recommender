<template>
    <div class="join-container">
      <h1>Join a Group</h1>
      <form @submit.prevent="joinGroup">
        <div>
          <label for="group-id">Group ID:</label>
          <input type="number" v-model="groupId" id="group-id" required />
        </div>
  
        <div>
          <label for="email">Your Email:</label>
          <input type="email" v-model="email" id="email" required />
        </div>
  
        <button type="submit">Join Group</button>
      </form>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        groupId: '',  // ID del grupo al que unirse
        email: ''     // Email del usuario
      };
    },
    methods: {
      async joinGroup() {
        try {
          const response = await axios.post('/api/groups/join', {
            groupName: this.groupName,
            email: this.email
          });
          alert('Successfully joined the group!');
          this.$router.push('/groups'); // Redirigir después de unirse al grupo
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
    background-color: #191a1a;
    color: white;
    text-align: center;
  }
  
  form {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  label {
    margin-right: 10px;
  }
  
  button {
    margin-top: 20px;
    padding: 10px 20px;
    background-color: #4CAF50;
    color: white;
    border: none;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #45a049;
  }
  </style>
  