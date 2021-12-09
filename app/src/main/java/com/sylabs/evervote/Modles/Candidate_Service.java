package com.sylabs.evervote.Modles;

public class Candidate_Service {
    private String SID,title,date,disc;

    public Candidate_Service(String SID, String title, String date, String disc) {
        this.SID = SID;
        this.title = title;
        this.date = date;
        this.disc = disc;
    }

    public Candidate_Service() {
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }
}
