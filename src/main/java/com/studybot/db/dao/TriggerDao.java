package com.studybot.db.dao;

import com.studybot.db.dto.Trigger;
import com.studybot.exceptions.DatabaseException;

import java.util.Set;

public interface TriggerDao {
    Set<Trigger> getAllTriggers() throws DatabaseException;
    Set<Trigger> getCommonTriggers() throws DatabaseException;
    Set<Trigger> getBotTriggers(String token) throws DatabaseException;
}
