package models;

import java.time.LocalDateTime;

public class Offer {
    private int id;
    private String jobTitle;
    private double maxSalary;
    private double minSalary;
    private String description;
    private String company;
    private String category;
    private LocalDateTime dateOfCreation;
    private String skills;
    
    private String image;

    public Offer(int id, String jobTitle, double maxSalary, double minSalary, String description, String company, String category, LocalDateTime dateOfCreation, String skills) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.maxSalary = maxSalary;
        this.minSalary = minSalary;
        this.description = description;
        this.company = company;
        this.category = category;
        this.dateOfCreation = dateOfCreation;
        this.skills = skills;
    }

    // Getters et setters pour dateOfCreation
    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    // Autres getters et setters...
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(double minSalary) {
        this.minSalary = minSalary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
}
