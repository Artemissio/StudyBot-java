package com.studybot.bot;

import com.studybot.db.dao.UserChatDao;
import com.studybot.db.dao.UserDao;
import com.studybot.db.dao.PostgresUserChatDaoImpl;
import com.studybot.db.dao.PostgresUserDaoImpl;
import com.studybot.db.dto.User;
import com.studybot.db.dto.UserChat;
import com.studybot.exceptions.ConfigurationException;
import com.studybot.exceptions.DatabaseException;
import com.studybot.utils.ExceptionHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class Bot extends TelegramLongPollingBot {

    protected final String token, botName;

    protected Bot(String token, String botName) {
        this.token = token;
        this.botName = botName;
    }

    public static void runBot(Bot newBot) {
        try {
            new TelegramBotsApi().registerBot(newBot);
        } catch (TelegramApiException e) {
            newBot.processException(e);
        }
    }

    public Message sendTextMessage(long chatId, String text) {
        try {
            SendMessage send = new SendMessage().setChatId(chatId);
            send.setText(text.trim());
            return execute(send);
        } catch (Exception e) {
            processException(e);
            return null;
        }
    }

    protected final void processException(Exception e) {
        ExceptionHandler.processException(e);
    }

    @Override
    public final String getBotUsername() {
        return botName;
    }

    @Override
    public final String getBotToken() {
        return token;
    }

    protected void register(Message message, String type) {
        long chatId = message.getChatId();
        int userId = message.getFrom().getId();
        try {
            UserDao userDao = new PostgresUserDaoImpl();
            User user = userDao.getUserByTelegramUserIdAndType(userId, type);
            if (user != null) {
                UserChatDao userChatDao = new PostgresUserChatDaoImpl();
                UserChat userChat = userChatDao.getUserChat(user.getUserId(), chatId);
                if (userChat == null) {
                    userChatDao.addUserChat(new UserChat(user.getUserId(), chatId));
                }
            }
        } catch (DatabaseException | ConfigurationException e) {
            processException(e);
        }
    }
}