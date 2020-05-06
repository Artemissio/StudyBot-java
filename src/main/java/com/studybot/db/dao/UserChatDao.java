package com.studybot.db.dao;

import com.studybot.db.dto.UserChat;
import com.studybot.exceptions.DatabaseException;

import java.util.Set;

public interface UserChatDao {
    UserChat getUserChat(int userId, long chatId) throws DatabaseException;
    void addUserChat(UserChat userChat) throws DatabaseException;
    Set<UserChat> getUserChatsByType(String type) throws DatabaseException;
}
