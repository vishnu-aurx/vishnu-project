package com.aurx.core.services.impl;

import com.aurx.core.services.MoviesService;
import com.aurx.core.services.ProductDetailService;
import com.aurx.core.services.config.MoviesConfiguration;
import com.aurx.core.services.config.ProductDetailsConfiguration;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Designate(ocd = ProductDetailsConfiguration.class)
@Component(service = ProductDetailService.class, immediate = true)
public class ProductDetailServiceImpl implements ProductDetailService {
    private JsonArray jasonObject;
    @Reference
    private MoviesService moviesService;
    private ProductDetailsConfiguration configuration;

    private final Logger logger = LoggerFactory.getLogger(ProductDetailServiceImpl.class);

    @Activate
    protected void activate(ProductDetailsConfiguration configuration) {
        this.configuration = configuration;
        try {
            populateProductDetails();
        } catch (IOException e) {

        }
    }

    @Modified
    protected void modified(ProductDetailsConfiguration configuration) {
        this.configuration = configuration;
        try {
            populateProductDetails();
        } catch (IOException e) {

        }
    }

    private void populateProductDetails() throws IOException {
        String response = "";
        String baseURL = configuration.url();
        if(baseURL.startsWith("https")||baseURL.startsWith("http")) {
            URL url = new URL(baseURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            InputStream responseStream = connection.getInputStream();
            response = IOUtils.toString(responseStream, StandardCharsets.UTF_8);
            if (!response.trim().equals("")) {
                JsonParser parser = new JsonParser();
                jasonObject = parser.parse(response).getAsJsonObject().get("products").getAsJsonArray();
            } else {
                jasonObject = new JsonArray();
            }
        }
    }

    @Override
    public JsonArray fetchAllProducts() {
        logger.info(Arrays.toString(moviesService.fetchAllMoviesName()));
       // logger.info("Movies : "+ Arrays.toString(moviesService.fetchAllMoviesNameInProductServiceImpl()));
        return jasonObject;
    }
}
