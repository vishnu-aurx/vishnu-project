package com.aurx.core.schedulers;

import static com.aurx.core.constant.ApplicationConstants.CART_PNG;
import static com.aurx.core.constant.ApplicationConstants.PAGE_CREATION_MODEL_PATH;

import com.aurx.core.services.config.SchedulerConfig;
import com.aurx.core.utils.PopulateDataFromAPI;
import com.aurx.core.utils.ResolverUtils;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
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
 * This Scheduler is used to run the workflow process that create the page
 */
@Component(service = Runnable.class, immediate = true)
@Designate(ocd = SchedulerConfig.class)
public class CreatingPageScheduler implements Runnable {

  /**
   * scheduler - Scheduler object
   */
  @Reference
  Scheduler scheduler;
  /**
   * workflowService -WorkflowService object
   */
  @Reference
  private WorkflowService workflowService;
  /**
   * resourceResolverFactory - ResourceResolverFactory object
   */
  @Reference
  private ResourceResolverFactory resourceResolverFactory;
  /**
   * logger -Logger object
   */
  private static final Logger logger = LoggerFactory.getLogger(CreatingPageScheduler.class);

  /**
   * schedulerId - int type schedulerId
   */
  private int schedulerId;
  /**
   * url -String object
   */
  private String url;

  /**
   * This method is used to activate the scheduler
   *
   * @param schedulerConfig
   */
  @Activate
  @Modified
  protected void activate(SchedulerConfig schedulerConfig) {
    logger.info("inside the active methode createPageCofig : {}", schedulerConfig);
    schedulerId = schedulerConfig.schedulerName().hashCode();
    logger.info("=================================this is scheduler name : {}",
        schedulerConfig.schedulerName());
    this.url = schedulerConfig.url();
    logger.info("=================================this is URL : {}",
        schedulerConfig.url());

    addScheduler(schedulerConfig);

  }

  /**
   * This method is used to Deactivate the scheduler
   *
   * @param schedulerConfig
   */
  @Deactivate
  protected void deactivate(SchedulerConfig schedulerConfig) {
    removeScheduler();
  }

  /**
   * This is method run when scheduler is call
   */
  @Override
  public void run() {

    logger.info("===============run methode of scheduler===========");
    setData();
  }

  /**
   * This method add the scheduler
   *
   * @param schedulerConfig
   */
  private void addScheduler(SchedulerConfig schedulerConfig) {
    logger.info("==============this is cron expression : {}, isEnable : {}",
        schedulerConfig.cronExpression(), schedulerConfig.isEnable());
    if (schedulerConfig.isEnable()) {
      ScheduleOptions scheduleOptions = scheduler.EXPR(schedulerConfig.cronExpression());
      scheduleOptions.name(String.valueOf(schedulerId));
      scheduleOptions.canRunConcurrently(false);
      scheduler.schedule(this, scheduleOptions);
      logger.info("=====this is scheduler add methode");
    } else {
      removeScheduler();
      logger.info("============Schedule Service is disable===========");
    }
  }

  /**
   * This method remove the Scheduler
   */
  private void removeScheduler() {
    scheduler.unschedule(String.valueOf(schedulerId));
  }

  /**
   * This method is used to getData from populateData() util method and pass the param to
   * createPageUsingModel() method
   */
  private void setData() {
    JsonArray jsonElements = PopulateDataFromAPI.populateData(url);
    for (JsonElement jsonElement : jsonElements) {
      String carTitle = jsonElement.getAsJsonObject().get("carTitle").getAsString();
      String carCode = jsonElement.getAsJsonObject().get("carCode").getAsString();
      String pageTitle = jsonElement.getAsJsonObject().get("pageTitle").getAsString();
      String pageDescription = jsonElement.getAsJsonObject().get("pageDescription").getAsString();
      String id = jsonElement.getAsJsonObject().get("id").getAsString();

      createPageUsingModel(carTitle, carCode, pageTitle, pageDescription, id);
    }
  }

  /**
   * This method call the workflow process and create the page
   *
   * @param carTitle
   * @param carCode
   * @param pageTitle
   * @param pageDescription
   * @param id
   */
  private void createPageUsingModel(String carTitle, String carCode, String pageTitle,
      String pageDescription, String id) {
    logger.info("createPageUsingModel Method start");
    ResourceResolver resourceResolver = null;
    Map<String, Object> workflowMetadata = new HashMap<>();
    workflowMetadata.put("carTitle", carTitle);
    workflowMetadata.put("carCode", carCode);
    workflowMetadata.put("pageTitle", pageTitle);
    workflowMetadata.put("pageDescription", pageDescription);
    workflowMetadata.put("id", id);

    try {
      resourceResolver = ResolverUtils.getResourceResolver(resourceResolverFactory);
    } catch (LoginException e) {
      logger.error("Exception : {}", e.getMessage());
    }
    if (resourceResolver != null) {
      logger.info("resourceResolver is not null");
      Session session = resourceResolver.adaptTo(Session.class);
      if (session != null) {
        logger.info("session is not null");
        WorkflowSession wfSession = workflowService.getWorkflowSession(session);
        WorkflowModel wfModel = null;
        try {
          wfModel = wfSession.getModel(PAGE_CREATION_MODEL_PATH);
          logger.info("wfSession ========= {}", wfSession);
          WorkflowData wfData = wfSession.newWorkflowData("JCR_PATH", CART_PNG);
          logger.info("wfData ========= {}", wfData);
          wfSession.startWorkflow(wfModel, wfData, workflowMetadata);
          session.save();
          session.logout();
        } catch (WorkflowException | RepositoryException e) {
          logger.info("Exception :{}", e.getMessage());
        }
        logger.info("createPageUsingModel Method  end ");
      }
    }
  }
}