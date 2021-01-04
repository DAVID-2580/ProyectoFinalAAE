package utils;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Angel Ricardo Ramírez de la Torre
 */
public class HttpRequestService {

  public static boolean CONTENT_TYPE_JSON = true;
  public static boolean CONTENT_TYPE_PLAIN = false;
  private HttpClient httpClient;
  private Gson gson;

  public HttpRequestService() {
    httpClient = HttpClientBuilder.create().build();
    gson = new Gson();
  }

  public <T> T post(String url, String body, boolean content_type, Class<T> responseType)
          throws InstantiationException, IllegalAccessException, UnsupportedEncodingException, IOException {
    HttpPost request = new HttpPost(GlobalConfig.API_ENDPOINT + url);

    if (content_type == CONTENT_TYPE_JSON) {
      request.addHeader("content-type", "application/json");
      request.setEntity(new StringEntity(body));
    }

    HttpResponse response = httpClient.execute(request);
    String Stringbody = IOUtils.toString(response.getEntity().getContent());

    return gson.fromJson(Stringbody, responseType);
  }
  
  public <T> T put(String url, String body, boolean content_type, Class<T> responseType)
          throws InstantiationException, IllegalAccessException, UnsupportedEncodingException, IOException {
    HttpPut request = new HttpPut(GlobalConfig.API_ENDPOINT + url);

    if (content_type == CONTENT_TYPE_JSON) {
      request.addHeader("content-type", "application/json");
      request.setEntity(new StringEntity(body));
    }

    HttpResponse response = httpClient.execute(request);
    String Stringbody = IOUtils.toString(response.getEntity().getContent());

    return gson.fromJson(Stringbody, responseType);
  }
  
  public <T> T get(String url, boolean content_type, Class<T> responseType)
          throws InstantiationException, IllegalAccessException, UnsupportedEncodingException, IOException {
    HttpGet request = new HttpGet(GlobalConfig.API_ENDPOINT + url);

    if (content_type == CONTENT_TYPE_JSON)
      request.addHeader("content-type", "application/json");

    HttpResponse response = httpClient.execute(request);
    String Stringbody = IOUtils.toString(response.getEntity().getContent());

    return gson.fromJson(Stringbody, responseType);
  }

}
