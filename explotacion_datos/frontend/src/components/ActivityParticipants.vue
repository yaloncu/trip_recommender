<template>
  <div class="page-with-menu">
    <SideMenu />
    <main class="participants-container" role="main" aria-labelledby="participantsTitle">
      <h1 id="participantsTitle" class="main-title">
        {{ $t('participantsTitle') }} ({{ participants.length }})
      </h1>

      <section class="card" role="region" :aria-label="$t('participantsTitle')">
        <p v-if="participants.length === 0">{{ $t('noParticipants') }}</p>
        <ul v-else class="participants-list">
          <li
            v-for="(participant, index) in participants"
            :key="index"
            class="participant-item"
            :aria-label="participant.name || participant.email"
          >
            <div class="participant-info">
              <strong>{{ participant.name || participant.email }}</strong>
              <span v-if="participant.name && participant.email">
                ({{ participant.email }})
              </span>
              <ul class="details">
                <li v-if="participant.aboutMe">
                  {{ $t('aboutMe') }}: {{ participant.aboutMe }}
                </li>
                <li v-if="participant.gender">
                  {{ $t('gender') }}: {{ participant.gender }}
                </li>
                <li v-if="participant.age !== null && participant.age !== undefined">
                  {{ $t('age') }}: {{ participant.age }}
                </li>
              </ul>
            </div>
          </li>
        </ul>
      </section>
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
      participants: []
    };
  },
  created() {
    this.fetchParticipants();
  },
  methods: {
    async fetchParticipants() {
      try {
        const activityId = this.$route.params.id;
        const response = await axios.get(`/api/activities/participants/${activityId}`);
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
  margin-bottom: 30px;
  text-align: center;
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

.details {
  margin-top: 10px;
  padding-left: 15px;
  font-size: 0.95rem;
  color: #bdc3c7;
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
