package me.khun.studentmanagement.model.entity;

import java.util.Objects;

public class User {

	public enum Role {
		ADMIN, USER
	}
	
    private String id;

    private String name;

    private String email;

    private String password;
    
    private Role role;
    
    private boolean approved;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	@Override
	public int hashCode() {
		return Objects.hash(approved, email, id, name, password, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return approved == other.approved && Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(password, other.password) && role == other.role;
	}

}