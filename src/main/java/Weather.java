import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Weather {
    private static final String API_KEY = "274eb70b414e144bc089e4f68cecbd14";

    public String getTemp(String userCity) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + userCity + "&appid=" + API_KEY + "&units=metric";
        String output = getUrlContent(url);
        if (!output.isEmpty()) {
            JSONObject obj = new JSONObject(output);
            return Double.toString(obj.getJSONObject("main").getDouble("temp"));
        }
        return "Не найден город";
    }
//            "Температура: " + obj.getJSONObject("main").getDouble("temp"));
//            "Ощущается: " + obj.getJSONObject("main").getDouble("feels_like"));
//            "Максимум: " + obj.getJSONObject("main").getDouble("temp_max"));
//            "Минимум: " + obj.getJSONObject("main").getDouble("temp_min"));
//            "Давление: " + obj.getJSONObject("main").getDouble("pressure"));

    // Обработка URL адреса и получение данных с него
    private static String getUrlContent(String urlAddress) {
        StringBuffer content = new StringBuffer();

        try {
            URL url = new URL(urlAddress);
            URLConnection urlConn = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Такой город не был найден!");
        }
        return content.toString();
    }
}
