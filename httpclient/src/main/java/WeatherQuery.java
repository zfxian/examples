
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
    String encodedCityName = URLEncoder.encode(args[0], charset); // 获取参数，并进行编码
    CloseableHttpClient client = HttpClients.createDefault(); // 创建一个Http客户端
    try {
      HttpGet httpget = new HttpGet(url + "?city=" + encodedCityName);
      // add Authorized Header
      httpget.addHeader(new BasicHeader("Authorization", "APPCODE " + appCode)); // 设置认证头信息
      System.out.println("Executing request: " + httpget.getRequestLine());
      CloseableHttpResponse response = client.execute(httpget); // 执行请求，返回响应
      try {
        System.out.println("-----------------------------------------");
        System.out.println(response.getStatusLine());
        String content = EntityUtils.toString(response.getEntity(), charset); // 将请求体转出字符串
        System.out.println("Response: " + content);
        Map<String, ?> map = parseJson(content); // 将请求回到到的JSON字符串转换成Map
        Map<String, ?> weatherData = (Map<String, ?>) map.get("result"); // 获取天气信息
        // 打印天气信息
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
