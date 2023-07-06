package com.aurx.core.models;

import com.aurx.core.pojo.ComponentReport;
import com.aurx.core.services.ComponentReportService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This model generates the component report.
 */
@Model(adaptables = {Resource.class,
    SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ComponentReportModel {

  /**
   * componentReportService - The componentReportService.
   */
  @OSGiService
  private ComponentReportService componentReportService;

  /**
   * componentReportList - The componentReportList.
   */
  private List<ComponentReport> componentReportList;

  /**
   * The logger - logger  object.
   */
  private static final Logger logger = LoggerFactory.getLogger(ComponentReportModel.class);

  /**
   * The method is used to add.
   */
  @PostConstruct
  protected void init() {
    componentReportList = new ArrayList<>();
    logger.info("inside the init method");
    componentReportList = componentReportService.comopnentList();
    logger.info("init method end comopnentHierachyList : {}", componentReportList);

  }

  /**
   * This method return the list of componentReport.
   *
   * @return List of componentReport.
   */
  public List<ComponentReport> getComponentReportList() {
    return componentReportList;
  }
}
