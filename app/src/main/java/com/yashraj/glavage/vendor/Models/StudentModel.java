package com.yashraj.glavage.vendor.Models;

public class StudentModel {
    private String user_room_no;
    public StudentModel(String user_room_no){
        this.user_room_no=user_room_no;
    }

    public String getUser_room_no() {
        return user_room_no;
    }

    public void setUser_room_no(String user_room_no) {
        this.user_room_no = user_room_no;
    }
}
