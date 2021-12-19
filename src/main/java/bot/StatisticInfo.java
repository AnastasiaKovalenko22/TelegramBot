package bot;

/**
 * Класс StatisticInfo - объект, содержащий информацию о результатах пользователя для статистики
 * <p>
 * 15.12.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class StatisticInfo implements Comparable<StatisticInfo>{
    /**
     * Поле имя пользователя для статистики
     */
    private String userName;
    /**
     * Поле количество выполненных тренировок
     */
    private int finishedWorkoutsCount;

    /**
     * Функция получения значения поля {@link StatisticInfo#userName}
     * @return - имя пользователя для статистики
     */
    public String getUserName(){
        return userName;
    }

    /**
     * Функция получения значения поля {@link StatisticInfo#finishedWorkoutsCount}
     * @return - количество выполненных тренировок
     */
    public int getFinishedWorkoutsCount(){
        return finishedWorkoutsCount;
    }

    /**
     * Конструктор
     * @param userName - имя пользователя для статистики
     * @param finishedWorkoutsCount - количество выполненных тренировок
     */
    public StatisticInfo(String userName, int finishedWorkoutsCount){
        this.finishedWorkoutsCount = finishedWorkoutsCount;
        this.userName = userName;
    }

    /**
     * Функция полуения строкового представления экземпляра класса
     * @return - строковое представление экземпляра класса
     */
    @Override
    public String toString() {
        return "Имя пользователя: " + userName +
                ", количество тренировок: " + finishedWorkoutsCount;
    }

    /**
     * Функция сравнения экземпляра класса с другим экземпляром
     * @param statisticInfo - другой экземпляр класса
     * @return - целое число в зависимости от результата сравнения
     */
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
