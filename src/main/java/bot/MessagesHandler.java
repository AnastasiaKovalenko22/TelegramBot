package bot;


import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Класс MessagesHandler - класс, отвечающий за обработку сообщений пользователя
 * <p>
 * 04.12.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class MessagesHandler {
    /**
     * Константа - сообщение об отмене тренировки
     */
    private static final String WORKOUT_CANCEL_MESSAGE = "Тренировка отменена!";
    /**
     * Константа - сообщение о завершении тренировки
     */
    private static final String WORKOUT_FINISHED_MESSAGE = "Тренировка завершена!";
    /**
     * Константа - сообщение о начале отдыха
     */
    private static final String START_REST_MESSAGE = "Отдых!";
    /**
     * Константа - сообщение о начале работы
     */
    private static final String START_WORK_MESSAGE = "Paботаем!";
    /**
     * Константа - ответное сообщение бота на непонятное сообщение польователя
     */
    private static final String MISUNDERSTAND_MESSAGE = "Извините, я вас не понял :(, попробуйте еще раз";
    /**
     * Константа - сообщение о выбранных группах мышц
     */
    private static final String CHOSEN_GROUP_MESSAGE = "Вы выбрали следующую группу мышц: ";
    /**
     * Константа - сообщение о выбранном уровне сложности
     */
    private static final String CHOSEN_LEVEL_MESSAGE = "Вы выбрали уровень ";
    /**
     * Константа - сообщение с пописание функционала бота (ответ на команду /help)
     */
    private static final String HELP_MESSAGE = "Привет, я - бот для создания тренировок по методу табата! Табата – система тренировок, которая была придумана японским физиологом Изуми Табата. Он доказал, что интервальные тренировки при мощности работы на 70% от МПК (максимального потребления кислорода) способны одновременно привести к росту аэробной и анаэробной выносливости. Упражнения выполняются циклами. 1 цикл: 20 секунд работы, 10 секунд отдыха, 8 подходов. Количество пражнений в 1 раунде = колчество циклов. Я составлю тебе тренировку по выбранным тобою параметрам (уровень сложности, целевая группа мышц) Пиши /start, чтобы начать!";
    /**
     * Константа - сообщение c просьбой выбрать уровень сложности
     */
    private static final String CHOOSE_LEVEL_REQUEST = "Выберите уровень сложности\nновичок : 1 раунд 6 циклов\nлюбитель : 2 раунда по 8 циклов\nпродвинутый : 3 раунда по 8 циклов";
    /**
     * Константа - команда help
     */
    private static final String HELP_COMMAND = "/help";
    /**
     * Константа - команда start
     */
    private static final String START_COMMAND = "/start";
    /**
     * Константа - сообщение о первом упражнении в раунде
     */
    private static final String FIRST_EXERCISE_IN_ROUND_MESSAGE = " раунд! 1 упражнение: ";
    /**
     * Константа - список возможных групп мышц и их комбинаций
     */
    private static final String[] GROUPS_TO_CHOOSE = new String[]{"ноги", "пресс", "руки+грудь+спина",
            "пресс, руки+грудь+спина", "ноги, пресс", "ноги, руки+грудь+спина"};
    /**
     * Константа - вопрос о начале подхода
     */
    private static final String START_APPROACH_QUESTION = "! Начать?";
    /**
     * Константа - вопрос о начале тренировки
     */
    private static final String START_WORKOUT_QUESTION = "Начать тренировку ?";
    /**
     * Константа - подпись кнопки старта
     */
    private static final String START_OPTION = "начать";
    /**
     * Константа - подпись кнопки завершения тренировки
     */
    private static final String FINISH_OPTION = "завершить тренировку";
    /**
     * Константа - подпись кнопки показа техники выполнения
     */
    private static final String TECH_OPTION = "техника выполнения";
    /**
     * Константа - подпись кнопки отмены тренировки
     */
    private static final String CANCEL_OPTION = "отменить";
    /**
     * Константа - сообщение c просьбой выбрать группу мышц
     */
    private static final String CHOOSE_GROUP_REQUEST = "Выберите целевую группу мышц";
    /**
     * Константа - приветственное сообщение в уведомлении
     */
    private static final String NOTIFYING_HELLO_MESSAGE = "Привет! На этой неделе осталось тренировок: ";
    /**
     * Константа - сообщение в уведомлении с группами мышц, на которые польователь же делал тренировки
     */
    private static final String USER_DID_WORKOUTS_MESSAGE = " Вы уже делали тренировку на ";
    /**
     * Константа - сообщение в уведомлении с рекомендацией выбрать группу мышц из тех, которые еще не тренировал пользователь
     */
    private static final String RECOMMENDATION_MESSAGE = "рекомендую выбрать дргую групп мышц";
    /**
     * Константа - название дня недели воскресенье на русском
     */
    private static final String SUNDAY = "воскресенье";
    /**
     * Константа - коллбэк при выборе уровня
     */
    private static final String CHOSEN_LEVEL_CALLBACK = "chosen level";
    /**
     * Константа - коллбэк при выборе группы мышц
     */
    private static final String CHOSEN_GROUP_CALLBACK = "chosen group";
    /**
     * Константа - коллбэк при старте тренировки
     */
    private static final String START_WORKOUT_CALLBACK = "start workout";
    /**
     * Константа - коллбэк при старте тренировки (вторая часть)
     */
    private static final String START_WORKOUT_CALLBACK_SECOND_PART = "\":\"start\"}";
    /**
     * Константа - коллбэк при отмене тренировки
     */
    private static final String CANCEL_WORKOUT_CALLBACK = "cancel";
    /**
     * Константа - коллбэк при отмена тренировки (вторая часть)
     */
    private static final String CANCEL_WORKOUT_CALLBACK_SECOND_PART = "\":\"cancel\"}";
    /**
     * Константа - коллбэк при старте подхода
     */
    private static final String START_APPROACH_CALLBACK = "start approach";
    /**
     * Константа - коллбэк при старте подхода (вторая часть)
     */
    private static final String START_APPROACH_CALLBACK_SECOND_PART = "\":\"start\"}";
    /**
     * Константа - коллбэк при завершении тренировки
     */
    private static final String STOP_WORKOUT_CALLBACK = "stop";
    /**
     * Константа - коллбэк при завершении тренировки (вторая часть)
     */
    private static final String STOP_WORKOUT_CALLBACK_SECOND_PART = "\":\"stop\"}";
    /**
     * Константа - коллбэк при показе техники выполнения
     */
    private static final String TECH_CALLBACK = "tech";
    /**
     * Константа - коллбэк при показе техники выполнения (вторая часть)
     */
    private static final String TECH_CALLBACK_SECOND_PART = "\":\"show\"}";
    /**
     * Константа - коллбэк при начале отдыха
     */
    private static final String START_REST_CALLBACK = "start rest";
    /**
     * Константа - сообщение о том, что прошло 60 секнуд
     */
    private static final String SIXTY_SECONDS_PASSED_MESSAGE = "60 секунд прошло! ";
    /**
     * Константа - сообщение о том, что прошло 10 секнуд
     */
    private static final String TEN_SECONDS_PASSED_MESSAGE = "10 секунд прошло! ";
    /**
     * Константа - сообщение о текущем упражнении
     */
    private static final String CURRENT_EXERCISE_MESSAGE = " упражнение: ";
    /**
     * Константа - сообщение о начале подхода
     */
    private static final String START_CURRENT_APPROACH_MESSAGE = " подход?";
    /**
     * Константа - сообщение старте
     */
    private static final String START_MESSAGE = "Начать ";
    /**
     * Константа - сообщение о начале отдыха между раундами
     */
    private static final String START_REST_BETWEEN_ROUNDS_MESSAGE = "20 секунд прошло! Начать отдых между раундами 60 секунд?";
    /**
     * Константа - сообщение о начале отдыха между упражнениями
     */
    private static final String START_REST_BETWEEN_EXERCISES_MESSAGE = "20 секунд прошло! Начать отдых между упражнениями 10 секунд?";
    /**
     * Константа - сообщение о начале отдыха между подходами
     */
    private static final String START_REST_BETWEEN_APPROACHES_MESSAGE = "20 секунд прошло! Начать отдых между подходами 10 секунд?";
    /**
     * Константа - количество подходов
     */
    private static final int APPROACH_COUNT = 8;
    /**
     * Константа - время работы
     */
    private static final int WORK_TIME = 20*1000;
    /**
     * Константа - время между уведомлениями
     */
    private static final int DAY_TIME = 24*60*60*1000;

    /**
     * Поле словарь пользователей (ключ - id чата, значение - экземпляр класса пользователь)
     */
    private Map<String, User> users = new HashMap<>();

    /**
     * Поле контроллера YouTubeApi
     */
    private YoutubeApiController youtubeApiController = new YoutubeApiController();
    /**
     * поле бот
     */
    private ChatBot bot;

    /**
     * конструктор - создание нового объекта
     * @param bot - бот, к которому привязан обработчик
     */
    public MessagesHandler(ChatBot bot){
        this.bot = bot;
    }

    /**
     * конструктор по умолчанию
     */
    public MessagesHandler(){}

    /** Процедура обработки сообщений пользователя, содержащих команду
     * @param command - сообщение пользователя, текстовая команда
     * @param chatId - id чата
     */
    public void handleCommandMessage(String command, String chatId) {
        switch (command) {
            case HELP_COMMAND:
                bot.sendTextMessage(HELP_MESSAGE, chatId);
                break;
            case START_COMMAND:
                if (users.containsKey(chatId)){
                    users.get(chatId).getTimerForNotifying().cancel();
                }
                users.put(chatId, new User());
                String [] levels = new String[]{WorkoutMaker.getBeginLevel(), WorkoutMaker.getMediumLevel(), WorkoutMaker.getAdvancedLevel()};
                Map<String, String> callbacks = new HashMap<>();
                for (String level : levels){
                    callbacks.put(level, "{\"" + CHOSEN_LEVEL_CALLBACK + "\":\"" + level + "\"}");
                }
                bot.sendMessageWithButtons(CHOOSE_LEVEL_REQUEST, chatId, levels, callbacks);
                break;
            default:
                handleUnclearMessage(chatId);
                break;
        }
    }

    /**Процедура обработки нажатия на кнопки
     * @param callbackData - данные коллбэка
     * @param chatId - id чата
     */
    public void handleCallback(String callbackData, String chatId) {
        String[] params = callbackData.split(":\"");
        String name = params[0].substring(2, params[0].length() - 1);
        String value = params[1].substring(0, params[1].length() - 2);
        switch (name) {
            case CHOSEN_LEVEL_CALLBACK:
                Map<String, String> callbacks = new HashMap<>();
                for (String group : GROUPS_TO_CHOOSE) {
                    callbacks.put(group, "{\"" + CHOSEN_GROUP_CALLBACK + "\":\"" + group + "\"}");
                }
                users.get(chatId).setLevel(value);
                setNotifications(chatId);
                bot.sendTextMessage(CHOSEN_LEVEL_MESSAGE + value, chatId);
                bot.sendMessageWithButtons(CHOOSE_GROUP_REQUEST, chatId, GROUPS_TO_CHOOSE, callbacks);
                break;
            case CHOSEN_GROUP_CALLBACK:
                bot.sendTextMessage(CHOSEN_GROUP_MESSAGE + value, chatId);
                users.get(chatId).setTargetGroups(value.split(", "));
                String [] options = new String[]{START_OPTION, CANCEL_OPTION};
                callbacks = new HashMap<>();
                callbacks.put(START_OPTION, "{\"" + START_WORKOUT_CALLBACK + START_WORKOUT_CALLBACK_SECOND_PART);
                callbacks.put(CANCEL_OPTION, "{\"" + CANCEL_WORKOUT_CALLBACK + CANCEL_WORKOUT_CALLBACK_SECOND_PART);
                bot.sendMessageWithButtons(START_WORKOUT_QUESTION, chatId, options, callbacks);
                break;
            case START_WORKOUT_CALLBACK:
                User user = users.get(chatId);
                user.setWorkout(new WorkoutMaker(user.getLevel(), user.getTargetGroups()).createWorkout());
                options = new String[]{START_OPTION, FINISH_OPTION, TECH_OPTION};
                callbacks = new HashMap<>();
                callbacks.put(START_OPTION, "{\"" + START_APPROACH_CALLBACK + START_APPROACH_CALLBACK_SECOND_PART);
                callbacks.put(FINISH_OPTION, "{\"" + STOP_WORKOUT_CALLBACK + STOP_WORKOUT_CALLBACK_SECOND_PART);
                callbacks.put(TECH_OPTION,"{\"" + TECH_CALLBACK + TECH_CALLBACK_SECOND_PART);
                bot.sendMessageWithButtons(users.get(chatId).getCurrentRound() + FIRST_EXERCISE_IN_ROUND_MESSAGE + users.get(chatId).getExerciseName() + START_APPROACH_QUESTION,
                        chatId, options, callbacks);
                break;
            case START_APPROACH_CALLBACK:
                bot.sendTextMessage(START_WORK_MESSAGE, chatId);
                doExercise(chatId);
                break;
            case START_REST_CALLBACK:
                bot.sendTextMessage(START_REST_MESSAGE, chatId);
                rest(Integer.parseInt(value), chatId);
                break;
            case TECH_CALLBACK:
                String text = null;
                try {
                    text = youtubeApiController.getVideos(YoutubeApiController.getExerciseKeywordBeginning() + users.get(chatId).getExerciseName().replaceAll(" ", "+"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bot.sendTextMessage(text, chatId);
                break;
            case CANCEL_WORKOUT_CALLBACK:
                bot.sendTextMessage(WORKOUT_CANCEL_MESSAGE, chatId);
                break;
            case STOP_WORKOUT_CALLBACK:
                bot.sendTextMessage(WORKOUT_FINISHED_MESSAGE, chatId);
                break;
        }
    }

    /**Процедура обработки непонятных боту сообщений
     * @param chatId - id чата
     */
    public void handleUnclearMessage(String chatId){
        bot.sendTextMessage(MISUNDERSTAND_MESSAGE, chatId);
    }

    /**
     * Процедура установки таймера для работы
     * @param chatId - ID чата пользователя
     */
    private void doExercise(String chatId) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                startRest(chatId);
            }
        }, WORK_TIME);

    }

    /**
     * Процедура отправки поьзователю сообщений о завершении рабочего времени
     * @param chatId - ID чата пользователя
     */
    private void startRest(String chatId) {
        String text = getTextForStartRestMessage(chatId);
        if (!text.equals(WORKOUT_FINISHED_MESSAGE)) {
            String[] textWords = text.split(" ");
            String[] options = new String[]{START_OPTION, FINISH_OPTION};
            Map<String, String> callbacks = new HashMap<>();
            callbacks.put(START_OPTION, "{\"" + START_REST_CALLBACK + "\":\"" + textWords[textWords.length - 2] + "\"}");
            callbacks.put(FINISH_OPTION, "{\"" + STOP_WORKOUT_CALLBACK + STOP_WORKOUT_CALLBACK_SECOND_PART);
            bot.sendMessageWithButtons(text, chatId, options, callbacks);
        } else {
            bot.sendTextMessage(text, chatId);
        }
    }

    /**
     * Процедура создания таймера для отдыха
     * @param restTime - время отдыха
     * @param chatId - ID чата пользователя
     */
    private void rest(int restTime, String chatId) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                startWork(chatId);
            }
        }, restTime * 1000);
    }

    /**
     * Процедура отправки пользователю сообщения о завершении отдыха и начале работы
     * @param chatId - ID чата пользователя
     */
    private void startWork(String chatId) {
        String text = getTextForStartWorkMessage(chatId);
        if(users.get(chatId).getCurrentApproach() > 1) {
            String[] options = new String[]{START_OPTION, FINISH_OPTION};
            Map<String, String> callbacks = new HashMap<>();
            callbacks.put(START_OPTION, "{\"" + START_APPROACH_CALLBACK + START_APPROACH_CALLBACK_SECOND_PART);
            callbacks.put(FINISH_OPTION, "{\"" + STOP_WORKOUT_CALLBACK + STOP_WORKOUT_CALLBACK_SECOND_PART);
            bot.sendMessageWithButtons(text, chatId, options, callbacks);
        } else{
            String[] options = new String[]{START_OPTION, FINISH_OPTION, TECH_OPTION};
            Map<String, String> callbacks = new HashMap<>();
            callbacks.put(START_OPTION, "{\"" + START_APPROACH_CALLBACK + START_APPROACH_CALLBACK_SECOND_PART);
            callbacks.put(FINISH_OPTION, "{\"" + STOP_WORKOUT_CALLBACK + STOP_WORKOUT_CALLBACK_SECOND_PART);
            callbacks.put(TECH_OPTION, "{\"" + TECH_CALLBACK + TECH_CALLBACK_SECOND_PART);
            bot.sendMessageWithButtons(text, chatId, options, callbacks);
        }
    }

    /**
     * Функция получения текста сообщения о завершении отдыха и начале работы
     * @return - строка - текст сообщения
     */
    private String getTextForStartWorkMessage(String chatId){
        String text = "";
        int currentApproach = users.get(chatId).getCurrentApproach();
        int currentExercise = users.get(chatId).getCurrentExercise();
        int currentRound = users.get(chatId).getCurrentRound();
        String currentExerciseName = users.get(chatId).getExerciseName();
        if (currentApproach > 1) {
            text = TEN_SECONDS_PASSED_MESSAGE + START_MESSAGE + currentApproach + START_CURRENT_APPROACH_MESSAGE;
        } else if (currentExercise > 0) {
            text = TEN_SECONDS_PASSED_MESSAGE + (currentExercise + 1) + CURRENT_EXERCISE_MESSAGE + currentExerciseName + START_APPROACH_QUESTION;
        } else {
            text = SIXTY_SECONDS_PASSED_MESSAGE + currentRound + FIRST_EXERCISE_IN_ROUND_MESSAGE + currentExerciseName + START_APPROACH_QUESTION;
        }
        return text;
    }

    /**
     * Функция получения текста сообщения о завершении работы и начале отдыха
     * @return - строка - текст сообщения
     */
    private String getTextForStartRestMessage(String chatId){
        String text = "";
        User user = users.get(chatId);
        int currentApproach = user.getCurrentApproach();
        int currentExercise = user.getCurrentExercise();
        int currentRound = user.getCurrentRound();
        List<String> workout = user.getWorkout();
        int roundsCount = user.getRoundsCount();
        if (currentApproach < APPROACH_COUNT) {
            user.setCurrentApproach(currentApproach + 1);
            text = START_REST_BETWEEN_APPROACHES_MESSAGE;
        } else if (currentExercise < workout.size() - 1) {
            user.setCurrentApproach(1);
            user.setCurrentExercise(currentExercise + 1);
            text = START_REST_BETWEEN_EXERCISES_MESSAGE;
        } else if (currentRound < roundsCount) {
            user.setCurrentApproach(1);
            user.setCurrentExercise(0);
            user.setCurrentRound(currentRound + 1);
            text = START_REST_BETWEEN_ROUNDS_MESSAGE;
        } else {
            user.setCurrentApproach(1);
            user.setCurrentExercise(0);
            user.setCurrentRound(1);
            text = WORKOUT_FINISHED_MESSAGE;
            user.setFinishedWorkoutCount(user.getFinishedWorkoutCount() + 1);
            addTrainedGroup(chatId);
        }
        return text;
    }

    /**
     * Процедура добавления очередной группы мышц, на которую выполнена тренировка
     */
    private void addTrainedGroup(String chatId){
        User user = users.get(chatId);
        String[] targetGroups = user.getTargetGroups();
        String trainedGroups = user.getTrainedGroups();
        if(targetGroups.length == 2){
            user.setTrainedGroups(trainedGroups + targetGroups[0] + ", " + targetGroups[1] + "; ");
        }
        else{
            user.setTrainedGroups(trainedGroups + targetGroups[0] + "; ");
        }
    }

    /**
     * Процедура установки таймера для уведомлений
     * @param chatId - ID чата пользователя
     */
    private void setNotifications(String chatId) {
        users.get(chatId).getTimerForNotifying().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                notifyUser(chatId);
            }
        }, DAY_TIME, DAY_TIME);
    }

    /**
     * Функция получения текущего дня недели
     * @return - строка - название дня недели
     */
    public String getDay(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        Locale rus = new Locale("ru", "RU");
        String dayOfWeek = new SimpleDateFormat("EEEE", rus).format(date.getTime());
        return  dayOfWeek;
    }

    /**
     * Процедура отправки уведомления пользователю
     */
    private void notifyUser(String chatId){
        int weeklyWorkoutCount = users.get(chatId).getWeeklyWorkoutCount();
        int finishedWorkoutCount = users.get(chatId).getFinishedWorkoutCount();
        String trainedGroups = users.get(chatId).getTrainedGroups();
        if (weeklyWorkoutCount - finishedWorkoutCount > 0) {
            if(trainedGroups.equals("")) {
                bot.sendTextMessage(NOTIFYING_HELLO_MESSAGE + (weeklyWorkoutCount - finishedWorkoutCount), chatId);
            }
            else{
                bot.sendTextMessage(NOTIFYING_HELLO_MESSAGE + (weeklyWorkoutCount - finishedWorkoutCount) +
                        USER_DID_WORKOUTS_MESSAGE + trainedGroups + RECOMMENDATION_MESSAGE, chatId);
            }
        }
        if(getDay().equals(SUNDAY)){
            users.get(chatId).setFinishedWorkoutCount(0);
            users.get(chatId).setTrainedGroups("");
        }
    }
}
