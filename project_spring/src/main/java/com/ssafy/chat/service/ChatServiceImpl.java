package com.ssafy.chat.service;

import com.ssafy.chat.model.ChatMessage;
import com.ssafy.chat.model.ChatRoom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {
    private final JdbcTemplate jdbcTemplate;

    public ChatServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ChatRoom> getAllRooms() {
        return jdbcTemplate.query(
                "SELECT room_id, room_name, created_by, user_count FROM chat_rooms",
                (rs, rowNum) -> new ChatRoom(
                        rs.getString("room_id"),
                        rs.getString("room_name"),
                        rs.getString("created_by"),
                        rs.getInt("user_count")
                )
        );
    }

    @Override
    public ChatRoom createRoom(String roomName, String createdBy) {
        String roomId = UUID.randomUUID().toString();
        jdbcTemplate.update(
                "INSERT INTO chat_rooms (room_id, room_name, created_by, broker_id, user_count) VALUES (?, ?, ?, NULL, ?)",
                roomId, roomName, createdBy, 0
        );
        return new ChatRoom(roomId, roomName, createdBy, 0);
    }

    @Override
    public ChatRoom getRoomById(String roomId) {
        return jdbcTemplate.queryForObject(
                "SELECT room_id, room_name, created_by, broker_id, user_count FROM chat_rooms WHERE room_id = ?",
                (rs, rowNum) -> new ChatRoom(
                        rs.getString("room_id"),
                        rs.getString("room_name"),
                        rs.getString("created_by"),
                        rs.getString("broker_id"),
                        rs.getInt("user_count")
                ),
                roomId
        );
    }


    @Override
    @Transactional
    public boolean enterRoom(String roomId, String userId, int userType) {
        try {
            // 중복 여부 확인
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM chat_room_users WHERE room_id = ? AND user_id = ?",
                Integer.class,
                roomId, userId
            );

            if (count != null && count > 0) {
                System.out.println("사용자가 이미 채팅방에 있습니다.");
                return false; // 이미 존재하므로 추가 삽입하지 않음
            }

            // 중복이 없으면 삽입
            jdbcTemplate.update(
                "INSERT INTO chat_room_users (room_id, user_id) VALUES (?, ?)",
                roomId, userId
            );

            return true;
        } catch (Exception e) {
            System.err.println("enterRoom 오류: " + e.getMessage());
            throw e; // 예외 전달
        }
    }




    @Override
    @Transactional
    public boolean leaveRoom(String roomId, String userId) {
        ChatRoom room = getRoomById(roomId);

        if (room == null) {
            return false;
        }

        jdbcTemplate.update(
                "DELETE FROM chat_room_users WHERE room_id = ? AND user_id = ?", roomId, userId
        );
        jdbcTemplate.update(
                "UPDATE chat_rooms SET user_count = user_count - 1 WHERE room_id = ?", roomId
        );

        return true;
    }

    @Override
    @Transactional
    public boolean deleteRoom(String roomId) {
        // 채팅방에 속한 모든 메시지와 사용자 삭제
        jdbcTemplate.update("DELETE FROM chat_messages WHERE chat_room_id = ?", roomId);
        jdbcTemplate.update("DELETE FROM chat_room_users WHERE room_id = ?", roomId);
        
        // 채팅방 삭제
        int rowsAffected = jdbcTemplate.update("DELETE FROM chat_rooms WHERE room_id = ?", roomId);
        
        return rowsAffected > 0;
    }


    @Override
    public String getUserBySession(String sessionId) {
        // Placeholder logic
        return "exampleUser";
    }

    @Override
    public String getRoomBySession(String sessionId) {
        // Placeholder logic
        return "exampleRoomId";
    }

    @Override
    public void saveMessage(ChatMessage message) {
        jdbcTemplate.update(
                "INSERT INTO chat_messages (chat_room_id, sender, content, created_at) VALUES (?, ?, ?, NOW())",
                message.getChatRoomId(), message.getSender(), message.getContent()
        );
    }

    @Override
    public List<ChatMessage> getMessages(String roomId) {
        return jdbcTemplate.query(
                "SELECT chat_room_id, sender, content, created_at FROM chat_messages WHERE chat_room_id = ? ORDER BY created_at ASC",
                (rs, rowNum) -> new ChatMessage(
                        rs.getString("chat_room_id"),
                        rs.getString("sender"),
                        rs.getString("content")
                ),
                roomId
        );
    }
}
