package com.aurx.core.services.impl;


import static com.aurx.core.utils.ComponentUtils.COMPONENT_PATH;
import static com.aurx.core.utils.ComponentUtils.CQ_COMPONENT;
import static com.aurx.core.utils.ComponentUtils.JCR_TITLE;
import static com.aurx.core.utils.ComponentUtils.PATH;
import static com.aurx.core.utils.ComponentUtils.P_LIMIT;
import static com.aurx.core.utils.ComponentUtils.TYPE;
import static com.aurx.core.utils.ResolverUtils.getResourceResolver;

import com.aurx.core.pojo.ComponentReport;
import com.aurx.core.services.ComponentReportService;
import com.aurx.core.utils.QueryBuilderUtilImpl;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jcr.RepositoryException;
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

/**
 * {@link ComponentReportService is used to fetch the data from crx }
 */
@Component(service = ComponentReportService.class, immediate = true)
public class ComponentReportServiceImpl implements ComponentReportService {
private static final Logger logger = LoggerFactory.getLogger(ComponentReportServiceImpl.class);
private List<ComponentReport> comopnentHierachyList;

  @Reference
  private ResourceResolverFactory resourceResolverFactory;
  @Activate
  protected void activate() {
    logger.info("inside the active methode component  service : ");
    fetchTitle();

  }
  @Modified
  protected void modified() {
    logger.info("inside the modified methode component  service : ");
    fetchTitle();

  }

  /**
   * This Method  Fetch the components Resource List
   * @return =resourceList
   */
  @Override
  public List<Resource> fetchComponents() {
    logger.info("===========fetchComponents methode start================");
    List<Resource> resourceList = new ArrayList<>();

      ResourceResolver resourceResolver = null;
      try {
        resourceResolver = getResourceResolver(resourceResolverFactory);
      } catch (LoginException e) {
        logger.info("================Exception : {}",e.getMessage());
      }

      Map<String, String> predicateMap = new HashMap<>();
      predicateMap.put(PATH, COMPONENT_PATH);
      predicateMap.put(TYPE, CQ_COMPONENT);
      predicateMap.put(P_LIMIT, "-1");
      if(resourceResolver !=null) {
        QueryBuilderUtilImpl queryBuilderUtil= new QueryBuilderUtilImpl();
        SearchResult result=queryBuilderUtil.getQueryuilderResult(resourceResolver, predicateMap);
        logger.info("============search result ============= : {}",result);
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
      logger.info("================fetchComponents method end ====resourceList : {}  ",resourceList);
    return resourceList;
  }

  /**
   * This method fetch the  jcr:title of the component
   */
 public void fetchTitle(){
   logger.info("==========fetch title method statrt =================");
     comopnentHierachyList = new ArrayList<>();
    List<Resource> resources = fetchComponents();
    for (Resource componetsResource:resources) {
      if(componetsResource !=null) {
        ValueMap valueMap = componetsResource.getValueMap();
        String title = valueMap.get(JCR_TITLE, "title");
        String componentsGroup = valueMap.get("componentGroup", "component group");
        String componentPath=componetsResource.getPath();
        comopnentHierachyList.add(new ComponentReport(title,componentsGroup,componentPath));
      }
    }
    comopnentHierachyList.sort(new ComponentReport());
    logger.info("================fetch title methode end ====== comopnentHierachyList : {}",comopnentHierachyList);
  }

  /**
   * This method return the List of Component report
   * @return = comopnentHierachyList
   */
  public List<ComponentReport> comopnentList(){
    logger.info("this is component list :{}",comopnentHierachyList);
    return this.comopnentHierachyList;
  }

}
