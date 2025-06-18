<template>
  <div class="page-with-menu">
    <SideMenu />

    <div class="user-container">
      <h1 class="main-title">{{ $t('yourGroups') }}</h1>

      <div class="cards-container">
        <div class="form-card">
          <h1 class="main-title">{{ $t('yourGroups') }}</h1>
          <div v-if="groups.length" class="groups-grid">
            <div v-for="group in groups" :key="group.id" class="group-card">
              <h3 class="group-name">{{ group.name }}</h3>
              <p>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-plane-takeoff-icon lucide-plane-takeoff">
                  <path d="M2 22h20"/>
                  <path d="M6.36 17.4 4 17l-2-4 1.1-.55a2 2 0 0 1 1.8 0l.17.1a2 2 0 0 0 1.8 0L8 12 5 6l.9-.45a2 2 0 0 1 2.09.2l4.02 3a2 2 0 0 0 2.1.2l4.19-2.06a2.41 2.41 0 0 1 1.73-.17L21 7a1.4 1.4 0 0 1 .87 1.99l-.38.76c-.23.46-.6.84-1.07 1.08L7.58 17.2a2 2 0 0 1-1.22.18Z"/>
                </svg>
                {{ group.departureDate ? new Date(group.departureDate).toLocaleDateString() : $t('noDepartureDate') }}
              </p>
              <div class="button-row">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-plane-landing-icon lucide-plane-landing">
                  <path d="M2 22h20"/>
                  <path d="M3.77 10.77 2 9l2-4.5 1.1.55c.55.28.9.84.9 1.45s.35 1.17.9 1.45L8 8.5l3-6 1.05.53a2 2 0 0 1 1.09 1.52l.72 5.4a2 2 0 0 0 1.09 1.52l4.4 2.2c.42.22.78.55 1.01.96l.6 1.03c.49.88-.06 1.98-1.06 2.1l-1.18.15c-.47.06-.95-.02-1.37-.24L4.29 11.15a2 2 0 0 1-.52-.38Z"/>
                </svg>
                {{ group.returnDate ? new Date(group.returnDate).toLocaleDateString() : $t('noReturnDate') }}
              </div>
              <div v-if="isAdmin(group.email) && group.privated === 'private'" class="admin-actions">
                <input v-model="inviteEmail" type="email" :placeholder="$t('enterEmailToInvite')" class="input" />
                <button @click="inviteUser(group.name)" class="invite-button">{{ $t('invite') }}</button>
              </div>

              <div class="group-actions">
                <div v-if="group.closed && group.closedVoting">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-map-pin-icon lucide-map-pin">
                    <path d="M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0"/>
                    <circle cx="12" cy="10" r="3"/>
                  </svg>
                  <span>{{ group.finalDestination }}</span>
                </div>
              </div>
              <div class="group-actions">
                <button @click="$router.push(`/chat/${group.id}`)" class="recommend-button">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-message-square-text-icon lucide-message-square-text">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                    <path d="M13 8H7"/>
                    <path d="M17 12H7"/>
                  </svg>
                </button>
                <button @click="$router.push(`/group-participants/${group.id}`)" class="recommend-button">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-users-icon lucide-users">
                    <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
                    <path d="M16 3.128a4 4 0 0 1 0 7.744"/>
                    <path d="M22 21v-2a4 4 0 0 0-3-3.87"/>
                    <circle cx="9" cy="7" r="4"/>
                  </svg>
                </button>
              </div>
              <div class="group-actions">
                <button v-if="isAdmin(group.email) && !group.closed && group.privated === 'public'" @click="closeGroup(group.name)" class="close-button">
                  {{ $t('closeGroup') }}
                </button>
                <button v-if="isAdmin(group.email) && !group.closed && group.privated === 'private'" @click="closeGroupPrivate(group.name)" class="close-button">
                  {{ $t('closeGroup') }}
                </button>
                <button v-if="group.closed && !group.closedVoting" @click="goToVoting(group.id)" class="recommend-button">
                  {{ $t('vote') }}
                </button>
                <button v-if="isAdmin(group.email) && group.closed && !group.closedVoting" @click="closeVoting(group.name)" class="close-button">
                  {{ $t('closeVoting') }}
                </button>
                <button @click="leaveGroup(group.name)" class="leave-button">{{ $t('leaveGroup') }}</button>
              </div>
            </div>
          </div>
          <div v-else>
            <p>{{ $t('notBelongGroup') }}</p>
          </div>
        </div>

        <div class="form-card invitations-card">
          <h1 class="main-title">{{ $t('pendingInvitations') }}</h1>
          <div v-if="invitedGroups.length" class="invited-groups-container">
            <div v-for="group in invitedGroups" :key="group.id" class="group-card">
              <h2 class="group-name">{{ group.name }}</h2>

              <div>
                <h3 class="title">{{ $t('selectType') }}</h3>
                <div class="checkbox-column">
                  <label v-for="(type, index) in vacationTypes" :key="index" class="radio-box">
                    <input type="radio" :value="type" v-model="group.selectedType" />
                    {{ $t(type) }}
                  </label>
                </div>
              </div>

              <div>
                <h3 class="title">{{ $t('selectAvailabilityDates') }}</h3>
                <div v-for="(range, index) in group.dateRanges" :key="index" class="compact-date-row">
                  <div class="icon-date-group">
                    <svg xmlns="http://www.w3.org/2000/svg" class="icon-small" viewBox="0 0 24 24" fill="#16a085" title="Return">
                      <path d="M2.5 19h19v2h-19zM21.4 9.5l-9 3.5-3 7h-2l2.3-6-5.7 2.2-.8-2 7.6-3-2.2-5.8 1.9-.7 2.2 5.8 8.1-3.1z"/>
                    </svg>
                    <input type="date" v-model="range.start" class="input compact-date" />
                  </div>
                  <div class="icon-date-group">
                    <svg xmlns="http://www.w3.org/2000/svg" class="icon-small" viewBox="0 0 24 24" fill="#16a085" title="Departure">
                      <path d="M2.5 19h19v2h-19zM21.4 9.5l-9-3.5-3-7h-2l2.3 6-5.7-2.2-.8 2 7.6 3-2.2 5.8 1.9.7 2.2-5.8 8.1 3.1z"/>
                    </svg>
                    <input type="date" v-model="range.end" class="input compact-date" />
                  </div>
                  <button @click="removeDateRange(group, index)" class="range-btn delete">
                    {{ $t('delete') }}
                  </button>
                </div>
                <button @click="addDateRange(group)" class="range-btn add">
                  {{ $t('addDateRange') }}
                </button>
              </div>

              <button @click="acceptInvitation(group)" class="create-group-button">
                {{ $t('acceptInvite') }}
              </button>
            </div>
          </div>
          <div v-else>
            <p>{{ $t('noPendingInvites') }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import GroupChat from '@/components/GroupChat.vue';
import SideMenu from '@/components/SideMenu.vue';

export default {
  data() {
    return {
      groups: [],
      inviteEmail: '',
      invitedGroups: [],
      vacationTypes: [
        'Cultural', 'Playa', 'Romántica', 'Relax',
        'Aventura', 'Gastronómica', 'Bienestar', 'Montaña'
      ],
      selectedType: '',
      userEmail: '' 
    };
  },
  components: {
    GroupChat,
    SideMenu
  },
  methods: {
    async fetchInvitedGroups() {
      try {
        const response = await axios.get(`/api/groups/invitations/${this.userEmail}`);
        this.invitedGroups = response.data.map(group => ({
          ...group,
          selectedPreference: '',
          dateRanges: [{ start: '', end: '' }]
        }));
      } catch (error) {
        console.error('Error fetching invited groups:', error);
        alert('Error al obtener las invitaciones pendientes.');
      }
    },
    addDateRange(group) {
      group.dateRanges.push({ start: '', end: '' });
    },
    removeDateRange(group, index) {
      group.dateRanges.splice(index, 1);
    },
    joinGroup() {
      this.$router.push('/groups');
    },
    createGroup() { // Keep this for consistency, even if the button is removed from the header
      this.$router.push('/groups/create');
    },
    viewMyGroups() { // Added for the profile button
      this.$router.push('/groups/user');
    },
    isAdmin(groupEmail) {
      console.log("Admin Email:", groupEmail);
      console.log("User Email:", this.userEmail);
      return groupEmail.trim().toLowerCase() === this.userEmail.trim().toLowerCase();
    },
    goToVoting(groupId) {
      this.$router.push(`/vote/${groupId}`);
    },
    async fetchUserGroups() {
      try {
        const email = localStorage.getItem('email');
        if (!email) {
          throw new Error('No email found in session.');
        }

        const response = await axios.get(`/api/groups/user?email=${email}`);
        this.groups = response.data;
        console.log('Fetched groups:', this.groups);
      } catch (error) {
        console.error('Error fetching groups:', error);
        alert('Error loading your groups.');
      }
    },
    async viewRecommendation(groupId) {
      try {
        const response = await axios.get(`/api/recommendations/${groupId}`);
        const recommendations = response.data;
        if (Array.isArray(recommendations) && recommendations.length > 0) {
          this.$router.push({
            path: `/groups/${groupId}/recommendations/${this.userEmail}`,
            query: { recommendations: JSON.stringify(recommendations)}
          });
        } else {
          alert('No hay recomendaciones disponibles.');
        }
      } catch (error) {
        console.error('Error fetching recommendations:', error);
        alert('Error al obtener las recomendaciones.');
      }
    },
    async closeGroup(name) {
        console.log("Attempting to close group with name:", name); 
        if (!confirm(`Are you sure you want to close the group ${name}?`)) {
            return;
        }
        try {
            const response = await axios.put(`/api/groups/close/${name}`); // Using 'name'
            alert(`Grupo "${name}" cerrado exitosamente.`); 
            this.fetchUserGroups();
        } catch (error) {
            console.error('Error closing group:', error);
            alert('Error al cerrar el grupo.');
        }
    },
    async closeGroupPrivate(name) { 
        console.log("Attempting to close private group with name:", name);
        if (!confirm(`Are you sure you want to close the group ${name}?`)) {
            return;
        }
        try {
            const response = await axios.post(`/api/groups/closePrivate/${name}`); 
            alert(`Grupo privado "${name}" cerrado y recomendaciones calculadas.`);
            this.fetchUserGroups();
        } catch (error) {
            console.error('Error closing group:', error);
            alert('Error al cerrar el grupo.');
        }
    },
    async closeVoting(groupName) {
      if (!confirm(`Are you sure you want to close voting for the group ${groupName}?`)) {
        return;
      }
      try {
        const response = await axios.post(`/api/groups/closeVoting/${groupName}`);
        this.fetchUserGroups();
      } catch (error) {
        console.error('Error closing voting:', error);
        alert('Error al cerrar la votación.');
      }
    },
    async viewFinalDestination(groupId) {
      try {
        const response = await axios.get(`/api/groups/${groupId}/finalDestination`);
        const finalDestination = response.data.destination;

        if (finalDestination) {
          alert(`El destino final es: ${finalDestination}`);
        } else {
          alert('No hay un destino final disponible aún.');
        }
      } catch (error) {
        console.error('Error al obtener el destino final:', error);
        alert('Error al obtener el destino final.');
      }
    },
    async leaveGroup(groupName) {
      try {
        const email = localStorage.getItem('email');
        if (!email) {
          throw new Error('No email found in session.');
        }

        const response = await axios.delete('/api/groups/leave', {
          data: { email, groupName },
        });
        alert(response.data.message);
        this.fetchUserGroups();
      } catch (error) {
        console.error('Error leaving group:', error);
        alert('Error leaving the group.');
      }
    },
    async inviteUser(groupName) {
      if (!this.inviteEmail) {
        alert('Por favor, introduce un correo válido.');
        return;
      }

      try {
        const response = await axios.post('/api/groups/invite', {
          groupName: groupName,
          email: this.inviteEmail
        });
        alert(`Usuario ${this.inviteEmail} invitado exitosamente al grupo ${groupName}`);
        this.inviteEmail = '';
      } catch (error) {
        console.error('Error al invitar al usuario:', error);
        alert('Error al invitar al usuario. Por favor, inténtalo de nuevo.');
      }
    },
    async acceptInvitation(group) {
      const startDates = group.dateRanges.map(range => range.start).filter(date => date);
      const endDates = group.dateRanges.map(range => range.end).filter(date => date);

      if (startDates.length !== endDates.length || startDates.length === 0) {
        alert('Por favor, asegúrate de que todos los rangos de fechas estén completos.');
        return;
      }

      try {
        const response = await axios.post('/api/groups/accept-invite-details', {
          email: this.userEmail,
          groupName: group.groupName,
          preference: group.selectedType, // Changed to group.selectedType
          startDates: startDates,
          endDates: endDates
        });
        alert(response.data.message);
        this.fetchInvitedGroups();
        this.fetchUserGroups();
      } catch (error) {
        console.error('Error accepting invitation with details:', error);
        alert('Error al aceptar la invitación con detalles.');
      }
    }
  },
  mounted() {
    const storedEmail = localStorage.getItem('email');
    if (storedEmail) {
      this.userEmail = storedEmail;
      this.fetchUserGroups();
      this.fetchInvitedGroups();
    } else {
      alert('No user email found. Please log in again.');
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.page-with-menu {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex-grow: 1;
  margin-left: 220px; 
  box-sizing: border-box;
}

.user-container {
  position: relative;
  padding: 100px 40px 100px 40px; 
  min-height: 100vh;
  width: 100%;
  background-image: url('@/assets/mapamundi.png');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  background-color: #2c3e50;
  color: white;
  font-family: 'Poppins', sans-serif;
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

.create-button-header {
  position: absolute;
  top: 20px;
  right: 20px;
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

.create-button-header:hover {
  transform: scale(1.03);
}

.create-button-header:active {
  transform: scale(0.95);
}

.profile-button-wrapper {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 1000; /* Ensure it's above other content */
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

.main-title {
  color: #1abc9c;
  font-size: 2.2rem;
  margin-bottom: 20px;
  text-align: center;
}

.cards-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  width: 100%;
  max-width: 1200px;
  gap: 30px;
  margin-top: 50px; /* Adjusted margin-top to account for header buttons */
}

.form-card { /* Renamed from .card to .form-card for consistency */
  background: #34495e;
  padding: 30px;
  border-radius: 30px; /* Increased border-radius for consistency */
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  flex: 1;
  min-width: 360px;
  box-sizing: border-box; /* Ensures padding doesn't increase total width */
}

.groups-grid,
.invited-groups-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.group-card {
  background: #2c3e50;
  padding: 20px;
  border-radius: 15px;
  color: white;
  box-shadow: 0 6px 16px rgba(0,0,0,0.15);
}

.group-name {
  font-size: 1.5rem; /* Increased font size for group name */
  font-weight: bold;
  color: #1abc9c;
  margin-bottom: 10px;
}

.group-actions {
  margin-top: 15px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.button-row {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

button.leave-button,
button.close-button,
button.recommend-button,
button.final-destination-button,
button.invite-button {
  padding: 10px 20px;
  border-radius: 20px;
  font-size: 1rem;
  border: none;
  cursor: pointer;
  background-color: #1abc9c;
  color: white;
  transition: background-color 0.3s ease;
}

button.leave-button:hover,
button.close-button:hover,
button.recommend-button:hover,
button.final-destination-button:hover,
button.invite-button:hover {
  background-color: #16a085;
}

.input {
  width: 100%;
  background: white;
  border: none;
  padding: 15px 20px;
  border-radius: 15px;
  margin-top: 10px;
  margin-bottom: 20px; /* Adjusted margin-bottom */
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
  font-family: 'Poppins', sans-serif;
  box-sizing: border-box;
}

.admin-actions {
  margin-top: 10px;
}

.title { /* Added title class for consistency */
  color: #1abc9c;
  font-size: 1.2rem;
  margin-bottom: 10px;
  margin-top: 20px;
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
  fill: #16a085; 
}

.compact-date {
  flex: 1;
  padding: 10px 15px;
  border-radius: 12px;
  border: none; 
  background: white; 
  box-shadow: 0 2px 8px rgba(0,0,0,0.15); 
  font-family: 'Poppins', sans-serif;
}

.range-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  width: 100%; 
  gap: 10px; 
}

.range-btn {
  padding: 10px 20px;
  border-radius: 10px;
  font-weight: bold;
  border: none;
  cursor: pointer;
  flex: 1; 
}

.range-btn.add {
  background-color: #2980b9;
  color: white;
}

.range-btn.delete {
  background-color: #e74c3c;
  color: white;
}

.create-group-button { /* Used for the accept invitation button */
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
  width: 100%; /* Make button span full width */
}

.create-group-button:hover {
  transform: scale(1.03);
}

.final-destination-box {
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: #1abc9c;
  padding: 10px 15px;
  border-radius: 12px;
  color: white;
  font-weight: bold;
  margin-top: 10px;
  justify-content: center;
}

</style>