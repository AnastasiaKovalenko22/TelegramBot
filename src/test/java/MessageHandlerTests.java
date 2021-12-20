import bot.ChatBot;
import bot.MessagesHandler;
import bot.User;
import bot.WorkoutMaker;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.spi.ImageInputStreamSpi;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void getDayReturnsCorrectDayName() {
        MessagesHandler messagesHandler = new MessagesHandler();
        Assert.assertTrue(days.contains(messagesHandler.getDay()));
    }

    /**
     * Тест на корректную обработку команды help
     */
    @Test
    public void returnsCorrectHelpMessage() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/help", "123");
        Assert.assertEquals("Привет, я - бот для создания тренировок по методу табата! Табата – система тренировок, которая была придумана японским физиологом Изуми Табата. Он доказал, что интервальные тренировки при мощности работы на 70% от МПК (максимального потребления кислорода) способны одновременно привести к росту аэробной и анаэробной выносливости. Упражнения выполняются циклами. 1 цикл: 20 секунд работы, 10 секунд отдыха, 8 подходов. Количество пражнений в 1 раунде = колчество циклов. Я составлю тебе тренировку по выбранным тобою параметрам (уровень сложности, целевая группа мышц) Пиши /start, чтобы начать!", fakeBot.getBotMessages().get(0));
    }

    /**
     * Тест на корректную обработку непонятного сообщения
     */
    @Test
    public void returnsCorrectMisunderstandMessage() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("abraboooo", "555");
        Assert.assertEquals("Извините, я вас не понял :(, попробуйте еще раз", fakeBot.getBotMessages().get(0));
    }

    /**
     * Тест на корректную обработку команды start
     */
    @Test
    public void returnsCorrectStartMessage() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/start", "333");
        Assert.assertEquals("Выберите уровень сложности\nновичок : 1 раунд 6 циклов\nлюбитель : 2 раунда по 8 циклов\nпродвинутый : 3 раунда по 8 циклов", fakeBot.getBotMessages().get(0));
    }

    /**
     * Тест на корректную обработку команды top и всего, что с ней связано
     */
    @Test
    public void returnsCorrectMessagesWhenUserTriesToGetStatistics() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/top", "333");
        Assert.assertEquals("Вам необходимо выбрать имя пользователя для того, чтобы участвовать в рейтингах пользователей. Пришлите сообщение в формате: имя - ваше имя, которое вы хотите видеть в статистике(пример: имя - Вася Пупкин)", fakeBot.getBotMessages().get(0));
        messagesHandler.handleTextMessage("имя - Петя", "333");
        Assert.assertEquals("Вам присвоено имя: " + "Петя", fakeBot.getBotMessages().get(1));
        messagesHandler.handleTextMessage("/top", "333");
        Assert.assertEquals("Выберите тип статистики, который вы хотите увидеть", fakeBot.getBotMessages().get(2));
        messagesHandler.handleTextMessage("имя - Петя", "333");
        Assert.assertEquals("имя Петя уже занято, попробуйте другое", fakeBot.getBotMessages().get(3));
    }

    /**
     * Тест на корректную обработку выбранного уровня
     */
    @Test
    public void returnsCorrectChooseGroupMessage() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/start", "111");
        messagesHandler.handleCallback("{\"chosen level\":\"новичок\"}", "111");
        Assert.assertEquals("Вы выбрали уровень новичок", fakeBot.getBotMessages().get(1));
        Assert.assertEquals("Выберите целевую группу мышц", fakeBot.getBotMessages().get(2));
    }

    /**
     * Тест на корректную обработку выбранной группы мышц
     */
    @Test
    public void returnsCorrectStartWorkoutMessage() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/start", "111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        Assert.assertEquals("Вы выбрали следующую группу мышц: ноги", fakeBot.getBotMessages().get(1));
        Assert.assertEquals("Начать тренировку ?", fakeBot.getBotMessages().get(2));
    }

    /**
     * Тест на корректную обработку начала подхода
     */
    @Test
    public void returnsCorrectStartApproachMessage() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/start", "111");
        messagesHandler.handleCallback("{\"chosen level\":\"новичок\"}", "111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        messagesHandler.handleCallback("{\"start approach\":\"20\"}", "111");
        Assert.assertEquals("Работаем!", fakeBot.getBotMessages().get(5));
    }

    /**
     * Тест на корректную обработку начала отдыха
     */
    @Test
    public void returnsCorrectStartRestMessage() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/start", "111");
        messagesHandler.handleCallback("{\"chosen level\":\"новичок\"}", "111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        messagesHandler.handleCallback("{\"start rest\":\"10\"}", "111");
        Assert.assertEquals("Отдых!", fakeBot.getBotMessages().get(5));
    }

    /**
     * Тест на корректную обработку отмены тренировки
     */
    @Test
    public void returnsCorrectCancelWorkoutMessage() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleCallback("{\"cancel\":\"cancel\"}", "111");
        Assert.assertEquals("Тренировка отменена!", fakeBot.getBotMessages().get(0));
    }

    /**
     * Тест на корректную обработку завершения тренировки
     */
    @Test
    public void returnsCorrectStopWorkoutMessage() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleCallback("{\"stop\":\"stop\"}", "111");
        Assert.assertEquals("Тренировка завершена!", fakeBot.getBotMessages().get(0));
    }

    /**
     * Тест на корректную обработку запроса статистики
     */
    @Test
    public void returnsCorrectStatisticMessage() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("имя - Вася", "111");
        messagesHandler.handleCallback("{\"chosen statType\":\"общая\"}", "111");
        Assert.assertEquals("Вы еще не сделали ни одной тренировки в этой категории", fakeBot.getBotMessages().get(1));
    }

    @Test
    public void returnsCorrectStartRestBetweenApproachesQuestion() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/start", "111");
        messagesHandler.handleCallback("{\"chosen level\":\"новичок\"}", "111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        messagesHandler.handleCallback("{\"start workout\":\"start\"}", "111");
        messagesHandler.handleCallback("{\"start approach\":\"1\"}", "111");
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("20 секунд прошло! Начать отдых между подходами 10 секунд?", fakeBot.getBotMessages().get(fakeBot.getBotMessages().size() - 1));
    }

    @Test
    public void returnsCorrectStartRestBetweenExercisesQuestion() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/start", "111");
        messagesHandler.handleCallback("{\"chosen level\":\"новичок\"}", "111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        messagesHandler.handleCallback("{\"start workout\":\"start\"}", "111");
        for (int i = 1; i <= 8; i++) {
            messagesHandler.handleCallback("{\"start approach\":\"1\"}", "111");
            try {
                Thread.sleep(5);
                if (i < 8){
                    messagesHandler.handleCallback("{\"start rest\":\"1\"}", "111");
                    Thread.sleep(5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals("20 секунд прошло! Начать отдых между упражнениями 10 секунд?", fakeBot.getBotMessages().get(fakeBot.getBotMessages().size() - 1));
    }

    @Test
    public void returnsCorrectStartRestBetweenRoundsQuestion() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/start", "111");
        messagesHandler.handleCallback("{\"chosen level\":\"любитель\"}", "111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        messagesHandler.handleCallback("{\"start workout\":\"start\"}", "111");
        for (int i = 1; i <= 64; i++) {
            messagesHandler.handleCallback("{\"start approach\":\"1\"}", "111");
            try {
                Thread.sleep(5);
                if (i < 64){
                    messagesHandler.handleCallback("{\"start rest\":\"1\"}", "111");
                    Thread.sleep(5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals("20 секунд прошло! Начать отдых между раундами 60 секунд?", fakeBot.getBotMessages().get(fakeBot.getBotMessages().size() - 1));
    }

    @Test
    public void returnsCorrectStartFirstExerciseQuestion() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/start", "111");
        messagesHandler.handleCallback("{\"chosen level\":\"новичок\"}", "111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        messagesHandler.handleCallback("{\"start workout\":\"start\"}", "111");
        Pattern pattern = Pattern.compile("1 раунд! 1 упражнение:.+?! Начать?");
        Matcher matcher = pattern.matcher(fakeBot.getBotMessages().get(fakeBot.getBotMessages().size() - 1));
        Assert.assertTrue(matcher.find());
    }

    @Test
    public void returnsCorrectStartNotFirstApproachQuestion() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/start", "111");
        messagesHandler.handleCallback("{\"chosen level\":\"новичок\"}", "111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        messagesHandler.handleCallback("{\"start workout\":\"start\"}", "111");
        messagesHandler.handleCallback("{\"start approach\":\"1\"}", "111");
        try {
            Thread.sleep(5);
            messagesHandler.handleCallback("{\"start rest\":\"1\"}", "111");
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("10 секунд прошло! Начать 2 подход?", fakeBot.getBotMessages().get(fakeBot.getBotMessages().size() - 1));
    }

    @Test
    public void returnsCorrectStartExerciseQuestion() {
        FakeBot fakeBot = new FakeBot();
        MessagesHandler messagesHandler = new MessagesHandler(fakeBot);
        messagesHandler.handleTextMessage("/start", "111");
        messagesHandler.handleCallback("{\"chosen level\":\"любитель\"}", "111");
        messagesHandler.handleCallback("{\"chosen group\":\"ноги\"}", "111");
        messagesHandler.handleCallback("{\"start workout\":\"start\"}", "111");
        for (int i = 1; i <= 8; i++) {
            messagesHandler.handleCallback("{\"start approach\":\"1\"}", "111");
            try {
                Thread.sleep(5);
                messagesHandler.handleCallback("{\"start rest\":\"1\"}", "111");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Pattern pattern = Pattern.compile("10 секунд прошло! 2 упражнение:.+?! Начать?");
        Matcher matcher = pattern.matcher(fakeBot.getBotMessages().get(fakeBot.getBotMessages().size() - 1));
        Assert.assertTrue(matcher.find());
    }
}
