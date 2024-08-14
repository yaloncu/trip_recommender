<!-- src/components/Groups.vue -->
<template>
    <div class="groups-container">
      <h1>My Groups</h1>
      <div class="options">
        <button @click="createGroup">Create New Group</button>
        <button @click="joinGroup">Join a Group</button>
      </div>
      <div v-if="groups.length">
        <h2>Your Groups:</h2>
        <ul>
          <li v-for="group in groups" :key="group.id">
            {{ group.name }}
            <button @click="goToVoting(group.id)">Go to Voting</button>
        </li>
        </ul>
      </div>
      <div v-else>
        <p>You don't belong to any group yet.</p>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        groups: [] 
      };
    },
    methods: {
      createGroup() {
        this.$router.push('/create-group');
      },
      joinGroup() {
        this.$router.push('/join-group');
      }
    },
    mounted() {
      // cargar los grupos
    }
  };
  </script>
  
  <style scoped>
  .groups-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100vh;
    background-color: #191a1a;
    color: white;
    text-align: center;
  }
  
  .options {
    margin-top: 20px;
  }
  
  button {
    margin: 10px;
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
  