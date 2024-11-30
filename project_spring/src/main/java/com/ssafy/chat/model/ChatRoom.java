package com.ssafy.chat.model;

public class ChatRoom {
    private String roomId;
    private String roomName;
    private String createdBy;
    private String brokerId;
    private int userCount;

    // 기존 생성자
    public ChatRoom(String roomId, String roomName, String createdBy, String brokerId, int userCount) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.createdBy = createdBy;
        this.brokerId = brokerId;
        this.userCount = userCount;
    }

    // 추가 생성자
    public ChatRoom(String roomId, String roomName, String createdBy, int userCount) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.createdBy = createdBy;
        this.brokerId = null;
        this.userCount = userCount;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public int getUserCount() {
        return userCount;
    }
}
