import bot.ChatBot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeBot implements ChatBot {
    public static List<String> botMessages = new ArrayList<>();

    @Override
    public void sendTextMessage(String text, String chatId) {
        botMessages.add(text);
    }

    @Override
    public void sendMessageWithButtons(String text, String chatId, String[] options, Map<String, String> callbacks) {
        botMessages.add(text);
    }
}
