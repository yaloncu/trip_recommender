<template>
  <div class="user-container">
    <button class="join-button" @click="joinGroup">{{ $t('join') }}</button>
    <button class="create-button" @click="createGroup">{{ $t('create') }}</button>

    <div class="cards-container">

      <div class="card">
      <h1 class="main-title">{{ $t('yourGroups') }}</h1>
      <div v-if="groups.length" class="groups-grid">
        <div v-for="group in groups" :key="group.groupId" class="group-card">
          <h3 class="group-name">{{ group.groupName }}</h3>
          <p><strong>{{ $t('preference') }}:</strong> {{ group.preference || 'No preference' }}</p>
          <p><strong>{{ $t('departureDate') }}:</strong> {{ group.departureDate ? new Date(group.departureDate).toLocaleDateString() : 'No departure date available' }}</p>
          <p><strong>{{ $t('arrivalDate') }}:</strong> {{ group.returnDate ? new Date(group.returnDate).toLocaleDateString() : 'No return date available' }}</p>

          <div v-if="isAdmin(group.email) && group.privated === 'private'" class="admin-actions">
            <input v-model="inviteEmail" type="email" :placeholder="$t('enterEmailToInvite')" class="input invite-input" />
            <button @click="inviteUser(group.groupName)" class="invite-button">{{ $t('invite') }}</button>
          </div>

          <div class="group-actions">
            <button v-if="group.isClosed && !group.isClosedVoting" @click="viewRecommendation(group.groupId)" class="recommend-button">{{ $t('recomendation') }}</button>
            <button v-if="isAdmin(group.email) && !group.isClosed && group.privated === 'public'" @click="closeGroup(group.groupName)" class="close-button">{{ $t('closeGroup') }}</button>
            <button v-if="isAdmin(group.email) && !group.isClosed && group.privated === 'private'" @click="closeGroupPrivate(group.groupName)" class="close-button">{{ $t('closeGroup') }}</button>
            <button v-if="isAdmin(group.email) && group.isClosed && !group.isClosedVoting" @click="closeVoting(group.groupName)" class="close-button">{{ $t('closeVoting') }}</button>
            <button @click="leaveGroup(group.groupName)" class="leave-button">{{ $t('leaveGroup') }}</button>
            <button v-if="group.isVotingClosed" @click="viewFinalDestination(group.groupId)" class="final-destination-button">{{ $t('viewFinalDestination') }}</button>
          </div>
        </div>
      </div>
      <div v-else>
        <p>{{ $t('notBelongGroup') }}</p>
      </div>
    </div>

    <div class="card invitations-card">
      <h1 class="main-title">{{ $t('pendingInvitations') }}</h1>
      <div v-if="invitedGroups.length" class="invited-groups-container">
        <div v-for="group in invitedGroups" :key="group.groupId" class="group-card">
          <h2 class="group-name">{{ group.groupName }}</h2>

          <div>
            <h3>{{ $t('selectType') }}</h3>
            <div class="customCheckBoxHolder2">
              <label v-for="(type, index) in vacationTypes" :key="index">
                <input 
                  type="radio" 
                  :value="type" 
                  v-model="group.selectedType" 
                />
                {{ $t(type) }} 
              </label>
            </div>
          </div>

          <div>
            <h3>{{ $t('selectAvailabilityDates') }}</h3>
            <div v-for="(range, index) in group.dateRanges" :key="index" class="date-range">
              <label>{{ $t('availabilityStartDate') }}</label>
              <input type="date" v-model="range.start" class="input date-group" />

              <label>{{ $t('availabilityEndDate') }}</label>
              <input type="date" v-model="range.end" class="input date-group" />

              <button @click="removeDateRange(group, index)" class="delete-range-button">
                {{ $t('delete') }}
              </button>
            </div>
            <button @click="addDateRange(group)" class="add-range-button">
              {{ $t('addDateRange') }}
            </button>
          </div>

          <button @click="acceptInvitation(group)" class="accept-button">
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
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      groups: [] ,
      inviteEmail: '',
      invitedGroups: [],
      vacationTypes: [
        'Cultural', 'Playa', 'Romántica', 'Relax', 
        'Aventura', 'Gastronómica', 'Bienestar', 'Montaña'
      ],
      selectedType: '' ,
    };
  },
  methods: {
    async fetchGroups() {
      if (!this.userEmail) {
        return;
      }

      try {
        const response = await axios.post(`/api/groups/user`, {
          email: this.userEmail 
        });
        console.log('Fetched groups:', response.data); // Log para verificar las fechas
        this.groups = response.data;
      } catch (error) {
        console.error('Error fetching groups:', error);
        alert('Error loading your groups.');
      }
    },
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
    createGroup() {
      this.$router.push('/groups/create');
    },
    isAdmin(groupEmail) {
      console.log("Admin Email:", groupEmail);
      console.log("User Email:", this.userEmail);
      return groupEmail.trim().toLowerCase() === this.userEmail.trim().toLowerCase();
    },
    async fetchUserGroups() {
      try {
        const email = localStorage.getItem('email'); // Obtén el correo directamente desde la sesión
        if (!email) {
          throw new Error('No email found in session.');
        }

        const response = await axios.post('/api/groups/user', { email });
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
    async closeGroup(groupName) {
      if (!confirm(`Are you sure you want to close the group ${groupName}?`)) {
        return;
      }
      try {
        const response = await axios.put(`/api/groups/close/${groupName}`);
        alert(response.data.message);
        this.fetchUserGroups(); 
      } catch (error) {
        console.error('Error closing group:', error);
        alert('Error al cerrar el grupo.');
      }
    },
    async closeGroupPrivate(groupName) {
      if (!confirm(`Are you sure you want to close the group ${groupName}?`)) {
        return;
      }
      try {
        const response = await axios.post(`/api/groups/closePrivate/${groupName}`);
        alert(response.data.message);
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
        const response = await axios.put(`/api/groups/closeVoting/${groupName}`);
        alert(response.data.message);
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
          preference: this.selectedType,
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
.user-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  height: 100vh;
  background-color: #2c3e50;
  color: white;
  padding: 20px;
}

.cards-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-start;
  gap: 20px; /* Espaciado entre las tarjetas */
  width: 100%;
  max-width: 900px; /* Ancho máximo para las tarjetas */
}


.join-button {
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

.join-button:hover {
  transform: scale(1.03);
}

.join-button:active {
  transform: scale(0.95);
}

.create-group-button{
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

.create-button {
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
  margin-top: 60px; 
  margin-bottom: 20px;
}

.card {
  background: #34495e;
  border-radius: 20px;
  padding: 30px;
  flex: 1; /* Las tarjetas se expanden proporcionalmente */
  box-shadow: rgba(0, 0, 0, 0.1) 0px 30px 30px -20px;
}
.groups-card {
  margin-right: 10px;
}

.invitations-card {
  margin-left: 10px;
}

.group-item {
  margin-bottom: 10px;
  padding: 10px;
  background-color: #3c556c;
  border-radius: 10px;
}

.accept-button,
.leave-button {
  margin-top: 10px;
  padding: 5px 15px;
  background-color: #38a169;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.accept-button:hover,
.leave-button:hover {
  background-color: #2f855a;
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

ul {
  list-style-type: none;
  padding: 0;
  margin-top: 20px;
}

.group-item {
  margin-bottom: 10px;
  padding: 10px;
  background-color: white;
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

.close-button {
  margin-left: 10px;
  padding: 5px 10px;
  background-color: #38a169;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.close-button:hover {
  background-color: #2f855a;
}

.leave-button {
  margin-left: 10px;
  padding: 5px 10px;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.leave-button:hover {
  background-color: #c0392b;
}

.customCheckBoxHolder2 {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.customCheckBoxHolder2 label {
  margin: 5px 0;
  font-size: 1rem;
  cursor: pointer;
}

.date-range {
  display: block;
  margin: 5px 0;
  color: #ecf0f1;
}

.delete-range-button {
  margin-top: 5px;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.add-range-button {
  margin-top: 10px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.accept-button {
  margin-top: 15px;
  padding: 10px 20px;
  background-color: #38a169;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.groups-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.group-card {
  background-color: #2c3e50;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  color: #ecf0f1;
  text-align: left;
}

.invited-groups-container {
  display: flex;
  flex-wrap: wrap; /* Permite que las tarjetas se ajusten en filas */
  gap: 20px; /* Espacio entre las tarjetas */
  justify-content: center; /* Centra las tarjetas */
}

.group-name {
  color: #1abc9c;
  font-size: 1.25rem;
  font-weight: bold;
}

.group-actions {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.admin-actions {
  margin-top: 15px;
}

.invite-input {
  width: calc(100% - 10px);
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #1abc9c;
  margin-right: 5px;
}

.invite-button,
.recommend-button,
.close-button,
.leave-button,
.final-destination-button {
  background-color: #1abc9c;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.invite-button:hover,
.recommend-button:hover,
.close-button:hover,
.leave-button:hover,
.final-destination-button:hover {
  background-color: #16a085;
}

.date-group {
  width: 100%;
  margin-bottom: 10px;
}
</style>
