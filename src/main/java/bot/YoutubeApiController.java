package bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс YoutubeApi - класс, отвечающий за получение видео с youtube
 * <p>
 * 15.11.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class YoutubeApiController{

    /**
     * Константа - часть ссылки для поиска видео до ключевого слова
     */
    private static final String YOUTUBE_SEARCH_URL = "https://youtube.googleapis.com/youtube/v3/search?part=id&maxResults=10&q=";

    /**
     * Константа - продолжение ссыллки после ключевого слова
     */
    private static final String YOUTUBE_SEARCH_URL_KEY = "&key=AIzaSyAr-Ldoe67-ByfDlWRrj3Rx2iTCObxVe6A";

    /**
     * Константа - ссылка на видео
     */
    public static final String YOUTUBE_WATCH_VIDEO_URL = "https://www.youtube.com/watch?v=";

    /**
     * Функция получения видео по ключевому слову
     * @param keyword - ключевое слово для поиска
     * @return - строка - список ссылок на найденные видео
     * @throws IOException
     */
    public String getVideos(String keyword) throws IOException {

        String urlStr = YOUTUBE_SEARCH_URL + URLEncoder.encode(keyword, "UTF-8") + YOUTUBE_SEARCH_URL_KEY;
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine = in.readLine();
        StringBuffer response = new StringBuffer();
        while (inputLine != null) {
            response.append(inputLine);
            inputLine = in.readLine();
        }
        in.close();

        String[] responseBlocks = response.toString().split("\"id\":");
        Pattern pattern = Pattern.compile("\"videoId\": \".+?\"");
        String links = "";
        for (String el:
             responseBlocks) {
            Matcher matcher = pattern.matcher(el);
            if(matcher.find()) {
                String videoId = el.substring(matcher.start(), matcher.end());
                links += YOUTUBE_WATCH_VIDEO_URL + videoId.substring(12, videoId.length()-1) + "\n";
            }
        }
        return links;
    }
}
