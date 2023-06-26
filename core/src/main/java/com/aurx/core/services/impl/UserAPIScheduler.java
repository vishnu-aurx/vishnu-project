package com.aurx.core.services.impl;

import static com.aurx.core.constant.ApplicationConstants.APP_ID_TIME_PATH;
import static com.aurx.core.constant.ApplicationConstants.JCR_PRIMARY_TYPE;
import static com.aurx.core.utils.ResolverUtils.getResourceResolver;

import com.aurx.core.services.config.UserAPIConfiguration;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This UserAPIScheduler is used to remove tokens.
 */
@Component(service = Runnable.class, immediate = true)
@Designate(ocd = UserAPIConfiguration.class)
public class UserAPIScheduler implements Runnable {

  /**
   * scheduler - Scheduler object.
   */
  @Reference
  Scheduler scheduler;

  /**
   * resourceResolverFactory - ResourceResolverFactory object.
   */
  @Reference
  ResourceResolverFactory resourceResolverFactory;

  /**
   * logger - Logger object.
   */
  private static final Logger logger = LoggerFactory.getLogger(UserAPIScheduler.class);

  /**
   * schedulerId - The schedulerId.
   */
  private int schedulerId;

  /**
   * This method is invoked when configuration is active and modified.
   *
   * @param userAPIConfiguration - The userAPIConfiguration.
   */
  @Activate
  @Modified
  protected void activate(UserAPIConfiguration userAPIConfiguration) {
    logger.info("Start of active methode");
    schedulerId = userAPIConfiguration.schedulerName().hashCode();
    logger.info("This is scheduler name : {}",
        userAPIConfiguration.schedulerName());
    addScheduler(userAPIConfiguration);
    logger.info("End of active method");
  }

  /**
   * This method deactivate the Scheduler.
   *
   * @param userAPIConfiguration -The userAPIConfiguration.
   */
  @Deactivate
  protected void deactivate(UserAPIConfiguration userAPIConfiguration) {
    removeScheduler();
  }

  /**
   * This is the method that is run when the scheduler is invoked.
   */
  @Override
  public void run() {

    logger.info("===============run methode of scheduler===========");
    removeTokens();
  }

  /**
   * This method adds the scheduler.
   *
   * @param userAPIConfiguration - The userAPIConfiguration.
   */
  private void addScheduler(UserAPIConfiguration userAPIConfiguration) {
    logger.info("Start of addScheduler with cron expression : {}",
        userAPIConfiguration.cronExpression());
    if (userAPIConfiguration.isEnable()) {
      logger.info("userAPIConfiguration isEnable");
      ScheduleOptions scheduleOptions = scheduler.EXPR(userAPIConfiguration.cronExpression());
      scheduleOptions.name(String.valueOf(schedulerId));
      scheduleOptions.canRunConcurrently(false);
      scheduler.schedule(this, scheduleOptions);
      logger.info("This is scheduler add methode");
    } else {
      logger.info("Schedule Service is disable");
    }
    logger.info("End of addScheduler");
  }

  /**
   * This method remove Scheduler.
   */
  private void removeScheduler() {
    scheduler.unschedule(String.valueOf(schedulerId));
  }

  /**
   * This method removes the token.
   */
  private void removeTokens() {
    logger.info("Start of removeTokens method");
    try {
      ResourceResolver resourceResolver = getResourceResolver(resourceResolverFactory);
      Resource resource = resourceResolver.getResource(APP_ID_TIME_PATH);
      if (resource != null) {
        logger.info("resource is not null");
        ValueMap valueMap = resource.getValueMap();
        ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
        if (modifiableValueMap != null) {
          logger.info("modifiableValueMap is not null");
          Set<Entry<String, Object>> entries = valueMap.entrySet();
          logger.info("entries : {}",entries);
          for (Entry<String, Object> entry : entries) {
            if (!entry.getKey().equals(JCR_PRIMARY_TYPE)) {
              modifiableValueMap.remove(entry.getKey());
              resourceResolver.commit();
              logger.info("token removed");
            }

          }
        }

      }
    } catch (LoginException | PersistenceException e) {
      logger.error(e.getMessage());
    }
    logger.info("End of removeTokens method");
  }
}