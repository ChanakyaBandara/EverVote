package com.sylabs.evervote.Modles;

public class Post {
    private String ID;
    private String Title;
    private String Description;
    private String ImgURL;
    private String Date;
    private String CandidateName;
    private String CandidateParty;
    private String Time;

    public Post() {
    }

    public Post(String ID, String title, String description, String imgURL, String date, String candidateName, String candidateParty, String time) {
        this.ID = ID;
        Title = title;
        Description = description;
        ImgURL = imgURL;
        Date = date;
        CandidateName = candidateName;
        CandidateParty = candidateParty;
        Time = time;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String imgURL) {
        ImgURL = imgURL;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCandidateName() {
        return CandidateName;
    }

    public void setCandidateName(String candidateName) {
        CandidateName = candidateName;
    }

    public String getCandidateParty() {
        return CandidateParty;
    }

    public void setCandidateParty(String candidateParty) {
        CandidateParty = candidateParty;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
