package com.aurx.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "User Scheduler configuration")
public @interface UserAPIConfiguration {

  @AttributeDefinition(name = "cron expression",
      type = AttributeType.STRING)
  public String cronExpression() default "0 0/1 * 1/1 * ? *";
  @AttributeDefinition(name = "scheduler name",
      type = AttributeType.STRING)
  public String schedulerName()default " custom scheduler name" ;
@AttributeDefinition(name = "Enable Service",
type = AttributeType.BOOLEAN)
  public boolean isEnable() default false;
}
