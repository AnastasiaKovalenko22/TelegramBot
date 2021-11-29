package bot;

import lombok.Getter;
import lombok.Setter;

public class StatisticInfo implements Comparable<StatisticInfo> {
    @Getter
    @Setter
    private String userName;

    @Getter
    private int finishedWorkoutCount;

    public void setFinishedWorkoutCount(int value) {
        if (value >= 0) {
            finishedWorkoutCount = value;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public StatisticInfo(String userName, int finishedWorkoutCount) {
        setUserName(userName);
        setFinishedWorkoutCount(finishedWorkoutCount);
    }

    @Override
    public int compareTo(StatisticInfo s) {
        if (this.finishedWorkoutCount < s.getFinishedWorkoutCount()) {
            return -1;
        } else if (this.finishedWorkoutCount > s.getFinishedWorkoutCount()) {
            return 1;
        } else {
            return 0;
        }


    }
}
