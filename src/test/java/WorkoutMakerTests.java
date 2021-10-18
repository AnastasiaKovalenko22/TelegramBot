/**
 * Класс WorkoutMakerTests
 * <p>
 * 17.10.2021
 *
 * @author Анастасия Коваленко
 */

import bot.WorkoutMaker;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkoutMakerTests {
    private WorkoutMaker workoutMaker;
    private static final ArrayList<String> legsExercises = new ArrayList<>(Arrays.asList(new String[]{"Приседания", "Приседания плие", "Пружинящие приседания", "Приседания с выпрыгиванием", "Приседания с шагом", "Разведение ног в стороны в приседе", "Приседания с поворотом на 180° прыжком", "Приседания на 1 ноге", "Статика в приседе", "Выпады вперед", "Выпады назад", "Болгарские выпады", "Выпады со сменой ног прыжком", "Ягодичный мост",
            "Ягодичный мост с разведением ног в стороны", "Становая тяга", "Мертвая тяга", "Отведение ноги назад стоя", "Отведение ноги назад в упоре на четвереньках", "Отведение ноги назад лежа", "Отведение ноги в сторону в упоре на четвереньках", "Зашагивание на возвышенность", "Удар ногой вперед"}));
    private static final ArrayList<String> pressExercises = new ArrayList<>(Arrays.asList(new String[]{"Скручивания с отрывом только лопаток", "Полные скручивания", "Подъем ног", "Касание руками поднятых ног", "Планка классическая", "Планка боковая", "Планка с поворотом таза в стороны", "Планка с касанием плечей руками", "Планка с разведением ног прыжками",
            "Планка с переходом с локтей в упор лежа и обратно", "Скалолаз", "Обратные скручивания (выход в березку)", "Русский твист", "Ножницы", "Велосипед"}));
    private static final ArrayList<String> armsExercises = new ArrayList<>(Arrays.asList(new String[]{"Отжимания классические (можно с колен)", "Отжимания широким хватом", "Отжимания узким хватом", "Обратные отжимания", "Отведение рук в стороны (с гантелями)", "Сгибание рук на бицепс (с гантелями)", "Тяга к груди", "Тяга за голову", "Тяга к поясу в наклоне", "Тяга в планке", "Тяга к подбородку", "Разведение рук в наклоне", "Разгибание рук на трицепс в наклоне", "Гиперэкстензия", "Жим лежа", "Жим сидя", "Жим стоя", "Разгибание рук из-за головы", "Ходьба руками в планку и обратно"}));

    @Test
    public void createWorkoutForBeginnerPress() {
        workoutMaker = new WorkoutMaker("новичок", new String[]{"пресс"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(6, workout.size());
        for (String item:
             workout) {
            Assert.assertTrue(pressExercises.contains(item));
        }
    }

    @Test
    public void createWorkoutForBeginnerLegs() {
        workoutMaker = new WorkoutMaker("новичок", new String[]{"ноги + ягодицы"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(6, workout.size());
        for (String item:
                workout) {
            Assert.assertTrue(legsExercises.contains(item));
        }
    }

    @Test
    public void createWorkoutForBeginnerArms() {
        workoutMaker = new WorkoutMaker("новичок", new String[]{"руки + грудь + спина"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(6, workout.size());
        for (String item:
                workout) {
            Assert.assertTrue(armsExercises.contains(item));
        }
    }

    @Test
    public void createWorkoutForAmateurArms() {
        workoutMaker = new WorkoutMaker("любитель", new String[]{"руки + грудь + спина"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(8, workout.size());
        for (String item:
                workout) {
            Assert.assertTrue(armsExercises.contains(item));
        }
    }

    @Test
    public void createWorkoutForAmateurPress() {
        workoutMaker = new WorkoutMaker("любитель", new String[]{"пресс"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(8, workout.size());
        for (String item:
                workout) {
            Assert.assertTrue(pressExercises.contains(item));
        }
    }

    @Test
    public void createWorkoutForAdvancedLegs() {
        workoutMaker = new WorkoutMaker("продвинутый", new String[]{"ноги + ягодицы"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(8, workout.size());
        for (String item:
                workout) {
            Assert.assertTrue(legsExercises.contains(item));
        }
    }

    @Test
    public void createWorkoutForBeginnerLegsAndPress(){
        workoutMaker = new WorkoutMaker("новичок", new String[]{"ноги + ягодицы", "пресс"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        for (int i = 0; i < 6; i ++){
            if (i < 3)
                Assert.assertTrue(legsExercises.contains(workout.get(i)));
            else
                Assert.assertTrue(pressExercises.contains(workout.get(i)));
        }
    }

    @Test
    public void createWorkoutForAmateurArmsAndPress(){
        workoutMaker = new WorkoutMaker("любитель", new String[]{"руки + грудь + спина", "пресс"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        for (int i = 0; i < 8; i ++){
            if (i < 4)
                Assert.assertTrue(armsExercises.contains(workout.get(i)));
            else
                Assert.assertTrue(pressExercises.contains(workout.get(i)));
        }
    }

    @Test
    public void createWorkoutForAdvancedLegsAndArms(){
        workoutMaker = new WorkoutMaker("продвинутый", new String[]{"ноги + ягодицы","руки + грудь + спина"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        for (int i = 0; i < 8; i ++){
            if (i < 4)
                Assert.assertTrue(legsExercises.contains(workout.get(i)));
            else
                Assert.assertTrue(armsExercises.contains(workout.get(i)));
        }
    }

    @Test
    public void calculateRightTimeForBegginer(){
        workoutMaker = new WorkoutMaker("новичок", new String[]{"ноги + ягодицы","руки + грудь + спина"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        String strWorkout = workoutMaker.getStringWorkout(workout);
        int len = strWorkout.length();
        String time = strWorkout.substring(len-2);
        Assert.assertTrue(time.equals("23"));
        }

    @Test
    public void calculateRightTimeForAmateur() {
        workoutMaker = new WorkoutMaker("любитель", new String[]{"пресс", "руки + грудь + спина"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        String strWorkout = workoutMaker.getStringWorkout(workout);
        int len = strWorkout.length();
        String time = strWorkout.substring(len - 2);
        Assert.assertTrue(time.equals("64"));
    }

    @Test
    public void calculateRightTimeForAdvanced() {
        workoutMaker = new WorkoutMaker("продвинутый", new String[]{"ноги + ягодицы", "пресс"});
        ArrayList<String> workout = workoutMaker.createWorkout();
        String strWorkout = workoutMaker.getStringWorkout(workout);
        int len = strWorkout.length();
        String time = strWorkout.substring(len - 2);
        Assert.assertTrue(time.equals("97"));
    }

}



