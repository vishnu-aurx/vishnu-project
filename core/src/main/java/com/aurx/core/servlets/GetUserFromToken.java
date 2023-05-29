package com.aurx.core.servlets;

import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
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

@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/user",
    "sling.servlet.selectors=getuser",
    "sling.servlet.extensions=json"
})
public class GetUserFromToken extends SlingSafeMethodsServlet {

  private final Logger logger = LoggerFactory.getLogger(GetUserFromToken.class);
  private final static String apiKeyPath = "/etc/api-data/api-key";
  private final static String appIdPath = "/etc/api-data/app-id";

  @Override
  protected void doGet(SlingHttpServletRequest request,
      SlingHttpServletResponse response)
      throws ServletException, IOException {

    String appid = request.getParameter("appid");
    ResourceResolver resourceResolver = request.getResourceResolver();
    Resource appIdResource = resourceResolver.getResource(appIdPath);
     String tokenKey="";
    if(appIdResource !=null) {
      ValueMap appIdValueMap = appIdResource.getValueMap();
      Set<Entry<String, Object>> entries = appIdValueMap.entrySet();
      for (Entry entry : entries
      ){
        if(appid.equals(entry.getValue()))
        tokenKey= (String) entry.getKey();
      }
      Resource apiKeyResource = resourceResolver.getResource(apiKeyPath);
      if (apiKeyResource != null) {
        ValueMap apiKeyValueMap = apiKeyResource.getValueMap();
        String user = apiKeyValueMap.get(tokenKey, "");
        if(user.isEmpty()){
          response.getWriter().write("this token is not valid");
        }else
        response.getWriter().write("this is my token of : "+user);
      }
    }
  }
}
