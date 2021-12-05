import bot.MessagesHandler;
import bot.User;
import bot.WorkoutMaker;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Класс MessageHandlerTests
 * <p>
 * 08.11.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class MessageHandlerTests {
    /**
     * Список возможных значений дня недели
     */
    private static final List<String> days = List.of("понедельник", "вторник", "среда", "четверг", "пятница", "суббота", "воскресенье");

    /**
     * Тест на корректный формат названия дня недели
     */
    @Test
    public void getDayReturnsCorrectDayName(){
        MessagesHandler messagesHandler = new MessagesHandler();
        Assert.assertTrue(days.contains(messagesHandler.getDay()));
    }
}
