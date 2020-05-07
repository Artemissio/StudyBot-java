package com.studybot.Database.DataAccessObjects;

import com.studybot.Database.DataTransferObjects.UserChat;
import com.studybot.Exceptions.DatabaseException;

import java.util.Set;

public interface UserChatDao {
    UserChat getUserChat(int userId, long chatId) throws DatabaseException;
    void addUserChat(UserChat userChat) throws DatabaseException;
    Set<UserChat> getUserChatsByType(String type) throws DatabaseException;
}
