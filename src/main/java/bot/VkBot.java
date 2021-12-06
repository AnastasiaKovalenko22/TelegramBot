package bot;


import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.model.events.messages.MessageNewEvent;
import api.longpoll.bots.model.objects.basic.Message;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.objects.messages.KeyboardButton;

import java.io.IOException;
import java.util.*;

/**
 * Класс VkBot - класс, отвечающий за сущность бота ВКонтакте
 * <p>
 * 28.11.2021
 *
 * @author Анастасия Коваленко
 * @author Ксения Шорохова
 */
public class VkBot extends LongPollBot implements ChatBot {
    /**
     * Поле - интерфейс для предачи запросов к VK-API
     */
    private TransportClient transportClient = new HttpTransportClient();
    /**
     * Поле - интерфейс для взаимодействия с VK-API с помощью запросов
     */
    private VkApiClient vk = new VkApiClient(transportClient);
    /**
     * поле генератора рандомных значений
     */
    private Random random = new Random();
    /**
     * поле авторизации сообществ
     */
    private GroupActor actor = new GroupActor(getGroupId(), getAccessToken());
    /**
     * поле обработчика сообщений
     */
    private MessagesHandler messagesHandler = new MessagesHandler(this);

    /**
     * Функция получения ключа доступа
     *
     * @return - ключ доступа
     */
    @Override
    public String getAccessToken() {
        return System.getenv("VK_BOT_TOKEN");
    }

    /**
     * Функция получения id группы ВКонтакте
     *
     * @return - id группы
     */
    @Override
    public int getGroupId() {
        return Integer.parseInt(System.getenv("GROUP_ID"));
    }

    /**
     * Процедура обработки поступающих сообщений
     *
     * @param messageEvent - событие поступления сообщения
     */
    @Override
    public void onMessageNew(MessageNewEvent messageEvent) {
        Message message = messageEvent.getMessage();
        String callback = message.getPayload();
        String chatId = message.getPeerId().toString();
        if (callback != null) {
            messagesHandler.handleCallback(callback, chatId);
        } else {
            String text = message.getText();
            messagesHandler.handleCommandMessage(text, chatId);
        }
    }

    /**
     * Процедура отправки текстового сообщения ползователю
     *
     * @param text   - текст сообщения
     * @param chatId - id чата
     */
    @Override
    public void sendTextMessage(String text, String chatId) {
        try {
            vk.messages().send(actor).message(text).peerId(Math.toIntExact(Long.parseLong(chatId))).randomId(random.nextInt(10000)).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * Процедура отправки сообщений с кнопками
     *
     * @param text      - текст сообщения
     * @param chatId    - id чата
     * @param options   - подписи кнопок
     * @param callbacks - коллбэки
     */
    @Override
    public void sendMessageWithButtons(String text, String chatId, String[] options, Map<String, String> callbacks) {
        Keyboard keyboard = new Keyboard();
        keyboard.setButtons(makeButtons(options, callbacks));
        try {
            vk.messages().send(actor).message(text).peerId(Math.toIntExact(Long.parseLong(chatId))).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * Функция создания кнопок
     *
     * @param options   - подписи кнопок
     * @param callbacks - коллбэки
     * @return - список рядов кнопок
     */
    private List<List<KeyboardButton>> makeButtons(String[] options, Map<String, String> callbacks) {
        List<List<KeyboardButton>> buttons = new ArrayList<>();
        for (String option :
                options) {
            buttons.add(Arrays.asList(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel(option).setType(TemplateActionTypeNames.TEXT).setPayload(callbacks.get(option)))));
        }
        return buttons;
    }

}
