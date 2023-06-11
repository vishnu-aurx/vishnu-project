package com.aurx.core.pojo;

/**
 * This is pojo class
 */
public class Products {

    /**
     * id - String object
     */
   private String id;
    /**
     * price - String object
     */
    private String price;
    /**
     * title - String object
     */
   private String title;
    /**
     * description - String object
     */
    private String description;
    /**
     * image - String object
     */
  private   String image;

    /**
     * this is default constructor
     */
    public Products() {
    }

    /**
     * this is param constructor
     * @param id
     * @param price
     * @param title
     * @param description
     * @param image
     */
    public Products(String id, String price, String title, String description, String image) {
        this.id = id;
        this.price = price;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    /**
     * this method  return id
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * this method set id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * this method return price
     * @return
     */
    public String getPrice() {
        return price;
    }

    /**
     * this method set price
     * @param price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * this method return title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * this method set title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * this method return description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * this methode set description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * this method return image path
     * @return
     */
    public String getImage() {
        return image;
    }

    /**
     * this method set image path
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }

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
