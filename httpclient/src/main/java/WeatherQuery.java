
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherQuery {
  
  static String appCode = "4073983cf899411a8792dec11dc88e43";

  static String url = "http://jisutqybmf.market.alicloudapi.com/weather/query";
  
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage: <city name>");
      System.exit(-1);
    }
    String charset = "utf-8";
    String encodedCityName = URLEncoder.encode(args[0], charset);
    CloseableHttpClient client = HttpClients.createDefault();
    try {
      HttpGet httpget = new HttpGet(url + "?city=" + encodedCityName);
      // add Authorized Header
      httpget.addHeader(new BasicHeader("Authorization", "APPCODE " + appCode));
      System.out.println("Executing request: " + httpget.getRequestLine());
      CloseableHttpResponse response = client.execute(httpget);
      try {
        System.out.println("-----------------------------------------");
        System.out.println(response.getStatusLine());
        String content = EntityUtils.toString(response.getEntity(), charset);
        System.out.println("Response: " + content);
        Map<String, ?> map = parseJson(content);
        Map<String, ?> weatherData = (Map<String, ?>) map.get("result");
        System.out.printf("%s  %s%n", weatherData.get("date"), weatherData.get("week"));
        System.out.printf("%s  %s  %s~%s℃    %s%s", weatherData.get("city"),  weatherData.get("weather"), 
            weatherData.get("templow"), weatherData.get("temphigh"), 
            weatherData.get("winddirect"), weatherData.get("windpower"));
      } finally {
        response.close();
      }
    } finally {
      client.close();
    }
  }
  
  static Map<String, ?> parseJson(String content) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(content, Map.class);
  }
}
