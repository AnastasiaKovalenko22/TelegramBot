import bot.ChatBot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Класс FakeBot - фэйковый бот для тестов
 * <p>
 * 19.12.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class FakeBot implements ChatBot {
    /**
     * список ответов бота
     */
    public List<String> botMessages = new ArrayList<>();

    /**
     * Процедура отправки текстовых сообщений
     * @param text - текст сообщения
     * @param chatId - id чата
     */
    @Override
    public void sendTextMessage(String text, String chatId) {
        botMessages.add(text);
    }

    /**
     * Процедура отправки сообщений с кнопками
     * @param text - текст сообщения
     * @param chatId - id чата
     * @param options - подписи кнопок
     * @param callbacks - коллбэки
     */
    @Override
    public void sendMessageWithButtons(String text, String chatId, String[] options, Map<String, String> callbacks) {
        botMessages.add(text);
    }
}

