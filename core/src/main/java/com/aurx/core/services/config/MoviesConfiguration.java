package com.aurx.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * This is the MoviesConfiguration @interface used to create the Movies configuration.
 */
@ObjectClassDefinition(name = "Movies configuration")
public @interface MoviesConfiguration {

  /**
   * This method return boolean value based on the Enabled checkbox.
   *
   * @return - The boolean value.
   */
  @AttributeDefinition(name = "Enabled")
  boolean isEnable();

  /**
   * This method return movies name.
   *
   * @return The movies name.
   */
  @AttributeDefinition(name = "MOVIES")
  String[] movies();
}
