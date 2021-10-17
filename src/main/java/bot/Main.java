package bot;

import bot.Bot;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


/**
 * Класс Main
 * <p>
 * 17.10.2021
 *
 * @author Анастасия Коваленко
 */
public class Main {
    @SneakyThrows
    public static void main (String[] args){
        Bot myBot = new Bot("@tabata_fitness_bot","2050374412:AAH27CnBGW1KDUMVkKaPmq1MCPr9RgpDx5c");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(myBot);
    }
}
