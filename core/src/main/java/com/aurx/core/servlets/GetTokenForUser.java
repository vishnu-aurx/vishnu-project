package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.API_KEY;
import static com.aurx.core.constant.ApplicationConstants.API_KEY_VALUE;
import static com.aurx.core.constant.ApplicationConstants.APPLICATION_JSON;
import static com.aurx.core.constant.ApplicationConstants.APP_ID_PATH;
import static com.aurx.core.constant.ApplicationConstants.APP_ID_TIME_PATH;
import static com.aurx.core.constant.ApplicationConstants.APP_KEY;
import static com.aurx.core.constant.ApplicationConstants.DATE_FORMAT;
import static com.aurx.core.constant.ApplicationConstants.ERROR;
import static com.aurx.core.constant.ApplicationConstants.INVALID_API_KEY;
import static com.aurx.core.constant.ApplicationConstants.TOKEN;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
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
 * This class generate the token for user.
 */
@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/token",
    "sling.servlet.selectors=gettoken",
    "sling.servlet.extensions=json"
})
public class GetTokenForUser extends SlingSafeMethodsServlet {

  /**
   * rand - Random object.
   */
  private final Random rand = new Random();

  /**
   * LOGGER - Logger object.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(GetTokenForUser.class);

  /**
   * This method is used to generate the token for the user.
   *
   * @param request  - The request.
   * @param response - The response.
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(SlingHttpServletRequest request,
      SlingHttpServletResponse response)
      throws ServletException, IOException {
    LOGGER.info("Start of doGet Method");
    JsonObject jsonObject = new JsonObject();
    int tokenNumber = rand.nextInt(1000);
    String appKey = request.getParameter(APP_KEY);
    LOGGER.info("appKey : {} ", appKey);
    if (appKey != null) {
      LOGGER.info("appKey is not null");
      String token = "token" + tokenNumber;
      LOGGER.info("token is : {}", token);
      ResourceResolver resourceResolver = request.getResourceResolver();
      ModifiableValueMap modifiableValueMap = null;
      Resource resource = resourceResolver.getResource(APP_ID_PATH);
      Resource apiKeyResource = resourceResolver.getResource(API_KEY);
      if (resource != null && apiKeyResource != null) {
        LOGGER.info("resource and apiKeyResource is not null");
        ValueMap apiKeyValueMap = apiKeyResource.getValueMap();
        modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
        if (modifiableValueMap != null && apiKeyValueMap.containsKey(appKey)) {
          LOGGER.info("appKey : {},token :{}", appKey, token);
          saveToken(modifiableValueMap, appKey, token, jsonObject, resourceResolver);
        } else {
          LOGGER.info("appKey is : {}", appKey);
          jsonObject.addProperty(ERROR, INVALID_API_KEY);
          jsonObject.addProperty(API_KEY_VALUE, appKey);
        }
      }
    } else {
      jsonObject.addProperty(ERROR, INVALID_API_KEY);
      jsonObject.addProperty(API_KEY_VALUE, appKey);
    }
    response.setContentLength(jsonObject.toString().getBytes().length);
    response.setContentType(APPLICATION_JSON);
    response.getOutputStream().write(jsonObject.toString().getBytes());
  }

  /**
   * This method is used to save token in JCR.
   *
   * @param modifiableValueMap - The modifiableValueMap.
   * @param appKey             - The appKey.
   * @param token              - The token.
   * @param jsonObject         - The jsonObject.
   * @param resourceResolver   - The resourceResolver.
   * @throws PersistenceException
   */
  private void saveToken(ModifiableValueMap modifiableValueMap, String appKey, String token,
      JsonObject jsonObject, ResourceResolver resourceResolver)
      throws PersistenceException {
    LOGGER.info(
        "Start of saveToken method with appKey : {},token :{},modifiableValueMap :{},jsonObject : {} ,resourceResolver : {}",
        appKey, token, modifiableValueMap, jsonObject, resourceResolver);
    modifiableValueMap.put(appKey, token);
    resourceResolver.commit();
    Resource appIdTimeResource = resourceResolver.getResource(APP_ID_TIME_PATH);
    if (appIdTimeResource != null) {
      LOGGER.info("appIdTimeResource is not null");
      ModifiableValueMap modifiableValueMapTime = appIdTimeResource.adaptTo(
          ModifiableValueMap.class);
      if (modifiableValueMapTime != null) {
        LOGGER.info("modifiableValueMapTime is not null");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);
        modifiableValueMapTime.put(token, time);
        resourceResolver.commit();
        jsonObject.addProperty(TOKEN, token);
      }
    }
    LOGGER.info("End of saveToken with jsonObject : {}", jsonObject);
  }
}
