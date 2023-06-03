package com.aurx.core.servlets;

import static com.aurx.core.utils.ComponentUtils.CONTENT;
import static com.aurx.core.utils.ComponentUtils.JCR_TITLE;
import static com.aurx.core.utils.ComponentUtils.PATH;
import static com.aurx.core.utils.ComponentUtils.PROPERTY;
import static com.aurx.core.utils.ComponentUtils.PROPERTY_VALUE;
import static com.aurx.core.utils.ComponentUtils.P_LIMIT;
import static com.aurx.core.utils.ComponentUtils.SLING_RESOURCE_TYPE;

import com.aurx.core.utils.QueryBuilderUtilImpl;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.methods=POST",
    "sling.servlet.resourceTypes=vishnu-project/components/component-report",
    "sling.servlet.selectors=component-report", "sling.servlet.extensions=json"
})
public class ComponentReportServlet extends SlingAllMethodsServlet {

  private static final  Logger logger = LoggerFactory.getLogger(ComponentReportServlet.class);

  @Override
  protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    String resourceTypeValue = request.getParameter("resourceType");
    logger.info("this is resource: {}", resourceTypeValue);
    ResourceResolver resourceResolver = request.getResourceResolver();
    Set<String> paths = fetchPageDetails(resourceResolver, resourceTypeValue);
    Gson gson = new Gson();
    response.getWriter().write(gson.toJson(paths));

  }

  private Set<String> fetchPageDetails(ResourceResolver resourceResolver,
      String resourceTypeValue) {

    Set<String> paths = new TreeSet<>();
    Map<String, String> predicateMap = new HashMap<>();
    predicateMap.put(PATH, CONTENT);
    predicateMap.put(PROPERTY, SLING_RESOURCE_TYPE);
    predicateMap.put(PROPERTY_VALUE, resourceTypeValue);
    predicateMap.put(P_LIMIT, "-1");

    QueryBuilderUtilImpl queryBuilderUtil= new QueryBuilderUtilImpl();
    SearchResult result=queryBuilderUtil.getQueryuilderResult(resourceResolver, predicateMap);

    if (result != null) {
      List<Hit> hits = result.getHits();
      for (Hit hit : hits) {
        try {
          Resource pageResource = hit.getResource();
          if (pageResource != null) {
            String path = pageResource.getPath();
            String pagePath = path.split("/jcr:content")[0];
            ValueMap valueMap = pageResource.getValueMap();
            String title = valueMap.get(JCR_TITLE, "");
            String pagePathAndTitle = pagePath + "#" + title;
            logger.info("this is page and title : {}", pagePathAndTitle);
            paths.add(pagePathAndTitle);
          }
        } catch (RepositoryException e) {
          logger.error("exception : {}", e.getMessage());
        }
      }
    }
    return paths;
  }

}
