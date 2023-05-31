package com.aurx.core.servlets;

import static com.aurx.core.utils.ResolverUtils.API_KEY;
import static com.aurx.core.utils.ResolverUtils.APP_ID_PATH;
import static com.aurx.core.utils.ResolverUtils.APP_ID_TIME_PATH;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
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

@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/token",
    "sling.servlet.selectors=gettoken",
    "sling.servlet.extensions=json"
})
public class GetTokenForUser extends SlingSafeMethodsServlet {
  @Override
  protected void doGet(SlingHttpServletRequest request,
      SlingHttpServletResponse response)
      throws ServletException, IOException {
    Random rand = new Random();
    int tokenNumber = rand.nextInt(1000);
    String appKey = request.getParameter("appkey");
    String token = "token" + tokenNumber;

    ResourceResolver resourceResolver = request.getResourceResolver();
    ModifiableValueMap modifiableValueMap = null;
    Resource resource = resourceResolver.getResource(APP_ID_PATH);
    Resource apiKeyResource = resourceResolver.getResource(API_KEY);
    if (resource != null && apiKeyResource !=null) {
      ValueMap apiKeyValueMap = apiKeyResource.getValueMap();
      modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
      if (modifiableValueMap != null && apiKeyValueMap.containsKey(appKey)) {
        modifiableValueMap.put(appKey, token);
        resourceResolver.commit();
         Resource appIdTimeResource = resourceResolver.getResource(APP_ID_TIME_PATH);
        if (appIdTimeResource != null) {
          ModifiableValueMap modifiableValueMapTime = appIdTimeResource.adaptTo(
              ModifiableValueMap.class);
          if (modifiableValueMapTime != null) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:SS");
            LocalDateTime now = LocalDateTime.now();
            String time = dtf.format(now);
            modifiableValueMapTime.put(token, time);
            resourceResolver.commit();
            response.getWriter().write("this is my token :  " + token);
          }
      }
    } else
        response.getWriter().write("this is invalid key  ");
    }
  }
}
