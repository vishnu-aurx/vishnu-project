package com.aurx.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.paths=/bin/searchNew",
    "sling.servlet.methods=GET"
})
public class QueryBuilderServlet extends SlingSafeMethodsServlet {

  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
    ResourceResolver resourceResolver = request.getResourceResolver();
    JsonObject jsonObject = new JsonObject();
    JsonArray jsonArray = new JsonArray();

    String searchKeyword = request.getParameter("keyword");
    Map<String, String> predicateMap = new HashMap<>();
    predicateMap.put("fulltext", searchKeyword);
    predicateMap.put("path", "/content/we-retail/language-masters");
    predicateMap.put("type", "cq:Page");

    QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
    SearchResult result = null;
    if (builder != null) {
      Query query =
          builder.createQuery(PredicateGroup.create(predicateMap), resourceResolver.adaptTo(Session.class));
      result = query.getResult();
    }


    if(result != null) {
      List<Hit> hits = result.getHits();
      for(Hit hit : hits) {
        try {
          jsonArray.add(hit.getResource().getPath());
        } catch (RepositoryException e) {

        }
      }
    }
    jsonObject.add("results", jsonArray);
response.getWriter().write(jsonObject.toString());
  }

}
