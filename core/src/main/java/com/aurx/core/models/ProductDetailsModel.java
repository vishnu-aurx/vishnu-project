package com.aurx.core.models;

import static com.aurx.core.constant.ApplicationConstants.DESCRIPTION;
import static com.aurx.core.constant.ApplicationConstants.IMAGES;
import static com.aurx.core.constant.ApplicationConstants.PRICE;
import static com.aurx.core.constant.ApplicationConstants.TITLE;
import static com.day.cq.mcm.emailprovider.ESConstants.ID;

import com.aurx.core.pojo.Products;
import com.aurx.core.services.MoviesService;
import com.aurx.core.services.ProductDetailService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProductDetailsModel fetches the product details.
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductDetailsModel {

  /**
   * numberOfProducts - The numberOfProducts.
   */
  @ValueMapValue
  @Named("numberOfProducts")
  int numberOfProducts;

  /**
   * productDetailService - The productDetailService.
   */
  @OSGiService
  private ProductDetailService productDetailService;

  /**
   * moviesService - The moviesService.
   */
  @OSGiService
  private MoviesService moviesService;

  /**
   * numberOfProductList - The numberOfProductList.
   */
  private List<Products> numberOfProductList;

  /**
   * resource - The resource.
   */
  @SlingObject
  private Resource resource;


  /**
   * logger - The logger.
   */
  private static final Logger logger = LoggerFactory.getLogger(ProductDetailsModel.class);


  /**
   * This method is invoked on the object initialization.
   */
  @PostConstruct
  protected void init() {
    logger.info("Start of init method with number of products:{}", numberOfProducts);
    fetchProductsFromJson();
    logger.info("End of init method");

  }

  /**
   * This method is used to fetch Product details from JsonArray.
   */
  public void fetchProductsFromJson() {
    logger.info("fetchProductsFromJSON Method start numberOfProducts :{}", numberOfProducts);
    if (numberOfProducts != 0) {
      numberOfProductList = new ArrayList<>();

      JsonArray productsArray = productDetailService.fetchAllProducts();
      for (int i = 0; i < numberOfProducts; i++) {
        if (productsArray.size() > i) {
          JsonObject jsonObject = productsArray.get(i).getAsJsonObject();
          numberOfProductList.add(
              new Products(jsonObject.get(ID).toString(), jsonObject.get(PRICE).toString(),
                  jsonObject.get(TITLE).getAsString(),
                  jsonObject.get(DESCRIPTION).getAsString(),
                  jsonObject.get(IMAGES).getAsJsonArray().get(0).getAsString()));
        }
      }
      logger.info("getJSONData methode end");

    }
  }


  /**
   * This method returns the movie's name.
   *
   * @return - moviesName.
   */
  public String[] getMovieName() {
    logger.info("Start of getMovieName method");
    String[] moviesName = null;
    if (moviesService.isEnabled()) {
      logger.info("moviesService isEnable");
      moviesName = moviesService.fetchAllMoviesName();
    }
    return moviesName;
  }

  /**
   * This method return the number of Products.
   *
   * @return - numberOfProductList.
   */
  public List<Products> getNumberOfProductsList() {
    return numberOfProductList;
  }


}
