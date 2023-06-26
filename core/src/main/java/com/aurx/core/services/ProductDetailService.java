package com.aurx.core.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 * Interface ProductDetailService.
 */
public interface ProductDetailService {


  /**
   * This method returns all products in a JSON array.
   *
   * @return - The JsonArray
   */
  JsonArray fetchAllProducts();
}
