<template>
  <div class="voting-container">
    <h1>Vote for Your Preferred Vacation Types</h1>
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
    <button @click="submitVotes">Submit Votes</button>
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
        vacationTypes: [
          'Cultural', 'Beach', 'Romantic', 'Relax', 
          'Adventure', 'Gastronomy', 'Wellness', 'Mountain'
        ],
        selectedTypes: [],
        userId:''
      };
    },
    methods: {
      submitVotes() {
      }
    }
  };
  </script>
  
  <style scoped>
  .voting-container {
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
    margin: 20px 0;
  }
  
  label {
    display: block;
    margin: 10px 0;
  }
  
  input[type="checkbox"] {
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
  