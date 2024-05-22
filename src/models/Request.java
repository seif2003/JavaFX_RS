package models;

import java.time.LocalDateTime;

public class Request {
    private String details;
    private String postulator;
    private int offer;
    private LocalDateTime dateOfCreation; 
    private String etat;

    public Request(String details, String postulator, int offer, LocalDateTime dateOfCreation) {
        this.details = details;
        this.postulator = postulator;
        this.offer = offer;
        this.dateOfCreation = dateOfCreation;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPostulator() {
        return postulator;
    }

    public void setPostulator(String postulator) {
        this.postulator = postulator;
    }

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	@Override
    public String toString() {
        return "Request [details=" + details + ", postulator=" + postulator + ", offer=" + offer 
                + ", dateOfCreation=" + dateOfCreation + "]";
    }   
}
