package com.aurx.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class PagePropertiesDetailsModelTest {

  private final AemContext context = new AemContext();

  private PagePropertiesDetailsModel pagePropertiesDetailsModel;

  @Test
  void testGetAllPageTitleList() {
    context.load(true).json(
        "/com/aurx/core/models/PagePropertiesDetailsModel/PagePropertiesDetailsModelTestAllPagesTitle.json",
        "/content/vishnu-project/us/en/test");
    context.load(true).json("/com/aurx/core/models/PagePropertiesDetailsModel/childPages.json",
        "/content/we-retail/language-masters/en/experience");
    context.currentResource("/content/vishnu-project/us/en/test");
    pagePropertiesDetailsModel = context.request().adaptTo(PagePropertiesDetailsModel.class);
    assertNotNull(pagePropertiesDetailsModel);
    assertEquals(1, pagePropertiesDetailsModel.getAllPageTitleList().size());
    assertTrue(pagePropertiesDetailsModel.getAllPageTitleList().get(0).containsKey("Experience"));
    assertEquals(6,
        pagePropertiesDetailsModel.getAllPageTitleList().get(0).get("Experience").size());
  }

  @Test
  void testGetPageTitleList() {
    context.load(true).json(
        "/com/aurx/core/models/PagePropertiesDetailsModel/PagePropertiesDetailsModelTest.json",
        "/content/vishnu-project/us/en/test");
    context.load(true).json("/com/aurx/core/models/PagePropertiesDetailsModel/childPage.json",
        "/content/we-retail/language-masters/en/men");
    context.load(true).json("/com/aurx/core/models/PagePropertiesDetailsModel/childPage1.json",
        "/content/we-retail/language-masters/en/women");
    context.currentResource("/content/vishnu-project/us/en/test");
    pagePropertiesDetailsModel = context.request().adaptTo(PagePropertiesDetailsModel.class);
    assertNotNull(pagePropertiesDetailsModel);
    assertNotNull(pagePropertiesDetailsModel.getPageTitleList());
    assertEquals(2, pagePropertiesDetailsModel.getPageTitleList().size());
    assertEquals("Men", pagePropertiesDetailsModel.getPageTitleList().get(0));
  }

}