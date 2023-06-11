package com.aurx.core.services;

import com.google.gson.JsonArray;
import java.io.IOException;

/**
 * this is ProductDetailService interface
 */
public interface ProductDetailService {

    /**
     * This method fetch All Products and return json array
     * @return JsonArray
     * @throws IOException
     */
    JsonArray fetchAllProducts() throws IOException;
}
