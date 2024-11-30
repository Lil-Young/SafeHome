package com.ssafy.chat.model;

import java.time.LocalDateTime;

public class ChatMessage {
    private String chatRoomId;
    private String sender;
    private String content;
    private LocalDateTime timestamp;

    public ChatMessage(String chatRoomId, String sender, String content) {
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
