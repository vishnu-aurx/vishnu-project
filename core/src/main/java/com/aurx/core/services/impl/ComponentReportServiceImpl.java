package com.aurx.core.services.impl;

import static com.aurx.core.utils.ResolverUtils.getResourceResolver;

import com.aurx.core.pojo.ComponentReport;
import com.aurx.core.services.ComponentReportService;
import com.aurx.core.services.config.MoviesConfiguration;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ComponentReportService.class, immediate = true)
public class ComponentReportServiceImpl implements ComponentReportService {
final Logger logger = LoggerFactory.getLogger(ComponentReportServiceImpl.class);
private List<ComponentReport> comopnentHierachyList;

  @Reference
  private ResourceResolverFactory resourceResolverFactory;
  @Activate
  protected void activate(MoviesConfiguration moviesConfiguration) {
    logger.info("inside the active methode component  service : ");
    fetchTitle();

  }
  @Modified
  protected void modified(MoviesConfiguration moviesConfiguration) {
    logger.info("inside the modified methode component  service : ");
    fetchTitle();

  }
  @Override
  public List<Resource> fetchComponents() {
    List<Resource> resourceList = new ArrayList<>();

      ResourceResolver resourceResolver = null;
      try {
        resourceResolver = getResourceResolver(resourceResolverFactory);
      } catch (LoginException e) {
        logger.info("this is  Exception : {}",e.getMessage());
      }

      Map<String, String> predicateMap = new HashMap<>();
      predicateMap.put("path", "/apps/vishnu-project/components");
      predicateMap.put("type", "cq:Component");
      predicateMap.put("p.limit", "-1");
      if(resourceResolver !=null) {
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
              resourceList.add(hit.getResource());
            } catch (RepositoryException e) {
              logger.error("exception : {}", e.getMessage());
            }
          }
        }
      }
    return resourceList;
  }

 public void fetchTitle(){
     comopnentHierachyList = new ArrayList<>();
    List<Resource> resources = fetchComponents();
    for (Resource componetsResource:resources) {
      if(componetsResource !=null) {
        ValueMap valueMap = componetsResource.getValueMap();
        String title = valueMap.get("jcr:title", "title");
        String componentsGroup = valueMap.get("componentGroup", "component group");
        String componentPath=componetsResource.getPath();
        comopnentHierachyList.add(new ComponentReport(title,componentsGroup,componentPath));
      }
    }
    comopnentHierachyList.sort(new ComponentReport());
  }
  public List<ComponentReport> comopnentList(){
    logger.info("this is component list :{}",comopnentHierachyList);
    return this.comopnentHierachyList;
  }

}
