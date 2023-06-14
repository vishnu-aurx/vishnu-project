package com.aurx.core.models;

import static com.aurx.core.constant.ApplicationConstants.JCR_TITLE;
import static com.aurx.core.constant.ApplicationConstants.jcrContent;
import static com.aurx.core.constant.ApplicationConstants.pageTitleListPath;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * this class is used to fetch the  child page details
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PagePropertieDetailsModel {

    /**
     * resource - Resource object
     */
    @SlingObject
    private Resource resource;

    /**
     * pathResourceList - List of Resource
     */
    @ChildResource(name = "pathData")
    private List<Resource> pathResourceList;

    /**
     * pageTitleList - List of String object
     */
    private List<String> pageTitleList;
    /**
     * logger - Logger object
     */
    private static final Logger logger = LoggerFactory.getLogger(PagePropertieDetailsModel.class);
    /**
     * allPageTitle - List of Map<String, List<String>> object
     */
    private List<Map<String, List<String>>> allPageTitle;

    /**
     * this method is invoked when page is load
     */
    @PostConstruct
    protected void init() {
        boolean isPageResorce = false;
        if (resource != null) {
            if (resource.getResourceType().equals(pageTitleListPath)) {
                isPageResorce = true;
            }
            logger.info("inside the init method isChildResorce : {}", isPageResorce);
        }

        fetchAllPageTitle(isPageResorce);

    }

    /**
     * this method fetch the all page title in a list
     * @param isPageResorce - check page resource is resource type
     */
    private void fetchAllPageTitle(boolean isPageResorce) {
        allPageTitle = new ArrayList<>();
        pageTitleList = new ArrayList<>();
        ResourceResolver resourceResolver = resource.getResourceResolver();
        logger.info("getting the page title ");
        if (pathResourceList != null) {
            for (Resource eachResource : pathResourceList) {
                ValueMap valueMap = eachResource.getValueMap();
                String path = valueMap.get("path", "");
                Resource pageResource = resourceResolver.getResource(path);
                if (pageResource != null && isPageResorce) {
                       Resource childResource = pageResource.getChild(jcrContent);
                        if (childResource != null) {
                            ValueMap childResourceValueMap = childResource.getValueMap();
                            pageTitleList.add(childResourceValueMap.get(JCR_TITLE, ""));
                        }
                    } else if(pageResource != null) {
                         setPageTitle(pageResource);
                       }
                }
            }
            logger.info("set the page title and its sub pages title on list<map<string ,list>> ");
        }

    /**
     * this method used to set page title into a list
     * @param pageResource - the pageResource
     */
    private void setPageTitle(Resource pageResource){
        Iterator<Resource> resourceIterator = pageResource.listChildren();
        String pageTitle = "";
        List<String> childPageTitleList = new ArrayList<>();
        while (resourceIterator.hasNext()) {
            Resource childResource = resourceIterator.next();
            if (childResource.getName().equals(jcrContent)) {
                ValueMap childResourceValueMap = childResource.getValueMap();
                pageTitle = childResourceValueMap.get(JCR_TITLE, "");
            } else {
                Resource jcrContentChildResource = childResource.getChild(jcrContent);
                if (jcrContentChildResource != null) {
                    ValueMap jcrContentChildResourceValueMap = jcrContentChildResource.getValueMap();
                    childPageTitleList.add(jcrContentChildResourceValueMap.get(JCR_TITLE, ""));
                }
            }
        }
        if (!pageTitle.equals("")) {
            Map<String, List<String>> pageTitleMap = new HashMap<>();
            pageTitleMap.put(pageTitle, childPageTitleList);
            allPageTitle.add(pageTitleMap);
        }
    }

    /**
     * this method return the allPageTitle list
     * @return -allPageTitle
     */
    public List<Map<String, List<String>>> getAllPageTitleList() {
        return this.allPageTitle;
    }

    /**
     * this method return the pageTitleList
     * @return -pageTitleList
     */
    public List<String> getPageTitleList() {
        return this.pageTitleList;
    }

}
