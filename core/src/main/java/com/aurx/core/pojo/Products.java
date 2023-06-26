package com.aurx.core.pojo;

/**
 * This is a Product Pojo class.
 */
public class Products {

  /**
   * id - The id.
   */
  String id;

  /**
   * price - The price.
   */
  String price;

  /**
   * title - The title.
   */
  String title;

  /**
   * description - The description.
   */
  String description;

  /**
   * image - The image.
   */
  String image;

  /**
   * Default constructor of Products;
   */
  public Products() {
  }

  /**
   * This is a parameterized constructor used to set id,price,title, description, and image
   *
   * @param id          - The id.
   * @param price       - The price.
   * @param title       - The Title.
   * @param description -The description.
   * @param image       - The image.
   */
  public Products(String id, String price, String title, String description, String image) {
    this.id = id;
    this.price = price;
    this.title = title;
    this.description = description;
    this.image = image;
  }

  /**
   * This method returns id.
   *
   * @return - The id.
   */
  public String getId() {
    return id;
  }

  /**
   * This method sets the id.
   *
   * @param id - The id.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * This method returns the price.
   *
   * @return -The price.
   */
  public String getPrice() {
    return price;
  }

  /**
   * This method sets the price.
   *
   * @param price - The price.
   */
  public void setPrice(String price) {
    this.price = price;
  }

  /**
   * This method returns the title.
   *
   * @return - The title.
   */
  public String getTitle() {
    return title;
  }

  /**
   * This method sets the title.
   *
   * @param title - The title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * This method returns the description.
   *
   * @return - The description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * This method sets the description.
   *
   * @param description - The description.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * This method returns the image.
   *
   * @return The image.
   */
  public String getImage() {
    return image;
  }

  /**
   * This method sets the image.
   *
   * @param image - The image
   */
  public void setImage(String image) {
    this.image = image;
  }

  /**
   * Method to convert class object into string format.
   *
   * @return - string value of object
   */
  @Override
  public String toString() {
    return "Products{" +
        "id=" + id +
        ", price=" + price +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", image='" + image + '\'' +
        '}';
  }
}
