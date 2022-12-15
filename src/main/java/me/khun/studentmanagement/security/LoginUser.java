package me.khun.studentmanagement.security;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import me.khun.studentmanagement.model.dto.UserDto;

public class LoginUser {
	private UserDto user;
	private LocalDateTime loginnedInDateTime;

	public UserDto getUser() {
		return user;
	}
	
	public void setUser(UserDto user) {
		this.user = user;
	}

	public LocalDateTime getLoginnedInDateTime() {
		return loginnedInDateTime;
	}

	public void setLoggedInDateTime(LocalDateTime loginnedInDateTime) {
		this.loginnedInDateTime = loginnedInDateTime;
	}
	
	public String getLoggedInDateTimeStirng() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(loginnedInDateTime);
	}

}
