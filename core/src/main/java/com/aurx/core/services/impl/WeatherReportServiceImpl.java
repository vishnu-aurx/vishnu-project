package com.aurx.core.services.impl;

import com.aurx.core.services.WeatherReportService;
import com.aurx.core.services.config.WeatherConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to fetch the url and appId from the Weather Configuration.
 */
@Designate(ocd = WeatherConfiguration.class)
@Component(service = WeatherReportService.class, immediate = true)
public class WeatherReportServiceImpl implements WeatherReportService {

  /**
   * logger - Logger object.
   */
  private static final Logger logger = LoggerFactory.getLogger(WeatherReportServiceImpl.class);

  /**
   * weatherConfiguration - WeatherConfiguration object.
   */
  private WeatherConfiguration weatherConfiguration;

  /**
   * This method is invoked when the service is activated.
   *
   * @param weatherConfiguration - The weatherConfiguration.
   */
  @Activate
  protected void activate(WeatherConfiguration weatherConfiguration) {
    logger.info("Start of activate method with weatherConfiguration : {}", weatherConfiguration);
    this.weatherConfiguration = weatherConfiguration;
    logger.info("End of activate method ");


  }

  /**
   * This method is invoked when the Configuration is modified.
   *
   * @param weatherConfiguration - The weatherConfiguration.
   */
  @Modified
  protected void modified(WeatherConfiguration weatherConfiguration) {
    logger.info("Start of modified with weatherConfiguration : {}", weatherConfiguration);
    this.weatherConfiguration = weatherConfiguration;
    logger.info("End of modified method");
  }

  /**
   * This method return the Weather API URL.
   *
   * @return - The url.
   */
  @Override
  public String getWeatherUrl() {
    return weatherConfiguration.url();
  }

  /**
   * This method return the appId.
   *
   * @return - The appId.
   */
  @Override
  public String getWeatherAppId() {
    return weatherConfiguration.appId();
  }
}
