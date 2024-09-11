package com.dhlee.json.jackson.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.util.HashMap;
import java.util.Map;

public class JsonPathWithPropertyTransformerExample {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // ValueTransformer �������̽�
    public interface ValueTransformer<T, R> {
        void setProperty(String propertyName, Object value);
        R transform(T value);
    }

    // CustomValueTransformer Ŭ����
    public static class CustomValueTransformer implements ValueTransformer<String, String> {

        private final Map<String, Object> properties = new HashMap<>();

        @Override
        public void setProperty(String propertyName, Object value) {
            properties.put(propertyName, value);
        }

        @Override
        public String transform(String value) {
            String prefix = (String) properties.getOrDefault("prefix", "");
            String suffix = (String) properties.getOrDefault("suffix", "");
            return prefix + value + suffix;
        }
    }

    // JSON �����͸� JsonPath�� ����� �����ϴ� �Լ�
    public static <T, R> String modifyValuesAtPaths(String jsonString, Map<String, ValueTransformer<T, R>> pathTransformerMap) {
        try {
            // JsonPath�� ���� ������ ���� ���ؽ�Ʈ ����
            DocumentContext documentContext = JsonPath.parse(jsonString);

            // �� ��ο� ���� ��ȯ �۾� ����
            for (Map.Entry<String, ValueTransformer<T, R>> entry : pathTransformerMap.entrySet()) {
                String path = entry.getKey();
                ValueTransformer<T, R> transformer = entry.getValue();

                // ���� �� ����
                T currentValue = documentContext.read(path);
                // ��ȯ�� ���� �� �� ����
                R newValue = transformer.transform(currentValue);
                // ���ο� ������ ����
                documentContext.set(path, newValue);
            }

            // ������ JSON ��ȯ
            return prettyPrintJson(documentContext.jsonString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JSON�� pretty�ϰ� ����ϴ� �޼���
    private static String prettyPrintJson(String jsonString) {
        try {
            Object jsonObject = objectMapper.readValue(jsonString, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return jsonString;
        }
    }

    public static void main(String[] args) {
        String jsonString = "{ \"person\": { \"name\": \"John\", \"age\": 30, \"groups\": [ { \"id\": 1, \"name\": \"Group A\" }, { \"id\": 2, \"name\": \"Group B\" }, { \"id\": 3, \"name\": \"Group C\" } ] } }";

        // CustomValueTransformer ��� ����
        CustomValueTransformer nameTransformer = new CustomValueTransformer();
        nameTransformer.setProperty("prefix", "Mr. ");
        nameTransformer.setProperty("suffix", " Esq.");

        CustomValueTransformer groupTransformer = new CustomValueTransformer();
        groupTransformer.setProperty("prefix", "Team: ");
        groupTransformer.setProperty("suffix", " Ltd.");

        // JsonPath�� ��ȯ�� ����
        Map<String, ValueTransformer<String, String>> pathTransformerMap = new HashMap<>();
        pathTransformerMap.put("$.person.name", nameTransformer);                // ���: person.name
        pathTransformerMap.put("$.person.groups[0].name", groupTransformer);     // ���: groups[0].name
        pathTransformerMap.put("$.person.groups[1].name", groupTransformer);     // ���: groups[1].name
        pathTransformerMap.put("$.person.groups[2].name", groupTransformer);     // ���: groups[2].name

        // ��ο� ���� �� ����
        String modifiedJsonString = modifyValuesAtPaths(jsonString, pathTransformerMap);
        System.out.println("Modified JSON (Pretty): \n" + modifiedJsonString);
    }
}
