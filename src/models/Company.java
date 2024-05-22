package models;

public class Company extends User {
	private String name;
	private String location;
	private String logo;
	
	
	
	public Company(String email, String password, String phone, String name, String location, String logo) {
		super(email, password, phone);
		this.name = name;
		this.location = location;
		this.logo = logo;
	}

	public Company(String name, String location,String email, String password, String phone) {
		super(email, password, phone);
		this.name = name;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Override
	public String toString() {
		return super.toString() + "Company [name=" + name + ", location=" + location + "]";
	}
	
	
}
