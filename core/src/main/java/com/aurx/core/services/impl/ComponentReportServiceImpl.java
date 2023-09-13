package com.aurx.core.services.impl;


import static com.aurx.core.constant.ApplicationConstants.COMPONENT_GROUP;
import static com.aurx.core.constant.ApplicationConstants.COMPONENT_PATH;
import static com.aurx.core.constant.ApplicationConstants.CQ_COMPONENT;
import static com.aurx.core.constant.ApplicationConstants.JCR_TITLE;
import static com.aurx.core.constant.ApplicationConstants.MINUS_1;
import static com.aurx.core.constant.ApplicationConstants.PATH;
import static com.aurx.core.constant.ApplicationConstants.P_LIMIT;
import static com.aurx.core.constant.ApplicationConstants.TITLE;
import static com.aurx.core.constant.ApplicationConstants.TYPE;
import static com.aurx.core.utils.ResolverUtils.getResourceResolver;

import com.aurx.core.pojo.ComponentReport;
import com.aurx.core.services.ComponentReportService;
import com.aurx.core.services.QueryBuilderUtil;
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
 * The ComponentReportServiceImpl class is used to fetch components
 */
@Component(service = ComponentReportService.class, immediate = true)
public class ComponentReportServiceImpl implements ComponentReportService {

  /**
   * logger - Logger object
   */
  private static final Logger logger = LoggerFactory.getLogger(ComponentReportServiceImpl.class);

  /**
   * componentHierarchyList - The List of  ComponentReport.
   */
  private List<ComponentReport> componentHierarchyList;

  /**
   * resourceResolverFactory - ResourceResolverFactory object
   */
  @Reference
  private ResourceResolverFactory resourceResolverFactory;

  /**
   * queryBuilderUtil - The QueryBuilderUtil object.
   */
  @Reference
  private QueryBuilderUtil queryBuilderUtil;

  /**
   * This method is invoked when the service is activated.
   */
  @Activate
  @Modified
  protected void activate() {
    logger.info("Start of activate method");
    fetchTitle();
    logger.info("End of activate method");

  }

  /**
   * This method fetches the component resource list from QueryBuilder.
   *
   * @return - The resourceList.
   */
  @Override
  public List<Resource> fetchComponents() {
    logger.info("Start of FetchComponents method");
    List<Resource> resourceList = new ArrayList<>();
    ResourceResolver resourceResolver = null;
    try {
      resourceResolver = getResourceResolver(resourceResolverFactory);
    } catch (LoginException e) {
      logger.error("Exception : {}", e.getMessage());
    }
    Map<String, String> predicateMap = new HashMap<>();
    predicateMap.put(PATH, COMPONENT_PATH);
    predicateMap.put(TYPE, CQ_COMPONENT);
    predicateMap.put(P_LIMIT, MINUS_1);
    if (resourceResolver != null) {
      logger.info("resourceResolver is not null");
      SearchResult result = queryBuilderUtil.getQueryBuilderResult(resourceResolver, predicateMap);
      logger.info("Search result  : {}", result);
      if (result != null) {
        logger.info("result is not null");
        List<Hit> hits = result.getHits();
        for (Hit hit : hits) {
          try {
            resourceList.add(hit.getResource());
          } catch (RepositoryException e) {
            logger.error("Exception : {}", e.getMessage());
          }
        }
      }
    }
    logger.info("FetchComponents method end resourceList : {}  ", resourceList);
    return resourceList;
  }

  /**
   * This method fetches the title of the component.
   */
  public void fetchTitle() {
    logger.info("Fetch title method start");
    componentHierarchyList = new ArrayList<>();
    List<Resource> resources = fetchComponents();
    for (Resource componetsResource : resources) {
      if (componetsResource != null) {
        ValueMap valueMap = componetsResource.getValueMap();
        String title = valueMap.get(JCR_TITLE, TITLE);
        String componentsGroup = valueMap.get(COMPONENT_GROUP, COMPONENT_GROUP);
        String componentPath = componetsResource.getPath();
        componentHierarchyList.add(new ComponentReport(title, componentsGroup, componentPath));
      }
    }
    componentHierarchyList.sort(new ComponentReport());
    logger.info("fetch title methode end  componentHierarchyList : {}",
        componentHierarchyList);
  }

  /**
   * This method return the List of Component report
   *
   * @return - The componentHierarchyList
   */
  public List<ComponentReport> comopnentList() {
    logger.info("this is component list :{}", componentHierarchyList);
    return this.componentHierarchyList;
  }
}
