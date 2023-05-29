package com.aurx.core.servlets;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/token",
    "sling.servlet.selectors=gettoken",
    "sling.servlet.extensions=json"
})
public class GetTokenForUser extends SlingSafeMethodsServlet {

  private final static String apiKeyPath = "/etc/api-data/api-key";
  private final static String appIdPath = "/etc/api-data/app-id";

  @Override
  protected void doGet(SlingHttpServletRequest request,
      SlingHttpServletResponse response)
      throws ServletException, IOException {
    Random rand = new Random();
    int tokenNumber = rand.nextInt(1000);
    String appKey = request.getParameter("appkey");
    String token = "token" + tokenNumber;

    ResourceResolver resourceResolver = request.getResourceResolver();

    Resource resource = resourceResolver.getResource(appIdPath);
    if (resource != null) {
       ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
      Set<Entry<String, Object>> entries = resource.getValueMap().entrySet();
      if(modifiableValueMap !=null && modifiableValueMap.containsKey(appKey)){
          modifiableValueMap.put(appKey, token);
          resourceResolver.commit();
          response.getWriter().write("this is my token :  " + token);
      }else
          response.getWriter().write("this is invalid key  ");


    }
  }
}