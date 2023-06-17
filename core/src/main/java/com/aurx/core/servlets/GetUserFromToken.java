package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.API_KEY;
import static com.aurx.core.constant.ApplicationConstants.API_USER_NAME;
import static com.aurx.core.constant.ApplicationConstants.APP_ID;
import static com.aurx.core.constant.ApplicationConstants.APP_ID_PATH;
import static com.aurx.core.constant.ApplicationConstants.APP_ID_TIME_PATH;
import static com.aurx.core.constant.ApplicationConstants.DATE_FORMAT;
import static com.aurx.core.constant.ApplicationConstants.ERROR;
import static com.aurx.core.constant.ApplicationConstants.EXPIRE;
import static com.aurx.core.constant.ApplicationConstants.INVALID_API_KEY;
import static com.aurx.core.constant.ApplicationConstants.TOKEN_EXPIRE;
import static com.aurx.core.constant.ApplicationConstants.TOKEN_EXPIRE_MSG;
import static org.apache.commons.lang.StringUtils.EMPTY;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to get the username from a token.
 */
@Component(service = Servlet.class, immediate = true, property = {"sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/user", "sling.servlet.selectors=getuser",
    "sling.servlet.extensions=json"})
public class GetUserFromToken extends SlingSafeMethodsServlet {

  /**
   * jsonObject - The jsonObject.
   */
  private transient JsonObject jsonObject;

  /**
   * LOGGER - The Logger object.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(GetUserFromToken.class);

  /**
   * This method is used to get the username from a token.
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    String appId = request.getParameter(APP_ID);
    LOGGER.info("Start of doGet method with appId : {}", appId);
    jsonObject = new JsonObject();
    Gson gson = new Gson();
    ResourceResolver resourceResolver = request.getResourceResolver();
    if (appId != null) {
      LOGGER.info("appId is not null");
      String tokenKey = getTokenKey(appId, resourceResolver);
      LOGGER.info("tokenKey : {}", tokenKey);
      Resource apiIDTimeResource = resourceResolver.getResource(APP_ID_TIME_PATH);
      if (apiIDTimeResource != null) {
        LOGGER.info("apiIDTimeResource is not null");
        ValueMap valueMap = apiIDTimeResource.getValueMap();
        String time = valueMap.get(appId, EXPIRE);
        if (!time.equals(EXPIRE) && tokenKey != null) {
          LOGGER.info("time is no expire and tokenKey is not null tokenKey : {}", tokenKey);
          fetchUser(time, appId, tokenKey, resourceResolver);
        } else {
          LOGGER.info("Token is expire appId : {}, tokeKey : {}", appId, tokenKey);
          jsonObject.addProperty(TOKEN_EXPIRE, TOKEN_EXPIRE_MSG);
        }
      }
    } else {
      LOGGER.info("Invalid API key");
      jsonObject.addProperty(ERROR, INVALID_API_KEY);
    }
    response.getWriter().write(gson.toJson(jsonObject));
    LOGGER.info("End of doGet method with jsonObject : {}", jsonObject);
  }

  /**
   * This method is used to fetch the user.
   *
   * @param time             - The token time.
   * @param appid            - The appId.
   * @param tokenKey         - The tokenKey.
   * @param resourceResolver - The resourceResolver.
   */
  private void fetchUser(String time, String appid, String tokenKey,
      ResourceResolver resourceResolver) {
    LOGGER.info(
        "Start of fetchUser method with time : {} ,appId : {},  tokenKey : {}, resourceResolver : {}",
        time, appid, tokenKey, resourceResolver);
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
    LocalDateTime tokenTime = LocalDateTime.parse(time, dtf);
    LocalDateTime now = LocalDateTime.now();
    long minutesDifference = ChronoUnit.MINUTES.between(now, tokenTime);
    if (minutesDifference <= -5) {
      LOGGER.info("Token is expire");
      jsonObject.addProperty(appid, TOKEN_EXPIRE_MSG);
    } else {
      Resource apiKeyResource = resourceResolver.getResource(API_KEY);
      if (apiKeyResource != null) {
        LOGGER.info("apiKeyResource is not null");
        ValueMap apiKeyValueMap = apiKeyResource.getValueMap();
        String user = apiKeyValueMap.get(tokenKey, EMPTY);
        user = user.split(":")[0];
        LOGGER.info("User : {}", user);
        if (user.isEmpty()) {
          jsonObject.addProperty(ERROR, INVALID_API_KEY);
        } else {
          LOGGER.info("user is not empty");
          jsonObject.addProperty(API_USER_NAME, user);
        }
      }
    }
    LOGGER.info("End of fetchUser with jsonObject : {}", jsonObject);
  }

  /**
   * This method of getting the user token.
   *
   * @param appid            - The appId.
   * @param resourceResolver The resourceResolver.
   * @return
   */
  private String getTokenKey(String appid, ResourceResolver resourceResolver) {
    LOGGER.info("Start of getTokenKey method with appId : {}, resourceResolver : {}", appid,
        resourceResolver);
    String tokenKey = "";
    Resource appIdResource = resourceResolver.getResource(APP_ID_PATH);
    ValueMap appIdValueMap = appIdResource.getValueMap();
    Set<Entry<String, Object>> entries = appIdValueMap.entrySet();
    LOGGER.info("Entries : {} ", entries);
    for (Entry<String, Object> entry : entries) {
      if (appid.equals(entry.getValue())) {
        tokenKey = entry.getKey();
      }
    }
    LOGGER.info("End of getTokenKey with tokenKey : {}", tokenKey);
    return tokenKey;
  }
}
