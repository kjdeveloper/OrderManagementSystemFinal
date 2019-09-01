package com.app.repository.converters;

import com.app.exceptions.MyException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class JsonConverter<T> {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public String toJson(final T element) {
        String jsonObject = null;

        try  {
            if (element == null) {
                throw new NullPointerException("Element is null");
            }
            jsonObject = gson.toJson(element);
        } catch (Exception e) {
            throw new MyException("TO JSON EXCEPTION");
        }
        return jsonObject;
    }

    public Optional<T> fromJson(final String element) {
        Optional<T> object = null;
        try {

            object = Optional.of(gson.fromJson(element, type));
        } catch (Exception e){
            throw new MyException("FROM JSON EXCEPTION");
        }
        return object;
    }
}
