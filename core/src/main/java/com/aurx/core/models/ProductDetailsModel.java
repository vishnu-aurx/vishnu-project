package com.aurx.core.models;

import com.aurx.core.services.ProductDetailService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductDetailsModel {

    @Inject
    @Named("numberOfProducts")
    int numberOfProducts;

    @OSGiService
    private ProductDetailService productDetailService;
    private List<Products> numberOfProductList;
    @SlingObject
    private Resource resource;

    Logger logger = LoggerFactory.getLogger(ProductDetailsModel.class);


    @PostConstruct
    protected void init() {
        logger.info("Start of init method with number of products:{}", numberOfProducts);
        getJSONData();

    }


    public void getJSONData() {
        if (numberOfProducts != 0) {
            numberOfProductList = new ArrayList<>();
            try {
                JsonArray productsArray = productDetailService.fetchAllProducts();
                for (int i = 0; i < numberOfProducts; i++) {

                    JsonObject jsonObject = productsArray.get(i).getAsJsonObject();
                    numberOfProductList.add(new Products(jsonObject.get("id").toString(), jsonObject.get("price").toString(), jsonObject.get("title").getAsString(), jsonObject.get("description").getAsString(), jsonObject.get("images").getAsJsonArray().get(0).getAsString()));
                }

            } catch (IOException e) {

            }
        }
    }

    public List<Products> getNumberOfProductsList() {
        return numberOfProductList;
    }
}
