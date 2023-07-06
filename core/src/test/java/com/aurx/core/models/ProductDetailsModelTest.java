package com.aurx.core.models;

import static com.aurx.core.constant.ApplicationConstants.PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.aurx.core.services.MoviesService;
import com.aurx.core.services.ProductDetailService;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ProductDetailsModelTest {

  private final AemContext context = new AemContext();
  @Mock
  private ProductDetailService productDetailService;
  @Mock
  private MoviesService moviesService;
  private ProductDetailsModel productDetailsModel;

  @BeforeEach
  void setUp() throws IOException {
    context.registerService(ProductDetailService.class, productDetailService);
    context.registerService(MoviesService.class, moviesService);

  }

  @Test
  void testGetMovieName() throws IOException {
    String response = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream(
            "/com/aurx/core/models/ProductDetailsModel/apiResponse.json")),
        StandardCharsets.UTF_8);
    JsonArray array = JsonParser.parseString(response).getAsJsonObject().get(PRODUCTS)
        .getAsJsonArray();
    when(productDetailService.fetchAllProducts()).thenReturn(array);
    context.load(true).json("/com/aurx/core/models/ProductDetailsModel/ProductDetailsData.json",
        "/content/vishnu-project/us/en/test");
    context.currentResource("/content/vishnu-project/us/en/test");
    when(moviesService.fetchAllMoviesName()).thenReturn(new String[]{"Akash", "Vishnu"});
    when(moviesService.isEnabled()).thenReturn(true);
    productDetailsModel = context.request().adaptTo(ProductDetailsModel.class);
    assertNotNull(productDetailsModel.getMovieName());
    assertEquals(2, productDetailsModel.getMovieName().length);
  }

  @Test
  void testGetNumberOfProductsList() throws IOException {
    String response = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream(
            "/com/aurx/core/models/ProductDetailsModel/apiResponse.json")),
        StandardCharsets.UTF_8);
    JsonArray array = JsonParser.parseString(response).getAsJsonObject().get(PRODUCTS)
        .getAsJsonArray();
    when(productDetailService.fetchAllProducts()).thenReturn(array);
    context.load(true).json("/com/aurx/core/models/ProductDetailsModel/ProductDetailsData.json",
        "/content/vishnu-project/us/en/test");
    context.currentResource("/content/vishnu-project/us/en/test");
    productDetailsModel = context.request().adaptTo(ProductDetailsModel.class);
    assertNotNull(productDetailsModel);
    assertNotNull(productDetailsModel.getNumberOfProductsList());
    assertEquals(3, productDetailsModel.getNumberOfProductsList().size());
  }

  @Test
  void testGetNumberOfProductsListWithZeroProductValue() throws IOException {
    context.load(true).json(
        "/com/aurx/core/models/ProductDetailsModel/ProductDetailsDataWithZeroProductValue.json",
        "/content/vishnu-project/us/en/test");
    context.currentResource("/content/vishnu-project/us/en/test");
    productDetailsModel = context.request().adaptTo(ProductDetailsModel.class);
    assertNotNull(productDetailsModel);
    assertNull(productDetailsModel.getNumberOfProductsList());
  }

  @Test
  void testGetNumberOfProductsListWithHigherProductValue() throws IOException {

    String response = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream(
            "/com/aurx/core/models/ProductDetailsModel/apiResponse.json")),
        StandardCharsets.UTF_8);
    JsonArray array = JsonParser.parseString(response).getAsJsonObject().get(PRODUCTS)
        .getAsJsonArray();
    when(productDetailService.fetchAllProducts()).thenReturn(array);
    context.load(true).json(
        "/com/aurx/core/models/ProductDetailsModel/ProductDetailsDataWithHigherProductValue.json",
        "/content/vishnu-project/us/en/test");
    context.currentResource("/content/vishnu-project/us/en/test");
    productDetailsModel = context.request().adaptTo(ProductDetailsModel.class);
    assertNotNull(productDetailsModel);
    assertNotNull(productDetailsModel.getNumberOfProductsList());
    assertEquals(30, productDetailsModel.getNumberOfProductsList().size());
  }

  @Test
  void testGetMovieNameWithDisabledService() throws IOException {
    String response = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream(
            "/com/aurx/core/models/ProductDetailsModel/apiResponse.json")),
        StandardCharsets.UTF_8);
    JsonArray array = JsonParser.parseString(response).getAsJsonObject().get(PRODUCTS)
        .getAsJsonArray();
    when(productDetailService.fetchAllProducts()).thenReturn(array);
    context.load(true).json("/com/aurx/core/models/ProductDetailsModel/ProductDetailsData.json",
        "/content/vishnu-project/us/en/test");
    context.currentResource("/content/vishnu-project/us/en/test");
    productDetailsModel = context.request().adaptTo(ProductDetailsModel.class);
    when(moviesService.isEnabled()).thenReturn(false);
    assertNull(productDetailsModel.getMovieName());
  }

}