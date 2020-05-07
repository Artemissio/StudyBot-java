package com.studybot.RabbitMQ.DataTransferObjects;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 2304217959414830128L;

    private final int userId;
    private final String message;

    public UserMessage(int userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}
