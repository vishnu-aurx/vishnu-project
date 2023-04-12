package com.aurx.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Product Endpoint Configuration")
public @interface ProductDetailsConfiguration {
   @AttributeDefinition(name = "URL")
   String url();
}
