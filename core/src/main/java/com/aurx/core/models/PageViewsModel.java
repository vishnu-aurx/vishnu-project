package com.aurx.core.models;

import static com.aurx.core.constant.ApplicationConstants.AUTHOR;

import java.util.Set;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.settings.SlingSettingsService;

/**
 * This class determines whether the run mode is "author" or not.
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageViewsModel {

  /**
   * slingSettingsService - The slingSettingsService.
   */
  @OSGiService
  private SlingSettingsService slingSettingsService;

  /**
   * This method determines whether the run mode is "author" or not.
   *
   * @return isAuthor in the form of a boolean.
   */
  public boolean isAuthor() {

    Set<String> runModes = this.slingSettingsService.getRunModes();
    if (runModes != null && runModes.contains(AUTHOR)) {
      return true;
    }
    return false;
  }
}
