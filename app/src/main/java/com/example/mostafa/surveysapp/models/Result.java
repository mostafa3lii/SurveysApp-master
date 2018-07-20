package com.example.mostafa.surveysapp.models;

import java.util.List;

public class Result {
    private String ownerId;
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

    public Result() {

    }

    public Result(String ownerId, List<Question> questions) {

        this.ownerId = ownerId;
        this.questions = questions;
    }
}