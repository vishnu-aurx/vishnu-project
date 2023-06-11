package com.aurx.core.models;

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
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This model is used to getting products List in slightly
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductDetailsModel {

    /**
     * Inject numberOfProducts from crx
     */
    @Inject
    @Named("numberOfProducts")
    int numberOfProducts;
    /**
     * productDetailService - ProductDetailService object
     */
    @OSGiService
    private ProductDetailService productDetailService;
    /**
     * moviesService - MoviesService object
     */
    @OSGiService
    private MoviesService moviesService;
    /**
     * numberOfProductList - List<Products> object
     */
    private List<Products> numberOfProductList;
    /**
     * logger - Logger object
     */
    private static final Logger logger = LoggerFactory.getLogger(ProductDetailsModel.class);

    /**
     * this method is called by slightly
     */
    @PostConstruct
    protected void init() {
        logger.info("Start of init method with number of products:{}", numberOfProducts);
        getJSONData();

    }

    /**
     * this method used to getting JSON Data
     */
    public void getJSONData() {
        logger.info("getJSONData Method start numberOfProducts :{}", numberOfProducts);
        if (numberOfProducts != 0) {
            numberOfProductList = new ArrayList<>();
            try {
                JsonArray productsArray = productDetailService.fetchAllProducts();
                for (int i = 0; i < numberOfProducts; i++) {
                    if (productsArray.size() > i) {
                        JsonObject jsonObject = productsArray.get(i).getAsJsonObject();
                        numberOfProductList.add(new Products(jsonObject.get("id").toString(), jsonObject.get("price").toString(), jsonObject.get("title").getAsString(), jsonObject.get("description").getAsString(), jsonObject.get("images").getAsJsonArray().get(0).getAsString()));
                    }
                }
                logger.info("getJSONData methode end");
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * this method return the moviesName
     * @return
     */
    public String[] getMovieName() {
        logger.info("Start of getMovieName method");
        String[] moviesName = null;
        if (moviesService.isEnabled()) {
            moviesName = moviesService.fetchAllMoviesName();
        }
        logger.info("End of getMovieName method with moviesName: {}", moviesName);
        return moviesName;
    }

    /**
     * this method return the List of products
     * @return
     */
    public List<Products> getNumberOfProductsList() {
        return numberOfProductList;
    }


}
