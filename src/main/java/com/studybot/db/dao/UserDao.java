package com.studybot.db.dao;

import com.studybot.db.dto.User;
import com.studybot.exceptions.DatabaseException;

import java.util.Set;

public interface UserDao {
    User getUserById(int userId) throws DatabaseException;
    User getUserByTelegramUserIdAndType(int userId, String userType) throws DatabaseException;
    Set<User> getUsersByType(String type) throws DatabaseException;
}
