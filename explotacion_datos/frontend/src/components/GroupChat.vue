<template>
  <div class="chat-container">
    <h2>Chat del grupo: {{ groupName }}</h2>
    <div class="messages">
      <div v-for="(msg, index) in messages" :key="index" class="message">
        <strong>{{ msg.sender }}:</strong> {{ msg.content }}
        <div class="timestamp">{{ formatTimestamp(msg.timestamp) }}</div>
      </div>
    </div>
    <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Escribe un mensaje..." />
    <button @click="sendMessage">Enviar</button>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import axios from 'axios';

let stompClient = null;

export default {
  data() {
    return {
      messages: [],
      newMessage: '',
      groupName: '',
      username: '',
    };
  },
  created() {
    this.groupName = this.$route.params.groupName;
    this.username = localStorage.getItem('email') || 'Anon';

    axios.get(`/api/messages/${this.groupName}`)
        .then(res => {
        this.messages = res.data;
        this.$nextTick(() => {
            const messagesContainer = this.$el.querySelector('.messages');
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        });
        })
        .catch(err => {
        console.error('Error al cargar mensajes anteriores:', err);
        });

    const socket = new SockJS('http://localhost:8081/chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        stompClient.subscribe(`/topic/group/${this.groupName}`, (msg) => {
        this.messages.push(JSON.parse(msg.body));
        this.$nextTick(() => {
            const messagesContainer = this.$el.querySelector('.messages');
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        });
        });
    });
    },
  methods: {
    sendMessage() {
      if (!this.newMessage.trim()) return;

      const message = {
        sender: this.username,
        content: this.newMessage,
      };

      stompClient.send(`/app/chat.sendMessage/${this.groupName}`, {}, JSON.stringify(message));
      this.newMessage = '';
    },
    formatTimestamp(timestamp) {
        if (!timestamp) return '';
        const date = new Date(timestamp);
        return date.toLocaleString();
    }
  }
};
</script>

<style scoped>
.chat-container {
  padding: 30px;
  background-color: #2c3e50;
  color: white;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.messages {
  width: 100%;
  max-width: 600px;
  background: #34495e;
  border-radius: 15px;
  padding: 15px;
  margin-bottom: 20px;
  height: 300px;
  overflow-y: auto;
}

.message {
  margin: 10px 0;
}

input {
  width: 80%;
  padding: 10px;
  border-radius: 10px;
  border: none;
  margin-right: 10px;
}

button {
  padding: 10px 20px;
  background-color: #1abc9c;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}
</style>
