package com.dhlee.json.jackson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.function.Function;

public class JacksonPathWithGroupExample {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Ư�� ����� ���� �����ϴ� �Լ�
    public static String setValueAtPath(String jsonString, String path, String newValue) {
        try {
            // JSON ���ڿ��� JsonNode ��ü�� �Ľ�
            JsonNode rootNode = objectMapper.readTree(jsonString);

            // ��ο� �ش��ϴ� �θ� ��带 ������
            JsonNode parentNode = getParentNode(rootNode, getParentPath(path));

            // ����� ������ ��� �̸� �Ǵ� �迭 �ε��� ����
            String fieldNameOrIndex = getFieldName(path);

            // �迭 �Ǵ� ��ü���� ���� ����
            setNodeValue(parentNode, fieldNameOrIndex, newValue);

            // ������ JSON ���ڿ� ��ȯ
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Ư�� ����� ���� �������� �Լ�
    public static String getValueAtPath(String jsonString, String path) {
        try {
            // JSON ���ڿ��� JsonNode ��ü�� �Ľ�
            JsonNode rootNode = objectMapper.readTree(jsonString);

            // ��ο� �ش��ϴ� ����� ���� ��ȯ
            JsonNode resultNode = getNodeAtPath(rootNode, path);
            return resultNode != null ? resultNode.asText() : null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ���� ��ο� ��ȯ �Լ��� ���� ���� �ѹ��� �����ϴ� �Լ�
    public static String modifyValuesAtPaths(String jsonString, Map<String, Function<String, String>> pathFunctionMap) {
        try {
            // JSON ���ڿ��� JsonNode ��ü�� �Ľ�
            JsonNode rootNode = objectMapper.readTree(jsonString);

            for (Map.Entry<String, Function<String, String>> entry : pathFunctionMap.entrySet()) {
                String path = entry.getKey();
                Function<String, String> function = entry.getValue();

                // ���� ��ο� �ش��ϴ� �� ��������
                JsonNode currentNode = getNodeAtPath(rootNode, path);
                if (currentNode != null && currentNode.isValueNode()) {
                    // ��ȯ �Լ��� ����� ���ο� �� ����
                    String newValue = function.apply(currentNode.asText());

                    // ���ο� ���� ��ο� ����
                    setNodeValue(getParentNode(rootNode, getParentPath(path)), getFieldName(path), newValue);
                }
            }

            // ������ JSON ���ڿ� ��ȯ
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

    // ��ο��� �θ� ��θ� �����ϴ� ��ƿ��Ƽ �Լ�
    private static String getParentPath(String path) {
        int lastSlashIndex = path.lastIndexOf('/');
        return lastSlashIndex == 0 ? "/" : path.substring(0, lastSlashIndex);
    }

    // ��ο��� �ʵ� �̸� �Ǵ� �迭 �ε����� �����ϴ� ��ƿ��Ƽ �Լ�
    private static String getFieldName(String path) {
        int lastSlashIndex = path.lastIndexOf('/');
        return path.substring(lastSlashIndex + 1);
    }

    // Ư�� ��ο� �ش��ϴ� �θ� ��带 �������� ��ƿ��Ƽ �Լ�
    private static JsonNode getParentNode(JsonNode rootNode, String parentPath) {
        return getNodeAtPath(rootNode, parentPath);
    }

    // Ư�� ��ο� �ش��ϴ� ��带 �������� ��ƿ��Ƽ �Լ�
    private static JsonNode getNodeAtPath(JsonNode rootNode, String path) {
        String[] tokens = path.split("/");
        JsonNode currentNode = rootNode;

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (token.matches("\\d+")) {
                // �迭 �ε��� ó��
                currentNode = currentNode.get(Integer.parseInt(token));
            } else {
                // ��ü �ʵ� ó��
                currentNode = currentNode.get(token);
            }

            if (currentNode == null) {
                return null; // ��ΰ� �������� ����
            }
        }
        return currentNode;
    }

    public static void main(String[] args) {
        String jsonString = "{ \"person\": { \"name\": \"John\", \"age\": 30, \"groups\": [ { \"id\": 1, \"name\": \"Group A\" }, { \"id\": 2, \"name\": \"Group B\" }, { \"id\": 3, \"name\": \"Group C\" } ] } }";

        // ��θ� ����Ͽ� �� �������� (�ݺ��Ǵ� �׷� ����)
        String secondGroupName = getValueAtPath(jsonString, "/person/groups/1/name");
        System.out.println("Second Group Name: " + secondGroupName);

        // ��θ� ����Ͽ� �� �����ϱ� (�ݺ��Ǵ� �׷� ����) �� Pretty ���
        String updatedJsonString = setValueAtPath(jsonString, "/person/groups/2/name", "Group Z");
        System.out.println("Updated JSON (Pretty): \n" + updatedJsonString);

        // ���� ��ο� ���� �� ��ȯ ����
        Map<String, Function<String, String>> pathFunctionMap = Map.of(
                "/person/name", name -> name.toUpperCase(),
                "/person/age", age -> String.valueOf(Integer.parseInt(age) + 10),
                "/person/groups/0/name", groupName -> "New " + groupName
        );

        String modifiedJsonString = modifyValuesAtPaths(jsonString, pathFunctionMap);
        System.out.println("Modified JSON (Pretty): \n" + modifiedJsonString);
    }
}
