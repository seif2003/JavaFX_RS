package models;

public class User {
	private String email;
	private String password;
	private String phone;
	
	public User(String email, String password, String phone) {
		this.email = email;
		this.password = password;
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + ", phone=" + phone + "]";
	}
	
}
