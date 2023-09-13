package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.APPLICATION_JSON;
import static com.aurx.core.constant.ApplicationConstants.MASSAGE;
import static com.aurx.core.constant.ApplicationConstants.ONE;
import static com.aurx.core.constant.ApplicationConstants.PATH;
import static com.aurx.core.constant.ApplicationConstants.PROP;
import static com.aurx.core.constant.ApplicationConstants.PROP_VALUE;
import static com.aurx.core.constant.ApplicationConstants.THREE;
import static com.aurx.core.constant.ApplicationConstants.TWO;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static org.apache.commons.lang.StringUtils.EMPTY;

import com.aurx.core.pojo.FetchDataFromPage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
 * This class is used to fetch data from a path.
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
   * This method is used to fetch prop value from the jcr.
   *
   * @param request  - The request
   * @param response - The response
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    JsonObject jsonObject = new JsonObject();
    String path = request.getParameter(PATH);
    log.info("Start of doGet method path is : {}", path);
    ResourceResolver resourceResolver = request.getResourceResolver();
    Resource resource = resourceResolver.getResource(path);
    if (resource != null) {
      log.info("resource in not null");
      Resource childResource = resource.getChild(JCR_CONTENT);
      if (childResource != null) {
        log.info("childResource is not null");
        ValueMap valueMap = childResource.getValueMap();
        String value = valueMap.get(PROP, EMPTY);
        log.info("prop value is : {}", value);
        if (value.isEmpty()) {
          log.info("value is not isEmpty :{} ", value);
             jsonObject.addProperty(PROP_VALUE,value);
             jsonObject.addProperty(MASSAGE , TWO);
        } else {
          log.info("prop value is empty");
          jsonObject.addProperty(PROP_VALUE,value);
          jsonObject.addProperty(MASSAGE,ONE);
        }
        log.info("prop value : {}", value);
      }
    } else {
      log.info("resource is null");
      jsonObject.addProperty(PROP_VALUE,EMPTY);
      jsonObject.addProperty(MASSAGE,THREE);
    }
    response.setContentLength(jsonObject.toString().getBytes().length);
    response.setContentType(APPLICATION_JSON);
    response.getOutputStream().write(jsonObject.toString().getBytes());
  }

}
