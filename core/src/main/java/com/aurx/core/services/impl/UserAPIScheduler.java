package com.aurx.core.services.impl;

import static com.aurx.core.utils.ResolverUtils.APP_ID_TIME_PATH;
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

@Component(service = Runnable.class, immediate = true)
@Designate(ocd = UserAPIConfiguration.class)
public class UserAPIScheduler implements Runnable {

  @Reference
  Scheduler scheduler;
  @Reference
  ResourceResolverFactory resourceResolverFactory;
  private static final Logger logger = LoggerFactory.getLogger(UserAPIScheduler.class);
  private int schedulerId;

  @Activate
  @Modified
  protected void activate(UserAPIConfiguration userAPIConfiguration) {
    logger.info("inside the active methode");
    schedulerId = userAPIConfiguration.schedulerName().hashCode();
    logger.info("=================================this is scheduler name : {}",
        userAPIConfiguration.schedulerName());
    addScheduler(userAPIConfiguration);
  }

  @Deactivate
  protected void deactivate(UserAPIConfiguration userAPIConfiguration) {
    removeScheduler();
  }

  @Override
  public void run() {

    logger.info("===============run methode of scheduler===========");
    removeTokens();
  }

  private void addScheduler(UserAPIConfiguration userAPIConfiguration) {
    logger.info("=================================this is cron expression : {}",
        userAPIConfiguration.cronExpression());
    if (userAPIConfiguration.isEnable()) {
      ScheduleOptions scheduleOptions = scheduler.EXPR(userAPIConfiguration.cronExpression());
      scheduleOptions.name(String.valueOf(schedulerId));
      scheduleOptions.canRunConcurrently(false);
      scheduler.schedule(this, scheduleOptions);
      logger.info("=======this is scheduler add methode");
    } else {
      logger.info("============Schedule Service is disable===========");
    }
  }

  private void removeScheduler() {
    scheduler.unschedule(String.valueOf(schedulerId));
  }

  private void removeTokens() {
    try {
      ResourceResolver resourceResolver = getResourceResolver(resourceResolverFactory);

      if (resourceResolver != null) {

        Resource resource = resourceResolver.getResource(APP_ID_TIME_PATH);
        if (resource != null) {
          ValueMap valueMap = resource.getValueMap();
          ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
          if (modifiableValueMap != null) {
            Set<Entry<String, Object>> entries = valueMap.entrySet();
            for (Entry<String, Object> entry : entries) {
              if (!entry.getKey().equals("jcr:primaryType")) {
                modifiableValueMap.remove(entry.getKey());
                resourceResolver.commit();
                logger.info("token removed");
              }

            }
          }

        }
      }
    } catch (LoginException | PersistenceException e) {
      logger.error(e.getMessage());
    }
  }
}