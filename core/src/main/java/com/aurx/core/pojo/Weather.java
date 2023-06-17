package com.aurx.core.pojo;

/**
 * This is the Weather POJO class.
 */
public class Weather {

  /**
   * date - The date.
   */
  private String date;

  /**
   * temp - The temperature.
   */
  private double temp;

  /**
   * wind - The wind.
   */
  private double wind;

  /**
   * cloud -  The cloud.
   */
  private String cloud;

  /**
   * icons - The icons.
   */
  private String icons;

  /**
   * Default constructor of Weather;
   */
  public Weather() {
  }

  /**
   * This is a parameterized constructor used to set date,temp,wind,cloud,icons.
   *
   * @param date  - The date.
   * @param temp  - The temperature.
   * @param wind  - The wind.
   * @param cloud - The cloud.
   * @param icons - The icons.
   */
  public Weather(String date, double temp, double wind, String cloud, String icons) {
    this.date = date;
    this.temp = temp;
    this.wind = wind;
    this.cloud = cloud;
    this.icons = icons;
  }

  /**
   * This method returns the date.
   *
   * @return The date.
   */
  public String getDate() {
    return date;
  }

  /**
   * This method is used to set the date.
   *
   * @param date - The date
   */
  public void setDate(String date) {
    this.date = date;
  }

  /**
   * This method returns the temperature.
   *
   * @return - The  temperature.
   */
  public double getTemp() {
    return temp;
  }

  /**
   * This method is used to set the temperature.
   *
   * @param temp - The temperature.
   */
  public void setTemp(double temp) {
    this.temp = temp;
  }

  /**
   * This method returns the wind.
   *
   * @return - The wind.
   */
  public double getWind() {
    return wind;
  }

  /**
   * This method is used to set the wind.
   *
   * @param wind - The wind.
   */
  public void setWind(double wind) {
    this.wind = wind;
  }

  /**
   * This method returns the cloud.
   *
   * @return - The cloud.
   */
  public String getCloud() {
    return cloud;
  }

  /**
   * This method is used to set the cloud
   *
   * @param cloud - The cloud.
   */
  public void setCloud(String cloud) {
    this.cloud = cloud;
  }

  /**
   * This method returns the icons.
   *
   * @return The icons.
   */
  public String getIcons() {
    return icons;
  }

  /**
   * TThis method is used to set the icons.
   *
   * @param icons - The icons.
   */
  public void setIcons(String icons) {
    this.icons = icons;
  }

  /**
   * Method to convert class object into string format.
   *
   * @return - string value of object.
   */
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
