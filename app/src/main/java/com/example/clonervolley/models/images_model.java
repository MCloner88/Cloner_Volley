package com.example.clonervolley.models;

public class images_model
{
    String id ;
    String email;
    String image;
    String uploaded_at;
    Boolean multiAction = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(String uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    public Boolean getMultiAction() {
        return multiAction;
    }

    public void setMultiAction(Boolean multiAction) {
        this.multiAction = multiAction;
    }
}
