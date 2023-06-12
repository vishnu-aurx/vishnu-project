package com.aurx.core.servlets;

import com.aurx.core.pojo.FetchDataFromPage;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class is used to fetch data from path
 */
@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.resourceTypes=vishnu-project/components/page",
    "sling.servlet.selectors=fetch", "sling.servlet.extensions=json"
})
public class FetchDataFromPathServlet extends SlingSafeMethodsServlet {

  /**
   * log - Logger object
   */
  private static final Logger log = LoggerFactory.getLogger(FetchDataFromPathServlet.class);

  /**
   * this method is used to getting parameter and send responses in this method fetch the prop value
   * from the path and generate the response
   *
   * @param request  - SlingHttpServletRequest object
   * @param response - SlingHttpServletResponse object
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    String path = request.getParameter("path");
    log.info("path is : {}", path);
    ResourceResolver resourceResolver = request.getResourceResolver();
    Resource resource = resourceResolver.getResource(path);
    if (resource != null) {
      Resource childResource = resource.getChild("jcr:content");
      if (childResource != null) {
        ValueMap valueMap = childResource.getValueMap();
        String value = valueMap.get("prop", "");
        if (value.isEmpty()) {
          FetchDataFromPage dataFromPage = new FetchDataFromPage(value, "2");
          Gson gson = new Gson();
          response.getWriter().write(gson.toJson(dataFromPage));
        } else {
          FetchDataFromPage dataFromPage = new FetchDataFromPage(value, "1");
          Gson gson = new Gson();
          response.getWriter().write(gson.toJson(dataFromPage));
        }
        log.info("{}-value", value);
      }
    } else {
      FetchDataFromPage dataFromPage = new FetchDataFromPage("", "3");
      Gson gson = new Gson();
      response.getWriter().write(gson.toJson(dataFromPage));
    }

  }

}
