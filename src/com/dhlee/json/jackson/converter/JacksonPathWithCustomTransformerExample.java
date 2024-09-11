package com.dhlee.json.jackson.converter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

public class JacksonPathWithCustomTransformerExample {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // ����� ���� �������̽�
    @FunctionalInterface
    public interface ValueTransformer {
        String transform(String value);
    }

    // Ư�� ����� ���� �����ϴ� �Լ�
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

    // Ư�� ����� ���� �������� �Լ�
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

    // ���� ��ο� ��ȯ �Լ��� ���� ���� �ѹ��� �����ϴ� �Լ�
    public static String modifyValuesAtPaths(String jsonString, Map<String, ValueTransformer> pathTransformerMap) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);

            for (Map.Entry<String, ValueTransformer> entry : pathTransformerMap.entrySet()) {
                String path = entry.getKey();
                ValueTransformer transformer = entry.getValue();

                JsonNode currentNode = getNodeAtPath(rootNode, path);
                if (currentNode != null && currentNode.isValueNode()) {
                    String newValue = transformer.transform(currentNode.asText());
                    setNodeValue(getParentNode(rootNode, getParentPath(path)), getFieldName(path), newValue);
                }
            }

            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ��忡 ���� �����ϴ� ��ƿ��Ƽ �Լ�
    private static void setNodeValue(JsonNode parentNode, String fieldNameOrIndex, String newValue) {
        if (parentNode.isArray()) {
            ((ArrayNode) parentNode).set(Integer.parseInt(fieldNameOrIndex), objectMapper.convertValue(newValue, JsonNode.class));
        } else if (parentNode.isObject()) {
            ((ObjectNode) parentNode).put(fieldNameOrIndex, newValue);
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

        // ��θ� ����Ͽ� �� ��������
        String secondGroupName = getValueAtPath(jsonString, "/person/groups/1/name");
        System.out.println("Second Group Name: " + secondGroupName);

        // ��θ� ����Ͽ� �� �����ϱ� �� Pretty ���
        String updatedJsonString = setValueAtPath(jsonString, "/person/groups/2/name", "Group Z");
        System.out.println("Updated JSON (Pretty): \n" + updatedJsonString);

        // ���� ��ο� ���� �� ��ȯ ����
        Map<String, ValueTransformer> pathTransformerMap = Map.of(
                "/person/name", value -> value.toUpperCase(),
                "/person/age", value -> String.valueOf(Integer.parseInt(value) + 10),
                "/person/groups/0/name", value -> "New " + value
        );

        String modifiedJsonString = modifyValuesAtPaths(jsonString, pathTransformerMap);
        System.out.println("Modified JSON (Pretty): \n" + modifiedJsonString);
    }
}
