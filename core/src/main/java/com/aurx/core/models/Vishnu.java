package com.aurx.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Vishnu {

    Logger logger = LoggerFactory.getLogger(Vishnu.class);

    @SlingObject
    @Named("link:abc")
    String link;
    String linkModify;



    @SlingObject
    private Resource resource;
private List<TestComponent> properties;

@PostConstruct
protected  void init(){
    properties=new ArrayList<>();
    processData();
    logger.info("hello hello");
   linkModify=this.link+"this is init method";
}

    public String getLink() {
        return linkModify;
    }
    public void processData(){

        Iterator<Resource> resourceIterator = resource.getChild("fieldData").listChildren();
        while(resourceIterator.hasNext()){
            Resource eachResource=resourceIterator.next();
            ValueMap valueMap=eachResource.getValueMap();
            TestComponent testComponent=new TestComponent(valueMap.get("link","deva"),valueMap.get("text","shree"));
            properties.add(testComponent);
        }
    }
    public List<TestComponent> getProperties(){
    return this.properties;
    }
}
