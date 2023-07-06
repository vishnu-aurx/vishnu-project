package com.aurx.core.models;

import static com.aurx.core.constant.ApplicationConstants.PAGE_TITLE_LIST_COMPONENT_RESOURCE_TYPE;
import static com.aurx.core.constant.ApplicationConstants.PATH;
import static com.aurx.core.constant.ApplicationConstants.PATH_DATA;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static com.day.cq.commons.jcr.JcrConstants.JCR_TITLE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to fetch the child page details.
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PagePropertiesDetailsModel {

  /**
   * resource - Resource object.
   */
  @SlingObject
  private Resource resource;

  /**
   * pathResourceList - List of Resource.
   */
  @ChildResource(name = PATH_DATA)
  private List<Resource> pathResourceList;

  /**
   * pageTitleList - List of page titles.
   */
  private List<String> pageTitleList;

  /**
   * logger - Logger object.
   */
  private static final Logger logger = LoggerFactory.getLogger(PagePropertiesDetailsModel.class);

  /**
   * allPageTitle - Map contains parent page title as key and list of child page titles as value.
   */
  private List<Map<String, List<String>>> allPageTitle;

  /**
   * This method is invoked on the object initialization.
   */
  @PostConstruct
  protected void init() {
    logger.info("init method start");
    boolean isPageTitleListComponentResource = false;
    if (resource != null) {
      logger.info("resource is not null");
      if (resource.getResourceType().equals(PAGE_TITLE_LIST_COMPONENT_RESOURCE_TYPE)) {
        isPageTitleListComponentResource = true;
      }
      logger.info("inside the init method isPageTitleListComponentResource : {}",
          isPageTitleListComponentResource);
      fetchAllPageTitle(isPageTitleListComponentResource);
    }
  }

  /**
   * This method fetches all the page titles in a list.
   *
   * @param isPageTitleListComponentResource - check page resource is resource type
   */
  private void fetchAllPageTitle(boolean isPageTitleListComponentResource) {
    logger.info("Start of fetchAllPageTitle method with isPageTitleListComponentResource : {}",
        isPageTitleListComponentResource);
    allPageTitle = new ArrayList<>();
    pageTitleList = new ArrayList<>();
    ResourceResolver resourceResolver = null;
    if(resource != null) {
      resourceResolver = resource.getResourceResolver();
    }
    if (pathResourceList != null && resourceResolver != null) {
      logger.info("pathResourceList is not null ");
      for (Resource eachResource : pathResourceList) {
        ValueMap valueMap = eachResource.getValueMap();
        String path = valueMap.get(PATH, EMPTY);
        Resource pageResource = resourceResolver.getResource(path);
        if (pageResource != null && isPageTitleListComponentResource) {
          populatePageTitle(pageResource);
        } else if (pageResource != null) {
          setPageTitle(pageResource);
        }
      }
    }
    logger.info("End of fetchAllPageMethod with pageTitleList :{}",pageTitleList);
  }

  /**
   * This method is used to set page titles in a list.
   *
   * @param pageResource - the pageResource
   */
  private void setPageTitle(Resource pageResource) {
    logger.info("Start of setPageTitle method with pageResource: {}", pageResource);
    Iterator<Resource> resourceIterator = pageResource.listChildren();
    String pageTitle = EMPTY;
    List<String> childPageTitleList = new ArrayList<>();
    while (resourceIterator.hasNext()) {
      Resource childResource = resourceIterator.next();
      if (childResource.getName().equals(JCR_CONTENT)) {
        ValueMap childResourceValueMap = childResource.getValueMap();
        pageTitle = childResourceValueMap.get(JCR_TITLE, EMPTY);
      } else {
        Resource jcrContentChildResource = childResource.getChild(JCR_CONTENT);
        if (jcrContentChildResource != null) {
          ValueMap jcrContentChildResourceValueMap = jcrContentChildResource.getValueMap();
          childPageTitleList.add(jcrContentChildResourceValueMap.get(JCR_TITLE, EMPTY));
        }
      }
    }
    logger.info("inside the setPageTitle method pageTitle : {}", pageTitle);
    if (StringUtils.isNotBlank(pageTitle)) {
      Map<String, List<String>> pageTitleMap = new HashMap<>();
      pageTitleMap.put(pageTitle, childPageTitleList);
      allPageTitle.add(pageTitleMap);
    }
    logger.info("End of allPageTitle method with allPageTitle: {}", allPageTitle);
  }

  /**
   * This method populates the page title and adds it to a list.
   *
   * @param pageResource - The pageResource
   */
  private void populatePageTitle(Resource pageResource){
    logger.info("Start of populatePageTitle method with  pageResource : {}",pageResource);
    Resource childResource = pageResource.getChild(JCR_CONTENT);
    if (childResource != null) {
      ValueMap childResourceValueMap = childResource.getValueMap();
      String childTitle = childResourceValueMap.get(JCR_TITLE, EMPTY);
      if(StringUtils.isNotBlank(childTitle)){
        pageTitleList.add(childTitle);
      }
    }
    logger.info("End of populatePageTitle method with pageTitleList: {}",pageTitleList);
  }

  /**
   * This method returns the allPageTitle list.
   *
   * @return -allPageTitle
   */
  public List<Map<String, List<String>>> getAllPageTitleList() {
    return this.allPageTitle;
  }

  /**
   * This method returns the pageTitleList.
   *
   * @return -pageTitleList
   */
  public List<String> getPageTitleList() {
    return this.pageTitleList;
  }

}
