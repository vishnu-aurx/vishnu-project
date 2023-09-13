package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.APPLICATION_JSON;
import static com.aurx.core.constant.ApplicationConstants.LIST;
import static com.aurx.core.constant.ApplicationConstants.PROPERTY;
import static com.aurx.core.constant.ApplicationConstants.PROPERTY_VALUE;
import static com.aurx.core.constant.ApplicationConstants.SLING_RESOURCE_TYPE;
import static com.aurx.core.constant.ApplicationConstants.WE_RETAIL_TEASER_SLING_RESOURCE;

import com.aurx.core.services.QueryBuilderUtil;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class generates the query response..
 */
@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.paths=/bin/searchNew",
    "sling.servlet.methods=GET"
})
public class QueryBuilderServlet extends SlingSafeMethodsServlet {

  /**
   * queryBuilderUtil - QueryBuilderUtil Object
   */
  @Reference
  private transient QueryBuilderUtil queryBuilderUtil;

  /**
   * logger - Logger  object
   */
  private static final Logger logger = LoggerFactory.getLogger(QueryBuilderServlet.class);

  /**
   * This method generates the query response.
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    logger.info("start of doGet method");
    ResourceResolver resourceResolver = request.getResourceResolver();
    JsonObject jsonObject = new JsonObject();
    JsonArray jsonArray = new JsonArray();
    Map<String, String> predicateMap = new HashMap<>();
    predicateMap.put(PROPERTY, SLING_RESOURCE_TYPE);
    predicateMap.put(PROPERTY_VALUE, WE_RETAIL_TEASER_SLING_RESOURCE);
    SearchResult result = queryBuilderUtil.getQueryBuilderResult(resourceResolver, predicateMap);
    if (result != null) {
      logger.info("result is not null");
      List<Hit> hits = result.getHits();
      for (Hit hit : hits) {
        try {
          jsonArray.add(hit.getResource().getPath());
        } catch (RepositoryException e) {
          logger.error("Exception : {}",e.getMessage());
        }
      }
    }
    jsonObject.add(LIST, jsonArray);
    response.setContentLength(jsonObject.toString().getBytes().length);
    response.setContentType(APPLICATION_JSON);
    response.getOutputStream().write(jsonObject.toString().getBytes());
    logger.info("End of doGet method with json object : {}",jsonObject);
  }

}
