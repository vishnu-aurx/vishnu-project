package com.aurx.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Movies configuration")
public @interface MoviesConfiguration {
   @AttributeDefinition(name = "Enabled")
   boolean isEnable();
    @AttributeDefinition(name = "MOVIES")
    String[] movies();
}
