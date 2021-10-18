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
 * Класс Bot - класс, отвечающий за сущность бота, обрабтку сообщений пользователя
 * <p>
 * 16.10.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */


public class Bot extends TelegramLongPollingBot {

    /** Поле имя бота - username в телеграме */
    @Setter
    @Getter
    private String botName;

    /** Поле токен бота в телеграме для контроля над ботом */
    @Setter
    private String botToken;

    /** Поле сборщика тренировки */
    private WorkoutMaker workoutMaker = new WorkoutMaker();

    /** Конструктор - создание нового объекта с определенными значениями
     * @param botName - username бота в телеграме
     * @param botToken - токен бота в телеграме
     */
    public Bot(String botName, String botToken) {
        this.botName = botName;
        this.botToken = botToken;
    }

    /** Функция получения значения поля {@link Bot#botName} */
    @Override
    public String getBotUsername() {
        return botName;
    }

    /** Фнкция получения значения поля {@link Bot#botToken} */
    @Override
    public String getBotToken() {
        return botToken;
    }

    /** Процедура обработки обновлений */
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText() && message.hasEntities()){
                Optional<MessageEntity> commandEntity =
                        message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
                if(commandEntity.isPresent()){
                    handleCommandMessage(message, commandEntity);
                }
            }
            else if (message.hasText()){
                handleNonCommandMessage(message);
            }
        }
    }

    /** Процедура обработки сообщений пользователя, содержащих команду
     * @param message - сообщение пользователя
     * @param commandEntity - команда
     * */
    @SneakyThrows
    private void handleCommandMessage(Message message, Optional<MessageEntity> commandEntity) {
        String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
        switch (command) {
            case "/help":
                StringBuilder botMessage = new StringBuilder();
                botMessage.append("Привет, я - бот для создания тренировок по методу табата! Табата – система тренировок, которая была ");
                botMessage.append("придумана японским физиологом Изуми Табата. Он доказал, что интервальные тренировки при мощности работы на 70% от ");
                botMessage.append("МПК (максимального потребления кислорода) способны одновременно привести к росту аэробной и анаэробной выносливости. ");
                botMessage.append("Упражнения выполняются циклами. 1 цикл: 20 секунд работы, 10 секунд отдыха, 8 подходов. Количество пражнений в 1 раунде = колчество циклов. ");
                botMessage.append("Я составлю тебе тренировку по выбранным тобою параметрам (уровень сложности, целевая группа мышц) Пиши /start_training, чтобы начать!");
                execute(SendMessage.builder()
                        .text(botMessage.toString())
                        .chatId(message.getChatId().toString())
                        .build());
                return;
            case "/start_training":
                botMessage = new StringBuilder();
                botMessage.append("Выберите уровень сложности(варианты ответа: новичок, любитель, продвинутый)\nновичок : 1 раунд 6 циклов\nлюбитель : 2 раунда по 8 циклов\n");
                botMessage.append("продвинутый : 3 раунда по 8 циклов\nНапишите выбранный уровень.");
                execute(SendMessage.builder()
                        .text(botMessage.toString())
                        .chatId(message.getChatId().toString())
                        .build());
                return;
        }
    }

    /** Процедура обработки сообщений пользователя, не содержащих команду */
    @SneakyThrows
    private void handleNonCommandMessage(Message message){
        String msg = message.getText();
        if (msg.equals("новичок") || msg.equals("любитель") || msg.equals("продвинутый")){
            workoutMaker.setLevel(msg);
            execute(SendMessage.builder()
                    .text("Вы выбрали уровень " + msg)
                    .chatId(message.getChatId().toString())
                    .build());
            execute(SendMessage.builder()
                    .text("Выберите целевую группу мышц(варианты ответа: ноги + ягодицы, руки + грудь + спина, пресс)" + "\n" +
                            "Напишите выбранную группу мышц(максимум можно указать две группы, разделив запятой и пробелом пример: пресс, ноги + ягодицы).")
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
