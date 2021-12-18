package bot;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * Класс StatisticCalculator
 * <p>
 * 15.12.2021
 *
 * @author Анастасия Коваленко
 */
public class StatisticCalculator {
    private Map<String, Integer> generalStatData = new HashMap<>();

    private Map<String, Integer> pressStatData = new HashMap<>();

    private Map<String, Integer> legsStatData = new HashMap<>();

    private Map<String, Integer> armsStatData = new HashMap<>();

    private Map<String, Integer> pressAndArmsStatData = new HashMap<>();

    private Map<String, Integer> pressAndLegsStatData = new HashMap<>();

    private Map<String, Integer> armsAndLegsStatData = new HashMap<>();

    private Map<String, String> userNames = new HashMap<>();

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

    public boolean isNotFreeUserName(String userName){
        return userNames.containsValue(userName);
    }

    public boolean userHasUserName(String chatId){
        return userNames.containsKey(chatId);
    }

    public void setUserName(String chatId, String userName){
        userNames.put(chatId, userName);
    }


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
