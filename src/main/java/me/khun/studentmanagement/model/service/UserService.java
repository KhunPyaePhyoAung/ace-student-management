package me.khun.studentmanagement.model.service;

import java.util.List;

import me.khun.studentmanagement.model.dto.UserDto;

public interface UserService {

    public UserDto save(UserDto user);

    public boolean deleteById(String id);

    public UserDto findById(String id);

    public List<UserDto> findAll();

    public List<UserDto> search(String keyword);
    
    public List<UserDto> search(String keyword, boolean approved);

    public long getCount();
    
    public long getCount(boolean approved);

}