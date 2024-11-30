<template>
  <div
    class="chat-container"
    :class="{ minimized: isMinimized, elevated: isChatBotExpanded }"
  >
    <div v-if="isMinimized" class="chat-icon" @click="handleChatIconClick">
      <span class="animated-emoji">💌</span>
    </div>

    <div v-else class="chat-box">
      <div class="header">
        <span v-if="!chatRoomId">채팅방 목록</span>
        <span v-else>{{ chatRoomName }}</span>
        <button @click="toggleChat" aria-label="채팅창 최소화/확대">-</button>
      </div>

      <div v-if="!chatRoomId" class="chat-room-list">
        <h4>채팅방 목록</h4>
        <ul>
          <li
            v-for="room in chatRooms"
            :key="room.roomId"
            class="chat-room-item"
          >
            <div class="room-info" @click="selectRoom(room)">
              {{ room.roomName }}
            </div>
          </li>
        </ul>
        <div class="create-room">
          <input v-model="newRoomName" placeholder="채팅방 이름 입력" />
          <button @click="createRoom" class="btn btn-primary">방 생성</button>
        </div>
      </div>

      <div v-if="chatRoomId" class="chat-content">
        <div class="messages" ref="messages">
          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="{
              mine: message.sender === username,
              theirs: message.sender !== username,
            }"
          >
            <div class="bubble">
              <strong>{{ message.sender }}</strong
              >: {{ message.content }}
            </div>
          </div>
        </div>
        <div class="input-section">
          <div class="input-box">
            <input
              v-model="newMessage"
              @keyup.enter="sendMessage"
              placeholder="메시지를 입력하세요..."
            />
            <button @click="sendMessage" class="btn btn-send">전송</button>
          </div>
          <button @click="leaveRoom" class="btn btn-leave">나가기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import { BASE_URL } from "@/assets/js/Rest";

export default {
  props: {
    isChatBotExpanded: {
      type: Boolean,
      required: true,
    },
  },
  data() {
    return {
      stompClient: null,
      isMinimized: true,
      isAuthenticated: false,
      chatRooms: [],
      chatRoomId: null,
      chatRoomName: null,
      newRoomName: "",
      username: "",
      userType: 1,
      messages: [],
      newMessage: "",
      connected: false,
      currentSubscription: null,
    };
  },
  methods: {
    handleChatIconClick() {
      if (this.isAuthenticated) {
        this.toggleChat();
      } else {
        alert("로그인이 필요합니다.");
      }
    },
    toggleChat() {
      this.isMinimized = !this.isMinimized;
    },
    async loadUserInfo() {
      const token = localStorage.getItem("accessToken");
      if (!token) {
        this.isAuthenticated = false;
        console.warn("JWT 토큰이 존재하지 않습니다.");
        return;
      }

      try {
        const response = await axios.get(`${BASE_URL}/user/info`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.username = response.data.userName;
        this.userType = response.data.type;
        this.isAuthenticated = true;
      } catch (error) {
        console.error("사용자 정보 로드 실패:", error);
        alert("사용자 정보를 불러오지 못했습니다. 다시 로그인해주세요.");
        localStorage.removeItem("accessToken");
        this.isAuthenticated = false;
      }
    },
    connect() {
      const socket = new SockJS(`${BASE_URL}/websocket`);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({}, this.onConnected, this.onError);
    },
    onConnected() {
      this.connected = true;
      this.loadChatRooms();
    },
    onError(error) {
      console.error("WebSocket 연결 실패:", error);
      this.connected = false;
    },
    loadChatRooms() {
      axios
        .get(`${BASE_URL}/chat-room`)
        .then((response) => {
          this.chatRooms = response.data;
        })
        .catch((error) => {
          console.error("채팅방 목록 로드 실패:", error);
        });
    },
    createRoom() {
      if (!this.newRoomName.trim()) return;

      axios
        .post(`${BASE_URL}/chat-room/create`, {
          roomName: this.newRoomName,
          createdBy: this.username,
        })
        .then((response) => {
          this.chatRooms.push(response.data);
          this.selectRoom(response.data);
          this.newRoomName = "";
        })
        .catch((error) => {
          console.error("채팅방 생성 실패:", error);
        });
    },
    selectRoom(room) {
      this.chatRoomId = room.roomId;
      this.chatRoomName = room.roomName;
      this.messages = [];
      this.subscribeToRoom(room.roomId);
    },
    subscribeToRoom(roomId) {
      if (this.currentSubscription) {
        this.currentSubscription.unsubscribe();
      }

      this.currentSubscription = this.stompClient.subscribe(
        `/topic/chatRoom/${roomId}`,
        (message) => {
          this.messages.push(JSON.parse(message.body));
          this.scrollToBottom();
        }
      );

      this.chatRoomId = roomId;
      this.loadMessages(roomId);
      axios.post(`${BASE_URL}/chat-room/${roomId}/enter`, {
        userId: this.username,
      });
    },
    loadMessages(roomId) {
      axios
        .get(`${BASE_URL}/chat-room/${roomId}/messages`)
        .then((response) => {
          this.messages = response.data;
          this.$nextTick(() => {
            this.scrollToBottom();
          });
        })
        .catch((error) => {
          console.error("메시지 로드 실패:", error);
        });
    },
    sendMessage() {
      if (!this.newMessage.trim()) return;

      const message = {
        chatRoomId: this.chatRoomId,
        sender: this.username,
        content: this.newMessage,
        type: "CHAT",
      };

      try {
        this.stompClient.send("/app/chat.send", {}, JSON.stringify(message));
        this.newMessage = "";
      } catch (error) {
        console.error("메시지 전송 실패:", error);
      }
    },
    leaveRoom() {
      if (this.currentSubscription) {
        this.currentSubscription.unsubscribe();
        this.currentSubscription = null;
      }

      axios
        .post(`${BASE_URL}/chat-room/${this.chatRoomId}/leave`, {
          userId: this.username,
        })
        .then(() => {
          this.chatRoomId = null;
          this.chatRoomName = null;
          this.messages = [];
          this.loadChatRooms();
        })
        .catch((error) => {
          console.error("방 나가기 실패:", error);
        });
    },
    deleteRoom(roomId) {
      if (confirm("정말 이 채팅방을 삭제하시겠습니까?")) {
        const token = localStorage.getItem("accessToken");
        axios
          .delete(`${BASE_URL}/chat-room/${roomId}`, {
            headers: {
              Authorization: `Bearer ${token}`,
              username: this.username, // 방장 확인을 위해 username 헤더 전송
            },
          })
          .then(() => {
            alert("채팅방이 삭제되었습니다.");
            this.chatRooms = this.chatRooms.filter(
              (room) => room.roomId !== roomId
            );
            if (this.chatRoomId === roomId) {
              this.chatRoomId = null;
              this.chatRoomName = null;
              this.messages = [];
            }
          })
          .catch((error) => {
            console.error("채팅방 삭제 실패:", error);
            alert("채팅방 삭제에 실패했습니다.");
          });
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const messagesDiv = this.$refs.messages;
        if (messagesDiv) {
          messagesDiv.scrollTop = messagesDiv.scrollHeight;
        }
      });
    },
  },
  watch: {
    isChatBotExpanded(newVal) {
      if (newVal && !this.isMinimized) {
        this.isMinimized = true;
      }
    },
  },
  async mounted() {
    await this.loadUserInfo();
    if (this.isAuthenticated) {
      this.connect();
    }
  },
};
</script>

<style scoped>
.chat-container {
  position: fixed;
  right: 20px;
  z-index: 1001; /* ChatBot의 z-index(1000)보다 높게 설정 */
  transition: bottom 0.3s ease; /* 부드러운 위치 이동 */
  bottom: 90px; /* 챗봇이 최소화 상태일 때의 위치 */
}

.chat-container.elevated {
  bottom: 470px; /* 챗봇이 확장되었을 때의 위치 (20px + 450px) */
}

.chat-box {
  width: 400px;
  height: 500px;
  background: #ffffff; /* 더 밝은 배경 */
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15); /* 부드러운 그림자 */
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 내부 넘침 방지 */
}

.header {
  background: linear-gradient(45deg, #007bff, #0056b3); /* 그라데이션 */
  color: white;
  padding: 12px;
  font-size: 16px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 10px 10px 0 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 헤더 그림자 */
}

.chat-room-list {
  padding: 10px;
  max-height: 500px; /* 목록 높이 제한 */
  overflow-y: auto; /* 스크롤 활성화 */
  scrollbar-width: thin; /* 스크롤바 얇게 설정 (Firefox) */
  scrollbar-color: #007bff transparent; /* 스크롤 색상 */
}

.chat-room-list::-webkit-scrollbar {
  width: 8px;
}

.chat-room-list::-webkit-scrollbar-track {
  background: transparent;
}

.chat-room-list::-webkit-scrollbar-thumb {
  background-color: #007bff;
  border-radius: 4px;
  border: 2px solid transparent;
}

.chat-room-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border: 1px solid #eee;
  border-radius: 8px;
  margin-bottom: 8px;
  background-color: #f9f9f9; /* 밝은 배경 */
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease; /* 부드러운 애니메이션 */
}

.chat-room-item:hover {
  background-color: #e6f7ff; /* 하이라이트 색상 */
  transform: scale(1.02); /* 약간 확대 */
}

.room-info {
  flex: 1;
}

.btn-delete {
  background-color: red;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.btn-delete:hover {
  background-color: darkred;
  transform: scale(1.05);
}

.create-room {
  display: flex;
  align-items: center;
  gap: 10px; /* 요소 간격 */
  padding: 10px;
}

.create-room input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  background: #f7f7f7; /* 연한 배경 */
}

.create-room .btn-primary {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease; /* 버튼 애니메이션 */
}

.create-room .btn-primary:hover {
  background-color: #0056b3;
  transform: scale(1.05); /* 약간 확대 */
}

.chat-content {
  padding: 10px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  overflow: hidden;
  background: #fafafa; /* 부드러운 배경 */
}

.messages {
  flex: 1;
  overflow-y: auto; /* 스크롤 활성화 */
  margin-bottom: 10px;
  padding: 10px;
  background: #fdfdfd; /* 메시지 영역 배경 */
  border-radius: 8px;
}

.messages::-webkit-scrollbar {
  width: 8px;
}

.messages::-webkit-scrollbar-track {
  background: transparent;
}

.messages::-webkit-scrollbar-thumb {
  background-color: #007bff;
  border-radius: 4px;
  border: 2px solid transparent;
}

.messages .mine {
  text-align: right;
  margin: 5px 0;
}

.messages .theirs {
  text-align: left;
  margin: 5px 0;
}

.messages .bubble {
  display: inline-block;
  padding: 12px 16px;
  border-radius: 20px;
  max-width: 70%;
  word-wrap: break-word;
  font-size: 14px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 말풍선 그림자 */
}

.messages .mine .bubble {
  background: linear-gradient(45deg, #007bff, #0056b3); /* 그라데이션 */
  color: white;
  text-align: left;
}

.messages .theirs .bubble {
  background: #f1f1f1;
  color: #333;
}

.input-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.input-box {
  display: flex;
  align-items: center;
  gap: 10px; /* 요소 간격 */
}

.input-box input {
  flex: 1;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  background: #f7f7f7;
}

.btn-send {
  background: #007bff;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.btn-send:hover {
  background: #0056b3;
  transform: scale(1.05);
}

.btn-leave {
  background: #ff4d4f;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.btn-leave:hover {
  background: #d9363e;
  transform: scale(1.05);
}

.chat-icon {
  width: 50px;
  height: 50px;
  background: linear-gradient(45deg, #ffffff, #ffffff); /* 그라데이션 */
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 24px;
  transition: transform 0.2s ease, transform 0.2s ease-in-out;
}

.animated-emoji {
  font-size: 2rem;
  animation: bounce 2s infinite, glow 1.5s infinite alternate;
}

@keyframes bounce {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

@keyframes glow {
  0% {
    text-shadow: 0 0 5px rgba(255, 182, 193, 0.8),
      0 0 10px rgba(255, 105, 180, 0.6);
  }
  100% {
    text-shadow: 0 0 10px rgba(255, 105, 180, 1),
      0 0 20px rgba(255, 182, 193, 0.8);
  }
}

.chat-icon:hover {
  transform: scale(1.1); /* 약간 확대 */
}
</style>
