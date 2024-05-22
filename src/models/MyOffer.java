package models;

public class MyOffer {
    private int offer_id;
    private String job_title;
    private String etat;

    public MyOffer(int offer_id, String job_title, String etat) {
        this.offer_id = offer_id;
        this.job_title = job_title;
        this.etat = etat;
    }

    public int getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
