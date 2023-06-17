package com.aurx.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * This is the ProductDetailsConfiguration @interface used to create the Product Endpoint Configuration.
 */
@ObjectClassDefinition(name = "Product Endpoint Configuration")
public @interface ProductDetailsConfiguration {

   /**
    * This method is used to take url.
    * @return -The url
    */
   @AttributeDefinition(name = "URL")
   String url();
}
