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

/**
 * This model generate the ComponentReport
 */
@Model(adaptables =  Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ComponentReportModel {

  /**
   * The ComponentReportService object of {@link ComponentReportService}
   */
  @OSGiService
  private ComponentReportService componentReportService;
  /**
   * The comopnentHierachyList - it used to returning the pojo class object into slightly
   */
   private List<ComponentReport> comopnentHierachyList;
  /**
   * The logger - logger  object
   */
  private static  final Logger logger= LoggerFactory.getLogger(ComponentReportModel.class);

  /**
   * The method is used to add
   */
  @PostConstruct
  protected void init(){
    comopnentHierachyList = new ArrayList<>();
    logger.info("inside the init method");
    comopnentHierachyList=componentReportService.comopnentList();
    logger.info("init method end comopnentHierachyList : {}",comopnentHierachyList);

  }
  public List<ComponentReport> comopnentHierachyList(){
    return comopnentHierachyList;
  }
  }
