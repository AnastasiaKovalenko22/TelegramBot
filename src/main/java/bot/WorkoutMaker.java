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

    /**
     * константа - список упражнений на ноги
     */
    public static final List<String> LEGS_EXERCISES = Arrays.asList("Приседания", "Приседания плие", "Пружинящие приседания", "Приседания с выпрыгиванием", "Приседания с шагом", "Разведение ног в стороны в приседе", "Приседания с поворотом на 180° прыжком", "Приседания на 1 ноге", "Статика в приседе", "Выпады вперед", "Выпады назад", "Болгарские выпады", "Выпады со сменой ног прыжком", "Ягодичный мост",
            "Ягодичный мост с разведением ног в стороны", "Становая тяга", "Мертвая тяга", "Отведение ноги назад стоя", "Отведение ноги назад в упоре на четвереньках", "Отведение ноги назад лежа", "Отведение ноги в сторону в упоре на четвереньках", "Зашагивание на возвышенность", "Удар ногой вперед");

    /**
     * константа - список упражнений на пресс
     */
    public static final List<String> PRESS_EXERCISES = Arrays.asList("Скручивания с отрывом только лопаток", "Полные скручивания", "Подъем ног", "Касание руками поднятых ног", "Планка классическая", "Планка боковая", "Планка с поворотом таза в стороны", "Планка с касанием плечей руками", "Планка с разведением ног прыжками",
            "Планка с переходом с локтей в упор лежа и обратно", "Скалолаз", "Обратные скручивания", "Русский твист", "Ножницы", "Велосипед");

    /**
     * константа - список упражнений на руки, грудь и спину
     */
    public static final List<String> ARMS_EXERCISES = Arrays.asList("Отжимания классические", "Отжимания широким хватом", "Отжимания узким хватом", "Обратные отжимания", "Отведение рук в стороны с гантелями", "Сгибание рук на бицепс с гантелями", "Тяга к груди", "Тяга за голову", "Тяга к поясу в наклоне", "Тяга в планке", "Тяга к подбородку", "Разведение рук в наклоне c гантелями", "Разгибание рук на трицепс в наклоне", "Гиперэкстензия", "Жим лежа", "Жим сидя", "Жим стоя", "Разгибание рук из-за головы с гантелями", "Ходьба руками в планку и обратно");

    /**
     * константа - уровень новичок
     */
    public static final String BEGIN_LEVEL = "новичок";

    /**
     * константа - уровень любитель
     */
    public static final String MEDIUM_LEVEL = "любитель";

    /**
     * константа - уровень продвинутый
     */
    public static final String ADVANCED_LEVEL = "продвинутый";

    /**
     * константа - группа мышц пресс
     */
    public static final String PRESS_GROUP = "пресс";

    /**
     * константа - группа мышц ноги
     */
    public static final String LEGS_GROUP = "ноги";

    /**
     * константа - группа мышц руки+грудь+спина
     */
    public static final String ARMS_GROUP = "руки+грудь+спина";


    /**
     * поле - уровень сложности
     */
    private String level;

    /**
     * поле - целевые группы мышц
     */
    private String[] targetGroups;

    /**
     * Процедура присваивания значения полю {@link WorkoutMaker#level}
     *
     * @param value - переданное значение
     */
    public void setLevel(String value) {
        if (value.equals(BEGIN_LEVEL) || value.equals(MEDIUM_LEVEL) || value.equals(ADVANCED_LEVEL)) {
            level = value;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Процедра присваивания значения полю {@link WorkoutMaker#targetGroups}
     *
     * @param value - переданное значение
     */
    public void setTargetGroups(String[] value) {
        if (value.length == 0 || value == null) {
            throw new IllegalArgumentException();
        }
        for (String group :
                value) {
            if (!(group.equals(PRESS_GROUP) || group.equals(ARMS_GROUP) || group.equals(LEGS_GROUP))) {
                throw new IllegalArgumentException();
            }
        }
        targetGroups = value;
    }

    /**
     * конструктор - создание нового объекта с определенными значениями
     *
     * @param lvl    - уровень сложности
     * @param groups - целевые группы мышц
     */
    public WorkoutMaker(String lvl, String[] groups) {
        setLevel(lvl);
        setTargetGroups(groups);
    }

    /**
     * функция получения количества упражнений на 1 раунд
     */
    private int getExercisesCountInRound() {
        int exercisesCountInRound;
        if (level.equals(BEGIN_LEVEL)) {
            exercisesCountInRound = 6;
        } else {
            exercisesCountInRound = 8;
        }
        return exercisesCountInRound;
    }

    /**
     * функция создания тренировки
     * @return список случайно выбранных упражнений на целевые группы мышц
     */
    public List<String> createWorkout() {
        int targetGroupsCount = targetGroups.length;
        int exercisesCountInRound = getExercisesCountInRound();
        List<String> workout = new ArrayList<>();
        List<String> targetGroupEx1;
        List<String> targetGroupEx2;
        if (targetGroups[0].equals(LEGS_GROUP)) {
            targetGroupEx1 = LEGS_EXERCISES;
        } else if (targetGroups[0].equals(PRESS_GROUP)) {
            targetGroupEx1 = PRESS_EXERCISES;
        } else {
            targetGroupEx1 = ARMS_EXERCISES;
        }

        if (targetGroupsCount == 2) {
            if (targetGroups[1].equals(LEGS_GROUP)) {
                targetGroupEx2 = LEGS_EXERCISES;
            } else if (targetGroups[1].equals(PRESS_GROUP)) {
                targetGroupEx2 = PRESS_EXERCISES;
            } else {
                targetGroupEx2 = ARMS_EXERCISES;
            }
            addExercisesInWorkout(exercisesCountInRound / 2, targetGroupEx1, workout);
            addExercisesInWorkout(exercisesCountInRound / 2, targetGroupEx2, workout);
            return workout;
        }
        addExercisesInWorkout(exercisesCountInRound, targetGroupEx1, workout);
        return workout;
    }

    /**
     * функция добавления упражнений в список
     * @param exercises - упражнения, из которых случайным образом выбираются упражнения в итоговый список
     * @param workout - итоговый список
     * @param exercisesCount - количество упражнений, которое нужно выбрать
     */
    private void addExercisesInWorkout(int exercisesCount, List<String> exercises, List<String> workout) {

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

