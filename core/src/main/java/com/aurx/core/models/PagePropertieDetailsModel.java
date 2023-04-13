package com.aurx.core.models;

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

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PagePropertieDetailsModel {
    @SlingObject
    private Resource resource;

    @ChildResource(name = "pathData")
    private List<Resource> pathResourceList;
    private List<String> pageTitleList;
    private final Logger logger = LoggerFactory.getLogger(ProductDetailsModel.class);

    private List<Map<String, List<String>>> allPageTitle;

    @PostConstruct
    protected void init() {
        boolean isPageResorce = false;
        if (resource != null) {
            if (resource.getResourceType().equals("vishnu-project/components/pageTitleList")) {
                isPageResorce = true;
            }
            logger.info("inside the init method isChildResorce : {}", isPageResorce);
        }

        setAllPageTitle(isPageResorce);

    }


    private void setAllPageTitle(boolean isPageResorce) {
        allPageTitle = new ArrayList<>();
        pageTitleList = new ArrayList();
        ResourceResolver resourceResolver = resource.getResourceResolver();
        logger.info("getting the page title ");
        if (pathResourceList != null) {
            for (Resource eachResource : pathResourceList) {
                ValueMap valueMap = eachResource.getValueMap();
                String path = valueMap.get("path", "");
                Resource pageResource = resourceResolver.getResource(path);
                if (pageResource != null) {
                    if (isPageResorce) {
                        Resource childResource = pageResource.getChild("jcr:content");
                        if (childResource != null) {
                            ValueMap childResourceValueMap = childResource.getValueMap();
                            pageTitleList.add(childResourceValueMap.get("jcr:title", ""));
                        }
                    } else {

                        Iterator<Resource> resourceIterator = pageResource.listChildren();
                        String pageTitle = "";
                        List<String> childPageTitleList = new ArrayList();
                        while (resourceIterator.hasNext()) {
                            Resource childResource = resourceIterator.next();
                            if (childResource.getName().equals("jcr:content")) {
                                ValueMap childResourceValueMap = childResource.getValueMap();
                                pageTitle = childResourceValueMap.get("jcr:title", "");
                            } else {
                                Resource jcrContentChildResource = childResource.getChild("jcr:content");
                                if (jcrContentChildResource != null) {
                                    ValueMap jcrContentChildResourceValueMap = jcrContentChildResource.getValueMap();
                                    childPageTitleList.add(jcrContentChildResourceValueMap.get("jcr:title", ""));
                                }
                            }
                        }
                        if (!pageTitle.equals("")) {
                            Map<String, List<String>> pageTitleMap = new HashMap<>();
                            pageTitleMap.put(pageTitle, childPageTitleList);
                            allPageTitle.add(pageTitleMap);
                        }
                    }
                }
            }
            logger.info("set the page title and its sub pages title on list<map<string ,list>> ");
        }
    }

    public List<Map<String, List<String>>> getAllPageTitleList() {
        return this.allPageTitle;
    }

    public List<String> getPageTitleList() {
        return this.pageTitleList;
    }

}
