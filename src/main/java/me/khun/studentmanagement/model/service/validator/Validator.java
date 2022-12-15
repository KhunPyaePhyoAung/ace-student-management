package me.khun.studentmanagement.model.service.validator;

import me.khun.studentmanagement.model.service.exception.InvalidFieldException;

public interface Validator<T> {
	InvalidFieldException validate(T t);
}
