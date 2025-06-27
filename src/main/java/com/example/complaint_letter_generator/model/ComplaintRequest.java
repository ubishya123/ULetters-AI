package com.example.complaint_letter_generator.model;

public class ComplaintRequest {
    private String problemDescription;
    private String tone; // formal or informal

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }
}
