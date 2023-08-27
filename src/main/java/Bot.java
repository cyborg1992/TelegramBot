import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            log.info("Получено новое сообщение: " + text);
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            Weather weather = new Weather();
            if (text.equals("/start")) {
                message.setText("Добро пожаловать в мой тестовый бот. Пришлите название города, а я попробую найти текущую температуру в этом городе");
            } else {
                message.setText(weather.getTemp(text));
            }
            try {
                execute(message); // отправка ответа
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
