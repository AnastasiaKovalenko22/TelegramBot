package bot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Класс User - класс, отвечающий за сущность пользователя
 * <p>
 * 02.11.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
@NoArgsConstructor
public class User {
    /**
     * Константа - количество подходов
     */
    private static final int APPROACH_COUNT = 8;
    /**
     * Поле id чата пользователя и бота
     */
    @Setter
    @Getter
    private String chatId;

    /**
     * Поле бот
     */
    @Setter
    @Getter
    private ChatBot bot;

    /**
     * Поле индекс текущего упражнения
     */
    @Setter
    @Getter
    private int currentExercise = 0;

    /**
     * Поле номер текущего раунда
     */
    @Setter
    @Getter
    private int currentRound = 1;

    /**
     * Поле номер текущего подхода
     */
    @Setter
    @Getter
    private int currentApproach = 1;

    /**
     * Поле сборщик тренировки
     */
    @Setter
    @Getter
    private WorkoutMaker workoutMaker = new WorkoutMaker();

    /**
     * Поле список упражнений для тренировки
     */
    @Setter
    @Getter
    private List<String> workout;

    /**
     * Поле уровень сложности
     */
    @Getter
    private String level;

    /**
     * Поле количество законченных тренировок
     */
    @Setter
    @Getter
    private int finishedWorkoutCount = 0;

    /**
     * Поле количество тренировок в неделю
     */
    @Setter
    @Getter
    private int weeklyWorkoutCount = 0;

    /**
     * Поле - список гурпп мышц, на которые уже сделана тренировка на текущей неделе
     */
    private String trainedGroups = "";

    /**
     * Конструктор - создание нового объекта с определенными значениями
     *
     * @param chatId - id чата пользователя и бота
     */
    public User(String chatId) {
        this.chatId = chatId;
    }

    /**
     * Процедура присваивания значения полю {@link User#level} и полю {@link User#weeklyWorkoutCount}
     * @param level - уровень сложности
     */
    public void setLevel(String level) {
        this.level = level;
        if (level.equals("новичок")) {
            weeklyWorkoutCount = 3;
        } else if (level.equals("любитель")) {
            weeklyWorkoutCount = 4;
        } else
            weeklyWorkoutCount = 5;
    }

    /**
     * Поле таймер для уведомнений о количестве оставшихся тренировок на неделе
     */
    @Getter
    @Setter
    private Timer timerForNotifying = new Timer();

    /**
     * Поле калькулятора статистики
     */
    private StatisticCalculator calculator = StatisticCalculator.getInstance();

    /**
     * Функция получения названия упражнения
     */
    public String getExerciseName() {
        return workout.get(currentExercise);
    }

    /**
     * Процедура выполнения пользователем упражнения
     */
    public void doExercise() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                startRest();
            }
        }, 20 * 1000);

    }

    /**
     * Процедура отдыха пользователя
     *
     * @param restTime - время отдыха в секундах
     */
    public void rest(int restTime) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                startWork();
            }
        }, restTime * 1000);
    }

    /**
     * Процедура отправки поьзователю сообщений о завершении рабочего времени
     */
    private void startRest() {
        String text = getTextForStartRestMessage();
        if (!text.equals("Тренировка завершена!")) {
            String[] textWords = text.split(" ");
            String[] options = new String[]{"начать", "завершить тренировку"};
            Map<String, String> callbacks = new HashMap<>();
            callbacks.put("начать", "{\"start rest\":\"" + textWords[textWords.length - 2] + "\"}");
            callbacks.put("завершить тренировку", "{\"stop\":\"stop\"}");
            bot.sendMessageWithButtons(text, chatId, options, callbacks);
        } else {
            bot.sendTextMessage(text, chatId);
        }
    }

    /**
     * Функция получения текста сообщения о завершении работы и начале отдыха
     * @return - строка - текст сообщения
     */
    public String getTextForStartRestMessage(){
        String text = "";
        if (currentApproach < APPROACH_COUNT) {
            currentApproach++;
            text = "20 секунд прошло! Начать отдых между подходами 10 секунд?";
        } else if (currentExercise < workout.size() - 1) {
            currentApproach = 1;
            currentExercise++;
            text = "20 секунд прошло! Начать отдых между упражнениями 10 секунд?";
        } else if (currentRound < workoutMaker.setRoundsCount()) {
            currentApproach = 1;
            currentExercise = 0;
            currentRound++;
            text = "20 секунд прошло! Начать отдых между раундами 60 секунд?";
        } else {
            currentApproach = 1;
            currentExercise = 0;
            currentRound = 1;
            text = "Тренировка завершена!";
            finishedWorkoutCount++;
            addTrainedGroup();
        }
        return text;
    }

    /**
     * Процедура добавления очередной группы мышц, на которую выполнена тренировка в {@link User#trainedGroups}
     */
    private void addTrainedGroup(){
        if(workoutMaker.getTargetGroups().length == 2){
            trainedGroups += workoutMaker.getTargetGroups()[0] + ", " + workoutMaker.getTargetGroups()[1] + "; ";
        }
        else{
            trainedGroups += workoutMaker.getTargetGroups()[0] + "; ";
        }
    }

    /**
     * Процедура отправки пользователю сообщений о завершении времени отдыха
     */
    private void startWork() {
        String text = getTextForStartWorkMessage();
        if(currentApproach > 1) {
            String[] options = new String[]{"начать", "завершить тренировку"};
            Map<String, String> callbacks = new HashMap<>();
            callbacks.put("начать", "{\"start approach\":\"start\"}");
            callbacks.put("завершить тренировку", "{\"stop\":\"stop\"}");
            bot.sendMessageWithButtons(text, chatId, options, callbacks);
        } else{
            String[] options = new String[]{"начать", "завершить тренировку", "техника выполнения"};
            Map<String, String> callbacks = new HashMap<>();
            callbacks.put("начать", "{\"start approach\":\"start\"}");
            callbacks.put("завершить тренировку", "{\"stop\":\"stop\"}");
            callbacks.put("техника выполнения", "{\"tech\":\"show\"}");
            bot.sendMessageWithButtons(text, chatId, options, callbacks);
        }
    }

    /**
     * Функция получения текста сообщения о завершении отдыха и начале работы
     * @return - строка - текст сообщения
     */
    public String getTextForStartWorkMessage(){
        String text = "";
        if (currentApproach > 1) {
            text = "10 секунд прошло! Начать " + currentApproach + " подход?";
        } else if (currentExercise > 0) {
            text = "10 секунд прошло! " + (currentExercise + 1) + " упражнение: " + getExerciseName() + "! Начать?";
        } else {
            text = "60 секунд прошло! " + currentRound + " раунд! 1 упражнение: " + getExerciseName() + "! Начать?";
        }
        return text;
    }

    /**
     * Процедура переодической отправки уведомлений о количестве оставшихся тренировок на неделе пользователю
     */
    public void setNotifications() {
        timerForNotifying.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                notifyUser();
            }
        }, 24*60*60*1000, 24*60*60*1000); //24*60*60*1000 вместо обоих 10*1000
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
    private void notifyUser(){
        if (weeklyWorkoutCount - finishedWorkoutCount > 0) {
            if(trainedGroups.equals("")) {
                bot.sendTextMessage("Привет! На этой неделе осталось тренировок: " + (weeklyWorkoutCount - finishedWorkoutCount), chatId);
            }
            else{
                bot.sendTextMessage("Привет! На этой неделе осталось тренировок: " + (weeklyWorkoutCount - finishedWorkoutCount) +
                        " Вы уже делали тренировку на " + trainedGroups + "рекомендую выбрать дргую групп мышц", chatId);
            }
        }
        if(getDay().equals("воскресенье")){
            finishedWorkoutCount = 0;
            trainedGroups = "";
        }
    }
}
