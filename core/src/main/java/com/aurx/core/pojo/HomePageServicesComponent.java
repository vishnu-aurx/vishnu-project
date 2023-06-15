package com.aurx.core.pojo;

/**
 * HomePageServicesComponent is a pojo class
 */
public class HomePageServicesComponent {

  /**
   * image - the image.
   */
  private String image;
  /**
   * imageLabel - The imageLabel
   */
  private String imageLabel;


  /**
   * This constructor is used to set the values of image and image label.
   *
   * @param image      - The image.
   * @param imageLabel -The imageLabel.
   */
  public HomePageServicesComponent(String image, String imageLabel) {
    this.image = image;
    this.imageLabel = imageLabel;
  }

  /**
   * This method return image path.
   *
   * @return - image
   */
  public String getImage() {
    return image;
  }

  /**
   * This method set the image path.
   *
   * @param image - the image
   */
  public void setImage(String image) {
    this.image = image;
  }

  /**
   * This method return imageLabel.
   *
   * @return - imageLabel
   */
  public String getImageLabel() {
    return imageLabel;
  }

  /**
   * This method set the imageLabel.
   *
   * @param imageLabel - The imageLabel.
   */
  public void setImageLabel(String imageLabel) {
    this.imageLabel = imageLabel;
  }

  /**
   * This method convert object into String format.
   *
   * @return - String value
   */
  @Override
  public String toString() {
    return "HomePageServicesComponent{" +
        "image='" + image + '\'' +
        ", imageLabel='" + imageLabel + '\'' +
        '}';
  }
}
