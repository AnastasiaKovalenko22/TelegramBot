/**
 * Класс WorkoutMakerTests
 * <p>
 * 17.10.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */

import bot.WorkoutMaker;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkoutMakerTests {
    /**
     * Тест на некорректную группу мышц
     */
    @Test
    public void NotCorrectGroup() {
        boolean throwException = false;
        try {
            WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.BEGIN_LEVEL, new String[]{"уши"});
        } catch (IllegalArgumentException e) {
            throwException = true;
        } finally {
            Assert.assertTrue(throwException);
        }

    }

    /**
     * Тест на некорректный уровень
     */
    @Test
    public void NotCorrectLevel() {
        boolean throwException = false;
        try {
            WorkoutMaker workoutMaker = new WorkoutMaker("superchel", new String[]{WorkoutMaker.ARMS_GROUP});
        } catch (IllegalArgumentException e) {
            throwException = true;
        } finally {
            Assert.assertTrue(throwException);
        }

    }

    /**
     * Тест на пустой массив групп мышц
     */
    @Test
    public void EmptyGroupsArray() {
        boolean throwException = false;
        try {
            WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.BEGIN_LEVEL, new String[]{});
        } catch (IllegalArgumentException e) {
            throwException = true;
        } finally {
            Assert.assertTrue(throwException);
        }

    }

    /**
     * Тест на корректность сборки тренировки для новичка на пресс
     */
    @Test
    public void createWorkoutForBeginnerPress() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.BEGIN_LEVEL, new String[]{WorkoutMaker.PRESS_GROUP});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(6, workout.size());
        Set<String> differentExercises = new HashSet<>();
        for (String item :
                workout) {
            differentExercises.add(item);
            Assert.assertTrue(WorkoutMaker.PRESS_EXERCISES.contains(item));
        }
        Assert.assertEquals(6, differentExercises.size());
    }

    /**
     * Тест на корректность сборки тренировки для новичка на ноги
     */
    @Test
    public void createWorkoutForBeginnerLegs() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.BEGIN_LEVEL, new String[]{WorkoutMaker.LEGS_GROUP});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(6, workout.size());
        Set<String> differentExercises = new HashSet<>();
        for (String item :
                workout) {
            differentExercises.add(item);
            Assert.assertTrue(WorkoutMaker.LEGS_EXERCISES.contains(item));
        }
        Assert.assertEquals(6, differentExercises.size());
    }

    /**
     * Тест на корректность сборки тренировки для новичка на руки
     */
    @Test
    public void createWorkoutForBeginnerArms() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.BEGIN_LEVEL, new String[]{WorkoutMaker.ARMS_GROUP});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(6, workout.size());
        Set<String> differentExercises = new HashSet<>();
        for (String item :
                workout) {
            differentExercises.add(item);
            Assert.assertTrue(WorkoutMaker.ARMS_EXERCISES.contains(item));
        }
        Assert.assertEquals(6, differentExercises.size());
    }

    /**
     * Тест на корректность сборки тренировки для любителя на руки
     */
    @Test
    public void createWorkoutForAmateurArms() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.MEDIUM_LEVEL, new String[]{WorkoutMaker.ARMS_GROUP});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(8, workout.size());
        Set<String> differentExercises = new HashSet<>();
        for (String item :
                workout) {
            differentExercises.add(item);
            Assert.assertTrue(WorkoutMaker.ARMS_EXERCISES.contains(item));
        }
        Assert.assertEquals(8, differentExercises.size());
    }

    /**
     * Тест на корректность сборки тренировки для любителя на пресс
     */
    @Test
    public void createWorkoutForAmateurPress() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.MEDIUM_LEVEL, new String[]{WorkoutMaker.PRESS_GROUP});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(8, workout.size());
        Set<String> differentExercises = new HashSet<>();
        for (String item :
                workout) {
            differentExercises.add(item);
            Assert.assertTrue(WorkoutMaker.PRESS_EXERCISES.contains(item));
        }
        Assert.assertEquals(8, differentExercises.size());
    }

    /**
     * Тест на корректность сборки тренировки для продвинутого на ноги
     */
    @Test
    public void createWorkoutForAdvancedLegs() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.ADVANCED_LEVEL, new String[]{WorkoutMaker.LEGS_GROUP});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(8, workout.size());
        Set<String> differentExercises = new HashSet<>();
        for (String item :
                workout) {
            differentExercises.add(item);
            Assert.assertTrue(WorkoutMaker.LEGS_EXERCISES.contains(item));
        }
        Assert.assertEquals(8, differentExercises.size());
    }

    /**
     * Тест на корректность сборки тренировки для новичка на ноги и пресс
     */
    @Test
    public void createWorkoutForBeginnerLegsAndPress() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.BEGIN_LEVEL, new String[]{WorkoutMaker.LEGS_GROUP, WorkoutMaker.PRESS_GROUP});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(6, workout.size());
        Set<String> differentExercises = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            differentExercises.add(workout.get(i));
            if (i < 3)
                Assert.assertTrue(WorkoutMaker.LEGS_EXERCISES.contains(workout.get(i)));
            else
                Assert.assertTrue(WorkoutMaker.PRESS_EXERCISES.contains(workout.get(i)));
        }
        Assert.assertEquals(6, differentExercises.size());
    }

    /**
     * Тест на корректность сборки тренировки для любителя на руки и пресс
     */
    @Test
    public void createWorkoutForAmateurArmsAndPress() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.MEDIUM_LEVEL, new String[]{WorkoutMaker.ARMS_GROUP, WorkoutMaker.PRESS_GROUP});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(8, workout.size());
        Set<String> differentExercises = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            differentExercises.add(workout.get(i));
            if (i < 4)
                Assert.assertTrue(WorkoutMaker.ARMS_EXERCISES.contains(workout.get(i)));
            else
                Assert.assertTrue(WorkoutMaker.PRESS_EXERCISES.contains(workout.get(i)));
        }
        Assert.assertEquals(8, differentExercises.size());
    }

    /**
     * Тест на корректность сборки тренировки для продвинутого на ноги и руки
     */
    @Test
    public void createWorkoutForAdvancedLegsAndArms() {
        WorkoutMaker workoutMaker = new WorkoutMaker(WorkoutMaker.ADVANCED_LEVEL, new String[]{WorkoutMaker.LEGS_GROUP, WorkoutMaker.ARMS_GROUP});
        List<String> workout = workoutMaker.createWorkout();
        Assert.assertEquals(8, workout.size());
        Set<String> differentExercises = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            differentExercises.add(workout.get(i));
            if (i < 4)
                Assert.assertTrue(WorkoutMaker.LEGS_EXERCISES.contains(workout.get(i)));
            else
                Assert.assertTrue(WorkoutMaker.ARMS_EXERCISES.contains(workout.get(i)));
        }
        Assert.assertEquals(8, differentExercises.size());
    }
}
