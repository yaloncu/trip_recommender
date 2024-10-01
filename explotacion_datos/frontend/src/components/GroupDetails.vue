<template>
    <div class="group-details-container">
      <h1>{{ $t('group') }} {{ group.name }}</h1>
      <p><strong>{{ $t('audience') }}</strong> {{ group.audience }}</p>
      <p><strong>{{ $t('description') }}</strong> {{ group.description }}</p>
      
      <div v-if="!isMember">
        <input v-model="userEmail" placeholder="{{ $t('enterYourEmail') }}" class="email-input" />
        <button class="join-button" @click="joinGroup">{{ $t('joinAGroup') }}</button>
      </div>
      <div v-else>
        <p>{{ $t('alreadyMember') }}</p>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        group: {},
        userEmail: '',
        isMember: false
      };
    },
    methods: {
      async fetchGroupDetails() {
        const groupName = this.$route.params.groupName; 
        try {
          const response = await axios.get(`/api/groups/${groupName}`);
          this.group = response.data;
        } catch (error) {
          console.error('Error fetching group details:', error);
        }
      },
      joinGroup() {
        if (!this.validateEmail(this.userEmail)) {
          alert('Invalid email. Please enter a valid email address.');
          return;
        }
  
        axios.post('/api/groups/joinWithPreferences', {
          groupName: this.group.name,
          email: this.userEmail,
          preference: 'default'
        })
        .then(response => {
          alert(`Successfully joined group: ${this.group.name}`);
          this.isMember = true; 
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
    mounted() {
      this.fetchGroupDetails();
    }
  };
  </script>
  
  <style scoped>
  .group-details-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 40px;
    background-color: #34495e;
    color: white;
    border-radius: 10px;
    box-shadow: rgba(0, 0, 0, 0.1) 0px 20px 20px -15px;
  }
  
  .join-button {
    background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
    color: white;
    font-size: 1.2rem;
    padding: 10px 20px;
    border-radius: 10px;
    border: none;
    cursor: pointer;
    transition: all 0.2s ease-in-out;
  }
  
  .join-button:hover {
    transform: scale(1.03);
  }
  
  .email-input {
    padding: 10px;
    font-size: 1rem;
    margin-bottom: 20px;
    border: 2px solid #1abc9c;
    border-radius: 10px;
    width: 100%;
    box-sizing: border-box;
  }
  
  .email-input:focus {
    border-color: #16a085;
    outline: none;
  }
  </style>
  