package com.aurx.core.services;

import com.day.cq.search.result.SearchResult;
import java.util.Map;
import org.apache.sling.api.resource.ResourceResolver;

public interface QueryBuilderUtil {
   SearchResult getQueryuilderResult(ResourceResolver resourceResolver, Map<String, String> predicateMap);
}
