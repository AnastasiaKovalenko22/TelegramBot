package bot;

import api.longpoll.bots.BotsLongPoll;
import api.longpoll.bots.exceptions.BotsLongPollException;
import api.longpoll.bots.exceptions.BotsLongPollHttpException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;



/**
 * Класс Main
 * <p>
 * 17.10.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class Main {
    public static void main(String[] args) {
        Thread telegramThread = new Thread(new Runnable() {
            @Override
            public void run() {
                TelegramBot telegramBot = new TelegramBot();
                TelegramBotsApi telegramBotsApi = null;
                try {
                    telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                try {
                    telegramBotsApi.registerBot(telegramBot);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        });
        telegramThread.start();
        try {
            new BotsLongPoll(new VkBot()).run();
        } catch (BotsLongPollHttpException e) {
            e.printStackTrace();
        } catch (BotsLongPollException e) {
            e.printStackTrace();
        }

    }
}
