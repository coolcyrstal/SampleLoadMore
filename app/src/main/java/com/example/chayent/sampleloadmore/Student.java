package com.example.chayent.sampleloadmore;

/**
 * Student.java
 * SampleLoadMore
 * Created by Chayen Tansritrang on 8/23/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class Student {

    private String name;
    private String emailId;

    Student(String name, String emailId) {
        this.name = name;
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }


}
