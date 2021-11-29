package bot;

import api.longpoll.bots.BotsLongPoll;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
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
    @SneakyThrows
    public static void main (String[] args){
        Thread telegramThread = new Thread(new Runnable(){
            @SneakyThrows
            @Override
            public void run() {
                TelegramBot telegramBot = new TelegramBot();
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(telegramBot);
            }
        });
        telegramThread.start();
        new BotsLongPoll(new VkBot()).run();
    }
}
