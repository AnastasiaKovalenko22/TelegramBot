import bot.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс UserTests
 * <p>
 * 08.11.2021
 *
 * @author Анастасия Коваленко
 */
public class UserTests {
    private static final Set<String> days = new HashSet<String>(List.of(new String[]{"понедельник", "вторник", "среда", "четверг", "пятница", "суббота", "воскресенье"}));

    /**
     * Тест на корректный формат названия дня недели
     */
    @Test
    public void getDayReturnsCorrectDayName(){
        User user = new User();
        Assert.assertTrue(days.contains(user.getDay()));
    }

    /**
     * Тест на корректное сообщение о начале отдыха между подходами
     */
    @Test
    public void getTextForStartRestMessageReturnsCorrectTextWhenRestBetweenApproaches(){
        User user = new User();
        user.getWorkoutMaker().setLevel("новичок");
        user.getWorkoutMaker().setTargetGroups(new String[]{"ноги"});
        user.setWorkout(user.getWorkoutMaker().createWorkout());
        user.setCurrentApproach(3);
        user.setCurrentExercise(4);
        Assert.assertTrue(user.getTextForStartRestMessage().equals("20 секунд прошло! Начать отдых между подходами 10 секунд?"));
    }

    /**
     * Тест на корректное сообщение о начале отдыха между упражнениями
     */
    @Test
    public void getTextForStartRestMessageReturnsCorrectTextWhenRestBetweenExercises(){
        User user = new User();
        user.getWorkoutMaker().setLevel("новичок");
        user.getWorkoutMaker().setTargetGroups(new String[]{"ноги"});
        user.setWorkout(user.getWorkoutMaker().createWorkout());
        user.setCurrentApproach(8);
        user.setCurrentExercise(4);
        Assert.assertTrue(user.getTextForStartRestMessage().equals("20 секунд прошло! Начать отдых между упражнениями 10 секунд?"));
    }

    /**
     * Тест на корректное сообщение об окончании тренировки
     */
    @Test
    public void getTextForStartRestMessageReturnsCorrectTextWhenWorkoutIsFinished(){
        User user = new User();
        user.getWorkoutMaker().setLevel("новичок");
        user.getWorkoutMaker().setTargetGroups(new String[]{"ноги"});
        user.setWorkout(user.getWorkoutMaker().createWorkout());
        user.setCurrentApproach(8);
        user.setCurrentExercise(5);
        Assert.assertTrue(user.getTextForStartRestMessage().equals("Тренировка завершена!"));
    }

    /**
     * Тест на корректное сообщение о начале отдыха между раундами
     */
    @Test
    public void getTextForStartRestMessageReturnsCorrectTextWhenRestBetweenRounds(){
        User user = new User();
        user.getWorkoutMaker().setLevel("любитель");
        user.getWorkoutMaker().setTargetGroups(new String[]{"ноги"});
        user.setWorkout(user.getWorkoutMaker().createWorkout());
        user.setCurrentApproach(8);
        user.setCurrentExercise(7);
        Assert.assertTrue(user.getTextForStartRestMessage().equals("20 секунд прошло! Начать отдых между раундами 60 секунд?"));
    }

    /**
     * Тест на корректное сообщение о начале следующего подхода
     */
    @Test
    public void getTextForStartWorkMessageReturnsCorrectTextWhenStartApproach(){
        User user = new User();
        user.getWorkoutMaker().setLevel("любитель");
        user.getWorkoutMaker().setTargetGroups(new String[]{"ноги"});
        user.setWorkout(user.getWorkoutMaker().createWorkout());
        user.setCurrentApproach(8);
        user.setCurrentExercise(7);
        Assert.assertTrue(user.getTextForStartWorkMessage().equals("10 секунд прошло! Начать 8 подход?"));
    }

    /**
     * Тест на корректное сообщение о начале отдыха следующего упражнения
     */
    @Test
    public void getTextForStartWorkMessageReturnsCorrectTextWhenStartExercise(){
        User user = new User();
        user.getWorkoutMaker().setLevel("любитель");
        user.getWorkoutMaker().setTargetGroups(new String[]{"ноги"});
        user.setWorkout(user.getWorkoutMaker().createWorkout());
        user.setCurrentApproach(1);
        user.setCurrentExercise(7);
        Assert.assertTrue(user.getTextForStartWorkMessage().equals("10 секунд прошло! 8 упражнение: " + user.getExerciseName() + "! Начать?"));
    }

    /**
     * Тест на корректное сообщение о начале следующего раунда
     */
    @Test
    public void getTextForStartWorkMessageReturnsCorrectTextWhenStartRound(){
        User user = new User();
        user.getWorkoutMaker().setLevel("продвинутый");
        user.getWorkoutMaker().setTargetGroups(new String[]{"ноги"});
        user.setWorkout(user.getWorkoutMaker().createWorkout());
        user.setCurrentRound(2);
        user.setCurrentApproach(1);
        user.setCurrentExercise(0);
        Assert.assertTrue(user.getTextForStartWorkMessage().equals("60 секунд прошло! 2 раунд! 1 упражнение: " + user.getExerciseName() + "! Начать?"));
    }

}
