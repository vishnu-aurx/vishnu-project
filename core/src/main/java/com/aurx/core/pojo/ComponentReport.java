package com.aurx.core.pojo;

import com.adobe.cq.wcm.core.components.internal.DataLayerConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Comparator;
import lombok.Data;

/**
 * This is a ComponentReport POJO class.
 */
@Data
public class ComponentReport implements Comparator<ComponentReport> {

  /**
   * pagePath - The pagePath.
   */
  private String pagePath;

  /**
   * page - The page.
   */
  private String page;

  /**
   * title - The title.
   */
  @JsonProperty
  private String title;

  /**
   * group - The group.
   */
  @JsonProperty("componentGroup")
  private String group;

  /**
   * pathOfComponent - The pathOfComponent.
   */
  @JsonProperty("slingResourceType")
  private String pathOfComponent;

  /**
   * Default constructor of ComponentReport.
   */
  public ComponentReport() {
  }

  /**
   * This constructor is used to set the properties.
   *
   * @param title           - The title.
   * @param group           - The group.
   * @param pathOfComponent - The pageOfComponent.
   */
  public ComponentReport(String title, String group, String pathOfComponent) {
    this.title = title;
    this.group = group;
    this.pathOfComponent = pathOfComponent;
  }


  /**
   * This method returns the page path.
   *
   * @return - pagePath
   */
  public String getPagePath() {
    return pagePath;
  }

  /**
   * This method sets the page Path
   *
   * @param pagePath
   */
  public void setPagePath(String pagePath) {
    this.pagePath = pagePath;
  }

  /**
   * This method returns the page.
   *
   * @return - page
   */
  public String getPage() {
    return page;
  }

  /**
   * This method sets the page.
   *
   * @param page
   */
  public void setPage(String page) {
    this.page = page;
  }

  /**
   * This method returns the title.
   *
   * @return - title
   */
  public String getTitle() {
    return title;
  }

  /**
   * This method sets the title.
   *
   * @param title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * This method returns the group.
   *
   * @return - group
   */
  public String getGroup() {
    return group;
  }

  /**
   * This method sets the group.
   *
   * @param group
   */
  public void setGroup(String group) {
    this.group = group;
  }

  /**
   * This method returns the pathOfComponent.
   *
   * @return - pathOfComponent
   */
  public String getPathOfComponent() {
    return pathOfComponent;
  }

  /**
   * This method sets the pathOfComponent.
   *
   * @param pathOfComponent
   */
  public void setPathOfComponent(String pathOfComponent) {
    this.pathOfComponent = pathOfComponent;
  }

  /**
   * Method to convert class object into string format.
   *
   * @return - string value of object
   */
  @Override
  public String toString() {
    return "ComponentReport{" +
        "pagePath='" + pagePath + '\'' +
        ", page='" + page + '\'' +
        ", title='" + title + '\'' +
        ", group='" + group + '\'' +
        ", pathOfComponent='" + pathOfComponent + '\'' +
        '}';
  }

  /**
   * Compares two ComponentReport objects based on their group and title. The comparison is
   * case-insensitive.
   *
   * @param componentReport1 The first ComponentReport object to compare.
   * @param componentReport2 The second ComponentReport object to compare.
   * @return An integer value indicating the comparison result:
   */
  @Override
  public int compare(ComponentReport componentReport1, ComponentReport componentReport2) {
    int compareGroup = componentReport1.group.compareToIgnoreCase(componentReport2.group);
    if (compareGroup == 0) {
      return componentReport1.getTitle().compareToIgnoreCase(componentReport2.getTitle());
    } else {
      return compareGroup;
    }
  }
}
