package com.aurx.core.schedulers;

import static com.aurx.core.constant.ApplicationConstants.PAGE_CREATION_MODEL_PATH;

import com.aurx.core.services.PopulateDataFromAPI;
import com.aurx.core.services.config.SchedulerConfig;
import com.aurx.core.utils.ResolverUtils;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
 * This scheduler is used to run the workflow process that creates the page.
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
   * logger -Logger object.
   */
  private static final Logger logger = LoggerFactory.getLogger(CreatingPageScheduler.class);

  /**
   * schedulerId - The schedulerId.
   */
  private int schedulerId;

  /**
   * url - The url.
   */
  private String url;

  /**
   * populateDataFromAPI - PopulateDataFromAPI object.
   */
  @Reference
  private PopulateDataFromAPI populateDataFromAPI;

  /**
   * This method is used to activate the scheduler.
   *
   * @param schedulerConfig - the schedulerConfig object used to fetch the url and schedulerName.
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
    removeScheduler();
    addScheduler(schedulerConfig);

  }

  /**
   * This method is used to Deactivate the scheduler.
   */
  @Deactivate
  protected void deactivate() {
    removeScheduler();
  }

  /**
   * This is the method that is run when the scheduler is invoked.
   */
  @Override
  public void run() {

    logger.info("===============run methode of scheduler===========");
    fetchDataFromResponse();
  }

  /**
   * This method adds the scheduler.
   *
   * @param schedulerConfig - the schedulerConfig fetch the cron expression.
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
   * This method removes the scheduler.
   */
  private void removeScheduler() {
    scheduler.unschedule(String.valueOf(schedulerId));
  }

  /**
   * This method hits the API and fetches data from the response.
   */
  private void fetchDataFromResponse() {
    String responseData = populateDataFromAPI.populateData(url);
    if (responseData != null && !responseData.trim().equals("")) {
      JsonArray jsonElements = JsonParser.parseString(responseData).getAsJsonArray();
      for (JsonElement jsonElement : jsonElements) {
        String carTitle = jsonElement.getAsJsonObject().get("carTitle").getAsString();
        String carCode = jsonElement.getAsJsonObject().get("carCode").getAsString();
        String pageTitle = jsonElement.getAsJsonObject().get("pageTitle").getAsString();
        String pageDescription = jsonElement.getAsJsonObject().get("pageDescription").getAsString();
        String id = jsonElement.getAsJsonObject().get("id").getAsString();
        createPageUsingModel(carTitle, carCode, pageTitle, pageDescription, id);
      }
    }
  }

  /**
   * This method calls the workflow process and creates the page.
   *
   * @param carTitle        - The carTitle
   * @param carCode         - The carCode
   * @param pageTitle       - The pageTitle
   * @param pageDescription - The pageDescription
   * @param id              - The id
   */
  private void createPageUsingModel(String carTitle, String carCode, String pageTitle,
      String pageDescription, String id) {
    logger.info(
        "Start of createPageUsingModel with carTitle : {}, carCode :{}, pageTitle : {},pageDescription : {},id :{}",
        carTitle, carCode, pageTitle, pageDescription, id);
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
          WorkflowData wfData = wfSession.newWorkflowData("JCR_PATH", wfModel.getId());
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