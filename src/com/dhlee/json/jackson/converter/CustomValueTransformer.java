package com.dhlee.json.jackson.converter;

import java.util.HashMap;
import java.util.Map;

public class CustomValueTransformer implements ValueTransformer<String, String> {

    private final Map<String, Object> properties = new HashMap<>();

    @Override
    public void setProperty(String propertyName, Object value) {
        properties.put(propertyName, value);
    }

    @Override
    public String transform(String value) {
        // 예: 변환할 때 "prefix"와 "suffix" 속성을 사용
        String prefix = (String) properties.getOrDefault("PREFIX", "");
        String suffix = (String) properties.getOrDefault("SUFFIX", "");

        return prefix + value + suffix;
    }
}

