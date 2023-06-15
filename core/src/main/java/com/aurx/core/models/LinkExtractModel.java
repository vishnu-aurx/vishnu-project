package com.aurx.core.models;


import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
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

/**
 * LinkExtractModel is used to extract links from HTML code.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkExtractModel {

  /**
   * htmlCode - The htmlCode
   */
  @Inject
  String htmlCode;

  /**
   * resource - Resource object
   */
  @SlingObject
  Resource resource;

  /**
   * logger - Logger object
   */
  private static final Logger logger = LoggerFactory.getLogger(LinkExtractModel.class);

  /**
   * linksList - List of String object
   */
  private List<String> links;

  /**
   * This method is used to extract links from HTML code.
   */
  @PostConstruct
  protected void init() {
    logger.info("Start of init method htmlCode : {}",htmlCode);
    links = new ArrayList<>();
    if (htmlCode != null) {
      logger.info("Inside the init method htmlCode is not null");
      Document doc = Jsoup.parse(htmlCode);
      Elements elements = doc.select("a[href]");
      for (Element link : elements) {
        String href = link.attr("href");
        this.links.add(href);
      }
    }
    logger.info("End fo init method with linksList :{}", links);

  }

  /**
   * This method return the linksList
   *
   * @return - linksList
   */
  public List<String> getLinks() {
    return this.links;
  }
}
