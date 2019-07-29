package com.app.validation.generic;

import java.util.Map;

public interface Validator<T> {

    Map<String, String> validate(T t);
    boolean hasErrors();

}
