package bot;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


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
public class TelegramBot extends TelegramLongPollingBot implements ChatBot{
    /**
     *  Поле имя бота - username в телеграме
     */
    private String botName;

    /**
     *  Поле токен бота в телеграме для контроля над ботом
     */
    private String botToken;

    /**
     * Поле словарь пользователей (ключ - id чата, значение - экземпляр класса пользователь)
     */
    private Map<String, User> users = new HashMap<>();

    /**
     * Поле контроллера YouTubeApi
     */
    private YoutubeApiController youtubeApiController = new YoutubeApiController();

    /**
     *  Функция получения значения поля {@link TelegramBot#botName}
     */
    @Override
    public String getBotUsername() {
        Properties prop = new Properties();
        try {
            prop.load(TelegramBot.class.getClassLoader().getResourceAsStream("Telegram.properties"));
            botName = prop.getProperty("botName");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке файла конфигурации");
        }
        return botName;
    }

    /**
     *  Функция получения значения поля {@link TelegramBot#botToken}
     */
    @Override
    public String getBotToken() {
        Properties prop = new Properties();
        try {
            prop.load(TelegramBot.class.getClassLoader().getResourceAsStream("Telegram.properties"));
            botToken = prop.getProperty("botToken");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке файла конфигурации");
        }
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
        String chatId = message.getChatId().toString();
        switch (command) {
            case "/help":
                StringBuilder botMessage = new StringBuilder();
                botMessage.append("Привет, я - бот для создания тренировок по методу табата! Табата – система тренировок, которая была ");
                botMessage.append("придумана японским физиологом Изуми Табата. Он доказал, что интервальные тренировки при мощности работы на 70% от ");
                botMessage.append("МПК (максимального потребления кислорода) способны одновременно привести к росту аэробной и анаэробной выносливости. ");
                botMessage.append("Упражнения выполняются циклами. 1 цикл: 20 секунд работы, 10 секунд отдыха, 8 подходов. Количество пражнений в 1 раунде = колчество циклов. ");
                botMessage.append("Я составлю тебе тренировку по выбранным тобою параметрам (уровень сложности, целевая группа мышц) Пиши /start, чтобы начать!");
                sendTextMessage(botMessage.toString(), chatId);
                break;
            case "/start":
                if (users.containsKey(chatId)){
                    users.get(chatId).getTimerForNotifying().cancel();
                }
                users.put(chatId, new User(chatId));
                users.get(chatId).setBot(this);
                botMessage = new StringBuilder();
                botMessage.append("Выберите уровень сложности\nновичок : 1 раунд 6 циклов\nлюбитель : 2 раунда по 8 циклов\n");
                botMessage.append("продвинутый : 3 раунда по 8 циклов");
                String [] levels = new String[]{"новичок", "любитель", "продвинутый"};
                Map<String, String> callbacks = new HashMap<>();
                for (String level : levels){
                    callbacks.put(level, "{\"chosen level\":\"" + level + "\"}");
                }
                sendMessageWithButtons(botMessage.toString(), chatId, levels, callbacks);
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
    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String callbackData = callbackQuery.getData();
        String[] params = callbackData.split(":\"");
        String name = params[0].substring(2, params[0].length() - 1);
        String value = params[1].substring(0, params[1].length() - 2);
        String chatId = message.getChatId().toString();
        switch (name) {
            case "chosen level":
                String[] groups = new String[]{"ноги", "пресс", "руки+грудь+спина",
                        "пресс, руки+грудь+спина", "ноги, пресс", "ноги, руки+грудь+спина"};
                Map<String, String> callbacks = new HashMap<>();
                for (String group : groups) {
                    callbacks.put(group, "{\"chosen group\":\"" + group + "\"}");
                }
                users.get(chatId).getWorkoutMaker().setLevel(value);
                users.get(chatId).setLevel(value);
                users.get(chatId).setNotifications();
                sendTextMessage("Вы выбрали уровень " + value, chatId);
                sendMessageWithButtons("Выберите целевую группу мышц", chatId, groups, callbacks);
                break;
            case "chosen group":
                sendTextMessage("Вы выбрали следующую группу мышц: " + value, chatId);
                users.get(chatId).getWorkoutMaker().setTargetGroups(value.split(", "));
                String [] options = new String[]{"начать", "отменить"};
                callbacks = new HashMap<>();
                callbacks.put("начать", "{\"start workout\":\"start\"}");
                callbacks.put("отменить", "{\"cancel\":\"cancel\"}");
                sendMessageWithButtons("Начать тренировку ?", chatId, options, callbacks);
                break;
            case "start workout":
                users.get(chatId).setWorkout(users.get(chatId).getWorkoutMaker().createWorkout());
                options = new String[]{"начать", "завершить тренировку", "техника выполнения"};
                callbacks = new HashMap<>();
                callbacks.put("начать", "{\"start approach\":\"start\"}");
                callbacks.put("завершить тренировку", "{\"stop\":\"stop\"}");
                callbacks.put("техника выполнения","{\"tech\":\"show\"}");
                sendMessageWithButtons(users.get(chatId).getCurrentRound() + " раунд! 1 упражнение: " + users.get(chatId).getExerciseName() + "! Начать?",
                        chatId, options, callbacks);
                break;
            case "start approach":
                sendTextMessage("Paботаем!", chatId);
                users.get(chatId).doExercise();
                break;
            case "start rest":
                sendTextMessage("Отдых!", chatId);
                users.get(chatId).rest(Integer.parseInt(value));
                break;
            case "tech":
                String text = youtubeApiController.getVideos("упражнение+" + users.get(chatId).getExerciseName().replaceAll(" ", "+"));
                sendTextMessage(text, chatId);
                break;
            case "cancel":
                sendTextMessage("Тренировка отменена!", chatId);
                break;
            case "stop":
                sendTextMessage("Тренировка завершена!", chatId);
                break;
        }
    }
    /**
     * Процедура отправки пользователю текстового сообщения
     * @param text - текст сообщения
     * @param chatId - ID чата, в который нужно отправить сообщение
     */
    @SneakyThrows
    @Override
    public void sendTextMessage(String text, String chatId){
        execute(SendMessage.builder()
                .text(text)
                .chatId(chatId)
                .build());
    }

    /**
     * Процедура отправки пользователю сообщения с кнопками для ответа
     * @param text - текст сообщения
     * @param chatId - ID чата, в который нужно отправить сообщение
     */
    @SneakyThrows
    @Override
    public void sendMessageWithButtons(String text, String chatId, String[] options, Map<String, String> callbacks){
        List<List<InlineKeyboardButton>> buttons = makeButtons(options, callbacks);
        execute(SendMessage.builder()
                .text(text)
                .chatId(chatId)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    /**
     * Функция создания кнопок
     * @param options - подписи кнопок
     * @param callbacks - коллбэки
     * @return - список рядов кнопок
     */
    private List<List<InlineKeyboardButton>> makeButtons(String[] options, Map<String, String> callbacks){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String text:
                options) {
            buttons.add(Arrays.asList(InlineKeyboardButton.builder().text(text).callbackData(callbacks.get(text)).build()));
        }
        return buttons;
    }
}