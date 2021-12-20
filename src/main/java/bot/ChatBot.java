package bot;

import java.util.Map;
/**
 * Интерфейс чат-бота
 * <p>
 * 29.11.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public interface ChatBot {
    /**
     * Процедура отправки текстового сообщения пользователю
     * @param text - текст сообщения
     * @param chatId - id чата
     */
    void sendTextMessage(String text, String chatId);

    /**
     * Процедура отправки сообщения с кнопками
     * @param text - текст сообщения
     * @param chatId - id чата
     * @param options - подписи кнопок
     * @param callbacks - коллбэки
     */
    void sendMessageWithButtons(String text, String chatId, String[] options, Map<String, String> callbacks);
}
