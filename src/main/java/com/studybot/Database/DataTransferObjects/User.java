package com.studybot.Database.DataTransferObjects;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 3264345799553910737L;

    private final int userId;
    private final String userName;
    private final String userType;
    private final int telegramUserId;

    public User(int userId, String userName, String userType, int telegramUserId) {
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
        this.telegramUserId = telegramUserId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserType() {
        return userType;
    }

    public int getTelegramUserId() {
        return telegramUserId;
    }
}
