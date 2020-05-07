package com.studybot.Database.DataTransferObjects;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
public class Trigger implements Serializable {

    private static final long serialVersionUID = 1634378540228028764L;

    private final int triggerId;
    @NonNull
    private final String triggerType;
    private final String triggerSubType;
    private final String botToken;
    @NonNull
    private final String triggerValue;

    public Trigger(int triggerId, @NonNull String triggerType, String triggerSubType, String botToken, @NonNull String triggerValue) {
        this.triggerId = triggerId;
        this.triggerType = triggerType;
        this.triggerSubType = triggerSubType;
        this.botToken = botToken;
        this.triggerValue = triggerValue;
    }

    public int getTriggerId() {
        return triggerId;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public String getTriggerSubType() {
        return triggerSubType;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getTriggerValue() {
        return triggerValue;
    }
}
