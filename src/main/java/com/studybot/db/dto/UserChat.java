package com.studybot.db.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserChat implements Serializable {

    private static final long serialVersionUID = -2245628051162576161L;

    private final int userId;
    private final long chatId;

    public UserChat(int userId, long chatId) {
        this.userId = userId;
        this.chatId = chatId;
    }

    public int getUserId() {
        return userId;
    }

    public long getChatId() {
        return chatId;
    }
}
