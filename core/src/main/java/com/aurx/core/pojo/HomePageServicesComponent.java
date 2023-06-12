package com.aurx.core.models;

public class HomePageServicesComponent {
    private String image;
    private String imageLabel;

    public HomePageServicesComponent() {

    }

    public HomePageServicesComponent(String image, String imageLabel) {
        this.image = image;
        this.imageLabel = imageLabel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(String imageLabel) {
        this.imageLabel = imageLabel;
    }

    @Override
    public String toString() {
        return "HomePageServicesComponent{" +
                "image='" + image + '\'' +
                ", imageLabel='" + imageLabel + '\'' +
                '}';
    }
}
