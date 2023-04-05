package com.aurx.core.models;

public class TestComponent {
    String link;
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TestComponent(String link, String text) {
        this.link = link;
        this.text = text;
    }

    public TestComponent() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "TestComponent{" +
                "link='" + link + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
