package com.yashraj.glavage.student;

public class Model {
    private String dateadded;
    private String image;
    private String cloth_type;
    private String userid;


    public Model(String dateadded, String image, String cloth_type,String userid) {
        this.dateadded = dateadded;
        this.image = image;
        this.cloth_type = cloth_type;
        this.userid=userid;
    }
    public Model(){}

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCloth_type() {
        return cloth_type;
    }

    public void setCloth_type(String cloth_type) {
        this.cloth_type = cloth_type;
    }


    public String getDateadded() {
        return dateadded;
    }

    public void setDateadded(String dateadded) {
        this.dateadded = dateadded;
    }

}
