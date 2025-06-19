<template>
  <div class="page-with-menu">
    <SideMenu />
    <div class="participants-container">
      <h1 class="main-title">{{ $t('participantsTitle') }}</h1>
      <p class="total">{{ $t('totalParticipants') }}: {{ participants.length }}</p>

      <div class="card">
        <p v-if="participants.length === 0">{{ $t('noParticipants') }}</p>
        <ul v-else class="participants-list">
          <li
            v-for="(participant, index) in participants"
            :key="index"
            class="participant-item"
          >
            <div class="participant-info">
              <strong>{{ participant.name || participant.email }}</strong>
              <span v-if="participant.name && participant.email">
                ({{ participant.email }})
              </span>
              <p v-if="participant.aboutMe">
                {{ $t('aboutMe') }}: {{ participant.aboutMe }}
              </p>
              <p v-if="participant.gender">
                {{ $t('gender') }}: {{ participant.gender }}
              </p>
              <p v-if="participant.age">
                {{ $t('age') }}: {{ participant.age }}
              </p>
            </div>
          </li>
        </ul>
      </div>
    </div>
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
      participants: []
    };
  },
  created() {
    this.fetchParticipants();
  },
  methods: {
    async fetchParticipants() {
      try {
        const groupId = this.$route.params.id;
        const response = await axios.get(`/api/groups/participants/${groupId}`);
        this.participants = response.data;
      } catch (error) {
        console.error('Error fetching participants:', error);
        alert('Error al cargar los participantes.');
      }
    }
  }
};
</script>

<style scoped>
.page-with-menu {
  display: flex;
  min-height: 100vh;
}

.participants-container {
  padding: 100px 40px;
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

.main-title {
  color: #1abc9c;
  font-size: 2.2rem;
  margin-bottom: 10px;
  text-align: center;
}

.total {
  text-align: center;
  margin-bottom: 20px;
  font-size: 1.2rem;
}

.card {
  background: #34495e;
  padding: 30px;
  border-radius: 20px;
  max-width: 600px;
  margin: 0 auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.participants-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.participant-item {
  padding: 15px;
  border-bottom: 1px solid #1abc9c;
}

.participant-info {
  font-size: 1.1rem;
  color: #ecf0f1;
}
</style>
