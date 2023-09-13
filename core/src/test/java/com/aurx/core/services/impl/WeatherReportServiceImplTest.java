package com.aurx.core.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.aurx.core.services.config.WeatherConfiguration;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class WeatherReportServiceImplTest {

  private final AemContext context = new AemContext();

  private WeatherReportServiceImpl weatherReportService;

  @Mock
  private WeatherConfiguration weatherConfiguration;

  @BeforeEach
  void setUp() {
    weatherReportService = context.registerInjectActivateService(new WeatherReportServiceImpl());

  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getWeatherUrl() {
    when(weatherConfiguration.url()).thenReturn("http://api.openweathermap.org/data/2.5/forecast");
    weatherReportService.activate(weatherConfiguration);
    assertNotNull(weatherReportService);
    assertEquals("http://api.openweathermap.org/data/2.5/forecast",weatherReportService.getWeatherUrl());
  }

  @Test
  void getWeatherUrlWithNullValue() {
    when(weatherConfiguration.url()).thenReturn(null);
    weatherReportService.activate(weatherConfiguration);
    assertNotNull(weatherReportService);
    assertNull(weatherReportService.getWeatherUrl());
  }

  @Test
  void getWeatherAppId() {
    when(weatherConfiguration.appId()).thenReturn("3ca682f7f39db8b4f4691dac72e8b5f7");
    weatherReportService.activate(weatherConfiguration);
    assertNotNull(weatherReportService);
    assertNotNull(weatherReportService.getWeatherAppId());
  }
  @Test
  void getWeatherAppIdWithNullValue() {
    when(weatherConfiguration.appId()).thenReturn(null);
    weatherReportService.activate(weatherConfiguration);
    assertNotNull(weatherReportService);
    assertNull(weatherReportService.getWeatherAppId());
  }

}