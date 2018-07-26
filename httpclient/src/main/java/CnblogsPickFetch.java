import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CnblogsPickFetch {
  
  static String url = "https://www.cnblogs.com/pick/";
  
  public static void main(String[] args) throws Exception {
    CloseableHttpClient client = HttpClients.createDefault();
    try {
      HttpGet httpget = new HttpGet(url);
      System.out.println("Executing request: " + httpget.getRequestLine());
      CloseableHttpResponse response = client.execute(httpget);
      try {
        System.out.println("-----------------------------------------");
        System.out.println(response.getStatusLine());
        String content = EntityUtils.toString(response.getEntity(), "gbk");
        
        Document doc = Jsoup.parse(content); // 将获取到的html文档进行解析
        Elements postItems = doc.select("#post_list .post_item"); // 选择精华文章列表
        System.out.printf("%-9s\t%-24s\t%s%n", "推荐数", "作者", "标题");
        System.out.println("-----------------------------------------------------");
        for (Element postItem : postItems) {
          String diggit = postItem.select(".diggit").text(); // 获取推荐数
          String title = postItem.select(".post_item_body .titlelnk").text(); // 获取文章标题
          String author = postItem.select(".post_item_foot .lightblue").text(); // 获取文章作者
          System.out.printf("%-6s\t%-24s\t%s%n", diggit, author, title);
        }
      } finally {
        response.close();
      }
    } finally {
      client.close();
    }
  }

}
