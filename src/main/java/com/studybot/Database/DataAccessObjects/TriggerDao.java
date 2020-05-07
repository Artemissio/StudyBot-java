package com.studybot.Database.DataAccessObjects;

import com.studybot.Database.DataTransferObjects.Trigger;
import com.studybot.Exceptions.DatabaseException;

import java.util.Set;

public interface TriggerDao {
    Set<Trigger> getAllTriggers() throws DatabaseException;
    Set<Trigger> getCommonTriggers() throws DatabaseException;
    Set<Trigger> getBotTriggers(String token) throws DatabaseException;
}
