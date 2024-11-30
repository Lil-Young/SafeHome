<template>
  <div class="chatbot-container" :class="{ minimized: isMinimized }">
    <div v-if="!isMinimized" class="chatbot" @click.stop>
      <div class="chatbot-header">
        <div class="header-title">
          <span>세이프홈 챗봇</span>
          <span
            class="status-indicator"
            :class="{ connected: isConnected }"
            aria-label="GPT 연결 상태"
            title="GPT가 연결되었습니다."
          ></span>
        </div>
        <button
          class="close-btn"
          @click="toggleMinimize"
          aria-label="챗봇 창 닫기"
        >
          -
        </button>
      </div>
      <div class="chatbot-body">
        <div class="messages" ref="messages">
          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="message.sender"
          >
            <div class="message-bubble">
              <span class="sender-label">
                {{ message.sender === "bot" ? "세이프홈" : "" }}
              </span>
              <span class="message-text">
                <span v-if="message.type !== 'image'">{{ message.text }}</span>
              </span>
            </div>
          </div>
          <div v-if="isTyping" class="bot typing">
            <div class="message-bubble">
              <span class="sender-label">세이프홈</span>
              <span class="message-text">
                <span class="dot"></span>
                <span class="dot"></span>
                <span class="dot"></span>
              </span>
            </div>
          </div>
        </div>
        <div class="input-container" @click.stop>
          <input
            type="text"
            v-model="userInput"
            @keyup.enter="sendMessage"
            placeholder="매매, 월세, 전세, 지역 정보를 물어보세요..."
            aria-label="메시지 입력"
          />
          <button
            @click="sendMessage"
            aria-label="메시지 전송"
            class="send-button"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              class="arrow-icon"
            >
              <line x1="5" y1="12" x2="19" y2="12"></line>
              <polyline points="12 5 19 12 12 19"></polyline>
            </svg>
          </button>
        </div>
      </div>
    </div>
    <div
      v-else
      class="chatbot-icon"
      @click="toggleMinimize"
      aria-label="챗봇 열기"
    >
      ✨
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ChatBot",
  data() {
    return {
      isMinimized: true,
      userInput: "",
      isConnected: false,
      isTyping: false,
      messages: [
        {
          sender: "bot",
          text: "안녕하세요! 저는 '세이프홈 챗봇'입니다. 대한민국 부동산 매매, 전세, 월세 정보와 지역 특징, 지리를 빠삭하게 알고 있습니다. 궁금한 점을 물어보세요!",
          type: "text",
        },
      ],
    };
  },
  methods: {
    toggleMinimize() {
      this.isMinimized = !this.isMinimized;
      this.$emit("toggleChatBot", !this.isMinimized);
      if (!this.isMinimized && !this.isConnected) {
        this.checkConnection();
      }
    },
    async checkConnection() {
      this.isConnected = true;
    },
    async sendMessage() {
      if (this.userInput.trim() === "") return;

      const userMessage = this.userInput.trim();
      this.messages.push({ sender: "user", text: userMessage, type: "text" });
      this.userInput = "";

      this.isTyping = true; // 로딩 상태 표시

      try {
        // 1단계: GPT에게 질문의 관련성을 판단하도록 요청
        const relevanceCheckPayload = {
          model: "gpt-4o-2024-11-20",
          messages: [
            {
              role: "system",
              content:
                "당신은 부동산 전문 챗봇입니다. 사용자 질문이 한국 부동산, 지역 정보, 인사, 세이프홈 소개, 또는 주거 환경과 관련이 있는지 판단하세요. " +
                "만약 관련 있다면 'Yes', 관련 없다면 'No'라고만 답변하세요.",
            },
            { role: "user", content: userMessage },
          ],
        };

        const relevanceCheckResponse = await axios.post(
          "https://api.openai.com/v1/chat/completions",
          relevanceCheckPayload,
          {
            headers: {
              Authorization: `Bearer ${process.env.VUE_APP_OPENAI_API_KEY}`,
              "Content-Type": "application/json",
            },
          }
        );

        const relevance =
          relevanceCheckResponse.data.choices[0].message.content.trim();

        // 관련성이 없다고 판단된 경우 처리
        if (relevance.toLowerCase() === "no") {
          this.messages.push({
            sender: "bot",
            text: "죄송합니다. 저는 부동산, 지역 정보, 또는 주거 환경과 관련된 질문에만 답변할 수 있습니다.",
            type: "text",
          });
          this.isTyping = false;
          return;
        }

        // 2단계: 부동산 관련된 질문만 GPT에 요청
        const responsePayload = {
          model: "gpt-4o-2024-11-20",
          messages: [
            {
              role: "system",
              content:
                "안녕하세요! 세이프홈 챗봇입니다. " +
                "부동산, 지역 정보, 또는 주거 환경에 대한 질문에만 답변하세요. " +
                "질문에 부동산과 무관한 요소가 포함되어 있으면, 무관한 부분은 무시하고 부동산 관련 정보에만 답변하세요.",
            },
            { role: "user", content: userMessage },
          ],
        };

        const response = await axios.post(
          "https://api.openai.com/v1/chat/completions",
          responsePayload,
          {
            headers: {
              Authorization: `Bearer ${process.env.VUE_APP_OPENAI_API_KEY}`,
              "Content-Type": "application/json",
            },
          }
        );

        const botMessage = response.data.choices[0].message.content.trim();
        this.messages.push({ sender: "bot", text: botMessage, type: "text" });
      } catch (error) {
        console.error("Error:", error);
        this.messages.push({
          sender: "bot",
          text: "오류가 발생했습니다. 다시 시도해주세요.",
          type: "text",
        });
      } finally {
        this.isTyping = false; // 로딩 종료
        this.scrollToBottom();
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
};
</script>
<style scoped>
.chatbot-container {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
}

.chatbot {
  width: 350px;
  height: 450px;
  background-color: rgba(255, 255, 255, 0.95); /* 반투명 배경 */
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
  transition: transform 0.3s ease;
}

.chatbot-header {
  background: linear-gradient(135deg, #007bff, #0056b3); /* 그라데이션 */
  color: #ffffff;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  font-size: 18px;
  font-weight: bold;
}

.header-title {
  display: flex;
  align-items: center;
}

.status-indicator {
  width: 10px;
  height: 10px;
  background-color: #ff4d4f; /* 초기 상태는 연결되지 않음 */
  border-radius: 50%;
  margin-left: 10px;
  animation: pulse 1.5s infinite;
}

.status-indicator.connected {
  background-color: #52c41a; /* 연결되었을 때 초록색 */
  animation: none;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  70% {
    transform: scale(1.5);
    opacity: 0;
  }
  100% {
    transform: scale(1);
    opacity: 0;
  }
}

.close-btn {
  background: transparent;
  border: none;
  color: #ffffff;
  font-size: 20px;
  cursor: pointer;
}

.chatbot-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 10px;
  overflow-y: auto;
  background-color: rgba(249, 249, 249, 0.95); /* 반투명 배경 */
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding-right: 5px;
}

.user,
.bot {
  margin: 10px 0;
  display: flex;
}

.user {
  justify-content: flex-end;
}

.bot {
  justify-content: flex-start;
}

.message-bubble {
  max-width: 80%;
  padding: 10px 15px;
  border-radius: 20px;
  position: relative;
  font-size: 14px;
  line-height: 1.4;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user .message-bubble {
  background: #e0e0e0; /* 회색 말풍선 */
  color: #333333;
  border-bottom-right-radius: 0;
}

.bot .message-bubble {
  background: rgba(0, 123, 255, 0.7); /* 진하고 투명한 파란색 말풍선 */
  color: #ffffff;
  border-bottom-left-radius: 0;
}

.sender-label {
  font-weight: bold;
  margin-right: 5px;
}

.message-text {
  word-wrap: break-word;
}

/* 이미지 메시지 스타일 */
.message-image {
  max-width: 100px; /* 작게 표시 */
  border-radius: 10px;
  margin-top: 5px;
}

/* 로딩 애니메이션 스타일 */
.typing .message-text {
  display: flex;
  align-items: center;
}

.dot {
  height: 8px;
  width: 8px;
  margin: 0 2px;
  background-color: #ffffff;
  border-radius: 50%;
  display: inline-block;
  animation: blink 1.4s infinite both;
}

.dot:nth-child(2) {
  animation-delay: 0.2s;
}

.dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes blink {
  0%,
  80%,
  100% {
    opacity: 0;
  }
  40% {
    opacity: 1;
  }
}

.input-container {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.input-container input[type="text"] {
  flex: 1;
  padding: 10px 15px;
  border: 1px solid #cccccc;
  border-radius: 20px;
  font-size: 14px;
  outline: none;
  background: rgba(247, 247, 247, 0.9); /* 반투명 배경 */
}

.upload-button {
  background: #007bff; /* 버튼 배경색 */
  border: none;
  border-radius: 50%;
  padding: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.upload-button:hover {
  background-color: #0056b3;
  transform: scale(1.05);
}

.upload-icon {
  stroke: white;
}

.send-button {
  background: #007bff;
  border: none;
  border-radius: 50%;
  padding: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.send-button:hover {
  background-color: #0056b3;
  transform: scale(1.05);
}

.arrow-icon {
  stroke: white;
}

.chatbot-icon {
  animation: spin-and-glow 2s infinite ease-in-out;
  width: 50px; /* 아이콘 크기 통일 */
  height: 50px; /* 아이콘 크기 통일 */
  background-color: white;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  cursor: pointer;
  font-size: 30px; /* 아이콘 크기 통일 */
  /* box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3); */
  transition: transform 0.2s ease, background-color 0.3s ease;
}

.chatbot-icon:hover {
  transform: scale(1.1);
  background-color: #ffffff;
}
@keyframes spin-and-glow {
  0% {
    transform: scale(1) rotate(0deg);
    text-shadow: 0 0 5px rgba(255, 215, 0, 0.8), 0 0 10px rgba(255, 165, 0, 0.6);
  }
  50% {
    transform: scale(1.2) rotate(180deg);
    text-shadow: 0 0 10px rgba(255, 215, 0, 1), 0 0 20px rgba(255, 165, 0, 0.8);
  }
  100% {
    transform: scale(1) rotate(360deg);
    text-shadow: 0 0 5px rgba(255, 215, 0, 0.8), 0 0 10px rgba(255, 165, 0, 0.6);
  }
}
</style>
