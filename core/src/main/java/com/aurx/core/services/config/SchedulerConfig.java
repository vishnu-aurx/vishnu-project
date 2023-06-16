package com.aurx.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
/**
 * This is a Configuration @interface
 */
@ObjectClassDefinition(name = "Page Creation Config Scheduler")
public @interface SchedulerConfig {

  /**
   * @return - cronExpression
   */
  @AttributeDefinition(name = "cron expression",
      type = AttributeType.STRING)
  public String cronExpression() default "0 0/5 * 1/1 * ? *";

  /**
   * @return - schedulerName
   */
  @AttributeDefinition(name = "scheduler name",
      type = AttributeType.STRING)
  public String schedulerName()default "Car Page Creation Scheduler" ;

  /**
   * @return - scheduler isEnable in boolean type
   */
  @AttributeDefinition(name = "Enable Service",
      type = AttributeType.BOOLEAN)
  public boolean isEnable() default false;

  /**
   * @return - API url
   */
  @AttributeDefinition(name = "URL",
  type = AttributeType.STRING)
  String url() default "https://647d9d39af9847108549fa3a.mockapi.io/aurx/api/v1/create-car-pages-";
}
