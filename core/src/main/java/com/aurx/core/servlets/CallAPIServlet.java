package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.APPLICATION_JSON;
import static com.aurx.core.constant.ApplicationConstants.ERROR;
import static com.aurx.core.constant.ApplicationConstants.PASSWORD;
import static com.aurx.core.constant.ApplicationConstants.STATUS;
import static com.aurx.core.constant.ApplicationConstants.SUCCESSFUL;
import static com.aurx.core.constant.ApplicationConstants.TOKEN_API_URL;
import static com.aurx.core.constant.ApplicationConstants.USERNAME;

import com.aurx.core.services.PopulateDataFromAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
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
   * mapper - The mapper.
   */
  private ObjectMapper mapper = new ObjectMapper();

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
    if(httpResponse !=null) {
      String data = IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
      String responseJson = mapper.writer().withDefaultPrettyPrinter()
          .writeValueAsString(data);
      response.setContentLength(responseJson.getBytes().length);
      response.setContentType(APPLICATION_JSON);
      response.getOutputStream().write(responseJson.getBytes());
    }else{
      LOGGER.info("httpResponse is null");
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty(STATUS,ERROR);
      response.setContentLength(jsonObject.toString().getBytes().length);
      response.setContentType(APPLICATION_JSON);
      response.getOutputStream().write(jsonObject.toString().getBytes());
    }
    LOGGER.info("End of doGet method");
  }
}
