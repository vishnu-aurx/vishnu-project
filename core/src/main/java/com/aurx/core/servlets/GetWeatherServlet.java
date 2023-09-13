package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.APPLICATION_JSON;
import static com.aurx.core.constant.ApplicationConstants.APP_ID_URL;
import static com.aurx.core.constant.ApplicationConstants.CNT_URL;
import static com.aurx.core.constant.ApplicationConstants.DAYS;
import static com.aurx.core.constant.ApplicationConstants.DESCRIPTION;
import static com.aurx.core.constant.ApplicationConstants.DT_TXT;
import static com.aurx.core.constant.ApplicationConstants.HTTP;
import static com.aurx.core.constant.ApplicationConstants.HTTPS;
import static com.aurx.core.constant.ApplicationConstants.ICON;
import static com.aurx.core.constant.ApplicationConstants.ICON_URL;
import static com.aurx.core.constant.ApplicationConstants.LAT_URL;
import static com.aurx.core.constant.ApplicationConstants.LIST;
import static com.aurx.core.constant.ApplicationConstants.LON_URL;
import static com.aurx.core.constant.ApplicationConstants.MAIN;
import static com.aurx.core.constant.ApplicationConstants.PNG_URL;
import static com.aurx.core.constant.ApplicationConstants.SPEED;
import static com.aurx.core.constant.ApplicationConstants.TEMP;
import static com.aurx.core.constant.ApplicationConstants.TEN;
import static com.aurx.core.constant.ApplicationConstants.USER_LAT;
import static com.aurx.core.constant.ApplicationConstants.USER_LON;
import static com.aurx.core.constant.ApplicationConstants.WEATHER;
import static com.aurx.core.constant.ApplicationConstants.WIND;
import static org.apache.commons.lang.StringUtils.EMPTY;

import com.aurx.core.pojo.Weather;
import com.aurx.core.services.PopulateDataFromAPI;
import com.aurx.core.services.WeatherReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to get weather reports.
 */
@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=POST",
    "sling.servlet.resourceTypes=vishnu-project/components/GetWeather",
    "sling.servlet.selectors=weather", "sling.servlet.extensions=json"})
public class GetWeatherServlet extends SlingAllMethodsServlet {

  /**
   * log - The Logger object.
   */
  private static final Logger log = LoggerFactory.getLogger(GetWeatherServlet.class);

  /**
   * populateDataFromAPI - The populateDataFromAPI
   */
  @Reference
  private transient PopulateDataFromAPI populateDataFromAPI;

  /**
   * weatherReportService - The WeatherReportService object.
   */
  @Reference
  private transient WeatherReportService weatherReportService;

  /**
   * mapper - The mapper.
   */
  private ObjectMapper mapper = new ObjectMapper();


  /**
   * This method is used to get weather reports
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    String lat = request.getParameter(USER_LAT);
    String lon = request.getParameter(USER_LON);
    int days = Integer.parseInt(request.getResource().getValueMap().get(DAYS, TEN));
    String appId = weatherReportService.getWeatherAppId();
    log.info("lon : {}", lon);
    log.info("lat : {}", lat);
    log.info("days : {}", days);
    log.info("url : {}", weatherReportService.getWeatherUrl());
    String baseURL = weatherReportService.getWeatherUrl();
    List<Weather> weatherList = fetchWeatherReport(baseURL, lat, lon, days, appId);
    String responseJson = mapper.writer().withDefaultPrettyPrinter()
        .writeValueAsString(weatherList);
    response.setContentLength(responseJson.getBytes().length);
    response.setContentType(APPLICATION_JSON);
    response.getOutputStream().write(responseJson.getBytes());
    log.info("End of doPost method with report : {} ", weatherList);
  }


  /**
   * This method is used to fetch the weather report from the weather API..
   *
   * @param baseURL - The baseURL of API.
   * @param lat     - The Latitude.
   * @param lon     - The Longitude.
   * @param days    - The days.
   * @param appId   - The appId.
   * @return
   */
  private List<Weather> fetchWeatherReport(String baseURL, String lat, String lon, int days,
      String appId) {
    log.info(
        "Start of fetchWeatherReport with baseURL : {}, lat : {}, lon : {}, days : {}, appId : {}",
        baseURL, lat, lon, days, appId);
    String urlData;
    JsonObject jsonObject;
    List<Weather> weatherList = new ArrayList<>();
    if (baseURL.startsWith(HTTPS) || baseURL.startsWith(HTTP)) {
      if (days == 1) {
        baseURL = baseURL + LAT_URL + lat + LON_URL + lon + CNT_URL + days + APP_ID_URL + appId;
        log.info("baseUrl : {}", baseURL);
      } else {
        baseURL = baseURL + LAT_URL + lat + LON_URL + lon + CNT_URL + ((days - 1) * 8) + APP_ID_URL
            + appId;
        log.info("baseUrl : {}", baseURL);
      }
      log.info("Start of doPost method with url :{}", baseURL);
      urlData = populateDataFromAPI.populateData(baseURL);
      if (urlData != null && !urlData.trim().equals("")) {
        log.info(" urlData : {}", urlData);
        jsonObject = JsonParser.parseString(urlData).getAsJsonObject();
        JsonArray list = jsonObject.getAsJsonArray(LIST);
        log.info(" JsonArray list : {}", list);
        for (JsonElement element : list) {
          JsonObject weatherObject = element.getAsJsonObject();
          String dateTime = weatherObject.get(DT_TXT).getAsString();
          double temperature = weatherObject.getAsJsonObject(MAIN).get(TEMP).getAsDouble();
          JsonArray weather = weatherObject.getAsJsonArray(WEATHER);
          double windSpeed = weatherObject.getAsJsonObject(WIND).get(SPEED).getAsDouble();
          String cloud = EMPTY;
          String iconsId = EMPTY;
          for (JsonElement weatherElement : weather) {
            JsonObject weatherElementAsJsonObject = weatherElement.getAsJsonObject();
            cloud = weatherElementAsJsonObject.get(DESCRIPTION).getAsString();
            iconsId = weatherElementAsJsonObject.get(ICON).getAsString();
          }
          String icons = ICON_URL + iconsId + PNG_URL;
          weatherList.add(new Weather(dateTime, temperature, windSpeed, cloud, icons));
          log.info("Date/Time: {}  ,Temperature: {}, cloud: {} ,wind Speed : {}", dateTime,
              temperature, cloud, windSpeed);
        }
      }
    }
    /*Gson gson = new Gson();
    log.info("End of fetchWeatherReport with response : {} ", gson.toJson(weatherList));
    return gson.toJson(weatherList);*/

    return weatherList;
  }
}




