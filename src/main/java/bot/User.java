package bot;

import java.util.*;

/**
 * Класс User - класс, отвечающий за сущность пользователя
 * <p>
 * 02.11.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class User {

    /**
     * Поле индекс текущего упражнения
     */
    private int currentExercise = 0;

    /**
     * Поле номер текущего раунда
     */
    private int currentRound = 1;

    /**
     * Поле номер текущего подхода
     */
    private int currentApproach = 1;

    /**
     * Поле сборщик тренировки
     */
    private String[] targetGroups;

    /**
     * Поле список упражнений для тренировки
     */
    private List<String> workout;

    /**
     * Поле уровень сложности
     */
    private String level;

    private int roundsCount;

    /**
     * Поле количество законченных тренировок
     */
    private int finishedWorkoutCount = 0;

    /**
     * Поле количество тренировок в неделю
     */
    private int weeklyWorkoutCount = 0;

    /**
     * Поле - список гурпп мышц, на которые уже сделана тренировка на текущей неделе
     */
    private String trainedGroups = "";

    public User(){}

    /**
     * Процедура присваивания значения полю {@link User#level} и полю {@link User#weeklyWorkoutCount}
     * @param level - уровень сложности
     */
    public void setLevel(String level) {
        this.level = level;
        if (level.equals(WorkoutMaker.getBeginLevel())) {
            weeklyWorkoutCount = 3;
        } else if (level.equals(WorkoutMaker.getMediumLevel())) {
            weeklyWorkoutCount = 4;
        } else
            weeklyWorkoutCount = 5;
        if (level.equals(WorkoutMaker.getBeginLevel())) {
            roundsCount = 1;
        }
        if (level.equals(WorkoutMaker.getMediumLevel())) {
            roundsCount = 2;
        }else {
            roundsCount = 3;
        }
    }

    public String getLevel(){
        return level;
    }

    public int getWeeklyWorkoutCount(){
        return weeklyWorkoutCount;
    }
    public int getFinishedWorkoutCount(){
        return finishedWorkoutCount;
    }
    public void setFinishedWorkoutCount(int value){
        if (value >= 0){
            finishedWorkoutCount = value;
        }
        else{
            throw new IllegalArgumentException();
        }
    }
    public String getTrainedGroups(){
        return trainedGroups;
    }
    public void setTrainedGroups(String value){
        trainedGroups = value;
    }

    public void setWorkout(List<String> value){
        workout = value;
    }

    public List<String> getWorkout(){
        return workout;
    }

    public int getCurrentRound(){
        return currentRound;
    }


    public void setCurrentApproach(int value){
        if (value <= 8 && value >= 1) {
            currentApproach = value;
        }
        else
            throw new IllegalArgumentException();
    }

    public int getCurrentExercise(){
        return currentExercise;
    }

    public void setCurrentExercise(int value){
        if (value <= 7 && value >= 0) {
            currentApproach = value;
        }
        else
            throw new IllegalArgumentException();
    }

    public void setCurrentRound(int value){
        if (value <= 3 && value >= 1) {
            currentApproach = value;
        }
        else
            throw new IllegalArgumentException();
    }

    public int getRoundsCount(){
        return roundsCount;
    }

    public void setTargetGroups(String[] value){
        for (String group:
             value) {
            if (!(group.equals(WorkoutMaker.getPressGroup()) || group.equals(WorkoutMaker.getArmsGroup()) || group.equals(WorkoutMaker.getLegsGroup()))){
                throw new IllegalArgumentException();
            }
        }
        targetGroups = value;
    }

    public int getCurrentApproach(){
        return currentApproach;
    }

    public String[] getTargetGroups(){
        return targetGroups;
    }

    /**
     * Поле таймер для уведомнений о количестве оставшихся тренировок на неделе
     */
    private Timer timerForNotifying = new Timer();


    /**
     * Функция получения названия упражнения
     */
    public String getExerciseName() {
        return workout.get(currentExercise);
    }

    public Timer getTimerForNotifying(){
        return timerForNotifying;
    }
}
