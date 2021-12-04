package bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс WorkoutMaker - класс, отвечающий за создание тренировки
 * <p>
 * 17.10.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class WorkoutMaker {

    /** поле - список упражнений на ноги*/
    private static final List<String> LEGS_EXERCISES = Arrays.asList("Приседания", "Приседания плие", "Пружинящие приседания", "Приседания с выпрыгиванием", "Приседания с шагом", "Разведение ног в стороны в приседе", "Приседания с поворотом на 180° прыжком", "Приседания на 1 ноге", "Статика в приседе", "Выпады вперед", "Выпады назад", "Болгарские выпады", "Выпады со сменой ног прыжком", "Ягодичный мост",
            "Ягодичный мост с разведением ног в стороны", "Становая тяга", "Мертвая тяга", "Отведение ноги назад стоя", "Отведение ноги назад в упоре на четвереньках", "Отведение ноги назад лежа", "Отведение ноги в сторону в упоре на четвереньках", "Зашагивание на возвышенность", "Удар ногой вперед");

    /** поле - список упражнений на пресс */
    private static final List<String> PRESS_EXERCISES = Arrays.asList("Скручивания с отрывом только лопаток", "Полные скручивания", "Подъем ног", "Касание руками поднятых ног", "Планка классическая", "Планка боковая", "Планка с поворотом таза в стороны", "Планка с касанием плечей руками", "Планка с разведением ног прыжками",
            "Планка с переходом с локтей в упор лежа и обратно", "Скалолаз", "Обратные скручивания", "Русский твист", "Ножницы", "Велосипед");

    /** поле - список упражнений на руки, грудь и спину */
    private static final List<String> ARMS_EXERCISES = Arrays.asList("Отжимания классические", "Отжимания широким хватом", "Отжимания узким хватом", "Обратные отжимания", "Отведение рук в стороны с гантелями", "Сгибание рук на бицепс с гантелями", "Тяга к груди", "Тяга за голову", "Тяга к поясу в наклоне", "Тяга в планке", "Тяга к подбородку", "Разведение рук в наклоне c гантелями", "Разгибание рук на трицепс в наклоне", "Гиперэкстензия", "Жим лежа", "Жим сидя", "Жим стоя", "Разгибание рук из-за головы с гантелями", "Ходьба руками в планку и обратно");

    private static final String BEGIN_LEVEL = "новичок";

    private static final String MEDIUM_LEVEL = "любитель";

    private static final String ADVANCED_LEVEL = "продвинутый";

    private static final String PRESS_GROUP = "пресс";

    private static final String LEGS_GROUP = "ноги";

    private static final String ARMS_GROUP = "руки+грудь+спина";



    /** поле - уровень сложности */
    private String level;

    /** поле - целевые группы мышц */
    private String [] targetGroups;

    /**
     * Функция получения значения поля {@link WorkoutMaker#level}
     * @return - строка - значение поля level
     */
    public String getLevel(){
        return level;
    }

    /**
     * Процедра присваивания значения полю {@link WorkoutMaker#level}
     * @param value - переданное значение
     */
    public void setLevel(String value){
        if (value.equals(BEGIN_LEVEL) || value.equals(MEDIUM_LEVEL) || value.equals(ADVANCED_LEVEL)){
            level = value;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Функция получения значения поля {@link WorkoutMaker#targetGroups}
     * @return - массив строк - значение поля targetGroups
     */
    public String[] getTargetGroups(){
        return targetGroups;
    }

    /**
     * Процедра присваивания значения полю {@link WorkoutMaker#targetGroups}
     * @param value - переданное значение
     */
    public void setTargetGroups(String[] value){
        targetGroups = value;
    }

    public static List<String> getLegsExercises(){
        return LEGS_EXERCISES;
    }

    public static List<String> getPressExercises(){
        return PRESS_EXERCISES;
    }

    public static List<String> getArmsExercises(){
        return ARMS_EXERCISES;
    }

    public static String getBeginLevel(){
        return BEGIN_LEVEL;
    }

    public static String getMediumLevel(){
        return MEDIUM_LEVEL;
    }

    public static String getAdvancedLevel(){
        return ADVANCED_LEVEL;
    }

    public static String getPressGroup(){
        return PRESS_GROUP;
    }

    public static String getLegsGroup(){
        return LEGS_GROUP;
    }

    public static String getArmsGroup(){
        return ARMS_GROUP;
    }

    /** конструктор - создание нового объекта с определенными значениями
     *
     * @param lvl - уровень сложности
     * @param groups - целевые группы мышц
     */
    public WorkoutMaker(String lvl, String [] groups){
        setLevel(lvl);
        setTargetGroups(groups);
    }

    /** функция получения количества упражнений на 1 раунд */
    public int getExercisesCountInRound(){
        int exercisesCountInRound;
        if (level.equals(BEGIN_LEVEL)){
            exercisesCountInRound = 6;
        }
        else {
            exercisesCountInRound = 8;
        }
        return  exercisesCountInRound;
    }

    /** функция создания тренировки */
    public List<String> createWorkout() {
        int targetGroupsCount = targetGroups.length;
        int exercisesCountInRound = getExercisesCountInRound();
        List<String> workout = new ArrayList<>();
        List<String> targetGroupEx1;
        List<String> targetGroupEx2;
        if (targetGroups[0].equals(LEGS_GROUP)) {
            targetGroupEx1 = LEGS_EXERCISES;
        }
        else if (targetGroups[0].equals(PRESS_GROUP)) {
            targetGroupEx1 = PRESS_EXERCISES;
        }
        else {
            targetGroupEx1 = ARMS_EXERCISES;
        }

        if (targetGroupsCount == 2) {
            if (targetGroups[1].equals(LEGS_GROUP)) {
                targetGroupEx2 = LEGS_EXERCISES;
            }
            else if (targetGroups[1].equals(PRESS_GROUP)) {
                targetGroupEx2 = PRESS_EXERCISES;
            }
            else {
                targetGroupEx2 = ARMS_EXERCISES;
            }
            makeExerciseList(exercisesCountInRound/2, targetGroupEx1, workout);
            makeExerciseList(exercisesCountInRound/2, targetGroupEx2, workout);
            return workout;
        }
        makeExerciseList(exercisesCountInRound, targetGroupEx1, workout);
        return workout;
    }

    /** функция создания списка упражнений */
    private void makeExerciseList(int exercisesCount, List<String> exercises, List<String> workout){
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

