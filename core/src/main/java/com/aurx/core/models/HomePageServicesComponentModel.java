package com.aurx.core.models;

import static com.aurx.core.constant.ApplicationConstants.FILE_REFERENCE;
import static com.aurx.core.constant.ApplicationConstants.IMAGE_LABEL;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.aurx.core.pojo.HomePageServicesComponent;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to fetch the image path and image label.
 */
@Model(adaptables = {Resource.class,
    SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HomePageServicesComponentModel {

  /**
   * title - The title.
   */
  @ValueMapValue
  private String title;
  /**
   * multiFieldResourceList - List of Resources
   */
  @ChildResource(name = "fieldData")
  private List<Resource> multiFieldResourceList;
  /**
   * logger - Logger object.
   */
  private static final Logger logger = LoggerFactory.getLogger(
      HomePageServicesComponentModel.class);
  /**
   * homePageServicesComponentList - List of HomePageServicesComponent object.
   */
  private List<HomePageServicesComponent> homePageServicesComponentList;

  /**
   * This method is invoked on the object initialization.
   */
  @PostConstruct
  protected void init() {
    logger.info("Start of init method");
    setHomePageServicesComponents();
    logger.info("End of init method");
  }

  /**
   * This method sets the HomePageServicesComponent object into a list.
   */
  public void setHomePageServicesComponents() {
    logger.info("Start of setHomePageServicesComponents");
    String image;
    String imageLabel;
    homePageServicesComponentList = new ArrayList<>();
    if (multiFieldResourceList != null) {
      logger.info("resource is not null");
      for (Resource eachResource : multiFieldResourceList) {
        image = eachResource.getValueMap().get(FILE_REFERENCE, EMPTY);
        imageLabel = eachResource.getValueMap().get(IMAGE_LABEL, EMPTY);
        HomePageServicesComponent homePageServicesComponent = new HomePageServicesComponent(image,
            imageLabel);
        homePageServicesComponentList.add(homePageServicesComponent);
      }
    }
    logger.info(
        "End of setHomePageServicesComponents method with homePageServicesComponentList : {}",
        homePageServicesComponentList);
  }

  /**
   * This method returns the homePageServicesComponentList.
   *
   * @return - homePageServicesComponentList
   */
  public List<HomePageServicesComponent> getHomePageServicesComponentList() {
    return homePageServicesComponentList;
  }

  /**
   * This method return title.
   *
   * @return - title
   */
  public String getTitle() {
    return title;
  }
}
