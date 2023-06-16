package com.aurx.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * This is a Configuration @interface
 */
@ObjectClassDefinition(name = "Page Creation Config")

public @interface PageCreationConfig {

  /**
   * @return - parentPagePath
   */
  @AttributeDefinition(name = "parent page path",
      type = AttributeType.STRING)
  public String parentPagePath()default "/content/vishnu-project/us/en";
}
