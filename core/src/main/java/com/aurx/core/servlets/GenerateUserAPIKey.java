package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.APIKEY;
import static com.aurx.core.constant.ApplicationConstants.API_KEY;
import static com.aurx.core.constant.ApplicationConstants.COLUMN;
import static com.aurx.core.constant.ApplicationConstants.EMAIL;
import static com.aurx.core.constant.ApplicationConstants.ERROR_MASSAGE;
import static com.aurx.core.constant.ApplicationConstants.INVALID_EMAIL;
import static com.aurx.core.constant.ApplicationConstants.JCR_MIXIN_TYPES;
import static com.aurx.core.constant.ApplicationConstants.JCR_PRIMARY_TYPE;
import static com.aurx.core.constant.ApplicationConstants.USER_NAME;

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
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class used to Generate User API Key.
 */
@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/api-key",
    "sling.servlet.selectors=getAPIKey",
    "sling.servlet.extensions=json"
})
public class GenerateUserAPIKey extends SlingSafeMethodsServlet {

  /**
   * rand - Random object.
   */
  private final Random rand = new Random();

  /**
   * LOGGER - Logger object.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(GenerateUserAPIKey.class);

  /**
   * jsonObject - The jsonObject.
   */
  private transient JsonObject jsonObject;

  /**
   * This method is used to Generate User API Key
   *
   * @param request  - The request
   * @param response - The response.
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    LOGGER.info("Start of doGet method");
    Gson gson = new Gson();
    jsonObject = new JsonObject();
    String user = request.getParameter(USER_NAME);
    String email = request.getParameter(EMAIL);
    if (email != null) {
      LOGGER.info("email is not null  email : {}, user : {}", email, user);
      user = user + ":" + email;
      int keyNumber = rand.nextInt(9999999);
      String username = "user" + keyNumber;
      ResourceResolver resourceResolver = request.getResourceResolver();
      Resource resource = resourceResolver.getResource(API_KEY);
      if (resource != null) {
        LOGGER.info("resource is not null");
        ValueMap valueMap = resource.getValueMap();
        Set<Entry<String, Object>> entries = valueMap.entrySet();
        populateEntries(entries, email, username, user, resourceResolver, resource);
      }
    } else {
      LOGGER.info("email is empty or null");
      jsonObject.addProperty(ERROR_MASSAGE, INVALID_EMAIL);
    }
    response.getWriter().write(gson.toJson(jsonObject));
    LOGGER.info("End of doGet method");
  }


  /**
   * This method populate the entries.
   *
   * @param entries          - The set of Entry object.
   * @param email            - The email.
   * @param username         - The username.
   * @param user             - The user.
   * @param resourceResolver -The resourceResolver.
   * @param resource         - The resource.
   * @throws PersistenceException
   */
  private void populateEntries(Set<Entry<String, Object>> entries, String email, String username,
      String user, ResourceResolver resourceResolver, Resource resource)
      throws PersistenceException {
    LOGGER.info(
        "Start of The populateEntries method with entries : {},email : {}, username : {}, user : {}",
        entries, email, username, user);
    if (entries.size() > 2) {
      LOGGER.info(" entries.size() > 2 : {} ", entries.size());
      boolean isAuthor = false;
      for (Entry<String, Object> entry : entries) {
        String apiKey = entry.getKey();
        String apiKeyValue = entry.getValue().toString();
        if (!apiKey.equals(JCR_PRIMARY_TYPE) && !apiKey.equals(JCR_MIXIN_TYPES)) {
          String[] apiValue = apiKeyValue.split(COLUMN);
          if (apiValue[1].equals(email)) {
            isAuthor = true;
            jsonObject.addProperty(APIKEY, apiKey);
            break;
          }
        }
      }
      if (!isAuthor) {
        LOGGER.info("isAuthor : {}", isAuthor);
        ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
        modifiableValueMap.put(username, user);
        resourceResolver.commit();
        jsonObject.addProperty(APIKEY, username);
      }
    } else {
      LOGGER.info(" entries.size() > < 2 : {} ", entries.size());
      ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
      modifiableValueMap.put(username, user);
      resourceResolver.commit();
      jsonObject.addProperty(APIKEY, username);

    }
  }
}
