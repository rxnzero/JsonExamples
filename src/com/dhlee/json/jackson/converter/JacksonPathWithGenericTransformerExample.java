package com.dhlee.json.jackson.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

public class JacksonPathWithGenericTransformerExample {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 사용자 정의 인터페이스
//    public interface ValueTransformer<T, R> {
//        R transform(T value);
//    }

    // 특정 경로의 값을 설정하는 함수
    public static String setValueAtPath(String jsonString, String path, String newValue) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode parentNode = getParentNode(rootNode, getParentPath(path));
            String fieldNameOrIndex = getFieldName(path);
            setNodeValue(parentNode, fieldNameOrIndex, newValue);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 특정 경로의 값을 가져오는 함수
    public static String getValueAtPath(String jsonString, String path) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode resultNode = getNodeAtPath(rootNode, path);
            return resultNode != null ? resultNode.asText() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 여러 경로와 변환 함수를 통해 값을 한번에 변경하는 함수
    public static <T, R> String modifyValuesAtPaths(String jsonString, Map<String, ValueTransformer<T, R>> pathTransformerMap) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);

            for (Map.Entry<String, ValueTransformer<T, R>> entry : pathTransformerMap.entrySet()) {
                String path = entry.getKey();
                ValueTransformer<T, R> transformer = entry.getValue();

                JsonNode currentNode = getNodeAtPath(rootNode, path);
                if (currentNode != null && currentNode.isValueNode()) {
                    T currentValue = extractValue(currentNode); // 현재 값 가져오기
                    R newValue = transformer.transform(currentValue); // 변환된 값
                    setNodeValue(getParentNode(rootNode, getParentPath(path)), getFieldName(path), newValue);
                }
            }

            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 제네릭 타입으로 값을 추출하는 유틸리티 함수
    private static <T> T extractValue(JsonNode node) {
        if (node.isTextual()) {
            return (T) node.asText();
        } else if (node.isInt()) {
            return (T) (Integer) node.asInt();
        } else if (node.isDouble()) {
            return (T) (Double) node.asDouble();
        } else if (node.isBoolean()) {
            return (T) (Boolean) node.asBoolean();
        } else {
            return (T) node.asText(); // 기본적으로 텍스트로 처리
        }
    }

    // 노드에 값을 설정하는 유틸리티 함수
    private static <R> void setNodeValue(JsonNode parentNode, String fieldNameOrIndex, R newValue) {
        if (parentNode.isArray()) {
            ((ArrayNode) parentNode).set(Integer.parseInt(fieldNameOrIndex), objectMapper.convertValue(newValue, JsonNode.class));
        } else if (parentNode.isObject()) {
            ((ObjectNode) parentNode).set(fieldNameOrIndex, objectMapper.convertValue(newValue, JsonNode.class));
        }
    }

    private static String getParentPath(String path) {
        int lastSlashIndex = path.lastIndexOf('/');
        return lastSlashIndex == 0 ? "/" : path.substring(0, lastSlashIndex);
    }

    private static String getFieldName(String path) {
        int lastSlashIndex = path.lastIndexOf('/');
        return path.substring(lastSlashIndex + 1);
    }

    private static JsonNode getParentNode(JsonNode rootNode, String parentPath) {
        return getNodeAtPath(rootNode, parentPath);
    }

    private static JsonNode getNodeAtPath(JsonNode rootNode, String path) {
        String[] tokens = path.split("/");
        JsonNode currentNode = rootNode;

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (token.matches("\\d+")) {
                currentNode = currentNode.get(Integer.parseInt(token));
            } else {
                currentNode = currentNode.get(token);
            }

            if (currentNode == null) {
                return null;
            }
        }
        return currentNode;
    }

    public static void main(String[] args) {
        String jsonString = "{ \"person\": { \"name\": \"John\", \"age\": 30, \"groups\": [ { \"id\": 1, \"name\": \"Group A\" }, { \"id\": 2, \"name\": \"Group B\" }, { \"id\": 3, \"name\": \"Group C\" } ] } }";

        // 경로를 사용하여 값 가져오기
        String secondGroupName = getValueAtPath(jsonString, "/person/groups/1/name");
        System.out.println("Second Group Name: " + secondGroupName);

        // 경로를 사용하여 값 설정하기 및 Pretty 출력
        String updatedJsonString = setValueAtPath(jsonString, "/person/groups/2/name", "Group Z");
        System.out.println("Updated JSON (Pretty): \n" + updatedJsonString);
        CustomValueTransformer custom =new CustomValueTransformer();
        custom.setProperty("PREFIX", "PREFIX");
        custom.setProperty("SUFFIX", "SUFFIX");
        
        // 여러 경로에 대해 값 변환 적용
        Map<String, ValueTransformer<String, String>> pathTransformerMap = Map.of(
                "/person/name", custom, 
                "/person/groups/0/name", new ValueTransformer<String, String>() {
                    @Override
                    public String transform(String value) {
                        return "New " + value;
                    }

					@Override
					public void setProperty(String propertyName, Object value) {
						// TODO Auto-generated method stub
						
					}
                }
        );

        String modifiedJsonString = modifyValuesAtPaths(jsonString, pathTransformerMap);
        System.out.println("Modified JSON (Pretty): \n" + modifiedJsonString);
        
        // 예시: Integer -> Integer 변환 함수 사용
        Map<String, ValueTransformer<Integer, Integer>> intTransformerMap = Map.of(
                "/person/age", new ValueTransformer<Integer, Integer>() {
                    @Override
                    public Integer transform(Integer age) {
                        return age + 10;
                    }

					@Override
					public void setProperty(String propertyName, Object value) {
						// TODO Auto-generated method stub
						
					}
                }
        );

        String intModifiedJsonString = modifyValuesAtPaths(jsonString, intTransformerMap);
        System.out.println("Int Modified JSON (Pretty): \n" + intModifiedJsonString);
    }
}
