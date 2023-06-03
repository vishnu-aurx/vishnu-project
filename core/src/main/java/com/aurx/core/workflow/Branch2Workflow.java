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
    "process.label=vishnu branch 2 workflow"
})
public class Branch2Workflow implements WorkflowProcess {
private static final Logger LOGGER= LoggerFactory.getLogger(Branch2Workflow.class);
  @Override
  public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
      throws WorkflowException {
   LOGGER.info("=================this is branch 2 ==============");
  }
}
