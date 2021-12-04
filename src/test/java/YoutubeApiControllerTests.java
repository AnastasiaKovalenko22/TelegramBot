import bot.WorkoutMaker;
import bot.YoutubeApiController;
import org.apache.commons.validator.routines.UrlValidator;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Класс YoutubeApiControllerTests
 * <p>
 * 19.11.2021
 *
 * @author Анастасия Коваленко
 */
public class YoutubeApiControllerTests {

    /**
     * Тест на корректные ссылки для упражнений на ноги
     * @throws IOException
     */
    @Test
    public void getVideosReturnsValidLinksForLegsExercises() throws IOException {
        YoutubeApiController youtubeApiController = new YoutubeApiController();
        UrlValidator validator = new UrlValidator();
        int randomIndex = (int) Math.floor(Math.random() * WorkoutMaker.getLegsExercises().size());
        String keyword = "упражнение+" + WorkoutMaker.getLegsExercises().get(randomIndex).replaceAll(" ", "+");
        String[] links = youtubeApiController.getVideos(keyword).split("\n");
        for (String link:
             links) {
            Assert.assertTrue("ссылка " + link + " не валидная", validator.isValid(link));
        }
    }

    /**
     * Тест на корректные ссылки для упражнений на пресс
     * @throws IOException
     */
    @Test
    public void getVideosReturnsValidLinksForPressExercises() throws IOException {
        YoutubeApiController youtubeApiController = new YoutubeApiController();
        UrlValidator validator = new UrlValidator();
        int randomIndex = (int) Math.floor(Math.random() * WorkoutMaker.getPressExercises().size());
        String keyword = "упражнение+" + WorkoutMaker.getPressExercises().get(randomIndex).replaceAll(" ", "+");
        String[] links = youtubeApiController.getVideos(keyword).split("\n");
        for (String link:
                links) {
            Assert.assertTrue("ссылка " + link + " не валидная", validator.isValid(link));
        }
    }

    /**
     * Тест на корректные ссылки для упражнений на руки
     * @throws IOException
     */
    @Test
    public void getVideosReturnsValidLinksForArmsExercises() throws IOException {
        YoutubeApiController youtubeApiController = new YoutubeApiController();
        UrlValidator validator = new UrlValidator();
        int randomIndex = (int) Math.floor(Math.random() * WorkoutMaker.getArmsExercises().size());
        String keyword = "упражнение+" + WorkoutMaker.getArmsExercises().get(randomIndex).replaceAll(" ", "+");
        String[] links = youtubeApiController.getVideos(keyword).split("\n");
        for (String link:
                links) {
            Assert.assertTrue("ссылка " + link + " не валидная", validator.isValid(link));
        }
    }
}
