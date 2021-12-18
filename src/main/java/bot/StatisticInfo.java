package bot;

/**
 * Класс StatisticInfo
 * <p>
 * 15.12.2021
 *
 * @author Анастасия Коваленко
 */
public class StatisticInfo implements Comparable<StatisticInfo>{
    private String userName;
    private int finishedWorkoutsCount;

    public String getUserName(){
        return userName;
    }

    public int getFinishedWorkoutsCount(){
        return finishedWorkoutsCount;
    }

    public StatisticInfo(String userName, int finishedWorkoutsCount){
        this.finishedWorkoutsCount = finishedWorkoutsCount;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Имя пользователя: " + userName +
                ", количество тренировок: " + finishedWorkoutsCount;
    }

    @Override
    public int compareTo(StatisticInfo statisticInfo) {
        if (this.finishedWorkoutsCount > statisticInfo.finishedWorkoutsCount){
            return -1;
        }
        else if (this.finishedWorkoutsCount < statisticInfo.finishedWorkoutsCount){
            return 1;
        }
        else {
            return 0;
        }
    }
}
