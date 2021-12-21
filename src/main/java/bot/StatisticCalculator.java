package bot;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * Класс StatisticCalculator - калькулятор статистики
 * <p>
 * 15.12.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class StatisticCalculator {

    /**
     * Константа - общий тип статистики
     */
    private static final String GENERAL_STAT_TYPE = "общая";

    /**
     * Константа - тип статистики пресс
     */
    private static final String PRESS_STAT_TYPE = "пресс";

    /**
     * Константа - тип статистики ноги
     */
    private static final String LEGS_STAT_TYPE = "ноги";

    /**
     * Константа - тип статистики руки, грудь и спина
     */
    private static final String ARMS_STAT_TYPE = "руки+грудь+спина";

    /**
     * Константа - тип статистики пресс и руки,грудь и спина
     */
    private static final String PRESS_AND_ARMS_STAT_TYPE = "пресс, руки+грудь+спина";

    /**
     * Константа - тип статистики пресс и ноги
     */
    private static final String LEGS_AND_PRESS_STAT_TYPE = "ноги, пресс";

    /**
     * Константа - тип статистики ноги и руки,грудь и спина
     */
    private static final String LEGS_AND_ARMS_STAT_TYPE = "ноги, руки+грудь+спина";

    /**
     * Константа - сообщение о том, что пользователь еще не сделал ни одной тренировки
     */
    private static final String YOU_HAVE_NOT_DONE_ANY_WORKOUTS_MESSAGE = "Вы еще не сделали ни одной тренировки в этой категории";

    /**
     * Константа - сообщение о том, что пользователь на первом месте
     */
    private static final String FIRST_PLACE_MESSAGE = "Вы на 1 месте, поздравляю!";

    /**
     * Константа - начало сообщения о месте пользователя
     */
    private static final String YOUR_PLACE_MESSAGE = "Вы на ";

    /**
     * Константа - конец сообщения о месте пользователя
     */
    private static final String YOUR_PLACE_MESSAGE_SECOND_PART = " месте, чтобы догнать ближайшего соперника необходимо сделать тренировок: ";
    /**
     * Поле данные для общей статистики, (ключ - id чата, значение - количество тренировок)
     */
    private Map<String, Integer> generalStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на пресс, (ключ - id чата, значение - количество тренировок)
     */
    private Map<String, Integer> pressStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на ноги, (ключ - id чата, значение - количество тренировок)
     */
    private Map<String, Integer> legsStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на руки, грудь и спину, (ключ - id чата, значение - количество тренировок)
     */
    private Map<String, Integer> armsStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на пресс и руки, грудь и спину, (ключ - id чата, значение - количество тренировок)
     */
    private Map<String, Integer> pressAndArmsStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на ноги и пресс, (ключ - id чата, значение - количество тренировок)
     */
    private Map<String, Integer> pressAndLegsStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на ноги и руки, грудь и спину, (ключ - id чата, значение - количество тренировок)
     */
    private Map<String, Integer> armsAndLegsStatData = new HashMap<>();

    /**
     * Поле данные о имени пользователя для соответствующего id чата пользователя, (ключ - id чата, значение - имя пользователя)
     */
    private Map<String, String> userNames = new HashMap<>();

    /**
     * Процедура обновления информации о результатах пользователя
     * @param chatId - id чата
     * @param statType - тип статистики, в которой нужно обновить данные
     */
    public void updateStat(String chatId, String statType){
        switch (statType){
            case PRESS_STAT_TYPE:
                addFinishedWorkoutToStatData(chatId, pressStatData);
                break;
            case LEGS_STAT_TYPE:
                addFinishedWorkoutToStatData(chatId, legsStatData);
                break;
            case ARMS_STAT_TYPE:
                addFinishedWorkoutToStatData(chatId, armsStatData);
                break;
            case PRESS_AND_ARMS_STAT_TYPE:
                addFinishedWorkoutToStatData(chatId, pressAndArmsStatData);
                break;
            case LEGS_AND_PRESS_STAT_TYPE:
                addFinishedWorkoutToStatData(chatId, pressAndLegsStatData);
                break;
            case LEGS_AND_ARMS_STAT_TYPE:
                addFinishedWorkoutToStatData(chatId, armsAndLegsStatData);
                break;
            case GENERAL_STAT_TYPE:
                addFinishedWorkoutToStatData(chatId, generalStatData);
                break;
        }
    }

    /**
     * Процедура увеличения колисчества выолненных тренировок на 1
     * @param chatId - id чата
     * @param data - данные, в которых нужно увеличить количество тренировок
     */
    private void addFinishedWorkoutToStatData(String chatId, Map<String, Integer> data){
        if (!generalStatData.containsKey(chatId)){
            generalStatData.put(chatId, 1);
        }
        else {
            generalStatData.put(chatId, generalStatData.get(chatId) + 1);
        }
        if (!data.equals(generalStatData)) {
            if (!data.containsKey(chatId)) {
                data.put(chatId, 1);
            } else {
                data.put(chatId, data.get(chatId) + 1);
            }
        }
    }

    /**
     * Функция проверки на недоступность имени пользователя, которое хочет выбрать поьзователь
     * @param userName - желаемое имя
     * @return - true если имя не свободно, false если доступно
     */
    public boolean isNotFreeUserName(String userName){
        return userNames.containsValue(userName);
    }

    /**
     * Функция проверки существования у пользователя имени для статистики
     * @param chatId - id чата
     * @return - true если существует, false если не существует
     */
    public boolean userHasUserName(String chatId){
        return userNames.containsKey(chatId);
    }

    /**
     * Процедура установки имени пользователя в соответствие id чата
     * @param chatId - id чата
     * @param userName - имя пользователя
     */
    public void setUserName(String chatId, String userName){
        userNames.put(chatId, userName);
    }

    /**
     * Функция получения строки с сообщением о статистике
     * @param chatId - id чата
     * @param statType - тип статистики
     * @return - сообщение о статистике
     */
    public String getStatisticForUser(String chatId, String statType){
        String result = "";
        switch (statType){
            case GENERAL_STAT_TYPE:
                result = createStat(chatId, generalStatData);
                break;
            case PRESS_STAT_TYPE:
                result = createStat(chatId, pressStatData);
                break;
            case LEGS_STAT_TYPE:
                result = createStat(chatId, legsStatData);
                break;
            case ARMS_STAT_TYPE:
                result = createStat(chatId, armsStatData);
                break;
            case PRESS_AND_ARMS_STAT_TYPE:
                result = createStat(chatId, pressAndArmsStatData);
                break;
            case LEGS_AND_PRESS_STAT_TYPE:
                result = createStat(chatId, pressAndLegsStatData);
                break;
            case LEGS_AND_ARMS_STAT_TYPE:
                result = createStat(chatId, armsAndLegsStatData);
                break;
        }
        return result;
    }

    /**
     * Функция создания сообщения о статистике
     * @param chatId - id чата
     * @param data - данные для создания статистики
     * @return - строка с сообщением о статистике
     */
    private String createStat(String chatId, Map<String, Integer> data){
        if (!data.containsKey(chatId)){
            return YOU_HAVE_NOT_DONE_ANY_WORKOUTS_MESSAGE;
        }
        String result = "";
        Queue<StatisticInfo> rating = new PriorityBlockingQueue<>();
        for (Map.Entry<String, Integer> entry:
             data.entrySet()) {
            rating.add(new StatisticInfo(userNames.get(entry.getKey()), entry.getValue()));
        }
        Integer closestOpponentResult = null;
        Integer currentUserPlace = null;
        int currentUserResult = data.get(chatId);
        int lowestResult = rating.peek().getFinishedWorkoutsCount();
        int place = 1;
        int size = rating.size();
        for (int i = 1; i <= size; i++){
            StatisticInfo currentPlaceInfo = rating.poll();
            if(currentPlaceInfo.getFinishedWorkoutsCount() < lowestResult){
                lowestResult = currentPlaceInfo.getFinishedWorkoutsCount();
                place++;
            }
            if (i <= 10) {
                result += place + ") " + currentPlaceInfo + "\n";
            }
            if (currentPlaceInfo.getFinishedWorkoutsCount() == currentUserResult && currentPlaceInfo.getUserName().equals(userNames.get(chatId))){
                currentUserPlace = place;
                if (i > 10) {
                    break;
                }
            }
            else if (currentUserPlace == null && currentPlaceInfo.getFinishedWorkoutsCount() > currentUserResult){
                closestOpponentResult = currentPlaceInfo.getFinishedWorkoutsCount();
            }
        }
        if (closestOpponentResult != null){
            result += YOUR_PLACE_MESSAGE + currentUserPlace + YOUR_PLACE_MESSAGE_SECOND_PART + (closestOpponentResult - currentUserResult);
        }
        else{
            result += FIRST_PLACE_MESSAGE;
        }
        return result;
    }
}
