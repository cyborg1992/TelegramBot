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

    public Bot() {
        super(BotParams.BOT_TOKEN);
    }

    @Override
    public String getBotUsername() {
        return BotParams.BOT_NAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String text;
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            Weather weather = new Weather();
            if (message.hasText()) {
                text = update.getMessage().getText();
                log.info("Получено сообщение: " + text);
                if ("/start".equals(text)) {
                    sendMessage.setText("Добро пожаловать в мой тестовый бот. \nПришлите название города или свою геолокацию, а я попробую найти погоду");
                } else {
                    if (weather.getWeather(text)) {
                        sendMessage.setText(weather.toString());
                    } else {
                        sendMessage.setText("Город не найден");
                    }
                }
            }
            if (message.hasLocation()) {
                Location loc = update.getMessage().getLocation();
                log.info("Получена геолокация: " + loc);
                if (weather.getWeather(Double.toString(loc.getLatitude()), Double.toString(loc.getLongitude()))) {
                    sendMessage.setText(weather.toString());
                } else {
                    sendMessage.setText("Погода по координатам не найдена");
                }
            }
            try {
                execute(sendMessage); // отправка ответа
            } catch (TelegramApiException e) {
                log.error("Error", e);
            }
        }
    }

    public void botConnect() {
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
            log.info("Бот запущен");
        } catch (TelegramApiException e) {
            log.error("Не удалось подключиться. Ошибка: ", e);
        }
    }

}
