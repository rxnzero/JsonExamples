package com.dhlee.json.jackson.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.util.HashMap;
import java.util.Map;

public class JsonPathWithPropertyTransformerExample {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // ValueTransformer 인터페이스
    public interface ValueTransformer<T, R> {
        void setProperty(String propertyName, Object value);
        R transform(T value);
    }

    // CustomValueTransformer 클래스
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

    // JSON 데이터를 JsonPath를 사용해 수정하는 함수
    public static <T, R> String modifyValuesAtPaths(String jsonString, Map<String, ValueTransformer<T, R>> pathTransformerMap) {
        try {
            // JsonPath로 수정 가능한 문서 컨텍스트 생성
            DocumentContext documentContext = JsonPath.parse(jsonString);

            // 각 경로에 대해 변환 작업 수행
            for (Map.Entry<String, ValueTransformer<T, R>> entry : pathTransformerMap.entrySet()) {
                String path = entry.getKey();
                ValueTransformer<T, R> transformer = entry.getValue();

                // 현재 값 추출
                T currentValue = documentContext.read(path);
                // 변환기 통해 새 값 생성
                R newValue = transformer.transform(currentValue);
                // 새로운 값으로 설정
                documentContext.set(path, newValue);
            }

            // 수정된 JSON 반환
            return prettyPrintJson(documentContext.jsonString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JSON을 pretty하게 출력하는 메서드
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

        // CustomValueTransformer 사용 예제
        CustomValueTransformer nameTransformer = new CustomValueTransformer();
        nameTransformer.setProperty("prefix", "Mr. ");
        nameTransformer.setProperty("suffix", " Esq.");

        CustomValueTransformer groupTransformer = new CustomValueTransformer();
        groupTransformer.setProperty("prefix", "Team: ");
        groupTransformer.setProperty("suffix", " Ltd.");

        // JsonPath와 변환기 맵핑
        Map<String, ValueTransformer<String, String>> pathTransformerMap = new HashMap<>();
        pathTransformerMap.put("$.person.name", nameTransformer);                // 경로: person.name
        pathTransformerMap.put("$.person.groups[0].name", groupTransformer);     // 경로: groups[0].name
        pathTransformerMap.put("$.person.groups[1].name", groupTransformer);     // 경로: groups[1].name
        pathTransformerMap.put("$.person.groups[2].name", groupTransformer);     // 경로: groups[2].name

        // 경로에 따라 값 수정
        String modifiedJsonString = modifyValuesAtPaths(jsonString, pathTransformerMap);
        System.out.println("Modified JSON (Pretty): \n" + modifiedJsonString);
    }
}
