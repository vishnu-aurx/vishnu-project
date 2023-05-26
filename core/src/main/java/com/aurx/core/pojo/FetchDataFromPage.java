package com.aurx.core.pojo;

public class FetchDataFromPage {
private String value;
private String msg;

    public FetchDataFromPage(String value, String msg) {
    this.value = value;
    this.msg = msg;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getMsg() {
    return msg;
  }
  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override
  public String toString() {
    return "FetchDataFromPage{" +
        "value='" + value + '\'' +
        ", msg='" + msg + '\'' +
        '}';
  }
}
