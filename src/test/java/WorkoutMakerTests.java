/**
 * Класс WorkoutMakerTests - тесты на для сборщика тренировки
 * <p>
 * 17.10.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */

import bot.WorkoutMaker;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class WorkoutMakerTests {

    /**
     * Тест на корректность сборки тренировки для новичка на пресс
     */
    @Test
    public void createWorkoutForBeginnerPress() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.getBeginLevel(), new String[]{WorkoutMaker.getPressGroup()});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(6, workout.size());
        for (String item :
                workout) {
            Assert.assertTrue(WorkoutMaker.getPressExercises().contains(item));
        }
    }

    /**
     * Тест на корректность сборки тренировки для новичка на ноги
     */
    @Test
    public void createWorkoutForBeginnerLegs() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.getBeginLevel(), new String[]{WorkoutMaker.getLegsGroup()});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(6, workout.size());
        for (String item :
                workout) {
            Assert.assertTrue(WorkoutMaker.getLegsExercises().contains(item));
        }
    }

    /**
     * Тест на корректность сборки тренировки для новичка на руки
     */
    @Test
    public void createWorkoutForBeginnerArms() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.getBeginLevel(), new String[]{WorkoutMaker.getArmsGroup()});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(6, workout.size());
        for (String item :
                workout) {
            Assert.assertTrue(WorkoutMaker.getArmsExercises().contains(item));
        }
    }

    /**
     * Тест на корректность сборки тренировки для любителя на руки
     */
    @Test
    public void createWorkoutForAmateurArms() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.getMediumLevel(), new String[]{WorkoutMaker.getArmsGroup()});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(8, workout.size());
        for (String item :
                workout) {
            Assert.assertTrue(WorkoutMaker.getArmsExercises().contains(item));
        }
    }

    /**
     * Тест на корректность сборки тренировки для любителя на пресс
     */
    @Test
    public void createWorkoutForAmateurPress() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.getMediumLevel(), new String[]{WorkoutMaker.getPressGroup()});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(8, workout.size());
        for (String item :
                workout) {
            Assert.assertTrue(WorkoutMaker.getPressExercises().contains(item));
        }
    }

    /**
     * Тест на корректность сборки тренировки для продвинутого на ноги
     */
    @Test
    public void createWorkoutForAdvancedLegs() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.getAdvancedLevel(), new String[]{WorkoutMaker.getLegsGroup()});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(8, workout.size());
        for (String item :
                workout) {
            Assert.assertTrue(WorkoutMaker.getLegsExercises().contains(item));
        }
    }

    /**
     * Тест на корректность сборки тренировки для новичка на ноги и пресс
     */
    @Test
    public void createWorkoutForBeginnerLegsAndPress(){
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.getBeginLevel(), new String[]{WorkoutMaker.getLegsGroup(), WorkoutMaker.getPressGroup()});
        List<String> workout = workoutMaker.createWorkout();
        for (int i = 0; i < 6; i++) {
            if (i < 3)
                Assert.assertTrue(WorkoutMaker.getLegsExercises().contains(workout.get(i)));
            else
                Assert.assertTrue(WorkoutMaker.getPressExercises().contains(workout.get(i)));
        }
    }

    /**
     * Тест на корректность сборки тренировки для любителя на руки и пресс
     */
    @Test
    public void createWorkoutForAmateurArmsAndPress() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.getMediumLevel(), new String[]{WorkoutMaker.getArmsGroup(), WorkoutMaker.getPressGroup()});
        List<String> workout = workoutMaker.createWorkout();
        for (int i = 0; i < 8; i++) {
            if (i < 4)
                Assert.assertTrue(WorkoutMaker.getArmsExercises().contains(workout.get(i)));
            else
                Assert.assertTrue(WorkoutMaker.getPressExercises().contains(workout.get(i)));
        }
    }

    /**
     * Тест на корректность сборки тренировки для продвинутого на ноги и руки
     */
    @Test
    public void createWorkoutForAdvancedLegsAndArms(){
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.getAdvancedLevel(), new String[]{WorkoutMaker.getLegsGroup(), WorkoutMaker.getArmsGroup()});
        List<String> workout = workoutMaker.createWorkout();
        for (int i = 0; i < 8; i++) {
            if (i < 4)
                Assert.assertTrue(WorkoutMaker.getLegsExercises().contains(workout.get(i)));
            else
                Assert.assertTrue(WorkoutMaker.getArmsExercises().contains(workout.get(i)));
        }
    }
}
