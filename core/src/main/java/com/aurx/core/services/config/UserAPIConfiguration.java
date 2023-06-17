package com.aurx.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * This is the UserAPIConfiguration @interface used to create the User Scheduler configuration.
 */
@ObjectClassDefinition(name = "User Scheduler configuration")
public @interface UserAPIConfiguration {

  /**
   * This method return the cronExpression.
   *
   * @return - The cronExpression.
   */
  @AttributeDefinition(name = "cron expression",
      type = AttributeType.STRING)
  public String cronExpression() default "0 0/1 * 1/1 * ? *";

  /**
   * This method return the schedulerName.
   *
   * @return - The schedulerName.
   */
  @AttributeDefinition(name = "scheduler name",
      type = AttributeType.STRING)
  public String schedulerName() default " custom scheduler name";

  /**
   * This method return the boolean value based on the Enable Service checkbox.
   *
   * @return - The boolean value.
   */
  @AttributeDefinition(name = "Enable Service",
      type = AttributeType.BOOLEAN)
  public boolean isEnable() default false;
}
