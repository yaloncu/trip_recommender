<template>
    <div class="user-container">
      <h1>Your Groups</h1>
      <input v-model="userEmail" type="email" placeholder="Enter your email" />
      <button @click="fetchUserGroups">Fetch Groups</button>
      
      <div v-if="groups.length">
        <ul>
          <li v-for="group in groups" :key="group.groupId">
            {{ group.groupName }} - Preference: {{ group.preference }}
            <button @click="viewRecommendation(group.groupId)">Ver Recomendación</button>
          </li>
        </ul>
      </div>
      <div v-else>
        <p>No estás en ningún grupo.</p>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        userEmail: '', // Variable para almacenar el email ingresado por el usuario
        groups: [] // Lista de grupos
      };
    },
    methods: {
      // Método para obtener los grupos del usuario
      async fetchUserGroups() {
        if (!this.userEmail) {
        return;
        }
        
        try {
            const response = await axios.post(`/api/groups/user`, {
              email: this.userEmail 
            });
            this.groups = response.data;
        } catch (error) {
            console.error('Error fetching groups:', error);
            alert('Error loading your groups.');
        }
      },
      // Método para obtener las recomendaciones de un grupo
      async viewRecommendation(groupId) {
        try {
            const response = await axios.get(`/api/recommendations/${groupId}`);
            const recommendations = response.data;

            if (Array.isArray(recommendations)) {
                if (recommendations.length > 0) {
                    alert(`Recomendaciones: ${recommendations.join(', ')}`);
                } else {
                    alert('No hay recomendaciones disponibles.');
                }
            } else {
                console.error('Error: La respuesta no es un array.');
                alert('Error al obtener las recomendaciones.');
            }
        } catch (error) {
            console.error('Error fetching recommendations:', error);
            alert('Error al obtener las recomendaciones.');
        }
      }

    },
    mounted() {
      // Cargar los grupos del usuario al montar el componente
      this.fetchUserGroups();
    }
  };
  </script>
  
  <style scoped>
  .user-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
  }
  ul {
    list-style-type: none;
    padding: 0;
  }
  li {
    margin-bottom: 10px;
  }
  button {
    margin-left: 10px;
    padding: 5px 10px;
    background-color: #4CAF50;
    color: white;
    border: none;
    cursor: pointer;
  }
  button:hover {
    background-color: #45a049;
  }
  </style>
  