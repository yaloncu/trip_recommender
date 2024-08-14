<template>
    <div class="create-group-container">
      <h1>Create New Group</h1>
      <form @submit.prevent="createGroup">
        <div>
          <label for="group-name">Group Name:</label>
          <input type="text" v-model="groupName" id="group-name" required />
        </div>
        
        <div>
          <h3>Select the audience:</h3>
          <div>
            <input type="radio" id="adults" value="Adults" v-model="audience" />
            <label for="adults">Adults</label>
          </div>
          <div>
            <input type="radio" id="youth" value="Youth" v-model="audience" />
            <label for="youth">Youth</label>
          </div>
          <div>
            <input type="radio" id="families" value="Families" v-model="audience" />
            <label for="families">Families</label>
          </div>
        </div>
  
        <button type="submit">Create Group</button>
      </form>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        groupName: '',
        audience: 'Adults' 
      };
    },
    methods: {
      async createGroup() {
        try {
          const response = await axios.post('/api/groups/create', {
            name: this.groupName,
            audience: this.audience
          });
          console.log('Group created successfully:', response.data);
          this.$router.push('/groups'); 
        } catch (error) {
          console.error('Error during group creation:', error);
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .create-group-container {
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
  
  input[type="radio"] {
    margin-right: 5px;
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
  