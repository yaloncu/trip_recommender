<template>
  <div class="group-details-container">
    <h1>{{ $t('group') }} {{ group.name }}</h1>
    <p><strong>{{ $t('audience') }}</strong> {{ group.audience }}</p>
    <p><strong>{{ $t('type') }}</strong> {{ group.type }}</p>
    
    <div v-if="!isMember">
      <p><strong>{{ $t('departureDate') }}</strong> {{ formattedDepartureDate }}</p>
      <p><strong>{{ $t('returnDate') }}</strong> {{ formattedReturnDate }}</p>

      <div>
        <h3>{{ $t('selectType') }}</h3>
        <div class="customCheckBoxHolder2">
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

      <div v-for="(range, index) in dateRanges" :key="index" class="date-range">
        <label>{{ $t('availabilityStartDate') }}</label>
        <input type="date" v-model="range.start" class="input" />

        <label>{{ $t('availabilityEndDate') }}</label>
        <input type="date" v-model="range.end" class="input" />

        <button @click="removeDateRange(index)" class="delete-button">
          {{ $t('delete') }}
        </button>
      </div>
      <button @click="addDateRange" class="add-date-button">
        {{ $t('addDateRange') }}
      </button>

      <button class="join-button" @click="joinGroup">{{ $t('joinAGroup') }}</button>
    </div>
    <div v-else>
      <p>{{ $t('alreadyMember') }}</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      group: {},
      isMember: false,
      vacationTypes: [
        'Cultural', 'Playa', 'Romántica', 'Relax', 
        'Aventura', 'Gastronómica', 'Bienestar', 'Montaña'
      ],
      selectedType: '' ,
      dateRanges: [{ start: "", end: "" }], 
    };
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
      }
    },
    addDateRange() {
      this.dateRanges.push({ start: "", end: "" });
    },
    removeDateRange(index) {
      this.dateRanges.splice(index, 1);
    },
    async joinGroup() {
      const email = localStorage.getItem('email'); 
      if (!email) {
        alert("User not logged in. Please log in to join a group.");
        return;
      }
      try {
        const availabilityStartDates = this.dateRanges.map((range) => range.start);
        const availabilityEndDates = this.dateRanges.map((range) => range.end);
        const requestData = {
          groupName: this.group.name,
          email: email,
          preference: this.selectedType,
          availabilityStartDates: availabilityStartDates,
          availabilityEndDates: availabilityEndDates,
        };
        console.log("Sending data to backend:", requestData);
        const response = await axios.post("/api/groups/joinWithPreferences", requestData);
        alert(`Successfully joined group: ${this.group.name}`);
        this.isMember = true;
      } catch (error) {
        console.error("Error joining group:", error);
        alert("Failed to join group. Please try again.");
      }
    },
  },
  mounted() {
    this.fetchGroupDetails();
  },
};
</script>

<style scoped>
.group-details-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px;
  background-color: #34495e;
  color: white;
  border-radius: 10px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 20px 20px -15px;
}

.join-button,
.add-date-button,
.delete-button {
  margin-top: 10px;
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  font-size: 1.2rem;
  padding: 10px 20px;
  border-radius: 10px;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.add-date-button {
  margin-bottom: 10px;
}

.join-button:hover,
.add-date-button:hover,
.delete-button:hover {
  transform: scale(1.03);
}

.input {
  width: 100%;
  margin: 10px 0;
  padding: 10px;
  border-radius: 10px;
  border: 2px solid #1abc9c;
}
</style>
