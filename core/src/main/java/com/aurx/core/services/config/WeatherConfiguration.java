package com.aurx.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * This is the WeatherConfiguration @interface used to create the Weather URL Configuration.
 */
@ObjectClassDefinition(name = "Weather URL Configuration")
public @interface WeatherConfiguration {

  /**
   * This method returns the url.
   *
   * @return - The url.
   */
  @AttributeDefinition(name = "URL")
  String url();

  /**
   * This method returns the appId.
   *
   * @return The appId.
   */
  @AttributeDefinition(name = "App Id")
  String appId();
}
