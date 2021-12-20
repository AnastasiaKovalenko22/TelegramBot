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
     * Поле данные для общей статистики
     */
    private Map<String, Integer> generalStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на пресс
     */
    private Map<String, Integer> pressStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на ноги
     */
    private Map<String, Integer> legsStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на руки, грудь и спину
     */
    private Map<String, Integer> armsStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на пресс и руки, грудь и спину
     */
    private Map<String, Integer> pressAndArmsStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на ноги и пресс
     */
    private Map<String, Integer> pressAndLegsStatData = new HashMap<>();

    /**
     * Поле данные для статистики по тренировкам на ноги и руки, грудь и спину
     */
    private Map<String, Integer> armsAndLegsStatData = new HashMap<>();

    /**
     * Поле данные о имени пользователя для соответствующего id чата пользователя
     */
    private Map<String, String> userNames = new HashMap<>();

    /**
     * Процедура обновления информации о результатах пользователя
     * @param chatId - id чата
     * @param statType - тип статистики, в которой нужно обновить данные
     */
    public void updateStat(String chatId, String statType){
        switch (statType){
            case "пресс":
                addFinishedWorkoutToStatData(chatId, pressStatData);
                break;
            case "ноги":
                addFinishedWorkoutToStatData(chatId, legsStatData);
                break;
            case "руки+грудь+спина":
                addFinishedWorkoutToStatData(chatId, armsStatData);
                break;
            case "пресс, руки+грудь+спина":
                addFinishedWorkoutToStatData(chatId, pressAndArmsStatData);
                break;
            case "ноги, пресс":
                addFinishedWorkoutToStatData(chatId, pressAndLegsStatData);
                break;
            case "ноги, руки+грудь+спина":
                addFinishedWorkoutToStatData(chatId, armsAndLegsStatData);
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
        if (!data.containsKey(chatId)){
            data.put(chatId, 1);
        }
        else{
            data.put(chatId, data.get(chatId) + 1);
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
            case "общая":
                result = createStat(chatId, generalStatData);
                break;
            case "пресс":
                result = createStat(chatId, pressStatData);
                break;
            case "ноги":
                result = createStat(chatId, legsStatData);
                break;
            case "руки+грудь+спина":
                result = createStat(chatId, armsStatData);
                break;
            case "пресс, руки+грудь+спина":
                result = createStat(chatId, pressAndArmsStatData);
                break;
            case "ноги, пресс":
                result = createStat(chatId, pressAndLegsStatData);
                break;
            case "ноги, руки+грудь+спина":
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
            return "Вы еще не сделали ни одной тренировки в этой категории";
        }
        String result = "";
        Queue<StatisticInfo> rating = new PriorityBlockingQueue<>();
        for (Map.Entry<String, Integer> entry:
             data.entrySet()) {
            rating.add(new StatisticInfo(userNames.get(entry.getKey()), entry.getValue()));
        }
        Integer previous = null;
        Integer currentUserPlace = null;
        Integer currentUserResult = data.get(chatId);
        int size = rating.size();
        for (int i = 1; i <= size; i++){
            StatisticInfo currentPlaceInfo = rating.poll();
            if (i <= 10) {
                result += i + ") " + currentPlaceInfo + "\n";
            }
            if (currentPlaceInfo.getFinishedWorkoutsCount() == currentUserResult && currentPlaceInfo.getUserName().equals(userNames.get(chatId))){
                currentUserPlace = i;
                if (i > 10) {
                    break;
                }
            }
            else if (currentUserPlace == null && currentPlaceInfo.getFinishedWorkoutsCount() > currentUserResult){
                previous = currentPlaceInfo.getFinishedWorkoutsCount();
            }
        }
        if (previous != null){
            result += "Вы на " + currentUserPlace + " месте, чтобы догнать ближайшего соперника необходимо сделать тренировок: " + (previous - currentUserResult);
        }
        else{
            result += "Вы на 1 месте, поздравляю!";
        }
        return result;
    }
}
