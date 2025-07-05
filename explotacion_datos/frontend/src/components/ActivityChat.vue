<template>
  <div class="page-with-menu">
    <SideMenu />
    <main class="chat-container" role="main" aria-labelledby="chatTitle">

      <h1 id="chatTitle" class="sr-only">{{ $t('activityChat') }}</h1>

      <section class="messages" role="log" aria-live="polite" aria-relevant="additions">
        <article
          v-for="(msg, index) in messages"
          :key="index"
          :class="['message', msg.sender === username ? 'sent' : 'received']"
          :aria-label="`${msg.sender === username ? $t('you') : msg.sender}: ${msg.content}`">
          <div class="bubble">
            <div class="sender">{{ msg.sender === username ? $t('you') : msg.sender }}</div>
            <div class="content">{{ msg.content }}</div>
            <div class="timestamp">{{ formatTimestamp(msg.timestamp) }}</div>
          </div>
        </article>
      </section>

      <form class="messageBox" role="form" aria-label="{{ $t('writeMessageLabel') }}" @submit.prevent="sendMessage">
        <label for="messageInput" class="sr-only">{{ $t('writeMessageLabel') }}</label>
        <input
          required
          :placeholder="$t('writeMessagePlaceholder')"
          type="text"
          id="messageInput"
          v-model="newMessage"
          @keyup.enter="sendMessage"
          :aria-label="$t('writeMessageLabel')"
        />
        <button id="sendButton" type="submit" :aria-label="$t('sendMessage')">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 664 663" aria-hidden="true">
            <path
              stroke-linejoin="round"
              stroke-linecap="round"
              stroke-width="33.67"
              stroke="#1abc9c"
              d="M646.293 331.888L17.7538 17.6187L155.245 331.888
                M646.293 331.888L17.753 646.157L155.245 331.888
                M646.293 331.888L318.735 330.228L155.245 331.888"
            />
          </svg>
        </button>
      </form>
    </main>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import axios from 'axios';
import SideMenu from '@/components/SideMenu.vue';

let stompClient = null;

export default {
  components: {
    SideMenu,
  },
  data() {
    return {
      messages: [],
      newMessage: '',
      activityId: '',
      username: '',
    };
  },
  created() {
    this.activityId = this.$route.params.activityId;
    this.username = localStorage.getItem('email') || 'Anon';

    axios.get(`/api/messages/activity/${this.activityId}`)
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
      stompClient.subscribe(`/topic/activity/${this.activityId}`, (msg) => {
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

      stompClient.send(`/app/chat.sendActivityMessage/${this.activityId}`, {}, JSON.stringify(message));
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
/* Igual que el estilo que ya estás usando para mantener coherencia visual */

.page-with-menu {
  display: flex;
  min-height: 100vh;
}

.chat-container {
  padding: 30px;
  background-color: #2c3e50;
  color: white;
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-sizing: border-box;
}

.messages {
  width: 100%;
  max-width: 800px;
  background: #34495e;
  border-radius: 15px;
  padding: 15px;
  margin-bottom: 20px;
  height: 450px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  font-family: 'Poppins', sans-serif;
}

.message {
  display: flex;
  width: 100%;
}

.bubble {
  padding: 10px 15px;
  border-radius: 15px;
  max-width: 75%;
  word-wrap: break-word;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  font-family: 'Poppins', sans-serif;
}

.sent {
  justify-content: flex-end;
}

.sent .bubble {
  background-color: #1abc9c;
  color: white;
  border-bottom-right-radius: 0;
}

.received {
  justify-content: flex-start;
}

.received .bubble {
  background-color: #ecf0f1;
  color: #2c3e50;
  border-bottom-left-radius: 0;
}

.sender {
  font-weight: bold;
  margin-bottom: 5px;
}

.content {
  font-size: 1rem;
}

.timestamp {
  font-size: 0.75rem;
  color: #2c3e50;
  text-align: right;
  margin-top: 5px;
}

.messageBox {
  width: fit-content;
  max-width: 90%;
  height: 50px;
  display: flex;
  align-items: center;
  background-color: #34495e;
  padding: 0 15px;
  border-radius: 12px;
  border: 1px solid #1abc9c;
  margin-top: 20px;
}

input#messageInput {
  width: 300px;
  height: 100%;
  background-color: transparent;
  outline: none;
  border: none;
  padding-left: 10px;
  color: white;
  font-family: 'Poppins', sans-serif;
  font-size: 1rem;
}

#sendButton {
  background-color: transparent;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
}

#sendButton svg {
  height: 24px;
}

#sendButton:hover svg path {
  stroke: #16a085;
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
