package com.aurx.core.utils;

import static com.aurx.core.constant.ApplicationConstants.ACCEPT;
import static com.aurx.core.constant.ApplicationConstants.APPLICATION_JSON;
import static com.aurx.core.constant.ApplicationConstants.AUTHOR;
import static com.aurx.core.constant.ApplicationConstants.AUTHORIZATION;
import static com.aurx.core.constant.ApplicationConstants.BASIC;
import static org.apache.commons.lang.StringUtils.EMPTY;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is utility class
 */
public class PopulateDataFromAPI {

  private PopulateDataFromAPI() {
  }

  /**
   * LOGGER - Logger object
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(PopulateDataFromAPI.class);

  /**
   * This method populates data from the API and returns a response.
   *
   * @param baseURL - The baseURL
   * @return -response
   */
  public static String populateData(String baseURL) {
    LOGGER.info("Start of populateData method with baseURL :{}", baseURL);
    String response = EMPTY;
    URL url = null;
    try {
      if (baseURL != null) {
        url = new URL(baseURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty(ACCEPT, APPLICATION_JSON);
        byte[] encodedAuth = Base64.encodeBase64(AUTHOR.getBytes(StandardCharsets.UTF_8));
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

}
