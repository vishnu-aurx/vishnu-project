package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.APPLICATION_JSON;
import static com.aurx.core.constant.ApplicationConstants.ERROR;
import static com.aurx.core.constant.ApplicationConstants.PAGE_LOAD_COUNT;
import static com.aurx.core.constant.ApplicationConstants.STATUS;
import static com.aurx.core.constant.ApplicationConstants.SUCCESSFUL;
import static com.aurx.core.constant.ApplicationConstants.ZERO;

import com.aurx.core.utils.ResolverUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet is used to count the page load.
 */
@Component(service = Servlet.class, immediate = true, property = {"sling.servlet.methods=GET",
    "sling.servlet.resourceTypes=vishnu-project/components/structure/mypage",
    "sling.servlet.selectors=pageLoad", "sling.servlet.extensions=json"})
public class PageLoadCountServlet extends SlingSafeMethodsServlet {

  /**
   * resourceResolverFactory - The resourceResolverFactory.
   */
  @Reference
  private transient ResourceResolverFactory resourceResolverFactory;

  /**
   * log - The Logger object.
   */
  private static final Logger log = LoggerFactory.getLogger(PageLoadCountServlet.class);

  /**
   * mapper - The mapper.
   */
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * This method is used to fetch the page load count from JCR.
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    log.info("Start of doGet method");
    JsonObject jsonObject =  new JsonObject();
    Resource requestResource = request.getResource();
    ResourceResolver resourceResolver = null;
    try {
      resourceResolver = ResolverUtils.getResourceResolver(resourceResolverFactory);
      if (resourceResolver != null && requestResource != null) {
        log.info("resourceResolver is not null");
        Resource resource = resourceResolver.getResource(requestResource.getPath());
        log.info("this is resource resolver new {}", resourceResolver);
        if (resource != null) {
          log.info("resource is not null");
          String pageLoadCount = resource.getValueMap().get(PAGE_LOAD_COUNT, ZERO);
          Integer pageLoadingCount = Integer.parseInt(pageLoadCount) + 1;
          log.info("page load count : {}", pageLoadingCount);
          ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
          if (modifiableValueMap != null) {
            log.info("modifiableValueMap is not null");
            modifiableValueMap.put(PAGE_LOAD_COUNT, pageLoadingCount);
            resourceResolver.commit();
            jsonObject.addProperty(STATUS,SUCCESSFUL);
          }
        }
      } else {
        log.info("resourceResolver is null");
        jsonObject.addProperty(STATUS,ERROR);
      }
    } catch (LoginException e) {
      log.error("exception occurred {}", e.getMessage());
      jsonObject.addProperty(STATUS,ERROR);
    }

    response.setContentLength(jsonObject.toString().getBytes().length);
    response.setContentType(APPLICATION_JSON);
    response.getOutputStream().write(jsonObject.toString().getBytes());
    log.info("End of doGet method");

  }

}
