package com.dhlee.json.jackson.converter;

public interface ValueTransformer<T, R> {
    void setProperty(String propertyName, Object value);
    R transform(T value);
}
