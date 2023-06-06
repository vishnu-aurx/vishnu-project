package com.aurx.core.pojo;

import java.util.Comparator;

/**
 * This class to set property of components
 */
public class ComponentReport implements Comparator<ComponentReport> {

  /**
   * pagePath - String object
   */
  private String pagePath;
  /**
   * page - String Object
   */
  private String page;
  /**
   * title - String Object
   */
  private String title;
  /**
   * group - String Object
   */
  private String group;
  /**
   * pathOfComponent - String object
   */
  private String pathOfComponent;

  /**
   * Default Constructor - ComponentReport
   */
  public ComponentReport() {
  }

  /**
   * This Constructor used to set the properties
   * @param title
   * @param group
   * @param pathOfComponent
   */
  public ComponentReport(String title, String group, String pathOfComponent) {
    this.title = title;
    this.group = group;
    this.pathOfComponent = pathOfComponent;
  }


  /**
   * This method return the pagePath
   * @return - pagePath
   */
  public String getPagePath() {
    return pagePath;
  }

  /**
   * This method set the pagePath
   * @param pagePath
   */
  public void setPagePath(String pagePath) {
    this.pagePath = pagePath;
  }

  /**
   * This method return the page
   * @return - page
   */
  public String getPage() {
    return page;
  }

  /**
   * This method set the page
   * @param page
   */
  public void setPage(String page) {
    this.page = page;
  }

  /**
   * This method return the title
   * @return - title
   */
  public String getTitle() {
    return title;
  }

  /**
   * This method set the title
   * @param title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * This method return the group
   * @return - group
   */
  public String getGroup() {
    return group;
  }

  /**
   * This method set the group
   * @param group
   */
  public void setGroup(String group) {
    this.group = group;
  }

  /**
   * This method return the pathOfComponent
   * @return - pathOfComponent
   */
  public String getPathOfComponent() {
    return pathOfComponent;
  }

  /** This method set the  pathOfComponent
   * @param pathOfComponent
   */
  public void setPathOfComponent(String pathOfComponent) {
    this.pathOfComponent = pathOfComponent;
  }

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
   * This method compare group and title for sorting
   * @param componentReport1 the first object to be compared.
   * @param componentReport2 the second object to be compared.
   * @return
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
