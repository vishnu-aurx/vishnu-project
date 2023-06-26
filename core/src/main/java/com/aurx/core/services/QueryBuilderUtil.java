package com.aurx.core.services;

import com.day.cq.search.result.SearchResult;
import java.util.Map;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * Interface QueryBuilderUtil.
 */
public interface QueryBuilderUtil {

  /**
   * This method return the search result.
   *
   * @param resourceResolver - The resourceResolver.
   * @param predicateMap     - The predicateMap.
   * @return - The result.
   */
  SearchResult getQueryBuilderResult(ResourceResolver resourceResolver,
      Map<String, String> predicateMap);
}
