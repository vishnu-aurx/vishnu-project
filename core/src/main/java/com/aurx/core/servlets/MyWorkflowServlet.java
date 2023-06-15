package com.aurx.core.servlets;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import java.io.IOException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Servlet Hit The WorkFlow Model
 */
@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/testworkflow",
     "sling.servlet.selectors=myworkflow",
     "sling.servlet.extensions=json"
})
public class MyWorkflowServlet extends SlingSafeMethodsServlet {

  private static final Logger log = LoggerFactory.getLogger(MyWorkflowServlet.class);
  @Reference
   private  WorkflowService workflowService;
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {

    log.info("==========Servlet Start====== ");
    ResourceResolver resourceResolver = request.getResourceResolver();
    Session session=resourceResolver.adaptTo(Session.class);
    WorkflowSession wfSession = workflowService.getWorkflowSession(session);
    WorkflowModel wfModel = null;
    try {
      wfModel = wfSession.getModel("/var/workflow/models/vishnu-test-workflow");
      WorkflowData wfData = wfSession.newWorkflowData("JCR_PATH", "/content/dam/we-retail/en/features/cart.png");
      wfSession.startWorkflow(wfModel, wfData);
      session.save();
      session.logout();
    } catch (WorkflowException | RepositoryException e) {
      log.info("========this is exception :{}",e.getMessage());
    }
   log.info("================workflow model run using servlet  ============");


  }

}
