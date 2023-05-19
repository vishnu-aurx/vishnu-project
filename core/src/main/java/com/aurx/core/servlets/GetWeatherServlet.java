package com.aurx.core.servlets;

import com.aurx.core.pojo.Weather;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class,immediate = true,property = {
    "sling.servlet.methods=POST",
    "sling.servlet.resourceTypes=vishnu-project/components/GetWeather",
    "sling.servlet.selectors=weather", "sling.servlet.extensions=json"})
public class GetWeatherServlet extends SlingAllMethodsServlet {
  private static final Logger log = LoggerFactory.getLogger(GetWeatherServlet.class);


  @Override
  protected void doPost(SlingHttpServletRequest request,SlingHttpServletResponse response)
      throws ServletException, IOException {
     List<Weather> weatherList = new ArrayList<>();
     JsonObject jsonObject;
    String lat= request.getParameter("userLat");
    String lon= request.getParameter("userLon");
    String days= request.getParameter("days");
    log.info("lon : {}",lon);
    log.info("lat : {}",lat);
    log.info("days : {}",days);

    String urlData = "";
    String baseURL = "http://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&cnt="+days+"&appid=3ca682f7f39db8b4f4691dac72e8b5f7";
    log.info(" method start url :{}",baseURL);
    URL url = new URL(baseURL);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty("accept", "application/json");
    InputStream responseStream = connection.getInputStream();
    urlData = IOUtils.toString(responseStream, StandardCharsets.UTF_8);
    if (urlData!= null && !urlData.trim().equals("")) {
      JsonParser parser = new JsonParser();
      jsonObject = parser.parse(urlData).getAsJsonObject();
      JsonArray list = jsonObject.getAsJsonArray("list");
      for (JsonElement element : list) {
        JsonObject weatherObject = element.getAsJsonObject();
        String dateTime = weatherObject.get("dt_txt").getAsString();
        double temperature = weatherObject.getAsJsonObject("main").get("temp").getAsDouble();
        JsonArray weather = weatherObject.getAsJsonArray("weather");
        double windSpeed = weatherObject.getAsJsonObject("wind").get("speed").getAsDouble();
        String cloud="";
        String iconsId="";
        for (JsonElement weatherElement :  weather) {
          JsonObject weatherElementAsJsonObject = weatherElement.getAsJsonObject();
           cloud = weatherElementAsJsonObject.get("description").getAsString();
          iconsId = weatherElementAsJsonObject.get("icon").getAsString();
        }
        String icons="https://openweathermap.org/img/wn/"+iconsId+"@2x.png";
        weatherList.add(new Weather(dateTime,temperature,windSpeed,cloud,icons));
        log.info("Date/Time: {}  ,Temperature: {}, cloud: {} ,wind Speed : {}" , dateTime,temperature,cloud,windSpeed);
      }
      Gson gson = new Gson();
      response.getWriter().write(gson.toJson(weatherList));
    }
  }



  }


