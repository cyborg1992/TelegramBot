import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Getter
@Setter
public class Weather {
    private static final Logger log = Logger.getLogger(Weather.class);
    private static final String API_KEY = WeatherAPI.KEY;

    private String cityName; //название города
    private Double temp;  //температура
    private Double feelsLike; //Ощущается как
    private Double tempMin; //минимальная температура
    private Double tempMax; //максимальная температура
    private int pressure;  //атмосферное давление
    private int humidity; //влажность %
    private int cloudsAll; //Облачность
    private String weatherDescription; //Погодные условия

    private boolean readJson(String json) {
        if (json.isEmpty()) return Boolean.FALSE;
        JSONObject obj = new JSONObject(json);
        setCityName(obj.getString("name"));
        setTemp(obj.getJSONObject("main").getDouble("temp"));
        setFeelsLike(obj.getJSONObject("main").getDouble("feels_like"));
        setTempMin(obj.getJSONObject("main").getDouble("temp_min"));
        setTempMax(obj.getJSONObject("main").getDouble("temp_max"));
        setPressure(obj.getJSONObject("main").getInt("pressure"));
        setHumidity(obj.getJSONObject("main").getInt("humidity"));
        setCloudsAll(obj.getJSONObject("clouds").getInt("all"));
        setWeatherDescription(obj.getJSONArray("weather").getJSONObject(0).getString("description"));
        return Boolean.TRUE;
    }

    public Boolean getWeather(String userCity) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" +
                userCity +
                "&appid=" +
                API_KEY +
                "&units=metric&lang=ru";
        log.info(url);
        String output = getUrlContent(url);
        return readJson(output);
    }

    public Boolean getWeather(String lat, String lon) {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" +
                lat +
                "&lon=" +
                lon +
                "&appid=" +
                API_KEY +
                "&units=metric&lang=ru";
        log.info(url);
        String output = getUrlContent(url);
        return readJson(output);
    }

    // Обработка URL адреса и получение данных с него
    private static String getUrlContent(String urlAddress) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(urlAddress);
            URLConnection urlConn = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            log.info(content.toString());
            bufferedReader.close();
        } catch (Exception e) {
            log.error("", e);
        }
        return content.toString();
    }

    @Override
    public String toString() {
        return "Город: " + cityName + '\n' +
                "Температура: " + temp + " ℃\n" +
                "Ощущается как: " + feelsLike + " ℃\n" +
                "Минимальная температура: " + tempMin + " ℃\n" +
                "Максимальная температура: " + tempMax + " ℃\n" +
                "Атмосферное давление: " + pressure + " гПа\n" +
                "Влажность: " + humidity + "%\n" +
                "Облачность: " + cloudsAll + "%\n" +
                "Погода: " + weatherDescription + '\n';
    }
}
