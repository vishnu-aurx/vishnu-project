package com.aurx.core.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
   * This method populate data from API and return jsonArray
   * @param baseURL
   *  @return
   */
  public static String populateData(String baseURL) {
    String response = "";
    JsonArray jsonElements = null;
      URL url = null;
      try {
        url = new URL(baseURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");
        InputStream responseStream = connection.getInputStream();
        response = IOUtils.toString(responseStream, StandardCharsets.UTF_8);

      } catch (IOException e) {
        LOGGER.error("Exception : {}", e.getMessage());
      }
    return response;
  }

}
