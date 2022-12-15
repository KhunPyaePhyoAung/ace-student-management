package me.khun.studentmanagement.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.khun.studentmanagement.model.entity.User;
import me.khun.studentmanagement.model.entity.User.Role;

public class UserDto {

    private String id;

    private String name;

    private String email;

    private String password;
    
    private String confirmPassword;
    
    private boolean approved;
    
    private Role role;
    
    public static UserDto of(User user) {
    	var dto = new UserDto();
    	dto.setId(user.getId());
    	dto.setName(user.getName());
    	dto.setEmail(user.getEmail());
    	dto.setPassword(user.getPassword());
    	dto.setConfirmPassword(user.getPassword());
    	dto.setRole(user.getRole());
    	dto.setApproved(user.isApproved());
    	return dto;
    }

    public static List<UserDto> ofList(List<User> users) {
    	var list = new ArrayList<UserDto>(users.size());
    	for (var u : users) {
    		list.add(of(u));
    	}
    	return list;
    }
    
    public static User parse(UserDto dto) {
    	var user = new User();
    	user.setId(dto.getId());
    	user.setName(dto.getName());
    	user.setEmail(dto.getEmail());
    	user.setPassword(dto.getPassword());
    	user.setRole(dto.getRole());
    	user.setApproved(dto.isApproved());
    	return user;
    }
    
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	@Override
	public int hashCode() {
		return Objects.hash(approved, confirmPassword, email, id, name, password, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDto other = (UserDto) obj;
		return approved == other.approved && Objects.equals(confirmPassword, other.confirmPassword)
				&& Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(password, other.password) && role == other.role;
	}

}