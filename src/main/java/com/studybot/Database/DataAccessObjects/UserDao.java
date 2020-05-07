package com.studybot.Database.DataAccessObjects;

import com.studybot.Database.DataTransferObjects.User;
import com.studybot.Exceptions.DatabaseException;

import java.util.Set;

public interface UserDao {
    User getUserById(int userId) throws DatabaseException;
    User getUserByTelegramUserIdAndType(int telegramUserId, String userType) throws DatabaseException;
    Set<User> getUsersByType(String type) throws DatabaseException;
}
