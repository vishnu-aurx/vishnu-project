package com.aurx.core.services;

import org.apache.http.HttpResponse;

/**
 * Interface PopulateDataFromAPI.
 */
public interface PopulateDataFromAPI {

  /**
   * This method populates data from the API and returns a response.
   *
   * @param baseURL - The baseURL.
   * @return - response.
   */
  String populateData(String baseURL);

  /**
   * This method populates data from the API and returns a httpResponse.
   *
   * @param apiURL       - The apiURL.
   * @param userName     - The userName.
   * @param userPassword - The userPassword.
   * @return - httpResponse.
   */
  HttpResponse getAPIResponseWithUserPassword(String apiURL, String userName,
      String userPassword);
}
