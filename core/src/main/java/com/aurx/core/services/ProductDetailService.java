package com.aurx.core.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

public interface ProductDetailService {
    JsonArray fetchAllProducts() throws IOException;
}
