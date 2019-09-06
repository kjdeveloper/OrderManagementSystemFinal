package com.app.repository.converters;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class JsonConverter<T> {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public String toJsonView(final T element) {
        if (element == null) {
            throw new NullPointerException("Element is null");
        }

        String json = null;

        try {
            json = gson.toJson(element);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.TO_JSON_EXCEPTION, "TO JSON EXCEPTION");
        }

        return json;
    }

    public Optional<T> fromJson(final String element) {
        try {
            return Optional.of(gson.fromJson(element, type));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.FROM_JSON_EXCEPTION, "FROM JSON EXCEPTION");
        }
    }
}
