package bot;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

/**
 * Класс User - класс, отвечающий за сущность пользователя
 * <p>
 * 02.11.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шоохова
 */
public class User {
     /**
      * Константа - количество подходов
      */
     private final int approachCount = 2;
     /**
      * Поле id чата пользователя и бота
      */
     @Setter
     @Getter
     private String chatId;

     /**
      * Поле бот
      */
     @Setter
     @Getter
     private Bot bot;

     /**
      * Поле индекс текущего упражнения
      */
     @Setter
     @Getter
     private int currentExercise = 0;

     /**
      * Поле номер текущего раунда
      */
     @Setter
     @Getter
     private int currentRound = 1;

     /**
      * Поле номер текущего подхода
      */
     @Setter
     @Getter
     private int currentApproach = 1;

     /**
      * Поле сборщик тренировки
      */
     @Setter
     @Getter
     private WorkoutMaker workoutMaker = new WorkoutMaker();

     /**
      * Поле список упражнений для тренировки
      */
     @Setter
     @Getter
     private List<String> workout;

     /**
      * Конструктор - создание нового объекта с определенными значениями
      * @param chatId - id чата пользователя и бота
      */
     public User(String chatId){
          this.chatId = chatId;
     }

     /**
      * Функция получения названия упражнения
      */
     public String getExerciseName(){
          return workout.get(currentExercise);
     }

     /**
      * Процедура выполнения пользователем упражнения
      */
     public void doExercise(){
          Timer timer = new Timer();
          timer.schedule(new TimerTask() {
               public void run() {
                    startRest();
               }
          }, 1*1000);

     }

     /**
      * Процедура отдыха пользователя
      * @param restTime - время отдыха в секундах
      */
     public void rest(int restTime){
          Timer timer = new Timer();
          timer.schedule(new TimerTask(){
               public void run(){
                    startWork();
               }

          }, 1*1000);
     }

     private void startRest(){
          String text = "";
          if(currentApproach < approachCount){
               currentApproach ++;
               text = "20 секунд прошло! Начать отдых между подходами 10 секунд?";
          }
          else if (currentExercise < workout.size() - 1) {
               currentApproach = 1;
               currentExercise ++;
               text = "20 секунд прошло! Начать отдых между упражнениями 10 секунд?";
          }
          else if (currentRound < workoutMaker.setRoundsCount()) {
               currentApproach = 1;
               currentExercise = 0;
               currentRound ++;
               text = "20 секунд прошло! Начать отдых между раундами 60 секунд?";
          }
          else {
               currentApproach = 1;
               currentExercise = 0;
               currentRound = 1;
               text = "Тренировка завершена!";
          }
          if(!text.equals("Тренировка завершена!")) {
               String [] textWords = text.split(" ");
               List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
               buttons.add(Arrays.asList(InlineKeyboardButton.builder().text("начать").callbackData("start rest: " + textWords[textWords.length - 2]).build(),
                       InlineKeyboardButton.builder().text("завершить тренировку").callbackData("stop").build()));
               bot.sendMessageWithButtons(text, chatId, buttons);
          }
          else {
               bot.sendTextMessage(text, chatId);
          }
     }

     private void startWork(){
          String text = "";
          if(currentApproach > 1){
               text = "10 секунд прошло! Начать " + currentApproach + " подход?";
               List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
               buttons.add(Arrays.asList(InlineKeyboardButton.builder().text("начать").callbackData("start approach").build(),
                       InlineKeyboardButton.builder().text("завершить тренировку").callbackData("stop").build()));
               bot.sendMessageWithButtons(text, chatId, buttons);
          }
          else if (currentExercise > 0){
               text = "10 секунд прошло! " + (currentExercise + 1) + " упражнение: " + getExerciseName() + "! Начать?";
               List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
               buttons.add(Arrays.asList(InlineKeyboardButton.builder().text("начать").callbackData("start approach").build(),
                       InlineKeyboardButton.builder().text("завершить тренировку").callbackData("stop").build()));
               bot.sendMessageWithButtons(text, chatId, buttons);
          }
          else{
               text = "60 секунд прошло! " + currentRound + " раунд! 1 упражнение: " + getExerciseName() + "! Начать?";
               List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
               buttons.add(Arrays.asList(InlineKeyboardButton.builder().text("начать").callbackData("start approach").build(),
                       InlineKeyboardButton.builder().text("завершить тренировку").callbackData("stop").build()));
               bot.sendMessageWithButtons(text, chatId, buttons);
          }
     }
}
