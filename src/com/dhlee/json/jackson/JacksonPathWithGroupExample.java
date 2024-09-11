package com.dhlee.json.jackson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.function.Function;

public class JacksonPathWithGroupExample {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 특정 경로의 값을 설정하는 함수
    public static String setValueAtPath(String jsonString, String path, String newValue) {
        try {
            // JSON 문자열을 JsonNode 객체로 파싱
            JsonNode rootNode = objectMapper.readTree(jsonString);

            // 경로에 해당하는 부모 노드를 가져옴
            JsonNode parentNode = getParentNode(rootNode, getParentPath(path));

            // 경로의 마지막 노드 이름 또는 배열 인덱스 추출
            String fieldNameOrIndex = getFieldName(path);

            // 배열 또는 객체에서 값을 설정
            setNodeValue(parentNode, fieldNameOrIndex, newValue);

            // 수정된 JSON 문자열 반환
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 특정 경로의 값을 가져오는 함수
    public static String getValueAtPath(String jsonString, String path) {
        try {
            // JSON 문자열을 JsonNode 객체로 파싱
            JsonNode rootNode = objectMapper.readTree(jsonString);

            // 경로에 해당하는 노드의 값을 반환
            JsonNode resultNode = getNodeAtPath(rootNode, path);
            return resultNode != null ? resultNode.asText() : null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 여러 경로와 변환 함수를 통해 값을 한번에 변경하는 함수
    public static String modifyValuesAtPaths(String jsonString, Map<String, Function<String, String>> pathFunctionMap) {
        try {
            // JSON 문자열을 JsonNode 객체로 파싱
            JsonNode rootNode = objectMapper.readTree(jsonString);

            for (Map.Entry<String, Function<String, String>> entry : pathFunctionMap.entrySet()) {
                String path = entry.getKey();
                Function<String, String> function = entry.getValue();

                // 현재 경로에 해당하는 값 가져오기
                JsonNode currentNode = getNodeAtPath(rootNode, path);
                if (currentNode != null && currentNode.isValueNode()) {
                    // 변환 함수를 사용해 새로운 값 생성
                    String newValue = function.apply(currentNode.asText());

                    // 새로운 값을 경로에 설정
                    setNodeValue(getParentNode(rootNode, getParentPath(path)), getFieldName(path), newValue);
                }
            }

            // 수정된 JSON 문자열 반환
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 노드에 값을 설정하는 유틸리티 함수
    private static void setNodeValue(JsonNode parentNode, String fieldNameOrIndex, String newValue) {
        if (parentNode.isArray()) {
            ((ArrayNode) parentNode).set(Integer.parseInt(fieldNameOrIndex), objectMapper.convertValue(newValue, JsonNode.class));
        } else if (parentNode.isObject()) {
            ((ObjectNode) parentNode).put(fieldNameOrIndex, newValue);
        }
    }

    // 경로에서 부모 경로를 추출하는 유틸리티 함수
    private static String getParentPath(String path) {
        int lastSlashIndex = path.lastIndexOf('/');
        return lastSlashIndex == 0 ? "/" : path.substring(0, lastSlashIndex);
    }

    // 경로에서 필드 이름 또는 배열 인덱스를 추출하는 유틸리티 함수
    private static String getFieldName(String path) {
        int lastSlashIndex = path.lastIndexOf('/');
        return path.substring(lastSlashIndex + 1);
    }

    // 특정 경로에 해당하는 부모 노드를 가져오는 유틸리티 함수
    private static JsonNode getParentNode(JsonNode rootNode, String parentPath) {
        return getNodeAtPath(rootNode, parentPath);
    }

    // 특정 경로에 해당하는 노드를 가져오는 유틸리티 함수
    private static JsonNode getNodeAtPath(JsonNode rootNode, String path) {
        String[] tokens = path.split("/");
        JsonNode currentNode = rootNode;

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (token.matches("\\d+")) {
                // 배열 인덱스 처리
                currentNode = currentNode.get(Integer.parseInt(token));
            } else {
                // 객체 필드 처리
                currentNode = currentNode.get(token);
            }

            if (currentNode == null) {
                return null; // 경로가 존재하지 않음
            }
        }
        return currentNode;
    }

    public static void main(String[] args) {
        String jsonString = "{ \"person\": { \"name\": \"John\", \"age\": 30, \"groups\": [ { \"id\": 1, \"name\": \"Group A\" }, { \"id\": 2, \"name\": \"Group B\" }, { \"id\": 3, \"name\": \"Group C\" } ] } }";

        // 경로를 사용하여 값 가져오기 (반복되는 그룹 포함)
        String secondGroupName = getValueAtPath(jsonString, "/person/groups/1/name");
        System.out.println("Second Group Name: " + secondGroupName);

        // 경로를 사용하여 값 설정하기 (반복되는 그룹 포함) 및 Pretty 출력
        String updatedJsonString = setValueAtPath(jsonString, "/person/groups/2/name", "Group Z");
        System.out.println("Updated JSON (Pretty): \n" + updatedJsonString);

        // 여러 경로에 대해 값 변환 적용
        Map<String, Function<String, String>> pathFunctionMap = Map.of(
                "/person/name", name -> name.toUpperCase(),
                "/person/age", age -> String.valueOf(Integer.parseInt(age) + 10),
                "/person/groups/0/name", groupName -> "New " + groupName
        );

        String modifiedJsonString = modifyValuesAtPaths(jsonString, pathFunctionMap);
        System.out.println("Modified JSON (Pretty): \n" + modifiedJsonString);
    }
}
