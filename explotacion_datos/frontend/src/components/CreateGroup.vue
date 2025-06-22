<template>
  <div class="page-with-menu">
    <SideMenu />
    <div class="create-group-container">
      <h1 class="main-title">{{ $t('createGroup') }}</h1>

      <div class="form-card">
        <input v-model="groupName" type="text" :placeholder="$t('enterGroupName')" class="input" />

        <h3 class="title">{{ $t('selectAudience') }}</h3>
        <div class="checkbox-row">
          <label v-for="aud in ['adultos', 'jóvenes', 'familias', 'terceraEdad']" :key="aud" class="radio-box">
            <input type="radio" :value="aud" v-model="audience" />
            {{ $t(aud) }}
          </label>
        </div>

        <h3 class="title">{{ $t('selectPrivacity') }}</h3>
        <div class="checkbox-row">
          <label class="radio-box">
            <input type="radio" value="private" v-model="privated" />
            {{ $t('private') }}
          </label>
          <label class="radio-box">
            <input type="radio" value="public" v-model="privated" />
            {{ $t('public') }}
          </label>
        </div>

        <h3 class="title">{{ $t('selectType') }}</h3>
        <div class="checkbox-column">
          <label v-for="type in vacationTypes" :key="type" class="radio-box">
            <input type="radio" :value="type" v-model="selectedType" />
            {{ $t(type) }}
          </label>
        </div>

        <div v-if="privated === 'private'">
          <h3 class="title">{{ $t('selectAvailabilityDates') }}</h3>
          <div v-for="(range, index) in dateRanges" :key="index" class="compact-date-row">
            <div class="icon-date-group">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                viewBox="0 0 24 24" fill="none" stroke="currentColor"
                stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                class="lucide lucide-plane-takeoff-icon">
                <path d="M2 22h20"/>
                <path d="M6.36 17.4 4 17l-2-4 1.1-.55a2 2 0 0 1 1.8 0l.17.1a2 2 0 0 0 1.8 0L8 12 5 6l.9-.45a2 2 0 0 1 2.09.2l4.02 3a2 2 0 0 0 2.1.2l4.19-2.06a2.41 2.41 0 0 1 1.73-.17L21 7a1.4 1.4 0 0 1 .87 1.99l-.38.76c-.23.46-.6.84-1.07 1.08L7.58 17.2a2 2 0 0 1-1.22.18Z"/>
              </svg>
              <input type="date" v-model="range.start" class="input compact-date" />
            </div>
            <div class="icon-date-group">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                viewBox="0 0 24 24" fill="none" stroke="currentColor"
                stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                class="lucide lucide-plane-landing-icon">
                <path d="M2 22h20"/>
                <path d="M3.77 10.77 2 9l2-4.5 1.1.55c.55.28.9.84.9 1.45s.35 1.17.9 1.45L8 8.5l3-6 1.05.53a2 2 0 0 1 1.09 1.52l.72 5.4a2 2 0 0 0 1.09 1.52l4.4 2.2c.42.22.78.55 1.01.96l.6 1.03c.49.88-.06 1.98-1.06 2.1l-1.18.15c-.47.06-.95-.02-1.37-.24L4.29 11.15a2 2 0 0 1-.52-.38Z"/>
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
        </div>

        <div v-if="privated === 'public'">
          <h3 class="title">{{ $t('selectAvailabilityDates') }}</h3>
          <div class="compact-date-row">
            <div class="icon-date-group">
              <svg xmlns="http://www.w3.org/2000/svg" class="icon-small" viewBox="0 0 24 24" fill="#16a085">
                <path d="M2.5 19h19v2h-19zM21.4 9.5l-9 3.5-3 7h-2l2.3-6-5.7 2.2-.8-2 7.6-3-2.2-5.8 1.9-.7 2.2 5.8 8.1-3.1z"/>
              </svg>
              <input type="date" v-model="departureDate" class="input compact-date" />
            </div>
            <div class="icon-date-group">
              <svg xmlns="http://www.w3.org/2000/svg" class="icon-small" viewBox="0 0 24 24" fill="#16a085">
                <path d="M2.5 19h19v2h-19zM21.4 9.5l-9-3.5-3-7h-2l2.3 6-5.7-2.2-.8 2 7.6 3-2.2 5.8 1.9.7 2.2-5.8 8.1 3.1z"/>
              </svg>
              <input type="date" v-model="returnDate" class="input compact-date" />
            </div>
          </div>
        </div>

        <h3 class="title">{{ $t('hasFinalDestination') }}</h3>
        <div class="destination-toggle">
          <span class="toggle-label">No</span>
          <label class="switch">
            <input type="checkbox" v-model="hasFinalDestination" />
            <span class="slider"></span>
          </label>
          <span class="toggle-label">Sí</span>
        </div>
        <div v-if="hasFinalDestination" class="final-destination-input">
          <input
            type="text"
            v-model="finalDestination"
            :placeholder="$t('enterFinalDestination')"
            class="input"
          />
        </div>
        <button @click="createGroup" class="create-group-button">
          {{ $t('createTheGroup') }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import SideMenu from '@/components/SideMenu.vue';

export default {
  data() {
    return {
      groupName: '',
      audience: '',
      privated: '',
      selectedType: '',
      departureDate: '',
      returnDate: '',
      dateRanges: [{ start: '', end: '' }],
      vacationTypes: ['Cultural', 'Playa', 'Romántica', 'Relax', 'Aventura', 'Gastronómica', 'Bienestar', 'Montaña'],
      hasFinalDestination: false,
      finalDestination: ''
    };
  },
  components: {
    SideMenu
  },
  methods: {
    addDateRange() {
      this.dateRanges.push({ start: '', end: '' });
    },
    removeDateRange(index) {
      if (this.dateRanges.length > 1) {
        this.dateRanges.splice(index, 1);
      } else {
        alert('Debes tener al menos un rango de fechas de disponibilidad.');
      }
    },
    joinGroup() {
      this.$router.push('/groups');
    },
    viewMyGroups() {
      this.$router.push('/groups/user');
    },
    async createGroup() {
      if (!this.groupName || !this.audience || !this.privated || !this.selectedType) {
        alert('Por favor, completa todos los campos obligatorios: Nombre del Grupo, Audiencia, Privacidad y Tipo de Viaje.');
        return;
      }

      // 2. Validar fechas según el tipo de privacidad
      if (this.privated === 'private') {
        const hasEmptyRange = this.dateRanges.some(range => !range.start || !range.end);
        if (hasEmptyRange) {
          alert('Para grupos privados, asegúrate de que todos los rangos de fechas de disponibilidad estén completos.');
          return;
        }
      } else if (this.privated === 'public') {
        if (!this.departureDate || !this.returnDate) {
          alert('Para grupos públicos, por favor, selecciona las fechas de salida y regreso.');
          return;
        }
      }


      try {
        const email = localStorage.getItem('email');
        if (!email) {
          alert('No se pudo encontrar tu correo electrónico. Por favor, inicia sesión de nuevo.');
          this.$router.push('/login'); 
          return;
        }
        const requestData = {
          name: this.groupName,
          audience: this.audience,
          privated: this.privated,
          email: email,
          tripType: this.selectedType,
          isClosed: false,
          isClosedVoting: false,
          description: "",
          departureDate: this.privated === 'public' ? this.departureDate : null,
          returnDate: this.privated === 'public' ? this.returnDate : null,
          availabilityStartDates: this.privated === 'private' ? this.dateRanges.map(d => d.start) : [],
          availabilityEndDates: this.privated === 'private' ? this.dateRanges.map(d => d.end) : [],
          finalDestination: this.hasFinalDestination ? this.finalDestination : ""
        };
        console.log("Datos enviados al backend:", requestData);

        await axios.post('/api/groups/create', requestData);
        alert('¡Grupo creado con éxito!');
        this.$router.push('/groups/user');
      } catch (error) {
        console.error('Error al crear el grupo:', error.response ? error.response.data : error.message);
        alert('Hubo un problema al crear el grupo. Por favor, inténtalo de nuevo.');
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

.final-destination-input {
  margin-top: 16px;
}

.label {
  height: 60px;
  width: 120px;
  background-color: #ffffff;
  border-radius: 30px;
  -webkit-box-shadow: inset 0 0 5px 4px rgba(255, 255, 255, 1),
    inset 0 0 20px 1px rgba(0, 0, 0, 0.488), 10px 20px 30px rgba(0, 0, 0, 0.096),
    inset 0 0 0 3px rgba(0, 0, 0, 0.3);
  box-shadow: inset 0 0 5px 4px rgba(255, 255, 255, 1),
    inset 0 0 20px 1px rgba(0, 0, 0, 0.488), 10px 20px 30px rgba(0, 0, 0, 0.096),
    inset 0 0 0 3px rgba(0, 0, 0, 0.3);
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  cursor: pointer;
  position: relative;
  -webkit-transition: -webkit-transform 0.4s;
  transition: -webkit-transform 0.4s;
  transition: transform 0.4s;
}

.label:hover {
  -webkit-transform: perspective(100px) rotateX(5deg) rotateY(-5deg);
  transform: perspective(100px) rotateX(5deg) rotateY(-5deg);
}

#checkbox:checked ~ .label:hover {
  -webkit-transform: perspective(100px) rotateX(-5deg) rotateY(5deg);
  transform: perspective(100px) rotateX(-5deg) rotateY(5deg);
}

#checkbox {
  display: none;
}

#checkbox:checked ~ .label::before {
  left: 70px;
  background-color: #000000;
  background-image: linear-gradient(315deg, #000000 0%, #414141 70%);
  -webkit-transition: 0.4s;
  transition: 0.4s;
}

.label::before {
  position: absolute;
  content: "";
  height: 40px;
  width: 40px;
  border-radius: 50%;
  background-color: #000000;
  background-image: linear-gradient(
    130deg,
    #757272 10%,
    #ffffff 11%,
    #726f6f 62%
  );
  left: 10px;
  -webkit-box-shadow: 0 2px 1px rgba(0, 0, 0, 0.3),
    10px 10px 10px rgba(0, 0, 0, 0.3);
  box-shadow: 0 2px 1px rgba(0, 0, 0, 0.3), 10px 10px 10px rgba(0, 0, 0, 0.3);
  -webkit-transition: 0.4s;
  transition: 0.4s;
}

.destination-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 10px;
}

.toggle-label {
  font-weight: bold;
  color: white;
  font-size: 0.9rem;
}

.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 26px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  background-color: #ccc;
  border-radius: 34px;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  transition: 0.4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 20px;
  width: 20px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  border-radius: 50%;
  transition: 0.4s;
}

input:checked + .slider {
  background-color: #1abc9c;
}

input:checked + .slider:before {
  transform: translateX(24px);
}
</style>
