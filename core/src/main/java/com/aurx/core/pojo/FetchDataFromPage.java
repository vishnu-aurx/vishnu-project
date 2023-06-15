package com.aurx.core.pojo;

/**
 * This is a pojo class.
 */
public class FetchDataFromPage {

  /**
   * propValue - The propValue.
   */
  private String propValue;
  /**
   * msg - The message
   */
  private String message;

  /**
   * This constructor is used to set the propValue and message.
   *
   * @param propValue -the propValue
   * @param message   -the message
   */
  public FetchDataFromPage(String propValue, String message) {
    this.propValue = propValue;
    this.message = message;
  }

  /**
   * This method returns the propValue.
   *
   * @return - propValue
   */
  public String getPropValue() {
    return propValue;
  }

  /**
   * This method sets the propValue.
   *
   * @param propValue - The propValue
   */
  public void setPropValue(String propValue) {
    this.propValue = propValue;
  }

  /**
   * This method returns the message.
   *
   * @return - message
   */
  public String getMessage() {
    return message;
  }

  /**
   * This method sets the message.
   *
   * @param message - The message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * This method converts class objects to string format.
   *
   * @return String value
   */
  @Override
  public String toString() {
    return "FetchDataFromPage{" +
        "propValue='" + propValue + '\'' +
        ", msg='" + message + '\'' +
        '}';
  }
}
