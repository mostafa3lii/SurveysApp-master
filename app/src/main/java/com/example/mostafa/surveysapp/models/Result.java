package com.example.mostafa.surveysapp.models;

import java.util.List;

public class Result {
    private String ownerId;
    private String ownerPic;
    private String ownerName;
    private List<Question> questions;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getOwnerPic() {
        return ownerPic;
    }

    public void setOwnerPic(String ownerPic) {
        this.ownerPic = ownerPic;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Result() {

    }

    public Result(String ownerId, String ownerPic, String ownerName, List<Question> questions) {
        this.ownerId = ownerId;
        this.ownerPic = ownerPic;
        this.ownerName = ownerName;
        this.questions = questions;
    }
}