package com.aurx.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aurx.core.utils.ResolverUtils;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.sling.api.resource.LoginException;
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
    "process.label=Replicate Page"
})
public class ReplicatePageProcess implements WorkflowProcess {

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
  private static final Logger LOGGER = LoggerFactory.getLogger(ReplicatePageProcess.class);

  /**
   * This method execute when Replicate Page invoked
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
      MetaDataMap wfd = workItem.getWorkflow().getMetaDataMap();
      String pagePath = wfd.get("nodePath").toString();
      pageReplicateToPublish(pagePath);
      LOGGER.info("Page Publish successfully==============");
    } catch (Exception e) {
      LOGGER.error("LoginException : {}", e.getMessage());
    }

  }

  /**
   * This method publish the page
   *
   * @param pagePath - the pagePath is used to publishing the page
   */
  private void pageReplicateToPublish(String pagePath) {
    try {
      ResourceResolver resolver = ResolverUtils.getResourceResolver(resourceResolverFactory);
      Session session = resolver.adaptTo(Session.class);

      replicator.replicate(session, ReplicationActionType.ACTIVATE, pagePath);
      LOGGER.info("Page Publishing");
      session.save();
      session.refresh(true);
    } catch (LoginException | ReplicationException | RepositoryException e) {
      LOGGER.error(" Page not Publish Exception : {}", e.getMessage());
    }
  }
}

