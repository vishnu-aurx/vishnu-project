package com.aurx.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class LinkExtractModelTest {

  private final AemContext context = new AemContext();
  private LinkExtractModel linkExtractModel;

  @Test
  void testGetLinks() {
    context.load(true).json("/com/aurx/core/models/LinkExtractModel/linkComponentProperty.json",
        "/content/vishnu-project/en/test");
    context.currentResource("/content/vishnu-project/en/test");
    linkExtractModel = context.request().adaptTo(LinkExtractModel.class);
    assertNotNull(linkExtractModel);
    assertNotNull(linkExtractModel.getLinks());
    assertEquals(4, linkExtractModel.getLinks().size());
  }

  @Test
  void testGetLinksWithBlank() {
    context.load(true).json("/com/aurx/core/models/LinkExtractModel/NonHTMLText.json",
        "/content/vishnu-project/en/NonHTMLText");
    context.currentResource("/content/vishnu-project/en/NonHTMLText");
    linkExtractModel = context.request().adaptTo(LinkExtractModel.class);
    assertNotNull(linkExtractModel);
    assertNotNull(linkExtractModel.getLinks());
    assertEquals(0, linkExtractModel.getLinks().size());
  }
}