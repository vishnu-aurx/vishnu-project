package com.aurx.core.models;

import com.aurx.core.pojo.ComponentReport;
import com.aurx.core.services.ComponentReportService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables =  Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ComponentReportModel {
  @OSGiService
  private ComponentReportService componentReportService;
   private List<ComponentReport> comopnentHierachyList;
  private static  final Logger logger= LoggerFactory.getLogger(ComponentReportModel.class);
  @PostConstruct
  protected void init(){
    comopnentHierachyList = new ArrayList<>();
    logger.info("============inside the init method===========");
    comopnentHierachyList=componentReportService.comopnentList();
    logger.info("============init method end ===========comopnentHierachyList : {}",comopnentHierachyList);

  }
  public List<ComponentReport> comopnentHierachyList(){
    return comopnentHierachyList;
  }
  }
