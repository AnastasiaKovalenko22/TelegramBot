import bot.StatisticCalculator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Класс StatisticCalculatorTests
 * <p>
 * 19.12.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class StatisticCalculatorTests {
    /**
     * Тест на корректное сообщние осо статистикой в лучае если пользователь в топе
     */
    @Test
    public void returnsCorrectStatisticMessageWhenUserIsInTop(){
        String chatId = "ID";
        String userName = "user";
        StatisticCalculator calculator = new StatisticCalculator();
        for(int i = 1; i <= 10; i++){
            calculator.setUserName(chatId + i, userName + i);
            for(int j = 1; j <= i; j++){
                calculator.updateStat(chatId + i, "пресс");
            }
        }
        Assert.assertTrue(("1) Имя пользователя: user10, количество тренировок: 10\n" +
                "2) Имя пользователя: user9, количество тренировок: 9\n" +
                "3) Имя пользователя: user8, количество тренировок: 8\n" +
                "4) Имя пользователя: user7, количество тренировок: 7\n" +
                "5) Имя пользователя: user6, количество тренировок: 6\n" +
                "6) Имя пользователя: user5, количество тренировок: 5\n" +
                "7) Имя пользователя: user4, количество тренировок: 4\n" +
                "8) Имя пользователя: user3, количество тренировок: 3\n" +
                "9) Имя пользователя: user2, количество тренировок: 2\n" +
                "10) Имя пользователя: user1, количество тренировок: 1\n" +
                "Вы на 8 месте, чтобы догнать ближайшего соперника необходимо сделать тренировок: 1").equals(calculator.getStatisticForUser(chatId + 3, "пресс")));
    }

    /**
     * Тест на корректное сообщние осо статистикой в лучае если пользователь не в топе
     */
    @Test
    public void returnsCorrectStatisticMessageWhenUserIsNotInTop(){
        String chatId = "ID";
        String userName = "user";
        StatisticCalculator calculator = new StatisticCalculator();
        for(int i = 1; i <= 15; i++){
            calculator.setUserName(chatId + i, userName + i);
            for(int j = 1; j <= i; j++){
                calculator.updateStat(chatId + i, "ноги");
            }
        }
        Assert.assertTrue(("1) Имя пользователя: user15, количество тренировок: 15\n" +
                "2) Имя пользователя: user14, количество тренировок: 14\n" +
                "3) Имя пользователя: user13, количество тренировок: 13\n" +
                "4) Имя пользователя: user12, количество тренировок: 12\n" +
                "5) Имя пользователя: user11, количество тренировок: 11\n" +
                "6) Имя пользователя: user10, количество тренировок: 10\n" +
                "7) Имя пользователя: user9, количество тренировок: 9\n" +
                "8) Имя пользователя: user8, количество тренировок: 8\n" +
                "9) Имя пользователя: user7, количество тренировок: 7\n" +
                "10) Имя пользователя: user6, количество тренировок: 6\n" +
                "Вы на 13 месте, чтобы догнать ближайшего соперника необходимо сделать тренировок: 1").equals(calculator.getStatisticForUser(chatId + 3, "ноги")));
    }

    /**
     * Тест на корректное сообщние осо статистикой в лучае если пользователь не сделал тренировку для участия в данном типе статистики
     */
    @Test
    public void returnsCorrectStatisticMessageWhenUserHasNotDoneWorkout(){
        String chatId = "ID";
        String userName = "user";
        StatisticCalculator calculator = new StatisticCalculator();
        for(int i = 1; i <= 15; i++){
            calculator.setUserName(chatId + i, userName + i);
            for(int j = 2; j <= i; j++){
                calculator.updateStat(chatId + i, "ноги");
            }
        }
        Assert.assertTrue(("Вы еще не сделали ни одной тренировки в этой категории").equals(calculator.getStatisticForUser(chatId + 1, "ноги")));
    }
}
