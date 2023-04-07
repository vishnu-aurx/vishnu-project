package com.aurx.core.services.impl;

import com.aurx.core.services.ProductDetailService;
import com.aurx.core.services.config.ProductDetailsConfiguration;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Designate(ocd = ProductDetailsConfiguration.class)
@Component(service = ProductDetailService.class, immediate = true)
public class ProductDetailServiceImpl implements ProductDetailService {
    private JsonArray ob;

    private ProductDetailsConfiguration configuration;

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
        String baseURL = configuration.url();
        URL url = new URL(baseURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");
        InputStream responseStream = connection.getInputStream();
        String response = IOUtils.toString(responseStream, StandardCharsets.UTF_8);
        JsonParser parser = new JsonParser();
        ob = parser.parse(response).getAsJsonObject().get("products").getAsJsonArray();
    }

    @Override
    public JsonArray fetchAllProducts() {
        return ob;
    }
}
