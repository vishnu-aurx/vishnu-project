package com.aurx.core.workflow;

import static com.aurx.core.constant.ApplicationConstants.CONTAINER;
import static com.aurx.core.constant.ApplicationConstants.IMAGE;
import static com.aurx.core.constant.ApplicationConstants.JCR_PRIMARY_TYPE;
import static com.aurx.core.constant.ApplicationConstants.NT_UNSTRUCTURED;
import static com.aurx.core.constant.ApplicationConstants.SLING_RESOURCE_IMAGE_PATH;
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
    "process.label=Drop Image Component"
})
public class DropImageComponentProcess implements WorkflowProcess {

  /**
   * resourceResolverFactory - ResourceResolverFactory object
   */
  @Reference
  private ResourceResolverFactory resourceResolverFactory;

  /**
   * LOGGER - Logger object
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(DropImageComponentProcess.class);

  /**
   * This method execute when workflow model run
   *
   * @param workItem        - WorkItem object
   * @param workflowSession - WorkflowSession object
   * @param metaDataMap     - MetaDataMap object
   * @throws WorkflowException
   */
  @Override
  public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
      throws WorkflowException {

    try {
      ResourceResolver resolver = ResolverUtils.getResourceResolver(resourceResolverFactory);
      MetaDataMap wfd = workItem.getWorkflow().getMetaDataMap();
      String nodePath = wfd.get("nodePath").toString();
      dropImageComponents(resolver, nodePath);
      LOGGER.info("Component Drop successfully==============");
    } catch (Exception e) {
      LOGGER.error("LoginException : {}", e.getMessage());
    }

  }


  /**
   * This method is used to Drop the image component on page
   *
   * @param resourceResolver - ResourceResolver object
   * @param nodePath - String object
   */
  private void dropImageComponents(ResourceResolver resourceResolver, String nodePath) {
    Resource resource = resourceResolver.getResource(nodePath + CONTAINER);
    Map<String, Object> map = new HashMap<>();
    map.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
    map.put(SLING_RESOURCE_TYPE, SLING_RESOURCE_IMAGE_PATH);
    if (resource != null) {
      try {
        resourceResolver.create(resource, IMAGE, map);
        resourceResolver.commit();
      } catch (PersistenceException e) {
        LOGGER.error("Exception : {}", e.getMessage());
      }
    }
  }
}

