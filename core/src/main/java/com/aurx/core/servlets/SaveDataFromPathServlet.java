package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.APPLICATION_JSON;
import static com.aurx.core.constant.ApplicationConstants.MASSAGE;
import static com.aurx.core.constant.ApplicationConstants.ONE;
import static com.aurx.core.constant.ApplicationConstants.PATH;
import static com.aurx.core.constant.ApplicationConstants.PROP;
import static com.aurx.core.constant.ApplicationConstants.PROP_VALUE;
import static com.aurx.core.constant.ApplicationConstants.TWO;
import static com.aurx.core.constant.ApplicationConstants.VALUE;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;

import com.aurx.core.pojo.FetchDataFromPage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to save data from a path.
 */
@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.resourceTypes=vishnu-project/components/page",
    "sling.servlet.selectors=save", "sling.servlet.extensions=json"
})
public class SaveDataFromPathServlet extends SlingSafeMethodsServlet {

  /**
   * log - Logger object.
   */
  private static final Logger log = LoggerFactory.getLogger(SaveDataFromPathServlet.class);

  /**
   * This method is used to Save data to path the location.
   *
   * @param request  - The request.
   * @param response -The response.
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    JsonObject jsonObject = new JsonObject();
    String path = request.getParameter(PATH);
    String pathValue = request.getParameter(VALUE);
    log.info("path is : {}", path);
    ResourceResolver resourceResolver = request.getResourceResolver();
    Resource resource = resourceResolver.getResource(path);
    if (resource != null) {
      log.info("resource is not null");
      Resource saveResource = resource.getChild(JCR_CONTENT);
      if (saveResource != null) {
        log.info("saveResource is not null");
        ModifiableValueMap modifiableValueMap = saveResource.adaptTo(ModifiableValueMap.class);
        if (modifiableValueMap != null) {
          log.info("modifiableValueMap is not null");
          modifiableValueMap.put(PROP, pathValue);
          resourceResolver.commit();
          jsonObject.addProperty(PROP_VALUE, pathValue);
          jsonObject.addProperty(MASSAGE, ONE);

        }
      }

    } else {
      log.info("resource is null");
      jsonObject.addProperty(PROP_VALUE, pathValue);
      jsonObject.addProperty(MASSAGE, TWO);
    }
    response.setContentLength(jsonObject.toString().getBytes().length);
    response.setContentType(APPLICATION_JSON);
    response.getOutputStream().write(jsonObject.toString().getBytes());
  }

}
