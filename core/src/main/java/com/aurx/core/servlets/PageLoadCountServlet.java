package com.aurx.core.servlets;

import com.aurx.core.utils.ResolverUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, immediate = true, property = {"sling.servlet.methods=GET",
    "sling.servlet.resourceTypes=vishnu-project/components/structure/mypage",
    "sling.servlet.selectors=pageLoad", "sling.servlet.extensions=json"})
public class PageLoadCountServlet extends SlingSafeMethodsServlet {

  @Reference
  private transient ResourceResolverFactory resourceResolverFactory;
  private static final Logger log = LoggerFactory.getLogger(PageLoadCountServlet.class);

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    Resource requestResource = request.getResource();
    ResourceResolver resourceResolver = null;
    try {
      resourceResolver = ResolverUtils.getResourceResolver(resourceResolverFactory);
      if (resourceResolver != null) {
        Resource resource = resourceResolver.getResource(requestResource.getPath());
        log.info("this is resource resolver new {}", resourceResolver);
        if (resource != null) {
          String pageLoadCount = resource.getValueMap().get("pageLoadCount", "0");
          Integer pageLoadingCount = Integer.parseInt(pageLoadCount) + 1;
          ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
          if (modifiableValueMap != null) {
            modifiableValueMap.put("pageLoadCount", pageLoadingCount);
            resourceResolver.commit();
          }
        }
      } else {
        log.info("resourceResolver is null");
      }
    } catch (LoginException e) {
      log.error("exception occurred {}", e.getMessage());
    }
    response.getWriter().write("this is second response");


  }

}
