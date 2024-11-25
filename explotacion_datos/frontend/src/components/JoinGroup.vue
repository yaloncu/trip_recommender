<template>
  <div class="join-container">
    <button class="create-button" @click="back">{{ $t('back') }}</button>
    
    <h1 class="main-title">{{ $t('joinAGroup') }}</h1>
    
    <div class="card">
      <input 
        v-model="groupName" 
        type="text" 
        :placeholder="$t('enterGroupName')"  
        class="input name-group" 
      />
      <input 
        v-model="email" 
        type="email" 
        :placeholder="$t('enterYourEmail')"  
        class="input name-group" 
      />

      <div>
        <h3>{{ $t('selectType') }}</h3>
        <div class="customCheckBoxHolder">
          <label v-for="(type, index) in vacationTypes" :key="index">
            <input 
              type="radio" 
              :value="type" 
              v-model="selectedType" 
            />
            {{ $t(type) }}
          </label>
        </div>
      </div>

      <div>
        <h3>{{ $t('selectAvailabilityDates') }}</h3>
        <div v-for="(range, index) in dateRanges" :key="index" class="date-range">
          <input type="date" v-model="range.start" />
          <input type="date" v-model="range.end" />
          <button @click="removeDateRange(index)">{{ $t('delete') }}</button>
        </div>
        <button @click="addDateRange">{{ $t('addDateRange') }}</button>
      </div>

      <button @click="joinGroupWithPreferences" class="create-group-button">{{ $t('joinGroup') }}</button>
    </div>
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
        'Cultural', 'Playa', 'Romántica', 'Relax', 
        'Aventura', 'Gastronómica', 'Bienestar', 'Montaña'
      ],
      selectedType: '',
      dateRanges: [{ start: '', end: '' }]
    };
  },
  methods: {
    back() {
      this.$router.push('/groups');
    },
    addDateRange() {
      this.dateRanges.push({ start: '', end: '' });
    },
    removeDateRange(index) {
      this.dateRanges.splice(index, 1);
    },
    async joinGroupWithPreferences() {
      const availabilityStartDates = this.dateRanges.map(range => range.start);
      const availabilityEndDates = this.dateRanges.map(range => range.end);
      try {
        await axios.post('/api/groups/joinWithPreferences', {
          groupName: this.groupName,
          email: this.email,
          preference: this.selectedType,
          availabilityStartDates,
          availabilityEndDates
        });
        this.$router.push('/groups'); 
      } catch (error) {
        console.error('Error al unirse al grupo:', error);
        if (error.response) {
          alert(`Error al unirse al grupo: ${error.response.data.message}`);
        } else {
          alert('Error al unirse al grupo. Por favor, intenta de nuevo.');
        }
      }
    }
  }
};
</script>

<style scoped>
.join-container {
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

.customCheckBoxHolder {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.customCheckBox {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 8px 15px;
  border-radius: 8px;
  background-color: rgba(0, 0, 0, 0.16);
  transition: background-color 0.3s;
}

.customCheckBox:hover {
  background-color: #2c2c2c;
}

.customCheckBoxInput {
  display: none;
}

.customCheckBoxInput:checked + .customCheckBoxWrapper .customCheckBox {
  background-color: #2d6737;
  color: white;
}

.customCheckBoxInput:checked + .customCheckBoxWrapper .customCheckBox:hover {
  background-color: #34723f;
}

.inner {
  font-size: 16px;
  font-weight: 700;
  color: white;
}
</style>
