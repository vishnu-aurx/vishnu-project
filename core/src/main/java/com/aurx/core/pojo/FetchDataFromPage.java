package com.aurx.core.pojo;

/**
 * this is a pojo class
 */
public class FetchDataFromPage {

  /**
   * propValue - String object
   */
  private String propValue;
  /**
   * msg - String object
   */
  private String msg;

  /**
   * This constructor is used to set the propValue and msg
   * @param propValue -the propValue
   * @param msg -the msg
   */
  public FetchDataFromPage(String propValue, String msg) {
    this.propValue = propValue;
    this.msg = msg;
  }

  /**
   * this method return the propValue
   * @return - propValue
   */
  public String getPropValue() {
    return propValue;
  }

  /**
   * this method set the propValue
   * @param propValue - the propValue
   */
  public void setPropValue(String propValue) {
    this.propValue = propValue;
  }

  /**
   * this method return the msg
   * @return - msg
   */
  public String getMsg() {
    return msg;
  }

  /**
   * this method set the msg
   * @param msg - the msg
   */
  public void setMsg(String msg) {
    this.msg = msg;
  }

  /**
   * this method convert class object to String
   * @return String
   */
  @Override
  public String toString() {
    return "FetchDataFromPage{" +
        "propValue='" + propValue + '\'' +
        ", msg='" + msg + '\'' +
        '}';
  }
}
