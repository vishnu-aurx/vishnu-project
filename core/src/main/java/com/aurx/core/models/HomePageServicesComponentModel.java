package com.aurx.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HomePageServicesComponentModel {
    @Inject
    String title;
@ChildResource(name = "fieldData")
   private List<Resource> multiFieldResourceList;
    private final Logger logger= LoggerFactory.getLogger(LinkExtractModel.class);
private List<HomePageServicesComponent>  homePageServicesComponentList;

    @PostConstruct
    protected void init() {
      logger.info("init method start");
        setHomePageServicesComponents();
        setHomePageServicesComponents();

    }

    public void setHomePageServicesComponents() {
        String image;
        String imageLabel;
        homePageServicesComponentList=new ArrayList<>();

        if(multiFieldResourceList!=null){
            logger.info("resource is not null");
            for(Resource eachResource :multiFieldResourceList){
               image= eachResource.getValueMap().get("fileReference","");
               imageLabel=eachResource.getValueMap().get("imageLabel","");
               HomePageServicesComponent homePageServicesComponent= new HomePageServicesComponent(image,imageLabel);
               homePageServicesComponentList.add(homePageServicesComponent);
            }
        }
    }

    public List<HomePageServicesComponent> getHomePageServicesComponentList() {
        return homePageServicesComponentList;
    }
    public String getTitle(){
        return title;
    }
}
