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
        // ��: ��ȯ�� �� "prefix"�� "suffix" �Ӽ��� ���
        String prefix = (String) properties.getOrDefault("PREFIX", "");
        String suffix = (String) properties.getOrDefault("SUFFIX", "");

        return prefix + value + suffix;
    }
}

