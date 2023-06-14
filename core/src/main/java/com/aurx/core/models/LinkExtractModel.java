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

/**
 * This is used to extract the links from the html code
 */
@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkExtractModel {

    /**
     * htmlTextfield - String object
     */
    @Inject
    String htmlTextfield;

    /**
     * resource - Resource object
     */
    @SlingObject
    Resource resource;
    /**
     * logger - Logger object
     */
    private static final Logger logger= LoggerFactory.getLogger(LinkExtractModel.class);
    /**
     * linksList - List of String object
     */
   private List<String> linksList;
    /**
     * this method invoke when page is load
     */
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

    /**
     * This method return the linksList
     * @return - linksList
     */
    public List<String> getLinks(){
        return this.linksList;
    }
}
