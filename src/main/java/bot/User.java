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
    /**
     * Поле количества раундов
     */
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

    /**
     * конструктор по умолчанию
     */
    public User() {
    }

    /**
     * Процедура присваивания значения полю {@link User#level} и полю {@link User#weeklyWorkoutCount}
     *
     * @param level - уровень сложности
     */
    public void setLevel(String level) {
        this.level = level;
        if (level.equals(WorkoutMaker.BEGIN_LEVEL)) {
            weeklyWorkoutCount = 3;
            roundsCount = 1;
        } else if (level.equals(WorkoutMaker.MEDIUM_LEVEL)) {
            weeklyWorkoutCount = 4;
            roundsCount = 2;
        } else {
            weeklyWorkoutCount = 5;
            roundsCount = 3;
        }
    }

    /**
     * функция получения значения поля уровень
     *
     * @return значение поля{@link User#level}
     */
    public String getLevel() {
        return level;
    }
    /**
     * функция получения значения поля количество тренировок в неделю
     *
     * @return значение поля{@link User#weeklyWorkoutCount}
     */
    public int getWeeklyWorkoutCount() {
        return weeklyWorkoutCount;
    }
    /**
     * функция получения значения поля количество выполненных тренировок в неделю
     *
     * @return значение поля{@link User#finishedWorkoutCount}
     */
    public int getFinishedWorkoutCount() {
        return finishedWorkoutCount;
    }

    /**
     * Процедура присваивания значения полю количество выполненных тренировок
     * @param value - присваиваемое значение
     */
    public void setFinishedWorkoutCount(int value) {
        if (value >= 0) {
            finishedWorkoutCount = value;
        } else {
            throw new IllegalArgumentException();
        }
    }
    /**
     * функция получения значения поля уже тренированные группы мышц
     *
     * @return значение поля{@link User#trainedGroups}
     */
    public String getTrainedGroups() {
        return trainedGroups;
    }

    /**
     * Процедура присваивания значения полю тренированные группы мышц
     * @param value - присваевоемое значение
     */
    public void setTrainedGroups(String value) {
        trainedGroups = value;
    }
    /**
     * Процедура присваивания значения полю тренировка
     * @param value - присваевоемое значение
     */
    public void setWorkout(List<String> value) {
        workout = value;
    }

    /**
     * функция получения значения поля тренировка
     *
     * @return значение поля{@link User#workout}
     */
    public List<String> getWorkout() {
        return workout;
    }
    /**
     * функция получения значения поля текущий раунд
     *
     * @return значение поля{@link User#currentRound}
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Процедура присваивания значения полю текущий подход
     * @param value - присваевоемое значение
     */
    public void setCurrentApproach(int value) {
        if (value <= 8 && value >= 1) {
            currentApproach = value;
        } else
            throw new IllegalArgumentException();
    }
    /**
     * функция получения значения поля текущее упражнение
     *
     * @return значение поля{@link User#currentExercise}
     */
    public int getCurrentExercise() {
        return currentExercise;
    }

    /**
     * Процедура присваивания значения полю текущее упражнение
     * @param value - присваевоемое значение
     */
    public void setCurrentExercise(int value) {
        if (value <= 7 && value >= 0) {
            currentExercise = value;
        } else
            throw new IllegalArgumentException();
    }
    /**
     * Процедура присваивания значения полю текущий раунд
     * @param value - присваевоемое значение
     */
    public void setCurrentRound(int value) {
        if (value <= 3 && value >= 1) {
            currentRound = value;
        } else
            throw new IllegalArgumentException();
    }
    /**
     * функция получения значения поля количество раундов
     *
     * @return значение поля{@link User#roundsCount}
     */
    public int getRoundsCount() {
        return roundsCount;
    }

    /**
     * Процедура присваивания значения полю целевые группы мышц
     * @param value - присваевоемое знаечение
     */
    public void setTargetGroups(String[] value) {
        for (String group :
                value) {
            if (!(group.equals(WorkoutMaker.PRESS_GROUP) || group.equals(WorkoutMaker.ARMS_GROUP) || group.equals(WorkoutMaker.LEGS_GROUP))) {
                throw new IllegalArgumentException();
            }
        }
        targetGroups = value;
    }
    /**
     * функция получения значения поля текущий подход
     *
     * @return значение поля{@link User#currentApproach}
     */
    public int getCurrentApproach() {
        return currentApproach;
    }
    /**
     * функция получения значения поля целевые группы мышц
     *
     * @return значение поля{@link User#targetGroups}
     */
    public String[] getTargetGroups() {
        return targetGroups;
    }

    /**
     * Поле таймер для уведомлений о количестве оставшихся тренировок на неделе
     */
    private Timer timerForNotifying = new Timer();


    /**
     * Функция получения названия упражнения
     */
    public String getExerciseName() {
        return workout.get(currentExercise);
    }

    /**
     * функция получения уведомлений
     * @return значение поля{@link User#timerForNotifying}
     */
    public Timer getTimerForNotifying() {
        return timerForNotifying;
    }
}
