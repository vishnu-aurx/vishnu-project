package com.aurx.core.pojo;

import java.util.Comparator;

public class ComponentReport implements Comparator<ComponentReport> {
  private String pagePath;
  private String page;
 private String title;
 private String group;
 private String pathOfComponent;

  public ComponentReport() {

  }

  public ComponentReport(String title, String group, String pathOfComponent) {
    this.title = title;
    this.group = group;
    this.pathOfComponent = pathOfComponent;
  }

  public String getPagePath() {
    return pagePath;
  }

  public void setPagePath(String pagePath) {
    this.pagePath = pagePath;
  }

  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getPathOfComponent() {
    return pathOfComponent;
  }

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

  @Override
  public int compare(ComponentReport o1, ComponentReport o2) {
    int grp=o1.group.compareToIgnoreCase(o2.group);
    if(grp ==0){
      return  o1.getTitle().compareToIgnoreCase(o2.getTitle());
    }else
    return grp ;
  }
}
