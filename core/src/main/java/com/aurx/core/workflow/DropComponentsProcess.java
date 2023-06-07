package com.aurx.core.workflow;

import static com.aurx.core.constant.ApplicationConstants.CONTAINER;
import static com.aurx.core.constant.ApplicationConstants.IMAGE;
import static com.aurx.core.constant.ApplicationConstants.JCR_PRIMARY_TYPE;
import static com.aurx.core.constant.ApplicationConstants.NT_UNSTRUCTURED;
import static com.aurx.core.constant.ApplicationConstants.PRODUCT_DETAILS;
import static com.aurx.core.constant.ApplicationConstants.SLING_RESOURCE_TYPE;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aurx.core.utils.ResolverUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * This class used to create the page on the aem
 */
@Component(service = WorkflowProcess.class, immediate = true, property = {
    "process.label=Drop Components"
})
public class DropComponentsProcess implements WorkflowProcess {

  /**
   * resourceResolverFactory - ResourceResolverFactory object
   */
  @Reference
  private ResourceResolverFactory resourceResolverFactory;

  /**
   * LOGGER - Logger object
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(DropComponentsProcess.class);

  /**
   * This method execute when wrokflow model run
   *
   * @param workItem
   * @param workflowSession
   * @param metaDataMap
   * @throws WorkflowException
   */
  @Override
  public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
      throws WorkflowException {

    try {
      ResourceResolver resolver = ResolverUtils.getResourceResolver(resourceResolverFactory);
      MetaDataMap wfd = workItem.getWorkflow().getMetaDataMap();
     String nodePath = wfd.get("nodePath").toString();
      dropProductDetailComponents(resolver,nodePath);
      dropImageComponents(resolver,nodePath);
      LOGGER.info("Component Drop successfully==============");
    } catch (Exception e) {
      LOGGER.error("LoginException : {}", e.getMessage());
    }

  }

  /**
   * This Method is used to drop the product-detail  component on the page
   *
   * @param resourceResolver
   * @param nodePath
   */
  private void dropProductDetailComponents(ResourceResolver resourceResolver, String nodePath) {
    Resource resource = resourceResolver.getResource(nodePath + CONTAINER);
    Map<String, Object> map = new HashMap<>();
    map.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
    map.put(SLING_RESOURCE_TYPE, PRODUCT_DETAILS);
    if (resource != null) {
      try {
        resourceResolver.create(resource, "product_details", map);
        resourceResolver.commit();
      } catch (PersistenceException e) {
        LOGGER.error("Exception : {}", e.getMessage());
      }


    }
  }
  /**
   * This method is used to Drop the image component on page
   *
   * @param resourceResolver
   * @param nodePath
   */
  private void dropImageComponents(ResourceResolver resourceResolver, String nodePath) {
    Resource resource = resourceResolver.getResource(nodePath + CONTAINER);
    Map<String, Object> map = new HashMap<>();
    map.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
    map.put(SLING_RESOURCE_TYPE, IMAGE);
    if (resource != null) {
      try {
        resourceResolver.create(resource, "image", map);
        resourceResolver.commit();
      } catch (PersistenceException e) {
        LOGGER.error("Exception : {}", e.getMessage());
      }
    }
  }
}

