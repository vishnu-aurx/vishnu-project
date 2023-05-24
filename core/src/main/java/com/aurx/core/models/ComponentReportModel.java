package com.aurx.core.models;

import com.aurx.core.pojo.ComponentReport;
import com.aurx.core.services.ComponentReportService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables =  Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ComponentReportModel {
  @OSGiService
  private ComponentReportService componentReportService;
  @SlingObject
  private Resource resource;
  private List<Resource> resourceList;
  private List<ComponentReport> comopnentHierachyList;
  private final Logger logger= LoggerFactory.getLogger(ComponentReportModel.class);
  @PostConstruct
  protected void init(){
    comopnentHierachyList = new ArrayList<>();
    logger.info("inside the init method");
    comopnentHierachyList=componentReportService.comopnentList();

  }
  public List<ComponentReport> comopnentHierachyList(){
    return comopnentHierachyList;
  }

}
