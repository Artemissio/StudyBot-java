package com.studybot.Bots;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.studybot.Database.DataAccessObjects.UserDao;
import com.studybot.Database.DataAccessObjects.PostgresUserDaoImpl;
import com.studybot.Database.DataAccessObjects.PostgresTriggerDaoImpl;
import com.studybot.Database.DataAccessObjects.TriggerDao;
import com.studybot.Database.DataTransferObjects.User;
import com.studybot.Database.DataTransferObjects.Trigger;
import com.studybot.Exceptions.ConfigurationException;
import com.studybot.Exceptions.DatabaseException;
import com.studybot.Exceptions.ServiceConnectionException;
import com.studybot.RabbitMQ.RabbitMqConnectionFactory;
import com.studybot.RabbitMQ.DataTransferObjects.UserMessage;
import com.studybot.Redis.RedissClientFactory;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class StudyHelperClientBot extends Bot {

    private static Logger log = Logger.getLogger(StudyHelperClientBot.class.getName());

    protected StudyHelperClientBot(String token, String botName) {
        super(token, botName);
        try {
            TriggerDao dao = new PostgresTriggerDaoImpl();
            Set<Trigger> botTriggers =  dao.getBotTriggers(token);
            RedissonClient client = RedissClientFactory.getInstance().getRedissClient();
            RSet<Trigger> inMemoryCommonTriggers = client.getSet("common");
            if(inMemoryCommonTriggers.isEmpty()){
                Set<Trigger> commonTriggers =  dao.getCommonTriggers();
                inMemoryCommonTriggers.addAll(commonTriggers);
            }
            RSet<Trigger> inMemoryBotTriggers = client.getSet(token);
            inMemoryBotTriggers.addAll(botTriggers);
        } catch (DatabaseException | ConfigurationException e) {
            handleException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            String text = message.getText().trim();
            if(text.equals("/register")) {
                super.register(message, "Lecturer");
            }else{
                Set<Trigger> triggers = getTriggers();
                Optional<Trigger> optionalTrigger = triggers.stream()
                        .filter(t -> t.getTriggerValue().contains(text)).findFirst();
                if (optionalTrigger.isPresent()) {
                    Trigger trigger = optionalTrigger.get();
                    if (trigger.getTriggerSubType() != null && trigger.getTriggerSubType().equals("Lecturer")) {
                        tryProcessLecturerMessage(message);
                    } else {
                        sendTextMessage(chatId, String.format("Have you just said \"%s\"? Bruh...", text.toUpperCase()));
                    }
                }
            }
        }
    }

    private void tryProcessLecturerMessage(Message message){
        int userId = message.getFrom().getId();
        String text = message.getText().trim();
        try {
            UserDao userDao = new PostgresUserDaoImpl();
            User user = userDao.getUserByTelegramUserIdAndType(userId, "Lecturer");
            if(user != null){
                UserMessage userMessage = new UserMessage(
                        user.getUserId(),
                        String.format("Lecturer %s says: \"%s\" in %s chat",
                                user.getUserName(),
                                text,
                                message.getChat().getTitle() == null ? "private" : message.getChat().getTitle())
                );
                publishMessage(userMessage);
            }
        } catch (DatabaseException | ConfigurationException e) {
            handleException(e);
        }
    }

    private void publishMessage(UserMessage message){
        try(Connection conn = RabbitMqConnectionFactory.getInstance().getConnection();
            Channel channel = conn.createChannel()){
            channel.queueDeclare("bot", false, false, false, null);
            channel.basicPublish("", "bot", null, SerializationUtils.serialize(message));
        } catch (ServiceConnectionException | ConfigurationException | IOException | TimeoutException e) {
            handleException(e);
        }
    }

    private Set<Trigger> getTriggers(){
        Set<Trigger> triggers = null;
        try {
            triggers = new HashSet<>();
            RedissonClient client = RedissClientFactory.getInstance().getRedissClient();
            RSet<Trigger> inMemoryCommonTriggers = client.getSet("common");
            RSet<Trigger> inMemoryBotTriggers = client.getSet(token);
            triggers.addAll(inMemoryCommonTriggers.readAll());
            triggers.addAll(inMemoryBotTriggers.readAll());
        } catch (ConfigurationException e) {
            handleException(e);
        }
        return triggers;
    }
}
