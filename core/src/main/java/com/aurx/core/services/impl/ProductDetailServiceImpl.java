package com.aurx.core.services.impl;

import com.aurx.core.services.MoviesService;
import com.aurx.core.services.ProductDetailService;
import com.aurx.core.services.config.ProductDetailsConfiguration;
import com.aurx.core.utils.PopulateDataFromAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class used to fetch product details from api response
 */
@Designate(ocd = ProductDetailsConfiguration.class)
@Component(service = ProductDetailService.class, immediate = true)
public class ProductDetailServiceImpl implements ProductDetailService {

    /**
     * jsonElements - JsonArray Object
     */
    private JsonArray jsonElements = new JsonArray();

    /**
     * moviesService - MoviesService object
     */
    @Reference
    private MoviesService moviesService;
    /**
     * configuration - ProductDetailsConfiguration object
     */
    private ProductDetailsConfiguration configuration;
    /**
     * logger - Logger object
     */
    private static final Logger logger = LoggerFactory.getLogger(ProductDetailServiceImpl.class);

    /**
     * this method used to fetch data to activation of bundle
     * @param configuration
     */
    @Activate
    protected void activate(ProductDetailsConfiguration configuration) {
        logger.info("activate method start");
        this.configuration = configuration;
            fetchProductDetails();

    }

    /**
     * this method used to fetch data to modification of bundle
     *
     * @param configuration
     */
    @Modified
    protected void modified(ProductDetailsConfiguration configuration) {
        logger.info("modified method start");
        this.configuration = configuration;
            fetchProductDetails();

    }

    /**
     * this method is used to fetch product details form api response
     */
    private void fetchProductDetails()  {
        String baseURL = configuration.url();
        String response = PopulateDataFromAPI.populateData(baseURL);
        logger.info("product res ==================== {}",response);
            if (response!= null && !response.trim().equals("")) {
                jsonElements= JsonParser.parseString(response).getAsJsonObject().get("products").getAsJsonArray();
            }
        }

    /**
     * this method return the jsonElements of products
     * @return
     */
    @Override
    public JsonArray fetchAllProducts() {
        return jsonElements;
    }
}
