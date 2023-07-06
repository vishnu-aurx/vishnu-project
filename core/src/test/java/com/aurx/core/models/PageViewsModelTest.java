package com.aurx.core.models;

import static com.aurx.core.constant.ApplicationConstants.AUTHOR;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.aurx.core.services.ProductDetailService;
import com.google.common.collect.ImmutableSet;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.sling.testing.mock.sling.services.MockSlingSettingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class PageViewsModelTest {

  private final AemContext context = new AemContext();

  private PageViewsModel pageViewsModel;

  @Mock
  private SlingSettingsService slingSettingsService;

  @BeforeEach
  void setUp() {
    context.registerService(SlingSettingsService.class, slingSettingsService);
  }

  @Test
  void testIsAuthor() {
    MockSlingSettingService mockSlingSettingService = (MockSlingSettingService) context.getService(
        SlingSettingsService.class);
    assert mockSlingSettingService != null;
    mockSlingSettingService.setRunModes(ImmutableSet.of(AUTHOR));
    pageViewsModel = context.request().adaptTo(PageViewsModel.class);
    assertNotNull(pageViewsModel);
    assertTrue(pageViewsModel.isAuthor());
  }

  @Test
  void testIsNotAuthor() {
    pageViewsModel = context.request().adaptTo(PageViewsModel.class);
    assertNotNull(pageViewsModel);
    assertFalse(pageViewsModel.isAuthor());
  }
}