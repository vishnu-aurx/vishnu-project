package com.aurx.core.servlets;

import static com.aurx.core.utils.ResolverUtils.API_KEY;
import static com.aurx.core.utils.ResolverUtils.APP_ID_PATH;
import static com.aurx.core.utils.ResolverUtils.APP_ID_TIME_PATH;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map.Entry;
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

@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/user",
    "sling.servlet.selectors=getuser",
    "sling.servlet.extensions=json"
})
public class GetUserFromToken extends SlingSafeMethodsServlet {






  @Override
  protected void doGet(SlingHttpServletRequest request,
      SlingHttpServletResponse response)
      throws ServletException, IOException {

    String appid = request.getParameter("appid");
    ResourceResolver resourceResolver = request.getResourceResolver();
    Resource appIdResource = resourceResolver.getResource(APP_ID_PATH);
     String tokenKey="";
    if(appIdResource !=null) {
      ValueMap appIdValueMap = appIdResource.getValueMap();
      Set<Entry<String, Object>> entries = appIdValueMap.entrySet();
      for (Entry<String,Object> entry : entries){

        if(appid.equals(entry.getValue())) {
          tokenKey = entry.getKey();
        }
      }

      Resource apiIDTimeResource = resourceResolver.getResource(APP_ID_TIME_PATH);
      if (apiIDTimeResource !=null){
        ValueMap valueMap = apiIDTimeResource.getValueMap();
        String time = valueMap.get(appid,"expire");
        if(!time.equals("expire")) {
          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:SS");
          LocalDateTime tokenTime = LocalDateTime.parse(time, dtf);
          LocalDateTime now = LocalDateTime.now();
          long minutesDifference = ChronoUnit.MINUTES.between(now, tokenTime);
          if (minutesDifference <= -5) {
            response.getWriter().write("token is expire " + appid);
          } else {
            Resource apiKeyResource = resourceResolver.getResource(API_KEY);
            if (apiKeyResource != null) {
              ValueMap apiKeyValueMap = apiKeyResource.getValueMap();
              String user = apiKeyValueMap.get(tokenKey, "");
              user = user.split(":")[0];
              if (user.isEmpty()) {
                response.getWriter().write("this token is not valid");
              } else
                response.getWriter().write("UserName : " + user);
            }
          }
        }else
          response.getWriter().write("token is not valid");


      }
    }
  }
}
