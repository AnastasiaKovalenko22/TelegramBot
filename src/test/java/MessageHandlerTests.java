import bot.ChatBot;
import bot.MessagesHandler;
import bot.User;
import bot.WorkoutMaker;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.spi.ImageInputStreamSpi;
import java.util.ArrayList;
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
    /**
     * Тест на корректную обработку команды help
     */
    @Test
    public void returnsCorrectHelpMessage(){
        ChatBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        FakeBot.botMessages = new ArrayList<>();
        messagesHandler.handleTextMessage("/help", "123");
        Assert.assertTrue(messagesHandler.getHelpMessage().equals(FakeBot.botMessages.get(0)));
    }
    /**
     * Тест на корректную обработку непонятного сообщения
     */
    @Test
    public void returnsCorrectMisunderstandMessage(){
        ChatBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        FakeBot.botMessages = new ArrayList<>();
        messagesHandler.handleTextMessage("abraboooo", "555");
        Assert.assertTrue(messagesHandler.getMisunderstandMessage().equals(FakeBot.botMessages.get(0)));
    }
    /**
     * Тест на корректную обработку команды start
     */
    @Test
    public void returnsCorrectStartMessage(){
        ChatBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        FakeBot.botMessages = new ArrayList<>();
        messagesHandler.handleTextMessage("/start", "333");
        Assert.assertTrue(messagesHandler.getChooseLevelRequest().equals(FakeBot.botMessages.get(0)));
    }
    /**
     * Тест на корректную обработку команды top и все, что с ней связано
     */
    @Test
    public void returnsCorrectMessagesWhenUserTriesToGetStatistics(){
        ChatBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        FakeBot.botMessages = new ArrayList<>();
        messagesHandler.handleTextMessage("/top", "333");
        Assert.assertTrue(messagesHandler.getChooseNameRequest().equals(FakeBot.botMessages.get(0)));
        messagesHandler.handleTextMessage("имя - Петя", "333");
        Assert.assertTrue((messagesHandler.getYourNameMessage() + "Петя").equals(FakeBot.botMessages.get(1)));
        messagesHandler.handleTextMessage("/top", "333");
        Assert.assertTrue(messagesHandler.getChooseStatisticRequest().equals(FakeBot.botMessages.get(2)));
        messagesHandler.handleTextMessage("имя - Петя", "333");
        Assert.assertTrue(("имя Петя уже занято, попробуйте другое").equals(FakeBot.botMessages.get(3)));
    }
    /**
     * Тест на корректную обработку выбранного уровня
     */
    @Test
    public void returnsCorrectChooseGroupMessage(){
        ChatBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        FakeBot.botMessages = new ArrayList<>();
        messagesHandler.handleTextMessage("/start","111");
        messagesHandler.handleCallback("{\"chosen level\":\"новичок\"}", "111");
        Assert.assertTrue("Вы выбрали уровень новичок".equals(FakeBot.botMessages.get(1)));
        Assert.assertTrue("Выберите целевую группу мышц".equals(FakeBot.botMessages.get(2)));
    }
    /**
     * Тест на корректную обработку выбранной группы мышц
     */
    @Test
    public void returnsCorrectStartWorkoutMessage(){
        ChatBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        FakeBot.botMessages = new ArrayList<>();
        messagesHandler.handleTextMessage("/start","111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        Assert.assertTrue("Вы выбрали следующую группу мышц: ноги".equals(FakeBot.botMessages.get(1)));
        Assert.assertTrue("Начать тренировку ?".equals(FakeBot.botMessages.get(2)));
    }
    /**
     * Тест на корректную обработку начала подхода
     */
    @Test
    public void returnsCorrectStartApproachMessage(){
        ChatBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        FakeBot.botMessages = new ArrayList<>();
        messagesHandler.handleTextMessage("/start","111");
        messagesHandler.handleCallback("{\"chosen level\":\"новичок\"}", "111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        messagesHandler.handleCallback("{\"start approach\":\"start\"}", "111");
        Assert.assertTrue("Paботаем!".equals(FakeBot.botMessages.get(5)));
    }
    /**
     * Тест на корректную обработку начала отдыха
     */
    @Test
    public void returnsCorrectStartRestMessage(){
        ChatBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        FakeBot.botMessages = new ArrayList<>();
        messagesHandler.handleTextMessage("/start","111");
        messagesHandler.handleCallback("{\"chosen level\":\"новичок\"}", "111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        messagesHandler.handleCallback("{\"start rest\":\"10\"}", "111");
        Assert.assertTrue("Отдых!".equals(FakeBot.botMessages.get(5)));
    }
    /**
     * Тест на корректную обработку отмены тренировки
     */
    @Test
    public void returnsCorrectCancelWorkoutMessage(){
        ChatBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        FakeBot.botMessages = new ArrayList<>();
        messagesHandler.handleCallback("{\"cancel\":\"cancel\"}", "111");
        Assert.assertTrue("Тренировка отменена!".equals(FakeBot.botMessages.get(0)));
    }
    /**
     * Тест на корректную обработку завершения тренировки
     */
    @Test
    public void returnsCorrectStopWorkoutMessage(){
        ChatBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        FakeBot.botMessages = new ArrayList<>();
        messagesHandler.handleCallback("{\"stop\":\"stop\"}", "111");
        Assert.assertTrue("Тренировка завершена!".equals(FakeBot.botMessages.get(0)));
    }
    /**
     * Тест на корректную обработку запроса статистики
     */
    @Test
    public void returnsCorrectStatisticMessage(){
        ChatBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        FakeBot.botMessages = new ArrayList<>();
        messagesHandler.handleTextMessage("имя - Вася","111");
        messagesHandler.handleCallback("{\"chosen statType\":\"общая\"}", "111");
        Assert.assertTrue("Вы еще не сделали ни одной тренировки в этой категории".equals(FakeBot.botMessages.get(1)));
    }
}
