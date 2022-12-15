package me.khun.studentmanagement.model.service;

import java.util.List;

import me.khun.studentmanagement.model.dto.CourseDto;

public interface CourseService {

    public CourseDto save(CourseDto course);

    public boolean deleteById(String id);

    public CourseDto findById(String id);

    public List<CourseDto> findAll();

    public List<CourseDto> search(String keyword);

    public long getCount();

}