<template>
  <div class="user-container">
    <button class="join-button" @click="joinGroup">Join</button>
    <div class="profile-button-wrapper">
      <div class="profile-button-container">
        <a title="Go to profile page" @click="viewMyGroups" class="profile-button">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 profile-icon" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"></path>
          </svg>
        </a>
      </div>
    </div>
    <h1 class="main-title">Create a group</h1>

    <div class="card">
      <input v-model="groupName" type="text" placeholder="Enter the group name" class="input name-group" />

      <div>
        <h3>Select the audience:</h3>
        <div class="customCheckBoxHolder">
          <input class="customCheckBoxInput" id="adults" type="radio" value="Adultos" v-model="audience">
          <label class="customCheckBoxWrapper" for="adults">
            <div class="customCheckBox">
              <div class="inner">Adults</div>
            </div>
          </label>

          <input class="customCheckBoxInput" id="youth" type="radio" value="Jóvenes" v-model="audience">
          <label class="customCheckBoxWrapper" for="youth">
            <div class="customCheckBox">
              <div class="inner">Youth</div>
            </div>
          </label>

          <input class="customCheckBoxInput" id="families" type="radio" value="Familias" v-model="audience">
          <label class="customCheckBoxWrapper" for="families">
            <div class="customCheckBox">
              <div class="inner">Families</div>
            </div>
          </label>
        </div>
      </div>

      
      <div>
        <h3>Select the privacity:</h3>
        <div class="customCheckBoxHolder">
          <input class="customCheckBoxInput" id="private" type="radio" value="private" v-model="privated">
          <label class="customCheckBoxWrapper" for="private">
            <div class="customCheckBox">
              <div class="inner">Private</div>
            </div>
          </label>

          <input class="customCheckBoxInput" id="public" type="radio" value="public" v-model="privated">
          <label class="customCheckBoxWrapper" for="public">
            <div class="customCheckBox">
              <div class="inner">Public</div>
            </div>
          </label>
        </div>
      </div>

        <button @click="createGroup" class="create-group-button">create group</button>


    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      groupName: '', 
      audience: '', 
      Privated: '', 
    };
  },
  methods: {
    joinGroup() {
      this.$router.push('/groups');
    },
    viewMyGroups() {
      this.$router.push('/groups/user');
    },
    async createGroup() {
      try {
        const response = await axios.post('/api/groups/create', {
          name: this.groupName,
          audience: this.audience,
          privated: this.privated
        });
        this.$router.push('/groups');
      } catch (error) {
        console.error('Error during group creation:', error);
        if (error.response) {
          console.error('Error response data:', error.response.data);
        } else if (error.request) {
          console.error('Error request:', error.request);
        } else {
          console.error('Error message:', error.message);
        }
        alert('Failed to create group. Please try again.');
      }
    }
  },
  mounted() {
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

.join-button, .create-button {
  position: absolute;
  top: 20px;
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

.join-button {
  left: 20px;
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

.create-group-button {
  display: block;
  width: 100%;
  font-weight: bold;
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  padding-block: 15px;
  margin-top: 20px;
  border-radius: 20px;
  border: none;
  transition: all 0.2s ease-in-out;
}

.create-group-button:hover {
  background: #1abc9c;
  transform: scale(1.03);
}

.create-group-button:active {
  transform: scale(0.95);
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

.customCheckBoxHolder {
  margin: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.customCheckBox {
  width: fit-content;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  user-select: none;
  padding: 2px 8px;
  background-color: rgba(0, 0, 0, 0.16);
  border-radius: 0px;
  color: rgba(255, 255, 255, 0.7);
  transition-timing-function: cubic-bezier(0.25, 0.8, 0.25, 1);
  transition-duration: 300ms;
  transition-property: color, background-color, box-shadow;
  display: flex;
  height: 32px;
  align-items: center;
  box-shadow: rgba(0, 0, 0, 0.15) 0px 2px 1px 0px inset, rgba(255, 255, 255, 0.17) 0px 1px 1px 0px;
  outline: none;
  justify-content: center;
  min-width: 55px;
}

.customCheckBox:hover {
  background-color: #2c2c2c;
  color: white;
  box-shadow: rgba(0, 0, 0, 0.23) 0px -4px 1px 0px inset, rgba(255, 255, 255, 0.17) 0px -1px 1px 0px, rgba(0, 0, 0, 0.17) 0px 2px 4px 1px;
}

.customCheckBox .inner {
  font-size: 18px;
  font-weight: 900;
  pointer-events: none;
  transition-timing-function: cubic-bezier(0.25, 0.8, 0.25, 1);
  transition-duration: 300ms;
  transition-property: transform;
  transform: translateY(0px);
}

.customCheckBox:hover .inner {
  transform: translateY(-2px);
}

.customCheckBoxInput {
  display: none;
}

.customCheckBoxInput:checked + .customCheckBoxWrapper .customCheckBox {
  background-color: #2d6737;
  color: white;
  box-shadow: rgba(0, 0, 0, 0.23) 0px -4px 1px 0px inset, rgba(255, 255, 255, 0.17) 0px -1px 1px 0px, rgba(0, 0, 0, 0.17) 0px 2px 4px 1px;
}

.customCheckBoxInput:checked + .customCheckBoxWrapper .customCheckBox:hover {
  background-color: #34723f;
}

</style>
