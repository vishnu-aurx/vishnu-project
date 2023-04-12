package com.aurx.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkExtractModel {

    @Inject
    String htmlTextfield;

    @SlingObject
    Resource resource;
    Logger logger= LoggerFactory.getLogger(LinkExtractModel.class);
    List<String> linksList;
    @PostConstruct
    protected void init(){
        logger.info("start init method");
       linksList =new ArrayList<>();
        if(htmlTextfield !=null) {
            Document doc = Jsoup.parse(htmlTextfield);
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String href = link.attr("href");
                this.linksList.add(href);
            }
        }
        logger.info("end init method");

    }
    public List getLinks(){
        return this.linksList;
    }
}
