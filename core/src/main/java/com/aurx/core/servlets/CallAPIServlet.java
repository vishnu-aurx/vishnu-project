package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.TOKEN_API_URL;
import static com.aurx.core.utils.PopulateDataFromAPI.populateData;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

/**
 * This servlet used to call api.
 */
@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/callAPI",
    "sling.servlet.selectors=api",
    "sling.servlet.extensions=json"
})
public class CallAPIServlet extends SlingSafeMethodsServlet {

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
    String data = populateData(TOKEN_API_URL);
    response.getWriter().write(data);
  }
}
