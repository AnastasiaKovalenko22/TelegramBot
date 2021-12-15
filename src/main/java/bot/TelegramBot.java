package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.IOException;
import java.util.*;

/**
 * Класс Bot - класс, отвечающий за сущность бота в Телеграм
 * <p>
 * 16.10.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class TelegramBot extends TelegramLongPollingBot implements ChatBot {
    /**
     * Поле имя бота - username в телеграме
     */
    private String botName;

    /**
     * Поле токен бота в телеграме для контроля над ботом
     */
    private String botToken;
    /**
     * Обработчик сообшений
     */
    private MessagesHandler messagesHandler = new MessagesHandler(this);


    /**
     * Функция получения значения поля {@link TelegramBot#botName}
     */
    @Override
    public String getBotUsername() {
        return System.getenv("TG_BOT_NAME");
    }

    /**
     * Функция получения значения поля {@link TelegramBot#botToken}
     */
    @Override
    public String getBotToken() {
        return System.getenv("TG_BOT_TOKEN");
    }

    /**
     * Процедура обработки обновлений
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();
            String callbackData = callbackQuery.getData();
            String chatId = message.getChatId().toString();
            messagesHandler.handleCallback(callbackData, chatId);
        } else if (update.hasMessage()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            if (message.hasText() && message.hasEntities()) {
                Optional<MessageEntity> commandEntity =
                        message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                if (commandEntity.isPresent()) {
                    messagesHandler.handleCommandMessage(command, chatId);
                }
            } else {
                messagesHandler.handleUnclearMessage(chatId);
            }
        }
    }

    /**
     * Процедура отправки пользователю текстового сообщения
     *
     * @param text   - текст сообщения
     * @param chatId - ID чата, в который нужно отправить сообщение
     */
    @Override
    public void sendTextMessage(String text, String chatId) {
        try {
            execute(SendMessage.builder()
                    .text(text)
                    .chatId(chatId)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Процедура отправки пользователю сообщения с кнопками для ответа
     *
     * @param text   - текст сообщения
     * @param chatId - ID чата, в который нужно отправить сообщение
     */
    @Override
    public void sendMessageWithButtons(String text, String chatId, String[] options, Map<String, String> callbacks) {
        List<List<InlineKeyboardButton>> buttons = makeButtons(options, callbacks);
        try {
            execute(SendMessage.builder()
                    .text(text)
                    .chatId(chatId)
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Функция создания кнопок
     *
     * @param options   - подписи кнопок
     * @param callbacks - коллбэки
     * @return - список рядов кнопок
     */
    private List<List<InlineKeyboardButton>> makeButtons(String[] options, Map<String, String> callbacks) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String text :
                options) {
            buttons.add(Arrays.asList(InlineKeyboardButton.builder().text(text).callbackData(callbacks.get(text)).build()));
        }
        return buttons;
    }
}