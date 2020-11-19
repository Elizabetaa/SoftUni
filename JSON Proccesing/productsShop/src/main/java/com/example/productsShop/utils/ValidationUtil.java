package com.example.productsShop.utils;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidationUtil {

    <T> boolean isValid(T entity);

    <T>Set<ConstraintViolation<T>> getValidation(T entity);


}
