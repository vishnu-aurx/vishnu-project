package com.aurx.core.services;

/**
 * Interface WeatherReportService.
 */
public interface WeatherReportService {

  /**
   * This method return the weather API URL.
   *
   * @return - The weather API URL
   */
  String getWeatherUrl();

  /**
   * This method return the AppID.
   *
   * @return - the appId.
   */
  String getWeatherAppId();
}
