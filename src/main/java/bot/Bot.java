package bot;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


import java.util.*;

/**
 * Класс Bot - класс, отвечающий за сущность бота, обрабтку сообщений пользователя
 * <p>
 * 16.10.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class Bot extends TelegramLongPollingBot {
    /**
     *  Поле имя бота - username в телеграме
     */
    @Setter
    @Getter
    private String botName;

    /**
     *  Поле токен бота в телеграме для контроля над ботом
     */
    @Setter
    private String botToken;

    /**
     *  Поле сборщика тренировки
     */
    private WorkoutMaker workoutMaker = new WorkoutMaker();

    /**
     *  Конструктор - создание нового объекта с определенными значениями
     * @param botName - username бота в телеграме
     * @param botToken - токен бота в телеграме
     */
    public Bot(String botName, String botToken) {
        this.botName = botName;
        this.botToken = botToken;
    }

    /**
     *  Функция получения значения поля {@link Bot#botName}
     */
    @Override
    public String getBotUsername() {
        return botName;
    }

    /**
     *  Функция получения значения поля {@link Bot#botToken}
     */
    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     *  Процедура обработки обновлений
     */
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasCallbackQuery()){
            handleCallback(update.getCallbackQuery());
        }
        else if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText() && message.hasEntities()){
                Optional<MessageEntity> commandEntity =
                        message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
                if(commandEntity.isPresent()){
                    handleCommandMessage(message, commandEntity);
                }
            }
            else{
                handleUnclearMessage(message);
            }
        }
    }

    /** Процедура обработки сообщений пользователя, содержащих команду
     * @param message - сообщение пользователя
     * @param commandEntity - команда
     */
    @SneakyThrows
    private void handleCommandMessage(Message message, Optional<MessageEntity> commandEntity) {
        String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
        switch (command) {
            case "/help":
                StringBuilder botMessage = new StringBuilder();
                botMessage.append("Привет, я - бот для создания тренировок по методу табата! Табата – система тренировок, которая была ");
                botMessage.append("придумана японским физиологом Изуми Табата. Он доказал, что интервальные тренировки при мощности работы на 70% от ");
                botMessage.append("МПК (максимального потребления кислорода) способны одновременно привести к росту аэробной и анаэробной выносливости. ");
                botMessage.append("Упражнения выполняются циклами. 1 цикл: 20 секунд работы, 10 секунд отдыха, 8 подходов. Количество пражнений в 1 раунде = колчество циклов. ");
                botMessage.append("Я составлю тебе тренировку по выбранным тобою параметрам (уровень сложности, целевая группа мышц) Пиши /start, чтобы начать!");
                sendTextMessage(botMessage.toString(), message.getChatId().toString());
                break;
            case "/start":
                botMessage = new StringBuilder();
                botMessage.append("Выберите уровень сложности\nновичок : 1 раунд 6 циклов\nлюбитель : 2 раунда по 8 циклов\n");
                botMessage.append("продвинутый : 3 раунда по 8 циклов");
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                String [] levels = new String[]{"новичок", "любитель", "продвинутый"};
                for (String level : levels){
                    buttons.add(Arrays.asList(InlineKeyboardButton.builder().text(level).callbackData("chosen level: " + level).build()));
                }
                sendMessageWithButtons(botMessage.toString(), message.getChatId().toString(), buttons);
                break;
            default:
                handleUnclearMessage(message);
                break;
        }
    }

    /**Процедура обработки непонятных боту сообщений
     * @param message - сообщение пользователя
     */
    @SneakyThrows
    private void handleUnclearMessage(Message message){
        sendTextMessage("Извините, я вас не понял :(, попробуйте еще раз", message.getChatId().toString());
    }

    /**Процедура обработки нажатия на кнопки
     * @param callbackQuery - обратный вызов
     */
    @SneakyThrows
    private void handleCallback(CallbackQuery callbackQuery){
        Message message = callbackQuery.getMessage();
        String[] params = callbackQuery.getData().split(": ");
        String name = params[0];
        String  value = params[1];
        switch (name){
            case "chosen level":
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                String [] groups = new String[]{"ноги", "пресс", "руки + грудь + спина",
                        "пресс, руки + грудь + спина", "ноги, пресс", "ноги, руки + грудь + спина"};
                for (String group : groups){
                    buttons.add(Arrays.asList(InlineKeyboardButton.builder().text(group).callbackData("chosen group: " + group).build()));
                }
                workoutMaker.setLevel(value);
                sendTextMessage("Вы выбрали уровень " + value, message.getChatId().toString());
                sendMessageWithButtons("Выберите целевую группу мышц", message.getChatId().toString(), buttons);
                break;
            case "chosen group":
                sendTextMessage("Вы выбрали следующую группу мышц: " + value, message.getChatId().toString());
                workoutMaker.setTargetGroups(value.split(", "));
                buttons = new ArrayList<>();
                buttons.add(Arrays.asList(InlineKeyboardButton.builder().text("начать").callbackData("chosen group: " + "start workout").build(),
                        InlineKeyboardButton.builder().text("отменить").callbackData("chosen group: " + "cancel").build()));
                sendMessageWithButtons("Начать тренировку ?", message.getChatId().toString(), buttons);
        }
    }

    /**
     * Процедура отправки пользователю текстового сообщения
     * @param text - текст сообщения
     * @param chatId - ID чата, в который нужно отправить сообщение
     */
    @SneakyThrows
    private void sendTextMessage(String text, String chatId){
        execute(SendMessage.builder()
                .text(text)
                .chatId(chatId)
                .build());
    }

    /**
     * Процедура отправки пользователю сообщения с кнопками для ответа
     * @param text - текст сообщения
     * @param chatId - ID чата, в который нужно отправить сообщение
     * @param buttons - список кнопок
     */
    @SneakyThrows
    private void sendMessageWithButtons(String text, String chatId, List<List<InlineKeyboardButton>> buttons){
        execute(SendMessage.builder()
                .text(text)
                .chatId(chatId)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }
}