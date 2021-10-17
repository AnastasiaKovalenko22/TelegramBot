package bot;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.File;

/**
 * Класс Bot
 * <p>
 * 16.10.2021
 *
 * @author Анастасия Коваленко
 */


public class Bot extends TelegramLongPollingBot {

    @Setter
    @Getter
    private String botName;

    @Setter
    private String botToken;

    private WorkoutMaker workoutMaker = new WorkoutMaker();

    public Bot(String botName, String botToken) {
        this.botName = botName;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }
    @SneakyThrows
    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/help":
                        execute(SendMessage.builder()
                                .text("Привет, я - бот для создания тренировок по методу табата!" +
                                        "\n" + "Табата – система тренировок, которая была" + "\n" +
                                        "придумана японским физиологом Изуми" + "\n" +
                                        "Табата. Он доказал, что интервальные" + "\n" +
                                        "тренировки при мощности работы на 70% от" + "\n" +
                                        "МПК (максимального потребления кислорода)" + "\n" +
                                        "способны одновременно привести к росту" + "\n" +
                                        "аэробной и анаэробной выносливости." + "\n" +
                                        "Упражнения выполняются циклами. 1 цикл: 20 секунд работы, 10 секунд отдыха, 8 подходов." + "\n" +
                                        "Количество пражнений в 1 раунде = колчество циклов." + "\n" +
                                        "Я составлю тебе тренировку по выбранным тобою параметрам (уровень сложности, целевая группа мышц)" + "\n" +
                                        "Пиши /start_training, чтобы начать")
                                .chatId(message.getChatId().toString())
                                .build());
                        return;
                    case "/start_training":
                        execute(SendMessage.builder()
                                .text("Выберите уровень сложности(варианты ответа: новичок, любитель, продвинутый)" + "\n" +
                                        "новичок : 1 раунд 6 циклов" + "\n" +
                                        "любитель : 2 раунда по 8 циклов" + "\n" +
                                        "продвинутый : 3 раунда по 8 циклов" + "\n" +
                                        "Напишите выбранный уровень.")
                                .chatId(message.getChatId().toString())
                                .build());
                        return;
                    case "/jojanna":
                        execute(SendPhoto.builder()
                                .photo(new InputFile(new File(getClass().getResource("E:\\java tools\\projects\\Telegram\\src\\main\\resources\\joja.jpg").getPath())))
                                .chatId(message.getChatId().toString())
                                .build());
                        return;
                }
            }
        }
        else if (message.hasText()){
            String msg = message.getText();
            if (msg.equals("новичок") || msg.equals("любитель") || msg.equals("продвинутый")){
                workoutMaker.setLevel(msg);
                execute(SendMessage.builder()
                        .text("Вы выбрали уровень " + msg)
                        .chatId(message.getChatId().toString())
                        .build());
                execute(SendMessage.builder()
                        .text("Выберите целевую группу мышц(варианты ответа: ноги + ягодицы, руки + грудь + спина, пресс)" + "\n" +
                                "Напишите выбранную группу мышц(максимум можно указать две группы, разделив запятой и побелом пример: пресс, ноги + ягодицы).")
                        .chatId(message.getChatId().toString())
                        .build());
            }
            String [] msgArr = msg.split(", ");
            ArrayList<String> possibleGroups = new ArrayList<>(Arrays.asList( new String []{"ноги + ягодицы", "руки + грудь + спина", "пресс"}));
            int msgLength = msgArr.length;
            if (msgLength == 1){
                if (possibleGroups.contains(msgArr[0])){
                    execute(SendMessage.builder()
                            .text("Вы выбрали следующую группу мышц: " + msg)
                            .chatId(message.getChatId().toString())
                            .build());
                    workoutMaker.setTargetGroups(msgArr);
                    ArrayList<String> workout = workoutMaker.createWorkout();
                    String strWorkout = workoutMaker.getStringWorkout(workout);
                    execute(SendMessage.builder()
                            .text(strWorkout)
                            .chatId(message.getChatId().toString())
                            .build());
                }

            }
            else if (msgLength == 2){
                if(possibleGroups.contains(msgArr[0]) && possibleGroups.contains(msgArr[1])){
                    execute(SendMessage.builder()
                            .text("Вы выбрали следующие группы мышц: " + msg)
                            .chatId(message.getChatId().toString())
                            .build());
                    workoutMaker.setTargetGroups(msgArr);
                    ArrayList<String> workout = workoutMaker.createWorkout();
                    String strWorkout = workoutMaker.getStringWorkout(workout);
                    execute(SendMessage.builder()
                            .text(strWorkout)
                            .chatId(message.getChatId().toString())
                            .build());
                }
            }
        }
    }
}
