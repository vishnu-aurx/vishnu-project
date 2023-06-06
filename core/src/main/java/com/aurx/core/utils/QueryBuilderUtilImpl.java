package com.aurx.core.utils;

import com.aurx.core.services.QueryBuilderUtil;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import java.util.Map;
import javax.jcr.Session;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = QueryBuilderUtil.class, immediate = true)
public class QueryBuilderUtilImpl implements QueryBuilderUtil {

  /**
   * LOGGER - Logger Object
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(QueryBuilderUtilImpl.class);

  /**
   * this method is used to generate the search result
   *
   * @param resourceResolver
   * @param predicateMap
   * @return = SearchResult object
   */
  @Override
  public SearchResult getQueryuilderResult(ResourceResolver resourceResolver,
      Map<String, String> predicateMap) {
    LOGGER.info("SearchResult method start   resourceResolver : {} ,predicateMap :{}",
        resourceResolver, predicateMap);
    QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
    SearchResult result = null;
    if (builder != null) {
      Query query =
          builder.createQuery(PredicateGroup.create(predicateMap),
              resourceResolver.adaptTo(Session.class));
      result = query.getResult();
    }
    LOGGER.info("SearchResult Method ends Result : {}", result);
    return result;

  }
}
