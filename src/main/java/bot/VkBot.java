package bot;


import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.model.events.messages.MessageNewEvent;
import api.longpoll.bots.model.objects.basic.Message;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.*;

/**
 * Класс VkBot - класс, отвечающий за сущность бота ВКонтакте
 * <p>
 * 28.11.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class VkBot extends LongPollBot implements ChatBot{

    /**
     * Поле - интерфейс для предачи запросов к VK-API
     */
    private TransportClient transportClient = new HttpTransportClient();
    /**
     * Поле - интерфейс для взаимодействия с VK-API с помощью запросов
     */
    private VkApiClient vk = new VkApiClient(transportClient);

    private Random random = new Random();
    /**
     *
     */
    private GroupActor actor = new GroupActor(getGroupId(), getAccessToken());

    /**
     * Поле словарь пользователей (ключ - id чата, значение - экземпляр класса пользователь)
     */
    private Map<String, User> users = new HashMap<>();

    /**
     * Поле контроллера YouTubeApi
     */
    private YoutubeApiController youtubeApiController = new YoutubeApiController();

    /**
     * Поле калькулятора статистики
     */
    private StatisticCalculator calculator = StatisticCalculator.getInstance();


    /**
     * Функция получения ключа доступа
     * @return - ключ доступа
     */
    @Override
    public String getAccessToken() {
        Properties prop = new Properties();
        String access_token = null;
        try {
            prop.load(VkBot.class.getClassLoader().getResourceAsStream("VK.properties"));
            access_token = prop.getProperty("access_token");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке файла конфигурации");
        }
        return access_token;
    }

    /**
     * Функция получения id группы ВКонтакте
     * @return - id группы
     */
    @Override
    public int getGroupId() {
        Properties prop = new Properties();
        Integer groupId = null;
        try {
            prop.load(VkBot.class.getClassLoader().getResourceAsStream("VK.properties"));
            groupId = Integer.valueOf(prop.getProperty("group_id"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке файла конфигурации");
        }
        return groupId;
    }

    /**
     * Процедура обработки поступающих сообщений
     * @param messageEvent - событие поступления сообщения
     */
    @Override
    public void onMessageNew(MessageNewEvent messageEvent){
        Message message = messageEvent.getMessage();
        if (message.getPayload() != null){
            handleButtonClick(message);
        }
        else{
            handleMessage(message);
        }
    }

    /**
     * Процедура обработки простых текстовых сообщений
     * @param message - сообщение
     */
    private void handleMessage(Message message){
        String text = message.getText();
        String chatId = message.getFromId().toString();
        switch (text){
            case "help":
                StringBuilder botMessage = new StringBuilder();
                botMessage.append("Привет, я - бот для создания тренировок по методу табата! Табата – система тренировок, которая была ");
                botMessage.append("придумана японским физиологом Изуми Табата. Он доказал, что интервальные тренировки при мощности работы на 70% от ");
                botMessage.append("МПК (максимального потребления кислорода) способны одновременно привести к росту аэробной и анаэробной выносливости. ");
                botMessage.append("Упражнения выполняются циклами. 1 цикл: 20 секунд работы, 10 секунд отдыха, 8 подходов. Количество пражнений в 1 раунде = колчество циклов. ");
                botMessage.append("Я составлю тебе тренировку по выбранным тобою параметрам (уровень сложности, целевая группа мышц) Пиши start, чтобы начать!");
                sendTextMessage(botMessage.toString(), chatId);
                break;
            case "start":
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
                sendTextMessage("Извините, я вас не понял :(, попробуйте еще раз", chatId);
        }
    }

    /**
     * Процедура обработки сообщения, вызванного нажатием на кнопку
     * @param message - сообщение
     */
    @SneakyThrows
    private void handleButtonClick(Message message) {
        String chatId = message.getPeerId().toString();
        String payLoad = message.getPayload();
        String[] components = payLoad.split(":\"");
        String name = components[0].substring(2, components[0].length()-1);
        String value = components[1].substring(0, components[1].length()-2);
        switch (name){
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
     * Процедура отправки текстового сообщения ползователю
     * @param text - текст сообщения
     * @param chatId - id чата
     */
    @SneakyThrows
    @Override
    public void sendTextMessage(String text, String chatId) {
        vk.messages().send(actor).message(text).peerId(Math.toIntExact(Long.parseLong(chatId))).randomId(random.nextInt(10000)).execute();
    }

    /**
     * Процедура отправки сообщений с кнопками
     * @param text - текст сообщения
     * @param chatId - id чата
     * @param options - подписи кнопок
     * @param callbacks - коллбэки
     */
    @SneakyThrows
    @Override
    public void sendMessageWithButtons(String text, String chatId, String[] options, Map<String, String> callbacks) {
        Keyboard keyboard = new Keyboard();
        keyboard.setButtons(makeButtons(options, callbacks));
        vk.messages().send(actor).message(text).peerId(Math.toIntExact(Long.parseLong(chatId))).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
    }

    /**
     * Функция создания кнопок
     * @param options - подписи кнопок
     * @param callbacks - коллбэки
     * @return - список рядов кнопок
     */
    private List<List<KeyboardButton>> makeButtons(String[] options, Map<String, String> callbacks){
        List<List<KeyboardButton>> buttons = new ArrayList<>();
        for (String option:
             options) {
            buttons.add(Arrays.asList(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel(option).setType(TemplateActionTypeNames.TEXT).setPayload(callbacks.get(option)))));
        }
        return buttons;
    }

}
