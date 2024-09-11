package com.dhlee.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonMultiLevelExample {

	public JacksonMultiLevelExample() {
	}
    
	private static final ObjectMapper objectMapper = new ObjectMapper();

    // 특정 경로의 값을 설정하는 함수
    public static String setValueAtPath(String jsonString, String path, String newValue, boolean isPretty) {
        try {
            // JSON 문자열을 JsonNode 객체로 파싱
            JsonNode rootNode = objectMapper.readTree(jsonString);

            // 경로에 해당하는 부모 노드를 가져옴
            JsonNode parentNode = getParentNode(rootNode, getParentPath(path));

            // 경로의 마지막 노드 이름 또는 배열 인덱스 추출
            String fieldNameOrIndex = getFieldName(path);

            // 배열 또는 객체에서 값을 설정
            if (parentNode.isArray()) {
                ((ArrayNode) parentNode).set(Integer.parseInt(fieldNameOrIndex), objectMapper.convertValue(newValue, JsonNode.class));
            } else if (parentNode.isObject()) {
                ((ObjectNode) parentNode).put(fieldNameOrIndex, newValue);
            }

            // 수정된 JSON 문자열 반환
            if(isPretty)
            	return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            else 
            	return objectMapper.writeValueAsString(rootNode);
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

        System.out.println("Source JSON: \n" + jsonString);
        
        // 경로를 사용하여 값 가져오기 (반복되는 그룹 포함)
        String getPath = "/person/groups/1/name";
        String secondGroupName = getValueAtPath(jsonString, getPath);
        System.out.println("getPath: " + getPath + " -> " + secondGroupName);

        // 경로를 사용하여 값 설정하기 (반복되는 그룹 포함) 및 Pretty 출력
        String putPath = "/person/groups/2/name";
        String updatedJsonString = setValueAtPath(jsonString, putPath, "Group Z", true);
        System.out.println("putPath: " + putPath);
        System.out.println("Updated JSON (Pretty): \n" + updatedJsonString);
        
        getPath = "/person/name";         
        System.out.println("getPath: " + getPath + " -> " + getValueAtPath(jsonString, getPath));
        getPath = "/person/age";         
        System.out.println("getPath: " + getPath + " -> " + getValueAtPath(jsonString, getPath));
        
        getPath = "/person/age1";         
        System.out.println("getPath: " + getPath + " -> " + getValueAtPath(jsonString, getPath));

    }
}
