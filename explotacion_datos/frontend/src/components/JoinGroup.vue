<template>
  <div class="join-group-container">
    <button class="join-button-header" @click="back">{{ $t('back') }}</button>

    <h1 class="main-title">{{ $t('joinAGroup') }}</h1>

    <div class="form-card">
      <input v-model="groupName" type="text" :placeholder="$t('enterGroupName')" class="input" />
      <input v-model="email" type="email" :placeholder="$t('enterYourEmail')" class="input" />

      <h3 class="title">{{ $t('selectType') }}</h3>
      <div class="checkbox-column">
        <label v-for="type in vacationTypes" :key="type" class="radio-box">
          <input type="radio" :value="type" v-model="selectedType" />
          {{ $t(type) }}
        </label>
      </div>

      <h3 class="title">{{ $t('selectAvailabilityDates') }}</h3>
      <div v-for="(range, index) in dateRanges" :key="index" class="compact-date-row">
        <div class="icon-date-group">
          <svg xmlns="http://www.w3.org/2000/svg" class="icon-small" viewBox="0 0 24 24" fill="#16a085">
            <path d="M2.5 19h19v2h-19zM21.4 9.5l-9 3.5-3 7h-2l2.3-6-5.7 2.2-.8-2 7.6-3-2.2-5.8 1.9-.7 2.2 5.8 8.1-3.1z"/>
          </svg>
          <input type="date" v-model="range.start" class="input compact-date" />
        </div>
        <div class="icon-date-group">
          <svg xmlns="http://www.w3.org/2000/svg" class="icon-small" viewBox="0 0 24 24" fill="#16a085">
            <path d="M2.5 19h19v2h-19zM21.4 9.5l-9-3.5-3-7h-2l2.3 6-5.7-2.2-.8 2 7.6 3-2.2 5.8 1.9.7 2.2-5.8 8.1 3.1z"/>
          </svg>
          <input type="date" v-model="range.end" class="input compact-date" />
        </div>
      </div>
      <div class="range-actions">
        <button class="range-btn delete" @click="removeDateRange(dateRanges.length - 1)">
          {{ $t('delete') }}
        </button>
        <button class="range-btn add" @click="addDateRange">
          {{ $t('addDateRange') }}
        </button>
      </div>

      <button @click="joinGroupWithPreferences" class="create-group-button">
        {{ $t('joinGroup') }}
      </button>
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
        alert('Error al unirse al grupo.');
      }
    }
  }
};
</script>

<style scoped>
.join-group-container {
  position: relative;
  min-height: 100vh;
  padding-top: 100px;
  padding-bottom: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-image: url('@/assets/aviones.png');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  background-color: #2c3e50;
  color: white;
  font-family: 'Poppins', sans-serif;
}

.join-button-header {
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
  z-index: 1000;
}

.join-button-header:hover {
  transform: scale(1.03);
}

.main-title {
  color: #1abc9c;
  font-size: 2.2rem;
  margin-bottom: 20px;
}

.form-card {
  background: #34495e;
  padding: 30px;
  border-radius: 30px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  width: 90%;
  max-width: 600px;
}

.input {
  width: 100%;
  max-width: 100%;
  background: white;
  border: none;
  padding: 15px 20px;
  border-radius: 15px;
  margin-top: 10px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  font-family: 'Poppins', sans-serif;
  box-sizing: border-box;
}

.title {
  font-weight: bold;
  margin: 20px 0 10px;
}

.checkbox-column {
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: flex-start;
  margin-bottom: 20px;
}

.radio-box {
  display: flex;
  align-items: center;
  gap: 8px;
  color: white;
  font-weight: bold;
  cursor: pointer;
}

.range-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.range-btn {
  padding: 10px 20px;
  border-radius: 10px;
  font-weight: bold;
  border: none;
  cursor: pointer;
}

.range-btn.add {
  background-color: #2980b9;
  color: white;
}

.range-btn.delete {
  background-color: #e74c3c;
  color: white;
}

.compact-date-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.icon-date-group {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  min-width: 150px;
}

.icon-small {
  width: 18px;
  height: 18px;
  min-width: 18px;
}

.compact-date {
  flex: 1;
  padding: 10px 15px;
  border-radius: 12px;
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
  font-family: 'Poppins', sans-serif;
}

.create-group-button:hover {
  background: #1abc9c;
  transform: scale(1.03);
}
</style>
