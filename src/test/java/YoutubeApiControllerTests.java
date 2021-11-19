import bot.YoutubeApiController;
import org.apache.commons.validator.routines.UrlValidator;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс YoutubeApiControllerTests
 * <p>
 * 19.11.2021
 *
 * @author Анастасия Коваленко
 */
public class YoutubeApiControllerTests {
    /**
     * Поле - контроллер youTubeApi
     */
    private YoutubeApiController youtubeApiController = new YoutubeApiController();
    /**
     *  поле - список упражнений на ноги
     */
    private static final ArrayList<String> legsExercises = new ArrayList<>(Arrays.asList(new String[]{"Приседания", "Приседания плие", "Пружинящие приседания", "Приседания с выпрыгиванием", "Приседания с шагом", "Разведение ног в стороны в приседе", "Приседания с поворотом на 180° прыжком", "Приседания на 1 ноге", "Статика в приседе", "Выпады вперед", "Выпады назад", "Болгарские выпады", "Выпады со сменой ног прыжком", "Ягодичный мост",
            "Ягодичный мост с разведением ног в стороны", "Становая тяга", "Мертвая тяга", "Отведение ноги назад стоя", "Отведение ноги назад в упоре на четвереньках", "Отведение ноги назад лежа", "Отведение ноги в сторону в упоре на четвереньках", "Зашагивание на возвышенность", "Удар ногой вперед"}));
    /**
     * поле - список упражнений на пресс
     */
    private static final ArrayList<String> pressExercises = new ArrayList<>(Arrays.asList(new String[]{"Скручивания с отрывом только лопаток", "Полные скручивания", "Подъем ног", "Касание руками поднятых ног", "Планка классическая", "Планка боковая", "Планка с поворотом таза в стороны", "Планка с касанием плечей руками", "Планка с разведением ног прыжками",
            "Планка с переходом с локтей в упор лежа и обратно", "Скалолаз", "Обратные скручивания", "Русский твист", "Ножницы", "Велосипед"}));
    /**
     * Поле - список упражнений на руки
     */
    private static final ArrayList<String> armsExercises = new ArrayList<>(Arrays.asList(new String[]{"Отжимания классические", "Отжимания широким хватом", "Отжимания узким хватом", "Обратные отжимания", "Отведение рук в стороны с гантелями", "Сгибание рук на бицепс с гантелями", "Тяга к груди", "Тяга за голову", "Тяга к поясу в наклоне", "Тяга в планке", "Тяга к подбородку", "Разведение рук в наклоне c гантелями", "Разгибание рук на трицепс в наклоне", "Гиперэкстензия", "Жим лежа", "Жим сидя", "Жим стоя", "Разгибание рук из-за головы с гантелями", "Ходьба руками в планку и обратно"}));

    /**
     * Тест на корректные ссылки для упражнений на ноги
     * @throws IOException
     */
    @Test
    public void getVideosReturnsValidLinksForLegsExercises() throws IOException {
        UrlValidator validator = new UrlValidator();
        int randomIndex = (int) Math.floor(Math.random() * legsExercises.size());
        String keyword = "упражнение+" + legsExercises.get(randomIndex).replaceAll(" ", "+");
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
        UrlValidator validator = new UrlValidator();
        int randomIndex = (int) Math.floor(Math.random() * pressExercises.size());
        String keyword = "упражнение+" + pressExercises.get(randomIndex).replaceAll(" ", "+");
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
        UrlValidator validator = new UrlValidator();
        int randomIndex = (int) Math.floor(Math.random() * armsExercises.size());
        String keyword = "упражнение+" + armsExercises.get(randomIndex).replaceAll(" ", "+");
        String[] links = youtubeApiController.getVideos(keyword).split("\n");
        for (String link:
                links) {
            Assert.assertTrue("ссылка " + link + " не валидная", validator.isValid(link));
        }
    }
}
