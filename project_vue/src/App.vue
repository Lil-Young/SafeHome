<!-- src/App.vue -->
<template>
  <v-app>
    <v-main>
      <router-view />
      <!-- ChatBot 컴포넌트에 이벤트 리스너 추가 -->
      <ChatBot @toggleChatBot="toggleChatBot" />
      <!-- Chat 컴포넌트에 isChatBotExpanded prop 전달 -->
      <Chat :isChatBotExpanded="isChatBotExpanded" />
    </v-main>
  </v-app>
</template>

<script>
import ChatBot from "./components/ChatBot.vue";
import Chat from "./components/Chat.vue";

export default {
  name: "App",
  components: {
    ChatBot,
    Chat
  },
  data() {
    return {
      isChatBotExpanded: false // 챗봇의 확장 상태 관리
    };
  },
  methods: {
    // ChatBot에서 이벤트를 받아 상태 업데이트
    toggleChatBot(isExpanded) {
      this.isChatBotExpanded = isExpanded;
    }
  }
};
</script>

<style scoped>
/* 필요 시 App.vue 스타일 추가 */
</style>
