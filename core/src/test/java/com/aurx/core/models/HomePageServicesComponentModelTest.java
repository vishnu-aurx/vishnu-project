package com.aurx.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class HomePageServicesComponentModelTest {

  private final AemContext context = new AemContext();

  private HomePageServicesComponentModel homePageServicesComponentModel;

  @Test
  void testGetHomePageServicesComponentList() {
    context.load(true).json(
        "/com/aurx/core/models/HomePageServicesComponentModel/HomePageServicesComponentModelTest.json",
        "/content/vishnu-project/us/en/test");
    context.currentResource("/content/vishnu-project/us/en/test");
    homePageServicesComponentModel = context.request()
        .adaptTo(HomePageServicesComponentModel.class);
    assertNotNull(homePageServicesComponentModel);
    assertNotNull(homePageServicesComponentModel.getHomePageServicesComponentList());
    assertEquals(3, homePageServicesComponentModel.getHomePageServicesComponentList().size());
  }

  @Test
  void testGetTitle() {
    context.load(true).json(
        "/com/aurx/core/models/HomePageServicesComponentModel/HomePageServicesComponentModelTest.json",
        "/content/vishnu-project/us/en/test");
    context.currentResource("/content/vishnu-project/us/en/test");
    homePageServicesComponentModel = context.request()
        .adaptTo(HomePageServicesComponentModel.class);
    assertNotNull(homePageServicesComponentModel);
    assertNotNull(homePageServicesComponentModel.getTitle());
    assertEquals("Radhe", homePageServicesComponentModel.getTitle());
  }

  @Test
  void testGetTitleWithBlankValue() {
    context.load(true).json(
        "/com/aurx/core/models/HomePageServicesComponentModel/HomePageServicesComponentModelTestWithBlankTitle.json",
        "/content/vishnu-project/us/en/test");
    context.currentResource("/content/vishnu-project/us/en/test");
    homePageServicesComponentModel = context.request()
        .adaptTo(HomePageServicesComponentModel.class);
    assertNotNull(homePageServicesComponentModel);
    assertNull(homePageServicesComponentModel.getTitle());
  }
}