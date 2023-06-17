package com.aurx.core.services.impl;

import static com.aurx.core.constant.ApplicationConstants.PRODUCTS;
import static org.apache.commons.lang.StringUtils.EMPTY;

import com.aurx.core.services.MoviesService;
import com.aurx.core.services.PopulateDataFromAPI;
import com.aurx.core.services.ProductDetailService;
import com.aurx.core.services.config.ProductDetailsConfiguration;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.IOException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the ProductDetailServiceImpl used to fetch all products from the API.
 */
@Designate(ocd = ProductDetailsConfiguration.class)
@Component(service = ProductDetailService.class, immediate = true)
public class ProductDetailServiceImpl implements ProductDetailService {

  /**
   * jsonElements - The jsonElements
   */
  private JsonArray jsonElements;

  /**
   * moviesService - The MoviesService object.
   */
  @Reference
  private MoviesService moviesService;
  /**
   * configuration - The ProductDetailsConfiguration object.
   */
  private ProductDetailsConfiguration configuration;

  /**
   * populateDataFromAPI - PopulateDataFromAPI object.
   */
  @Reference
  private PopulateDataFromAPI populateDataFromAPI;

  /**
   * logger - The Logger object.
   */
  private final Logger logger = LoggerFactory.getLogger(ProductDetailServiceImpl.class);

  /**
   * This method is invoked when the service is activated.
   *
   * @param configuration - The configuration.
   */
  @Activate
  protected void activate(ProductDetailsConfiguration configuration) {
    logger.info("Start of activate method");
    this.configuration = configuration;
    populateProductDetails();
    logger.info("End of activate method");
  }

  /**
   * This method is invoked when the Configuration is modified.
   *
   * @param configuration - The configuration.
   */
  @Modified
  protected void modified(ProductDetailsConfiguration configuration) {
    logger.info("Start of modified method");
    this.configuration = configuration;
    populateProductDetails();
    logger.info("End of modified method");
  }

  /**
   * This method populate product details from API.
   *
   * @throws IOException
   */
  private void populateProductDetails() {
    logger.info("Start fo populateProductDetails method");
    jsonElements = new JsonArray();
    String baseURL = configuration.url();
    logger.info("populateProductDetails method start url :{}", baseURL);
    String response = populateDataFromAPI.populateData(baseURL);
    if (response != null && !response.trim().equals(EMPTY)) {
      logger.info("response is not null response : {}", response);
      jsonElements = JsonParser.parseString(response).getAsJsonObject().get(PRODUCTS)
          .getAsJsonArray();
    }
    logger.info("End of populateProductDetails method");
  }

  /**
   * This method returns all products in a JSON array.
   *
   * @return - The jsonElements.
   */
  @Override
  public JsonArray fetchAllProducts() {
    return jsonElements;
  }
}
