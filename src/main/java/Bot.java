import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Bot extends TelegramLongPollingBot {
    private static final Logger log = Logger.getLogger(Bot.class);
    private static final String BOT_NAME = "TestMyTemporaryBot";
    private static final String BOT_TOKEN = "6013122445:AAFjT-SO3j5-xdeQ2yDc4vd3-0sBlFvQKZg";

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String text = null;
            if (message.hasText()) {
                text = update.getMessage().getText();
                log.info("Получено новое сообщение: " + text);
            }
            if (message.hasLocation()) {
                Location loc = update.getMessage().getLocation();
                log.info("Получена геолокация: " + loc);
                System.out.println(loc);
            }
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            Weather weather = new Weather();
            if ("/start".equals(text)) {
                sendMessage.setText("Добро пожаловать в мой тестовый бот. Пришлите название города, а я попробую найти текущую температуру в этом городе");
            } else {
                sendMessage.setText(weather.getTemp(text));
            }
            try {
                execute(sendMessage); // отправка ответа
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void botConnect() {
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        try {
            telegramBotsApi.registerBot(this);
            log.info("Бот запущен");
        } catch (TelegramApiException e) {
            log.info("Не удалось подключиться. Ошибка: " + e.getMessage());
        }
    }

}
