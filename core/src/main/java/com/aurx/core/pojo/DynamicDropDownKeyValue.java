package com.aurx.core.pojo;

/**
 * This is a pojo class.
 */
public class DynamicDropDownKeyValue {

  /**
   * key property.
   */
  private String key;
  /**
   * value property.
   */
  private String value;

  /**
   * constructor instance.
   *
   * @param newKey   - The newKey.
   * @param newValue - The newValue.
   */
  public DynamicDropDownKeyValue(final String newKey, final String newValue) {
    this.key = newKey;
    this.value = newValue;
  }

  /**
   * This method return the key.
   *
   * @return key
   */
  public String getKey() {
    return key;
  }

  /**
   * This method set the key.
   *
   * @param key - The key
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * This method return the value.
   *
   * @return value
   */
  public String getValue() {
    return value;
  }

  /**
   * This method  set the value.
   *
   * @param value - The value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Method to convert class object into string format.
   *
   * @return - string value of object
   */
  @Override
  public String toString() {
    return "DynamicDropDownKeyValue{" +
        "key='" + key + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}

