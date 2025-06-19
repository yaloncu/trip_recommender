<template>
  <div class="page-with-menu">
    <SideMenu />
    <div class="groups-container">
      <h1 class="main-title">{{ $t('group') }} {{ group.name }}</h1>

      <div class="groups-content">
        <div class="group-details-card">
          <p class="group-name group-audience">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
              viewBox="0 0 24 24" fill="none" stroke="currentColor"
              stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
              class="lucide lucide-users-icon">
              <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
              <path d="M16 3.128a4 4 0 0 1 0 7.744"/>
              <path d="M22 21v-2a4 4 0 0 0-3-3.87"/>
              <circle cx="9" cy="7" r="4"/>
            </svg>
            {{ $t(group.audience) }}
          </p>

          <p class="group-name">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
              viewBox="0 0 24 24" fill="none" stroke="currentColor"
              stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
              class="lucide lucide-compass-icon">
              <path d="m16.24 7.76-1.804 5.411a2 2 0 0 1-1.265 1.265L7.76 16.24l1.804-5.411a2 2 0 0 1 1.265-1.265z"/>
              <circle cx="12" cy="12" r="10"/>
            </svg>
            {{ $t(group.tripType) }}
          </p>

          <div class="group-dates">
            <span class="date-icon" :title="$t('departureDate')">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                viewBox="0 0 24 24" fill="none" stroke="currentColor"
                stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                class="lucide lucide-plane-takeoff-icon">
                <path d="M2 22h20"/>
                <path d="M6.36 17.4 4 17l-2-4 1.1-.55a2 2 0 0 1 1.8 0l.17.1a2 2 0 0 0 1.8 0L8 12 5 6l.9-.45a2 2 0 0 1 2.09.2l4.02 3a2 2 0 0 0 2.1.2l4.19-2.06a2.41 2.41 0 0 1 1.73-.17L21 7a1.4 1.4 0 0 1 .87 1.99l-.38.76c-.23.46-.6.84-1.07 1.08L7.58 17.2a2 2 0 0 1-1.22.18Z"/>
              </svg>
              {{ group.departureDate ? new Date(group.departureDate).toLocaleDateString() : 'N/A' }}
            </span>

            <span class="date-icon" :title="$t('returnDate')">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                viewBox="0 0 24 24" fill="none" stroke="currentColor"
                stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                class="lucide lucide-plane-landing-icon">
                <path d="M2 22h20"/>
                <path d="M3.77 10.77 2 9l2-4.5 1.1.55c.55.28.9.84.9 1.45s.35 1.17.9 1.45L8 8.5l3-6 1.05.53a2 2 0 0 1 1.09 1.52l.72 5.4a2 2 0 0 0 1.09 1.52l4.4 2.2c.42.22.78.55 1.01.96l.6 1.03c.49.88-.06 1.98-1.06 2.1l-1.18.15c-.47.06-.95-.02-1.37-.24L4.29 11.15a2 2 0 0 1-.52-.38Z"/>
              </svg>
              {{ group.returnDate ? new Date(group.returnDate).toLocaleDateString() : 'N/A' }}
            </span>
          </div>

          <div v-if="!isMember">
            <div class="section-spacing">
              <h3>{{ $t('selectType') }}</h3>
              <div class="customCheckBoxHolder2">
                <label 
                  v-for="(type, index) in vacationTypes" 
                  :key="index" 
                  class="radio-label"
                  :class="{ 'selected-radio': selectedType === type }"
                >
                  <input 
                    type="radio" 
                    :value="type" 
                    v-model="selectedType" 
                  />
                  {{ $t(type) }}
                </label>
              </div>
            </div>

            <button class="join-button" @click="joinGroup">
              {{ $t('joinGroup') }}
            </button>
          </div>

          <div v-else>
            <p class="already-member-message">
              {{ $t('alreadyMember') }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import SideMenu from '@/components/SideMenu.vue';

export default {
  data() {
    return {
      group: {},
      isMember: false,
      vacationTypes: [
        'Cultural', 'Playa', 'Romántica', 'Relax', 
        'Aventura', 'Gastronómica', 'Bienestar', 'Montaña'
      ],
      selectedType: '',
    };
  },
  components: {
    SideMenu
  },
  computed: {
    formattedDepartureDate() {
      return this.group.departureDate
        ? new Date(this.group.departureDate).toLocaleDateString()
        : "";
    },
    formattedReturnDate() {
      return this.group.returnDate
        ? new Date(this.group.returnDate).toLocaleDateString()
        : "";
    },
  },
  methods: {
    async fetchGroupDetails() {
      const groupName = this.$route.params.groupName;
      try {
        const response = await axios.get(`/api/groups/${groupName}`);
        this.group = response.data;
      } catch (error) {
        console.error("Error fetching group details:", error);
        alert("Failed to load group details.");
      }
    },
/*************  ✨ Windsurf Command ⭐  *************/
    /**
     * Navigates back to the previous page in the browser history.
     */

/*******  1f1c0f54-3587-489d-8590-219e7046c4e6  *******/
    goBack() {
      this.$router.go(-1); 
    },
    viewMyGroups() {
      this.$router.push('/groups/user'); 
    },
    async joinGroup() {
      const email = localStorage.getItem('email'); 
      if (!email) {
        alert(this.$t("userNotLoggedIn"));
        return;
      }

      if (!this.selectedType) {
        alert(this.$t("pleaseSelectVacationType"));
        return;
      }

      try {
        const requestData = {
          groupName: this.group.name,
          email: email,
          preference: this.selectedType,
          // availabilityStartDates y availabilityEndDates se eliminan
        };
        console.log("Sending data to backend:", requestData);
        const response = await axios.post("/api/groups/joinWithPreferences", requestData);
        alert(this.$t('successfullyJoinedGroup', { groupName: this.group.name }));
        this.isMember = true; 
      } catch (error) {
        console.error("Error joining group:", error);
        if (error.response && error.response.status === 409) { 
          alert(this.$t('alreadyMemberError'));
        } else {
          alert(this.$t('failedToJoinGroup'));
        }
      }
    },
  },
  mounted() {
    this.fetchGroupDetails();
  },
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

.groups-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #2c3e50;
  background-image: url('@/assets/mapamundi.png');
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  color: white;
  font-family: 'Poppins', sans-serif;
  min-height: 100vh;
  padding-bottom: 100px;
  overflow-y: auto;
  width: 100%;
  overflow-x: hidden;
}

.create-button { /* Reutilizado como botón de "back" */
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
}

.create-button:hover {
  transform: scale(1.03);
}

.create-button:active {
  transform: scale(0.95);
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

.w-5, .h-5, .profile-icon {
  width: 2rem;
  height: 2rem;
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
  margin: 80px 0 30px 0;
  font-family: 'Poppins', sans-serif;
}

.groups-content {
  text-align: center;
  max-width: 800px; /* Ajustado para los detalles del grupo */
  width: 90%;
  background: #34495e;
  border-radius: 30px;
  padding: 30px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 30px 30px -20px;
  font-family: 'Poppins', sans-serif;
  color: white; /* Asegúrate de que el texto dentro sea blanco */
}

.group-details-card {
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* Alinea el texto a la izquierda dentro de la tarjeta */
  padding: 15px;
  background-color: #ecf0f1;
  border-radius: 20px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 6px;
  color: #34495e; /* Color de texto para el contenido de la tarjeta */
  font-family: 'Poppins', sans-serif;
}

.group-name {
  color: #34495e;
  font-weight: bold;
  margin-bottom: 6px; /* Espacio entre los párrafos de detalles */
  display: flex;
  align-items: center;
}

.icon-group {
  width: 18px;
  height: 18px;
  margin-right: 5px;
  vertical-align: middle;
}

.group-dates {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  gap: 10px;
  font-weight: bold;
  color: #34495e;
  font-family: 'Poppins', sans-serif;
  width: 100%; /* Para que ocupe todo el ancho de la tarjeta */
}

.date-icon {
  display: flex;
  align-items: center;
  gap: 5px;
}

.date-icon .icon {
  width: 20px;
  height: 20px;
  fill: #16a085;
}

.join-button {
  align-self: flex-end; /* Alinea el botón a la derecha dentro de la tarjeta */
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  font-size: 1.2rem; /* Tamaño de fuente ajustado */
  padding: 10px 20px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  margin-top: 20px; /* Espacio superior */
  transition: all 0.2s ease-in-out;
  font-family: 'Poppins', sans-serif;
}

.join-button:hover {
  transform: scale(1.03);
}

.join-button:active {
  transform: scale(0.95);
}

/* Estilos para los radio buttons (vacation types) */
h3 {
  color: #34495e; /* Color del texto para los títulos de sección */
  margin-top: 20px;
  margin-bottom: 10px;
  align-self: flex-start; /* Alinea los títulos a la izquierda */
}

.customCheckBoxHolder2 {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: flex-start; /* Alinea los checkboxes a la izquierda */
  width: 100%;
}

.radio-label {
  background-color: #f1f1f1;
  color: #34495e;
  padding: 8px 15px;
  border-radius: 15px;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid #ddd;
}

.radio-label:hover {
  background-color: #e0e0e0;
}

.radio-label input[type="radio"] {
  display: none; /* Oculta el radio button original */
}

/* --- NUEVO ESTILO PARA EL TIPO SELECCIONADO --- */
.radio-label.selected-radio {
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%); /* Fondo degradado */
  color: white; /* Texto blanco para mayor contraste */
  border-color: #16a085; /* Borde que coincide con el degradado */
  font-weight: bold; /* Hacer el texto más llamativo */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Sombra para que destaque */
  transform: translateY(-2px); /* Un ligero efecto de elevación */
}
/* ------------------------------------------- */

.already-member-message {
    color: #16a085; /* Color para el mensaje de miembro */
    font-size: 1.2rem;
    font-weight: bold;
    margin-top: 20px;
    align-self: center; /* Centrar el mensaje */
}

.section-spacing {
    margin-bottom: 20px; /* Espacio entre secciones como tipo de vacaciones y fechas */
    width: 100%;
}

/* Asegúrate de que las fuentes se apliquen correctamente */
button,
.join-button,
.create-button,
.button,
label,
input,
select,
p,
h1,
h3 {
  font-family: 'Poppins', sans-serif !important;
}
</style>