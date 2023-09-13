package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.USER_LAT;
import static com.aurx.core.constant.ApplicationConstants.USER_LON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aurx.core.services.PopulateDataFromAPI;
import com.aurx.core.services.WeatherReportService;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class GetWeatherServletTest {

  private final AemContext context = new AemContext();

  private GetWeatherServlet getWeatherServlet;

  @Mock
  private WeatherReportService weatherReportService;

  @Mock
  private transient PopulateDataFromAPI populateDataFromAPI;

  @Mock
  private SlingHttpServletRequest request;

  @Mock
  private SlingHttpServletResponse response;

  @Mock
  private ServletOutputStream servletOutputStream;

  @Captor
  private ArgumentCaptor<byte[]> outputStream;

  @Test
  void doPost() throws ServletException, IOException {
    context.registerService(WeatherReportService.class, weatherReportService);
    context.registerService(PopulateDataFromAPI.class, populateDataFromAPI);
    when(request.getParameter(USER_LAT)).thenReturn("23.2487");
    when(request.getParameter(USER_LON)).thenReturn("77.4066");
    Resource res = context.load(true).json(
        "/com/aurx/core/servlets/GetWeatherServletResource/GetWeatherServletComponentTest.json",
        "/content/vishnu-project/us/en/test");
    when(request.getResource()).thenReturn(res);
    when(weatherReportService.getWeatherAppId()).thenReturn("3ca682f7f39db8b4f4691dac72e8b5f7");
    when(weatherReportService.getWeatherUrl()).thenReturn(
        "http://api.openweathermap.org/data/2.5/forecast");
    String weatherReport = IOUtils.toString((Objects.requireNonNull(getClass().getResourceAsStream(
            "/com/aurx/core/servlets/GetWeatherServletResource/WeatherReport.json"))),
        StandardCharsets.UTF_8);
    when(populateDataFromAPI.populateData(
        "http://api.openweathermap.org/data/2.5/forecast?lat=23.2487&lon=77.4066&cnt=8&appid=3ca682f7f39db8b4f4691dac72e8b5f7")).thenReturn(
        weatherReport);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    getWeatherServlet = context.registerInjectActivateService(new GetWeatherServlet());
    getWeatherServlet.doPost(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
    JsonArray responseJsonObject = JsonParser.parseString(responseJson).getAsJsonArray();
    assertEquals(2, responseJsonObject.size());
  }

  @Test
  void doPostWithNullAPIResponse() throws ServletException, IOException {
    context.registerService(WeatherReportService.class, weatherReportService);
    context.registerService(PopulateDataFromAPI.class, populateDataFromAPI);
    when(request.getParameter(USER_LAT)).thenReturn("23.2487");
    when(request.getParameter(USER_LON)).thenReturn("77.4066");
    Resource resource = context.load(true).json(
        "/com/aurx/core/servlets/GetWeatherServletResource/GetWeatherServletComponentDay1Test.json",
        "/content/vishnu-project/us/en/test");
    when(request.getResource()).thenReturn(resource);
    when(weatherReportService.getWeatherAppId()).thenReturn("3ca682f7f39db8b4f4691dac72e8b5f7");
    when(weatherReportService.getWeatherUrl()).thenReturn(
        "http://api.openweathermap.org/data/2.5/forecast");
    String weatherReport = IOUtils.toString((Objects.requireNonNull(getClass().getResourceAsStream(
            "/com/aurx/core/servlets/GetWeatherServletResource/WeatherReport.json"))),
        StandardCharsets.UTF_8);
    when(populateDataFromAPI.populateData(
        "http://api.openweathermap.org/data/2.5/forecast?lat=23.2487&lon=77.4066&cnt=1&appid=3ca682f7f39db8b4f4691dac72e8b5f7")).thenReturn(
        null);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    getWeatherServlet = context.registerInjectActivateService(new GetWeatherServlet());
    getWeatherServlet.doPost(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
    JsonArray responseJsonObject = JsonParser.parseString(responseJson).getAsJsonArray();
    assertEquals(0, responseJsonObject.size());
  }
}