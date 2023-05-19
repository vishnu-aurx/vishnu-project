package com.aurx.core.pojo;

public class Weather {
  private String date;
  private double temp;
  private double wind;
  private String cloud;
  private String icons;

  public Weather() {
  }

  public Weather(String date, double temp, double wind, String cloud, String icons) {
    this.date = date;
    this.temp = temp;
    this.wind = wind;
    this.cloud = cloud;
    this.icons = icons;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public double getTemp() {
    return temp;
  }

  public void setTemp(double temp) {
    this.temp = temp;
  }

  public double getWind() {
    return wind;
  }

  public void setWind(double wind) {
    this.wind = wind;
  }

  public String getCloud() {
    return cloud;
  }

  public void setCloud(String cloud) {
    this.cloud = cloud;
  }

  public String getIcons() {
    return icons;
  }

  public void setIcons(String icons) {
    this.icons = icons;
  }

  @Override
  public String toString() {
    return "Weather{" +
        "date='" + date + '\'' +
        ", temp=" + temp +
        ", wind=" + wind +
        ", cloud='" + cloud + '\'' +
        ", icons='" + icons + '\'' +
        '}';
  }
}
