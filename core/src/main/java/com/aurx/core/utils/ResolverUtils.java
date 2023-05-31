package com.aurx.core.utils;

import java.util.HashMap;
import java.util.Map;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

public class ResolverUtils {
  public static final String VISHNU_SERVICE_USER ="vishnuproject";
  public final static String APP_ID_TIME_PATH ="/etc/api-data/app-id-time";
  public final static String APP_ID_PATH = "/etc/api-data/app-id";
  public final static String API_KEY="/etc/api-data/api-key";

  public static ResourceResolver getResourceResolver(ResourceResolverFactory resourceResolverFactory) throws LoginException{
    final Map<String,Object> paramMap =   new HashMap<>();
    paramMap.put(ResourceResolverFactory.SUBSERVICE,VISHNU_SERVICE_USER);
    ResourceResolver resolver=resourceResolverFactory.getServiceResourceResolver(paramMap);
    return resolver;
  }

}
