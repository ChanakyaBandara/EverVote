package com.sylabs.evervote.Modles;

public class Candidate {

    private String ID;
    private String Name;
    private String Party;
    private String Followers;
    private String ImgURL;
    private String History;

    public Candidate() {
    }

    public Candidate(String ID, String name, String party, String followers, String imgURL, String history) {
        this.ID = ID;
        Name = name;
        Party = party;
        Followers = followers;
        ImgURL = imgURL;
        History = history;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getParty() {
        return Party;
    }

    public void setParty(String party) {
        Party = party;
    }

    public String getFollowers() {
        return Followers;
    }

    public void setFollowers(String followers) {
        Followers = followers;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String imgURL) {
        ImgURL = imgURL;
    }

    public String getHistory() {
        return History;
    }

    public void setHistory(String history) {
        History = history;
    }
}
