package com.aurx.core.models;

import com.aurx.core.pojo.HomePageServicesComponent;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is used to fetch image path and image label
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HomePageServicesComponentModel {

  /**
   * title - String object
   */
  @Inject
  private String title;
  /**
   * multiFieldResourceList - List of Resources
   */
  @ChildResource(name = "fieldData")
  private List<Resource> multiFieldResourceList;
  /**
   * logger - Logger object
   */
  private static final Logger logger = LoggerFactory.getLogger(HomePageServicesComponentModel.class);
  /**
   * homePageServicesComponentList - List of HomePageServicesComponent object
   */
  private List<HomePageServicesComponent> homePageServicesComponentList;

  /**
   * this method is invoked when page is loaded
   */
  @PostConstruct
  protected void init() {
    logger.info("init method start");
    setHomePageServicesComponents();
    setHomePageServicesComponents();

  }

  /**
   * this method set HomePageServicesComponent object  into list
   */
  public void setHomePageServicesComponents() {
    logger.info(" setHomePageServicesComponents method start ");
    String image;
    String imageLabel;
    homePageServicesComponentList = new ArrayList<>();

    if (multiFieldResourceList != null) {
      logger.info("resource is not null");
      for (Resource eachResource : multiFieldResourceList) {
        image = eachResource.getValueMap().get("fileReference", "");
        imageLabel = eachResource.getValueMap().get("imageLabel", "");
        HomePageServicesComponent homePageServicesComponent = new HomePageServicesComponent(image,
            imageLabel);
        homePageServicesComponentList.add(homePageServicesComponent);
      }
    }
    logger.info("setHomePageServicesComponents method end ");
  }

  /**
   * this method return homePageServicesComponentList
   * @return - homePageServicesComponentList
   */
  public List<HomePageServicesComponent> getHomePageServicesComponentList() {
    return homePageServicesComponentList;
  }

  /**
   * this method return String object
   * @return - title
   */
  public String getTitle() {
    return title;
  }
}
