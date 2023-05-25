package com.aurx.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
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
import javax.jcr.Session;
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

  private final Logger logger = LoggerFactory.getLogger(ComponentReportServlet.class);

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
    predicateMap.put("path", "/content");
    predicateMap.put("property", "sling:resourceType");
    predicateMap.put("property.value", resourceTypeValue);
    predicateMap.put("p.limit", "-1");
    QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
    SearchResult result = null;
    if (builder != null) {
      Query query =
          builder.createQuery(PredicateGroup.create(predicateMap),
              resourceResolver.adaptTo(Session.class));
      result = query.getResult();
    }
    if (result != null) {
      List<Hit> hits = result.getHits();
      for (Hit hit : hits) {
        try {
          Resource pageResource = hit.getResource();
          if (pageResource != null) {
            String path = pageResource.getPath();
            String pagePath = path.split("/jcr:content")[0];
            ValueMap valueMap = pageResource.getValueMap();
            String title = valueMap.get("jcr:title", "");
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
