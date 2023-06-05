package com.aurx.core.services.impl;


import static com.aurx.core.constant.ApplicationConstants.COMPONENT_PATH;
import static com.aurx.core.constant.ApplicationConstants.CQ_COMPONENT;
import static com.aurx.core.constant.ApplicationConstants.JCR_TITLE;
import static com.aurx.core.constant.ApplicationConstants.PATH;
import static com.aurx.core.constant.ApplicationConstants.P_LIMIT;
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
 * {@link ComponentReportService is used to fetch the data from crx }
 */
@Component(service = ComponentReportService.class, immediate = true)
public class ComponentReportServiceImpl implements ComponentReportService {

  /**
   * logger - Logger object
   */
  private static final Logger logger = LoggerFactory.getLogger(ComponentReportServiceImpl.class);
  /**
   * Create the List Of pojo class ComponentReport Object
   */
  private List<ComponentReport> comopnentHierachyList;

  /**
   * resourceResolverFactory - ResourceResolverFactory object
   */
  @Reference
  private ResourceResolverFactory resourceResolverFactory;
  @Reference
  QueryBuilderUtil queryBuilderUtil;

  /**
   * method start when we deploy the bundle in aem so in this method we fetch the title of
   * components
   */
  @Activate
  @Modified
  protected void activate() {
    logger.info("inside the active methode component  service : ");
    fetchTitle();

  }

  /**
   * This Method  Fetch the components Resource List from QueryBuilder
   *
   * @return =resourceList
   */
  @Override
  public List<Resource> fetchComponents() {
    logger.info("FetchComponents methode start");
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
    predicateMap.put(P_LIMIT, "-1");
    if (resourceResolver != null) {
      SearchResult result = queryBuilderUtil.getQueryuilderResult(resourceResolver, predicateMap);
      logger.info("Search result  : {}", result);
      if (result != null) {
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
   * This method fetch the  jcr:title of the component
   */
  public void fetchTitle() {
    logger.info("Fetch title method start");
    comopnentHierachyList = new ArrayList<>();
    List<Resource> resources = fetchComponents();
    for (Resource componetsResource : resources) {
      if (componetsResource != null) {
        ValueMap valueMap = componetsResource.getValueMap();
        String title = valueMap.get(JCR_TITLE, "title");
        String componentsGroup = valueMap.get("componentGroup", "component group");
        String componentPath = componetsResource.getPath();
        comopnentHierachyList.add(new ComponentReport(title, componentsGroup, componentPath));
      }
    }
    comopnentHierachyList.sort(new ComponentReport());
    logger.info("================fetch title methode end ====== comopnentHierachyList : {}",
        comopnentHierachyList);
  }

  /**
   * This method return the List of Component report
   *
   * @return = comopnentHierachyList
   */
  public List<ComponentReport> comopnentList() {
    logger.info("this is component list :{}", comopnentHierachyList);
    return this.comopnentHierachyList;
  }
}
