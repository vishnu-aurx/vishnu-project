package com.aurx.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Annotation Interface DataBaseNameConfiguration
 */
@ObjectClassDefinition(name = "DataBase Name Configuration")
public @interface DataBaseNameConfiguration {

  /**
   * This method returns the dataSource name.
   * @return - The dataSourceName.
   */
  @AttributeDefinition(name = "DataSource Name",
  type = AttributeType.STRING)
String dataSourceName() default "localDBTest";
}
