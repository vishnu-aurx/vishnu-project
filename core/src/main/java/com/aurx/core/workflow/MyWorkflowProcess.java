package com.aurx.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = WorkflowProcess.class,immediate = true,property = {
    "process.label=VishnuWorkflow"
})
public class MyWorkflowProcess implements WorkflowProcess{
private static final Logger LOGGER = LoggerFactory.getLogger(MyWorkflowProcess.class);
  @Override
  public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
      throws WorkflowException {
    workItem.getWorkflow().getMetaDataMap().put("test",true);
  LOGGER.info("==========this is my workflow process========== test : true");
  }
}
