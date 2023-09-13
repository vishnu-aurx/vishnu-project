package com.aurx.core.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.aurx.core.services.MoviesService;
import com.aurx.core.services.PopulateDataFromAPI;
import com.aurx.core.services.ProductDetailService;
import com.aurx.core.services.config.ProductDetailsConfiguration;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.service.component.annotations.Reference;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ProductDetailServiceImplTest {

  private final AemContext context = new AemContext();

  @Mock
  private ProductDetailsConfiguration configuration;

  private ProductDetailServiceImpl productDetailService;

  @Mock
  private PopulateDataFromAPI populateDataFromAPI;
  @Mock
  private MoviesService moviesService;

  @Test
  void fetchAllProducts() throws IOException {
    context.registerService(PopulateDataFromAPI.class, populateDataFromAPI);
    context.registerService(MoviesService.class, moviesService);
    productDetailService = context.registerInjectActivateService(new ProductDetailServiceImpl());
    String response = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream(
            "/com/aurx/core/services/ProductDetailsServiceResource/ProductDetails.json")),
        StandardCharsets.UTF_8);
    when(configuration.url()).thenReturn("https://dummyjson.com/products");
    when(populateDataFromAPI.populateData("https://dummyjson.com/products")).thenReturn(response);
    productDetailService.activate(configuration);
    assertNotNull(productDetailService);
    assertNotNull(productDetailService.fetchAllProducts());
  }

  @Test
  void fetchAllProductsWithNullURL() throws IOException {
    context.registerService(PopulateDataFromAPI.class, populateDataFromAPI);
    context.registerService(MoviesService.class, moviesService);
    productDetailService = context.registerInjectActivateService(new ProductDetailServiceImpl());
    when(configuration.url()).thenReturn(null);
    when(populateDataFromAPI.populateData(null)).thenReturn(null);
    productDetailService.activate(configuration);
    assertNotNull(productDetailService);
    assertEquals(0,productDetailService.fetchAllProducts().size());
  }
}