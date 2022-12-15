package me.khun.studentmanagement.model.service.impl;

import java.util.List;

import me.khun.studentmanagement.model.dto.UserDto;
import me.khun.studentmanagement.model.repo.UserRepo;
import me.khun.studentmanagement.model.repo.exception.DataAccessException;
import me.khun.studentmanagement.model.service.UserService;
import me.khun.studentmanagement.model.service.exception.InvalidFieldException;
import me.khun.studentmanagement.model.service.exception.ServiceException;
import me.khun.studentmanagement.model.service.validator.UserValidator;
import me.khun.studentmanagement.model.service.validator.Validator;

public class UserServiceImpl implements UserService {
	
	private UserRepo userRepo;
	private Validator<UserDto> userValidator;
	
	public UserServiceImpl(UserRepo userRepo) {
		this.userRepo = userRepo;
		this.userValidator = new UserValidator();
	}

	@Override
    public UserDto save(UserDto user) {
		var exp = userValidator.validate(user);
		if (!exp.isEmpty()) {
			throw exp;
		}
		
		try {
			var entity = UserDto.parse(user);
			
			
			if (user.getId() == null || user.getId().isBlank()) {
				var created = userRepo.create(entity);
	        	return UserDto.of(created);
	        }
			userRepo.update(entity);
			return user;
		} catch (DataAccessException e) {
			exp = new InvalidFieldException();
			exp.reject("email", "A user with this email already exists.");
			throw exp;
		}
    }

	@Override
    public boolean deleteById(String id) {
		if (id == null || id.isBlank()) {
			throw new ServiceException("Invalid user id");
		}
		
        try {
        	return userRepo.deleteById(id);
        } catch (DataAccessException e) {
        	throw new ServiceException("Could not delete this user.");
        }
    }

	@Override
    public UserDto findById(String id) {
		if (id == null || id.isBlank()) {
			return null;
		}
        
		try {
			var entity = userRepo.findById(id);
			if (entity == null) {
				return null;
			}
			return UserDto.of(entity);
		} catch (DataAccessException e) {
			throw new ServiceException("Could not find the user.");
		}
    }

	@Override
    public List<UserDto> findAll() {
		try {
			return UserDto.ofList(userRepo.findAll());
		} catch (DataAccessException e) {
			throw new ServiceException("Could not find the users.");
		}
    }

	@Override
    public List<UserDto> search(String keyword) {
		keyword = keyword == null ? "" : keyword;
        try {
        	return UserDto.ofList(userRepo.search(keyword));
        } catch (DataAccessException e) {
        	throw new ServiceException("Could not search the users.");
        }
    }
	
	@Override
	public List<UserDto> search(String keyword, boolean approved) {
		keyword = keyword == null ? "" : keyword;
        try {
        	return UserDto.ofList(userRepo.search(keyword, approved));
        } catch (DataAccessException e) {
        	throw new ServiceException("Could not search the users.");
        }
	}

	@Override
    public long getCount() {
        try {
        	return userRepo.getCount();
        } catch (DataAccessException e) {
        	throw new ServiceException("Could not get the user count.");
        }
    }
	
	@Override
	public long getCount(boolean approved) {
		try {
        	return userRepo.getCount(approved);
        } catch (DataAccessException e) {
        	throw new ServiceException("Could not get the user count.");
        }
	}

}