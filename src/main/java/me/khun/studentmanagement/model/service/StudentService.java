package me.khun.studentmanagement.model.service;

import java.util.List;

import me.khun.studentmanagement.model.dto.StudentDto;

public interface StudentService {

    public StudentDto save(StudentDto student);

    public boolean deleteById(String id);

    public StudentDto findById(String id);

    public List<StudentDto> findAll();

    public List<StudentDto> search(String studentKeyword, String courseKeyword);

    public long getCount();

}