<template>
  <div class="page-with-menu">
    <SideMenu />
    <main class="create-group-container" role="main" aria-labelledby="main-title">
      <h1 class="main-title" id="main-title">{{ $t('createActivity') }}</h1>

      <form class="form-card" @submit.prevent="createActivity" aria-label="{{ $t('createActivity') }}">
        <label for="title" class="sr-only">{{ $t('activityTitle') }}</label>
        <input
          id="title"
          v-model="title"
          type="text"
          :placeholder="$t('activityTitle')"
          class="input"
          required
        />

        <label for="description" class="sr-only">{{ $t('description') }}</label>
        <textarea
          id="description"
          v-model="description"
          :placeholder="$t('description')"
          class="input"
          rows="4"
        ></textarea>

        <label for="location" class="sr-only">{{ $t('location') }}</label>
        <input
          id="location"
          v-model="location"
          type="text"
          :placeholder="$t('location')"
          class="input"
          required
        />

        <h3 class="title">{{ $t('activityType') }}</h3>
        <fieldset class="checkbox-column" aria-label="{{ $t('activityType') }}">
          <legend class="sr-only">{{ $t('activityType') }}</legend>
          <label v-for="type in activityTypes" :key="type" class="radio-box">
            <input type="radio" :value="type" v-model="selectedType" required />
            {{ $t(type) }}
          </label>
        </fieldset>

        <h3 class="title">{{ $t('startDateTime') }}</h3>
        <label for="startDateTime" class="sr-only">{{ $t('startDateTime') }}</label>
        <input
          id="startDateTime"
          v-model="startDateTime"
          type="datetime-local"
          class="input"
          required
        />

        <button type="submit" class="create-group-button">
          {{ $t('createActivity') }}
        </button>
      </form>
    </main>
  </div>
</template>

<script>
import axios from 'axios';
import SideMenu from '@/components/SideMenu.vue';

export default {
  components: {
    SideMenu
  },
  data() {
    return {
      title: '',
      description: '',
      location: '',
      selectedType: '',
      startDateTime: '',
      activityTypes: [
        'Cultural',
        'Playa',
        'Romántica',
        'Relax',
        'Aventura',
        'Gastronómica',
        'Bienestar',
        'Montana'
      ]
    };
  },
  methods: {
    async createActivity() {
      if (!this.title || !this.location || !this.selectedType || !this.startDateTime) {
        alert('Por favor, completa todos los campos obligatorios.');
        return;
      }

      const email = localStorage.getItem('email');
      if (!email) {
        alert('Por favor, inicia sesión nuevamente.');
        this.$router.push('/login');
        return;
      }

      const localDateTime = new Date(this.startDateTime);
      const offsetMinutes = localDateTime.getTimezoneOffset();
      const offsetHours = String(Math.abs(offsetMinutes / 60)).padStart(2, '0');
      const offset = `${offsetMinutes <= 0 ? '+' : '-'}${offsetHours}:00`;

      const isoWithoutZ = localDateTime.toISOString().split('.')[0]; 
      const formattedDate = `${isoWithoutZ}${offset}`;

      const payload = {
        title: this.title,
        description: this.description,
        location: this.location,
        type: this.selectedType,
        startDateTime: formattedDate,
        email,
        adminEmail: email
      };

      try {
        await axios.post('/api/activities', payload);
        alert('¡Actividad creada con éxito!');
        this.$router.push('/activities');
      } catch (error) {
        console.error('Error al crear la actividad:', error);
        alert('Hubo un error al crear la actividad.');
      }
    }
  }
};
</script>

<style scoped>
.page-with-menu {
  display: flex;
  width: 100vw;
  overflow-x: hidden;
}

.main-content {
  margin-left: 220px; 
  width: calc(100% - 220px);
}

.create-group-container {
  position: relative;
  min-height: 100vh;
  padding-top: 80px;
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
  width: 100%;
  box-sizing: border-box;
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
  font-family: 'Poppins', sans-serif;
  z-index: 1000;
}

.join-button-header:hover {
  transform: scale(1.03);
}

.join-button-header:active {
  transform: scale(0.95);
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
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
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
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
  font-family: 'Poppins', sans-serif;
  box-sizing: border-box;
}

.group-dates {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.input-date-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
}

.date-icon .icon {
  width: 14px;
  height: 14px;
  fill: #16a085;
  margin-right: 4px;
}

.icon-date {
  flex: 1;
  min-width: 150px;
}

.checkbox-row {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  justify-content: center;
  margin-bottom: 20px;
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

.create-button, .create-group-button {
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  font-size: 1.2rem;
  padding: 15px 30px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  margin-top: 20px;
  font-family: 'Poppins', sans-serif;
  transition: 0.2s ease-in-out;
}

.create-button:hover, .create-group-button:hover {
  transform: scale(1.03);
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
  transition: 0.2s ease-in-out;
}

.profile-button {
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  cursor: pointer;
}

.profile-icon {
  width: 2rem;
  height: 2rem;
  fill: white;
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

</style>

