package com.aurx.core.utils;

import static com.aurx.core.constant.ApplicationConstants.ACCEPT;
import static com.aurx.core.constant.ApplicationConstants.APPLICATION_JSON;
import static com.aurx.core.constant.ApplicationConstants.AUTHOR_CREDENTIALS;
import static com.aurx.core.constant.ApplicationConstants.AUTHORIZATION;
import static com.aurx.core.constant.ApplicationConstants.BASIC;
import static com.aurx.core.constant.ApplicationConstants.HTTP;
import static com.aurx.core.constant.ApplicationConstants.HTTPS;
import static org.apache.commons.lang.StringUtils.EMPTY;

import com.aurx.core.services.CryptoUtil;
import com.aurx.core.services.PopulateDataFromAPI;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class hits the API and populates data from the API.
 */
@Component(service = PopulateDataFromAPI.class, immediate = true)
public class PopulateDataFromAPIImpl implements PopulateDataFromAPI {

  /**
   * cryptoUtil - CryptoUtil object.
   */
  @Reference
  private CryptoUtil cryptoUtil;
  /**
   * LOGGER - Logger object
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(PopulateDataFromAPIImpl.class);

  /**
   * This method populates data from the API and returns a response.
   *
   * @param baseURL - The baseURL
   * @return -response
   */
  public String populateData(String baseURL) {
    LOGGER.info("Start of populateData method with baseURL :{}", baseURL);
    String response = EMPTY;
    URL url = null;
    try {
      if (baseURL != null) {
        LOGGER.info("baseURL is not null");
        url = new URL(baseURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty(ACCEPT, APPLICATION_JSON);
        byte[] encodedAuth = Base64.encodeBase64(AUTHOR_CREDENTIALS.getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = BASIC + new String(encodedAuth);
        connection.setRequestProperty(AUTHORIZATION, authHeaderValue);
        InputStream responseStream = connection.getInputStream();
        response = IOUtils.toString(responseStream, StandardCharsets.UTF_8);
      }
    } catch (IOException e) {
      LOGGER.error("Exception : {}", e.getMessage());
    }
    LOGGER.info("End of populateData method with response : {}", response);
    return response;
  }


  /**
   * This method populates data from the API and returns a httpResponse.
   *
   * @param apiURL       - The apiURL.
   * @param userName     - The userName.
   * @param userPassword - The userPassword.
   * @return - httpResponse.
   */
  public HttpResponse getAPIResponseWithUserPassword(String apiURL, String userName,
      String userPassword) {
    LOGGER.info("Start of getAPIResponse method with apiURL is : {}", apiURL);
    HttpResponse httpResponse = null;
    if (StringUtils.isNotBlank(apiURL) && (apiURL.startsWith(HTTP) || apiURL.startsWith(HTTPS))
        && StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(userPassword)) {
      UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
          cryptoUtil.getDecryptedValue(userName),
          cryptoUtil.getDecryptedValue(userPassword));
      BasicCredentialsProvider provider = new BasicCredentialsProvider();
      provider.setCredentials(AuthScope.ANY, creds);
      HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider)
          .build();
      LOGGER.info("We create a Client that is  : {}", client);
      HttpGet request = new HttpGet();
      try {
        request.setURI(new URI(apiURL));
        LOGGER.info("get request Object from HttpGet : {}", request);
        BasicHttpContext localContext = new BasicHttpContext();
        httpResponse = client.execute(request, localContext);
        LOGGER.info("get response from client.execute() method : {}", httpResponse);
      } catch (URISyntaxException | IOException e) {
        LOGGER.error("Invalid URL Exception : {}", e.getMessage());
      }
      return httpResponse;
    }
    LOGGER.info("End of getAPIResponseWithUserPassword method with HttpResponse is : {}",
        httpResponse);
    return httpResponse;
  }

}
