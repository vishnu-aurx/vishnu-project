package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.PASSWORD;
import static com.aurx.core.constant.ApplicationConstants.TOKEN_API_URL;
import static com.aurx.core.constant.ApplicationConstants.USERNAME;

import com.aurx.core.services.PopulateDataFromAPI;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet used to call api.
 */
@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/APITest",
    "sling.servlet.selectors=call", "sling.servlet.extensions=json"
})
public class CallAPIServlet extends SlingSafeMethodsServlet {

  /**
   * populateDataFromAPI - The populateDataFromAPI
   */
  @Reference
  private PopulateDataFromAPI populateDataFromAPI;

  /**
   * LOGGER - The Logger object.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(CallAPIServlet.class);

  /**
   * This method used to generate the api response
   *
   * @param request  - The request.
   * @param response - The response.
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    LOGGER.info("Start doGet method of CallAPIServlet  ");
    HttpResponse httpResponse = populateDataFromAPI.getAPIResponseWithUserPassword(
        TOKEN_API_URL, USERNAME, PASSWORD);
    String data = IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
    response.getWriter().write(data);
    LOGGER.info("End of doGet method");
  }
}
