<template>
  <div class="voting-container" role="main" aria-labelledby="main-title">
    <button class="join-button-header" @click="back" :aria-label="$t('back')">
      {{ $t('back') }}
    </button>

    <h1 id="main-title" class="main-title">{{ $t('voteForTrip') }}</h1>

    <div class="form-card" role="form" aria-label="Form to vote for trip type">
      <h2 class="title">{{ $t('selectPreferredTypes') }}</h2>

      <div class="checkbox-column">
        <label
          v-for="type in vacationTypes"
          :key="type"
          class="radio-box"
          :for="`checkbox-${type}`"
        >
          <input
            type="checkbox"
            :id="`checkbox-${type}`"
            :value="type"
            v-model="selectedTypes"
            :aria-label="$t(type)"
          />
          {{ $t(type) }}
        </label>
      </div>

      <button
        @click="submitVotes"
        class="create-group-button"
        :aria-label="$t('submitVotes')"
      >
        {{ $t('submitVotes') }}
      </button>
    </div>
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
        'Cultural', 'Playa', 'Romántica', 'Relax',
        'Aventura', 'Gastronómica', 'Bienestar', 'Montaña'
      ],
      selectedTypes: [],
      userId: '',
      groupId: ''
    };
  },
  created() {
    this.groupId = this.$route.params.groupId;
  },
  methods: {
    back() {
      this.$router.push('/groups');
    },
    submitVotes() {
      axios.post('/api/votes', {
        groupId: this.groupId,
        selectedTypes: this.selectedTypes,
        userId: this.userId
      })
      .then(() => {
        alert('Voto enviado correctamente');
        this.$router.push('/groups');
      })
      .catch((error) => {
        console.error('Error al enviar el voto', error);
        alert('Error al enviar el voto');
      });
    }
  }
};
</script>

<style scoped>
.voting-container {
  position: relative;
  min-height: 100vh;
  padding-top: 100px;
  padding-bottom: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-image: url('@/assets/mapamundi.png');
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
:focus {
  outline: 3px solid #1abc9c;
  outline-offset: 2px;
}
input::placeholder {
  color: #bdc3c7;
}
#sendButton {
  min-width: 44px;
  min-height: 44px;
}
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}

</style>
