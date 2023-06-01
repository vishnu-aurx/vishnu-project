package com.aurx.core.servlets;

import static com.aurx.core.utils.ResolverUtils.API_KEY;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

@Component(service = Servlet.class,immediate = true,property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/api-key",
    "sling.servlet.selectors=getAPIKey",
    "sling.servlet.extensions=json"
    })
public class GenerateUserAPIKey extends SlingSafeMethodsServlet {
  Random rand = new Random();
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    Gson gson = new Gson();
    JsonObject jsonObject = new JsonObject();
    String user = request.getParameter("username");
    String email = request.getParameter("email");
    if (email != null) {
      user = user + ":" + email;

      int keyNumber = rand.nextInt(9999999);
      String username = "user" + keyNumber;
      ResourceResolver resourceResolver = request.getResourceResolver();
      Resource resource = resourceResolver.getResource(API_KEY);
      if (resource != null) {
        ValueMap valueMap = resource.getValueMap();
        Set<Entry<String, Object>> entries = valueMap.entrySet();
        if (entries.size() > 2) {
          boolean isAuthor = false;
          for (Entry<String, Object> entry : entries) {
            String apiKey = entry.getKey();
            String apiKeyValue = entry.getValue().toString();
            if (!apiKey.equals("jcr:primaryType") && !apiKey.equals("jcr:mixinTypes")) {
              String[] apiValue = apiKeyValue.split(":");
              if (apiValue[1].equals(email)) {
                isAuthor = true;
                jsonObject.addProperty("api_Key",apiKey);
                break;
              }
            }
          }
          if (!isAuthor) {
            ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
            modifiableValueMap.put(username, user);
            resourceResolver.commit();
            jsonObject.addProperty("api_key",username);
            }
        } else {
          ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
          modifiableValueMap.put(username, user);
          resourceResolver.commit();
          jsonObject.addProperty("api_Key",username);

        }
      }
    } else {
      jsonObject.addProperty("error_Massage","invalid email");
       }
    response.getWriter().write(gson.toJson(jsonObject));
  }
}
