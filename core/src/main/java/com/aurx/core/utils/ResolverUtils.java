package com.aurx.core.utils;

import static com.aurx.core.constant.ApplicationConstants.VISHNU_SERVICE_USER;

import java.util.HashMap;
import java.util.Map;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

public class ResolverUtils {


  public static ResourceResolver getResourceResolver(ResourceResolverFactory resourceResolverFactory) throws LoginException{
    final Map<String,Object> paramMap =   new HashMap<>();
    paramMap.put(ResourceResolverFactory.SUBSERVICE,VISHNU_SERVICE_USER);
    ResourceResolver resolver=resourceResolverFactory.getServiceResourceResolver(paramMap);
    return resolver;
  }

}
