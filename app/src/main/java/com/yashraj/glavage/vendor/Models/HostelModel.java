package com.yashraj.glavage.vendor.Models;

public class HostelModel {
    private String hostel_name;

    public HostelModel(String hostel_name) {
        this.hostel_name = hostel_name;
    }
    public HostelModel(){}

    public String getHostel_name() {
        return hostel_name;
    }

    public void setHostel_name(String hostel_name) {
        this.hostel_name = hostel_name;
    }
}
