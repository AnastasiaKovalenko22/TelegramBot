package bot;

import api.longpoll.bots.model.response.utils.UtilsGetLinkStatsResponse;

import java.util.*;

public class StatisticCalculator {
    public static final String GENERAL_STAT = "general";

    private static StatisticCalculator instance;

    private SortedSet<StatisticInfo> generalStatistic = new TreeSet<>();

    private StatisticCalculator() {
    }

    public static StatisticCalculator getInstance() {
        if (instance == null){
            instance = new StatisticCalculator();
        }
        return instance;
    }


    public SortedSet<StatisticInfo> calculateStatistic(String statisticType, StatisticInfo userInfo) {
        SortedSet<StatisticInfo> result = null;
        switch (statisticType) {
            case "general":
                result = generalStatistic;
                break;
        }
        addUser(result, userInfo);
        return result;
    }

    private void addUser(SortedSet<StatisticInfo> stat, StatisticInfo userInfo) {
        StatisticInfo previousResult = new StatisticInfo(userInfo.getUserName(), userInfo.getFinishedWorkoutCount() - 1);
        if (stat.contains(previousResult)) {
            stat.remove(previousResult);
        }
        stat.add(userInfo);
    }

    public String getStrStat(NavigableSet<StatisticInfo> stat, StatisticInfo userInfo) {
        int size = stat.size();
        String result = "";
        int place = 1;
        for (StatisticInfo element :
                stat) {
            result += place + ". " + element.getUserName() + " Сделано тренировок: " + element.getFinishedWorkoutCount() + "\n";
            place += 1;
            if (place > 10) {
                break;
            }
        }
        int userPlace = stat.floor(userInfo).getFinishedWorkoutCount();
        StatisticInfo previousPerson = stat.lower(userInfo);
        result += "Чтобы догнать " + previousPerson.getUserName() + ", вам нужно сделать тренировок: " + (previousPerson.getFinishedWorkoutCount() - userInfo.getFinishedWorkoutCount());
        return result;
    }


}
