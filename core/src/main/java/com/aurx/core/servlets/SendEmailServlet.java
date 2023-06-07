package com.aurx.core.servlets;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.mcm.emailprovider.EmailService;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/sendemail",
    "sling.servlet.selectors=email",
    "sling.servlet.extensions=json"
})
public class SendEmailServlet extends SlingSafeMethodsServlet {

  private static final Logger log = LoggerFactory.getLogger(SendEmailServlet.class);
  @Reference
  private transient MessageGatewayService messageGatewayService;
    @Override
      protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
      String recipient = request.getParameter("email");
      String username = request.getParameter("username");
     if (recipient != null) {
      JsonObject jsonResponse = new JsonObject();
      boolean sent = false;
      try {

        sendEmail("This is an test email",
            username, recipient);
        response.setStatus(200);
        sent = true;
      } catch (EmailException e) {
        response.setStatus(500);
      }
      jsonResponse.addProperty("result", sent ? "done" : "something went wrong");

      response.getWriter().write(jsonResponse.toString());
    }
  }

  private void sendEmail(String subjectLine, String msgBody, String recipient)
      throws EmailException {
    Email email = new HtmlEmail();
    email.addTo(recipient, recipient);
    email.setSubject(subjectLine);
    email.setMsg(msgBody);
    MessageGateway<Email> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
    if (messageGateway != null) {
      log.debug("sending out email");
      messageGateway.send(email);
    } else {
      log.error("The message gateway could not be retrieved.");
    }
  }
}

