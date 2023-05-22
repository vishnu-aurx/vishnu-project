package com.aurx.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Weather URL Configuration")
public @interface WeatherConfiguration {
  @AttributeDefinition(name = "URL")
  String url();
  @AttributeDefinition(name = "App Id")
  String appId();
}
