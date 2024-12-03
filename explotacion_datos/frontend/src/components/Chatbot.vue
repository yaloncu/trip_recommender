<template>
    <div class="chatbot-container">
      <div class="chatbot-messages">
        <div v-for="(msg, index) in messages" :key="index" :class="msg.sender">
          {{ msg.text }}
        </div>
      </div>
      <form @submit.prevent="sendMessage">
        <input v-model="userInput" placeholder="Escribe un mensaje..." />
        <button type="submit">Enviar</button>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        messages: [], // Almacena los mensajes del usuario y del chatbot
        userInput: "", // Entrada del usuario
      };
    },
    methods: {
      async sendMessage() {
        if (this.userInput.trim() === "") return;
  
        // Añadir mensaje del usuario a la vista
        this.messages.push({ sender: "user", text: this.userInput });
  
        const response = await fetch("http://localhost:5005/webhooks/rest/webhook", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            sender: "user1", // Identificador del usuario
            message: this.userInput,
          }),
        });
  
        const botResponses = await response.json();
        botResponses.forEach((botMsg) => {
          this.messages.push({ sender: "bot", text: botMsg.text });
        });
  
        // Limpiar la entrada del usuario
        this.userInput = "";
      },
    },
  };
  </script>
  
  <style>
  .chatbot-container {
    max-width: 400px;
    margin: 0 auto;
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 16px;
  }
  
  .chatbot-messages {
    height: 300px;
    overflow-y: auto;
    margin-bottom: 16px;
    border: 1px solid #ddd;
    padding: 8px;
    background-color: #f9f9f9;
  }
  
  .user {
    text-align: right;
    color: blue;
  }
  
  .bot {
    text-align: left;
    color: green;
  }
  
  form {
    display: flex;
  }
  
  input {
    flex: 1;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  button {
    margin-left: 8px;
    padding: 8px 12px;
    border: none;
    background-color: #007bff;
    color: white;
    border-radius: 4px;
    cursor: pointer;
  }
  </style>
  