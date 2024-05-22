package models;

public class Postulator extends User {
	private String fullName;
	private String skills;
	private String resume;

	public Postulator(String email, String password, String phone, String fullName, String skills, String resume) {
		super(email, password, phone);
		this.fullName = fullName;
		this.skills = skills;
		this.resume = resume;
	}

	public Postulator(String fullName,String email, String password, String phone) {
		super(email, password, phone);
		this.fullName = fullName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	@Override
	public String toString() {
		return super.toString() + " Postulator [fullName=" + fullName + "]";
	}
	
}
