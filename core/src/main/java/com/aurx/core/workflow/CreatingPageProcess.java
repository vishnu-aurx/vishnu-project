package com.aurx.core.workflow;

import static com.aurx.core.constant.ApplicationConstants.JCR_CONTENT;
import static com.aurx.core.constant.ApplicationConstants.TEMPLATE;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aurx.core.services.config.PageCreationConfig;
import com.aurx.core.utils.ResolverUtils;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import javax.jcr.Session;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class used to create the page on the aem
 */
@Component(service = WorkflowProcess.class, immediate = true, property = {
    "process.label=Create Page"
})
@Designate(ocd = PageCreationConfig.class)
public class CreatingPageProcess implements WorkflowProcess {

  /**
   * resourceResolverFactory - ResourceResolverFactory object
   */
  @Reference
  private ResourceResolverFactory resourceResolverFactory;
  /**
   * replicator - Replicator object
   */
  @Reference
  private Replicator replicator;
  /**
   * LOGGER - Logger object
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(CreatingPageProcess.class);
  /**
   * parentPagePath - String object hold the parentPagePath
   */
  private String parentPagePath;

  /**
   * This method used to set the parentPagePath
   *
   * @param config
   */
  @Activate
  protected void activate(PageCreationConfig config) {
    parentPagePath = config.parentPagePath();
  }

  /**
   * This method execute when Creating Page Process invoked
   *
   * @param workItem        - the workItem
   * @param workflowSession - the workflowSession
   * @param metaDataMap     - the metaDataMap
   * @throws WorkflowException
   */
  @Override
  public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
      throws WorkflowException {
    try {
      ResourceResolver resolver = ResolverUtils.getResourceResolver(resourceResolverFactory);
      String carCode = workItem.getWorkflow().getMetaDataMap().get("carCode").toString();
      String carTitle = workItem.getWorkflow().getMetaDataMap().get("carTitle").toString();
      String pageTitle = workItem.getWorkflow().getMetaDataMap().get("pageTitle").toString();
      createPage(resolver, workItem, carCode, carTitle, pageTitle);
      LOGGER.info("Page created successfully==============");
    } catch (Exception e) {
      LOGGER.error("LoginException : {}", e.getMessage());
    }
  }

  /**
   * This method create the page in AEM
   *
   * @param resolver  - the resolver
   * @param carCode   - the carCode
   * @param carTitle  - the carTitle
   * @param pageTitle - the pageTitle
   * @throws Exception
   */
  private void createPage(ResourceResolver resolver, WorkItem workItem, String carCode,
      String carTitle, String pageTitle) {

    Page prodPage = null;
    Session session = resolver.adaptTo(Session.class);
    try {
      if (session != null) {
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        if (pageManager != null) {
          LOGGER.info("pageManager is not null==========");
          if (pageTitle != null) {
            String pageName = pageTitle.replace(" ", "-");
            pageName = pageName.toLowerCase();
            prodPage = pageManager.create(parentPagePath, pageName, TEMPLATE, pageTitle);
            if (prodPage.hasContent()) {
              String nodePath = prodPage.getPath();
              Resource resource = resolver.getResource(nodePath +JCR_CONTENT);
              ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
              modifiableValueMap.put("carTitle", carTitle);
              modifiableValueMap.put("carCode", carCode);
              MetaDataMap wfd = workItem.getWorkflow().getMetaDataMap();
              wfd.put("nodePath", nodePath);
              resolver.commit();
            }
          }
        }
        session.save();
        session.refresh(true);
      }
    } catch (Exception e) {
      LOGGER.error("Exception  :  {}", e.getMessage());
    }
  }


}

