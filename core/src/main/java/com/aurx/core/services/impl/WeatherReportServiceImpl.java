package com.aurx.core.services.impl;

import com.aurx.core.services.MoviesService;
import com.aurx.core.services.WeatherReportService;
import com.aurx.core.services.config.MoviesConfiguration;
import com.aurx.core.services.config.WeatherConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Designate(ocd = WeatherConfiguration.class)
@Component(service = WeatherReportService.class, immediate = true)
public class WeatherReportServiceImpl implements WeatherReportService {
private final Logger logger = LoggerFactory.getLogger(WeatherReportServiceImpl.class);
  private WeatherConfiguration weatherConfiguration;
  @Activate
  protected void activate(WeatherConfiguration weatherConfiguration) {
  this.weatherConfiguration = weatherConfiguration;


  }

  @Modified
  protected void modified(WeatherConfiguration weatherConfiguration) {
    this.weatherConfiguration = weatherConfiguration;

  }

  @Override
  public String getWeatherUrl() {
    return weatherConfiguration.url();
  }

  @Override
  public String getWeatherAppId() {
    return weatherConfiguration.appId();
  }
}
