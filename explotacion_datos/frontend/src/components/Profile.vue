<template>
  <div class="page-with-menu">
    <SideMenu />
    <div class="profile-container">
      <h1 class="main-title">{{ $t('yourProfile') }}</h1>

      <div class="profile-card">
        <div class="profile-field" v-for="key in visibleFields" :key="key">
          <label class="field-label" :for="key">{{ $t(key) }}:</label>
          <div class="field-value">
            <input
              v-if="editing[key]"
              v-model="editableFields[key]"
              class="edit-input"
              :id="key"
              :type="key === 'age' ? 'number' : 'text'"
              :aria-label="$t(key)"
            />
            <span v-else :id="key">{{ editableFields[key] }}</span>

            <button
              @click="toggleEdit(key)"
              class="edit-btn"
              :aria-label="editing[key] ? $t('stopEditing') + ' ' + $t(key) : $t('edit') + ' ' + $t(key)"
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none"
                stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                class="lucide lucide-pencil">
                <path d="m18 2 4 4"/>
                <path d="m2 22 4-1 11-11-3-3L3 18z"/>
              </svg>
            </button>
          </div>
        </div>

        <button class="save-button" @click="saveProfile">{{ $t('saveChanges') }}</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import SideMenu from '@/components/SideMenu.vue';

export default {
  name: 'UserProfile',
  components: { SideMenu },
  data() {
    return {
      visibleFields: ['name', 'email', 'aboutMe', 'gender', 'age'],
      editableFields: {
        name: '',
        email: '',
        age: '',
        gender: '',
        aboutMe: ''
      },
      editing: {
        name: false,
        email: false,
        age: false,
        gender: false,
        aboutMe: false
      },
      fieldLabels: {
        name: 'Nombre',
        email: 'Email',
        age: 'Edad',
        gender: 'Género',
        aboutMe: 'Sobre mí'
      }
    };
  },
  mounted() {
    const email = localStorage.getItem('email');
    axios.get(`/api/users/${email}`)
      .then(response => {
        this.editableFields = response.data;
      })
      .catch(error => {
        console.error('Error al cargar perfil:', error);
      });
  },
  methods: {
    toggleEdit(field) {
      this.editing[field] = !this.editing[field];
    },
    async saveProfile() {
      try {
        await axios.post('/api/users/update', this.editableFields);
        alert('Perfil actualizado correctamente');
        Object.keys(this.editing).forEach(field => this.editing[field] = false);
      } catch (error) {
        console.error('Error al guardar perfil:', error);
        alert('Error al guardar perfil');
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

.profile-container {
  flex-grow: 1;
  padding: 100px 40px;
  background-image: url('@/assets/mapamundi.png');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  background-color: #2c3e50;
  color: white;
  font-family: 'Poppins', sans-serif;
}

.main-title {
  color: #1abc9c;
  font-size: 2.5rem;
  text-align: center;
  margin-bottom: 40px;
}

.profile-card {
  background: #34495e;
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  max-width: 600px;
  margin: auto;
}

.profile-field {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.field-label {
  font-weight: bold;
  width: 120px;
}

.field-value {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
}

.edit-input {
  flex: 1;
  padding: 10px;
  border-radius: 10px;
  border: none;
  font-family: 'Poppins', sans-serif;
}

.edit-btn {
  background: none;
  border: none;
  cursor: pointer;
  color: #1abc9c;
  transition: transform 0.2s;
}

.edit-btn:hover {
  transform: scale(1.1);
}

.save-button {
  background: linear-gradient(45deg, #16a085 0%, #1abc9c 100%);
  color: white;
  font-size: 1.2rem;
  padding: 12px 30px;
  border-radius: 20px;
  border: none;
  cursor: pointer;
  margin-top: 30px;
  width: 100%;
  font-family: 'Poppins', sans-serif;
  transition: all 0.2s ease;
}

.save-button:hover {
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

.edit-btn, .save-button {
  min-width: 44px;
  min-height: 44px;
}

</style>
