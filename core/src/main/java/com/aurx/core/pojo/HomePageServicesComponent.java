package com.aurx.core.pojo;

/**
 * HomePageServicesComponent is a pojo class
 */
public class HomePageServicesComponent {

    /**
     * image - String object
     */
    private String image;
    /**
     * imageLabel - String object
     */
    private String imageLabel;


    /**
     * this constructor used to set the values of image and imageLabel
     * @param image - String object
     * @param imageLabel -String object
     */
    public HomePageServicesComponent(String image, String imageLabel) {
        this.image = image;
        this.imageLabel = imageLabel;
    }

    /**
     * this method return image path
     * @return - image
     */
    public String getImage() {
        return image;
    }

    /**
     * this method set the image path
     * @param image - String object
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * this method return imageLabel
     * @return - imageLabel
     */
    public String getImageLabel() {
        return imageLabel;
    }

    /**
     * this method set the imageLabel
     * @param imageLabel - String object
     */
    public void setImageLabel(String imageLabel) {
        this.imageLabel = imageLabel;
    }

    /**
     * this method convert object into String
     * @return - String
     */
    @Override
    public String toString() {
        return "HomePageServicesComponent{" +
                "image='" + image + '\'' +
                ", imageLabel='" + imageLabel + '\'' +
                '}';
    }
}
