package me.khun.studentmanagement.model.service.impl;

import java.util.List;

import me.khun.studentmanagement.model.dto.StudentDto;
import me.khun.studentmanagement.model.repo.StudentRepo;
import me.khun.studentmanagement.model.repo.exception.DataAccessException;
import me.khun.studentmanagement.model.service.StudentService;
import me.khun.studentmanagement.model.service.exception.ServiceException;
import me.khun.studentmanagement.model.service.validator.StudentValidator;
import me.khun.studentmanagement.model.service.validator.Validator;

public class StudentServiceImpl implements StudentService {
	
	private StudentRepo studentRepo;
	private Validator<StudentDto> studentValidator;
	
	public StudentServiceImpl(StudentRepo studentRepo) {
		this.studentRepo = studentRepo;
		this.studentValidator = new StudentValidator();
	}

	@Override
    public StudentDto save(StudentDto student) {
		
		var exp = studentValidator.validate(student);
		if (!exp.isEmpty()) {
			throw exp;
		}
		
		try {
			var entity = StudentDto.parse(student);
			if (student.getId() == null || student.getId().isBlank()) {
				var created = studentRepo.create(entity);
	        	return StudentDto.of(created);
	        }
			studentRepo.update(entity);
			return findById(student.getId());
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException("Could not create the student");
		}
    }

    @Override
    public boolean deleteById(String id) {
    	if (id == null || id.isBlank()) {
			throw new ServiceException("Invalid student id");
		}
		
        try {
        	return studentRepo.deleteById(id);
        } catch (DataAccessException e) {
        	throw new ServiceException("Could not delete this student.");
        }
    }

    @Override
    public StudentDto findById(String id) {
    	if (id == null || id.isBlank()) {
			return null;
		}
        
		try {
			var entity = studentRepo.findById(id);
			if (entity == null) {
				return null;
			}
			return StudentDto.of(entity);
		} catch (DataAccessException e) {
			throw new ServiceException("Could not find the student.");
		}
    }

    @Override
    public List<StudentDto> findAll() {
    	try {
			return StudentDto.ofList(studentRepo.findAll());
		} catch (DataAccessException e) {
			throw new ServiceException("Could not find the students.");
		}
    }

    @Override
    public List<StudentDto> search(String studentKeywrod, String courseKeyword) {
        studentKeywrod = studentKeywrod == null ? "" : studentKeywrod;
        courseKeyword = courseKeyword == null ? "" : courseKeyword;
        
        try {
        	return StudentDto.ofList(studentRepo.search(studentKeywrod, courseKeyword));
        } catch (DataAccessException e) {
        	throw new ServiceException("Could not search the students.");
        }
    }

    @Override
    public long getCount() {
    	try {
        	return studentRepo.getCount();
        } catch (DataAccessException e) {
        	throw new ServiceException("Could not get the student count.");
        }
    }

}