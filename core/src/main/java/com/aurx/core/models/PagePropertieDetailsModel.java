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
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PagePropertieDetailsModel {
    @SlingObject
    private Resource resource;

    @ChildResource(name = "pathData")
    private List<Resource> pathResourceList;
    private List<String> pageTitleList;
    private final Logger logger = LoggerFactory.getLogger(ProductDetailsModel.class);

    @PostConstruct
    protected void init() {
        setPageTitleList();
    }

    private void setPageTitleList() {
        pageTitleList = new ArrayList();
        ResourceResolver resourceResolver = resource.getResourceResolver();
        logger.info("setting page title list");
        if (pathResourceList != null) {
            for (Resource eachResource : pathResourceList) {
                ValueMap valueMap = eachResource.getValueMap();
                String path = valueMap.get("path", "");
                Resource pageResource = resourceResolver.getResource(path);
                if (pageResource != null) {
                    Resource childResource = pageResource.getChild("jcr:content");
                    if (childResource != null) {
                        ValueMap childResourceValueMapValueMap = childResource.getValueMap();
                        pageTitleList.add(childResourceValueMapValueMap.get("jcr:title", ""));
                    }
                }
            }
        }
    }

    public List<String> getPageTitleList() {
        return this.pageTitleList;
    }

}
