package com.studybot.Bots;

import com.studybot.Database.DataAccessObjects.UserChatDao;
import com.studybot.Database.DataAccessObjects.UserDao;
import com.studybot.Database.DataAccessObjects.PostgresUserChatDaoImpl;
import com.studybot.Database.DataAccessObjects.PostgresUserDaoImpl;
import com.studybot.Database.DataTransferObjects.User;
import com.studybot.Database.DataTransferObjects.UserChat;
import com.studybot.Exceptions.ConfigurationException;
import com.studybot.Exceptions.DatabaseException;
import com.studybot.Handlers.ExceptionHandler;
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
    protected final void handleException(Exception e) {
        ExceptionHandler.handleException(e);
    }

    public static void runBot(Bot newBot) {
        try {
            new TelegramBotsApi().registerBot(newBot);
        } catch (TelegramApiException e) {
            newBot.handleException(e);
        }
    }

    public Message sendTextMessage(long chatId, String text) {
        try {
            SendMessage send = new SendMessage().setChatId(chatId);
            send.setText(text.trim());
            return execute(send);
        } catch (Exception e) {
            handleException(e);
            return null;
        }
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
            handleException(e);
        }
    }
}