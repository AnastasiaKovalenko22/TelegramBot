package bot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс WorkoutMaker - класс, отвечающий за создание тренировки
 * <p>
 * 17.10.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
@NoArgsConstructor
public class WorkoutMaker {

    /** поле - список упражнений на ноги*/
    private static final ArrayList<String> legsExercises = new ArrayList<>(Arrays.asList(new String[]{"Приседания", "Приседания плие", "Пружинящие приседания", "Приседания с выпрыгиванием", "Приседания с шагом", "Разведение ног в стороны в приседе", "Приседания с поворотом на 180° прыжком", "Приседания на 1 ноге", "Статика в приседе", "Выпады вперед", "Выпады назад", "Болгарские выпады", "Выпады со сменой ног прыжком", "Ягодичный мост",
            "Ягодичный мост с разведением ног в стороны", "Становая тяга", "Мертвая тяга", "Отведение ноги назад стоя", "Отведение ноги назад в упоре на четвереньках", "Отведение ноги назад лежа", "Отведение ноги в сторону в упоре на четвереньках", "Зашагивание на возвышенность", "Удар ногой вперед"}));

    /** поле - список упражнений на пресс */
    private static final ArrayList<String> pressExercises = new ArrayList<>(Arrays.asList(new String[]{"Скручивания с отрывом только лопаток", "Полные скручивания", "Подъем ног", "Касание руками поднятых ног", "Планка классическая", "Планка боковая", "Планка с поворотом таза в стороны", "Планка с касанием плечей руками", "Планка с разведением ног прыжками",
            "Планка с переходом с локтей в упор лежа и обратно", "Скалолаз", "Обратные скручивания (выход в березку)", "Русский твист", "Ножницы", "Велосипед"}));

    /** поле - список упражнений на руки, грудь и спину */
    private static final ArrayList<String> armsExercises = new ArrayList<>(Arrays.asList(new String[]{"Отжимания классические (можно с колен)", "Отжимания широким хватом", "Отжимания узким хватом", "Обратные отжимания", "Отведение рук в стороны (с гантелями)", "Сгибание рук на бицепс (с гантелями)", "Тяга к груди", "Тяга за голову", "Тяга к поясу в наклоне", "Тяга в планке", "Тяга к подбородку", "Разведение рук в наклоне", "Разгибание рук на трицепс в наклоне", "Гиперэкстензия", "Жим лежа", "Жим сидя", "Жим стоя", "Разгибание рук из-за головы", "Ходьба руками в планку и обратно"}));

    /** поле - уровень сложности */
    @Setter
    @Getter
    private String level;

    /** поле - целевые группы мышц */
    @Setter
    @Getter
    private String [] targetGroups;

    /** конструктор - создание нового объекта с определенными значениями
     *
     * @param lvl - уровень сложности
     * @param groups - целевые группы мышц
     */
    public WorkoutMaker(String lvl, String [] groups){
        level = lvl;
        targetGroups = groups;
    }

    /** функция получения количества упражнений на 1 раунд */
    public int setExercisesCountInRound(){
        int exercisesCountInRound;
        if (level.equals("новичок")){
            exercisesCountInRound = 6;
        }
        else {
            exercisesCountInRound = 8;
        }
        return  exercisesCountInRound;
    }

    /** функция получения количества раундов */
    public int setRoundsCount(){
        int roundsCount;
        if (level.equals("новичок")){
            roundsCount = 1;
        }
        else if (level.equals("любитель")){
            roundsCount = 2;
        }
        else {
            roundsCount = 3;
        }
        return  roundsCount;
    }

    /** функция создания тренировки */
    public ArrayList<String> createWorkout() {
        int targetGroupsCount = targetGroups.length;
        int exercisesCountInRound = setExercisesCountInRound();
        ArrayList<String> workout = new ArrayList<>();
        ArrayList<String> targetGroupEx1;
        ArrayList<String> targetGroupEx2;
        if (targetGroups[0].equals("ноги")) {
            targetGroupEx1 = legsExercises;
        }
        else if (targetGroups[0].equals("пресс")) {
            targetGroupEx1 = pressExercises;
        }
        else {
            targetGroupEx1 = armsExercises;
        }

        if (targetGroupsCount == 2) {
            if (targetGroups[1].equals("ноги")) {
                targetGroupEx2 = legsExercises;
            }
            else if (targetGroups[1].equals("пресс")) {
                targetGroupEx2 = pressExercises;
            }
            else {
                targetGroupEx2 = armsExercises;
            }
            makeExerciseList(exercisesCountInRound/2, targetGroupEx1, workout);
            makeExerciseList(exercisesCountInRound/2, targetGroupEx2, workout);
            return workout;
        }
        makeExerciseList(exercisesCountInRound, targetGroupEx1, workout);
        return workout;
    }

    /** функция создания списка упражнений */
    public void makeExerciseList(int exercisesCount, ArrayList<String> exercises, ArrayList<String> workout){
        int i = 0;
        while (i < exercisesCount) {
            int randomIndex = (int) Math.floor(Math.random() * exercises.size());
            if (!workout.contains(exercises.get(randomIndex))) {
                workout.add(exercises.get(randomIndex));
                i++;
            }
        }
    }
}

