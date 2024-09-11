package com.dhlee.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonMultiLevelExample {

	public JacksonMultiLevelExample() {
	}
    
	private static final ObjectMapper objectMapper = new ObjectMapper();

    // Ư�� ����� ���� �����ϴ� �Լ�
    public static String setValueAtPath(String jsonString, String path, String newValue, boolean isPretty) {
        try {
            // JSON ���ڿ��� JsonNode ��ü�� �Ľ�
            JsonNode rootNode = objectMapper.readTree(jsonString);

            // ��ο� �ش��ϴ� �θ� ��带 ������
            JsonNode parentNode = getParentNode(rootNode, getParentPath(path));

            // ����� ������ ��� �̸� �Ǵ� �迭 �ε��� ����
            String fieldNameOrIndex = getFieldName(path);

            // �迭 �Ǵ� ��ü���� ���� ����
            if (parentNode.isArray()) {
                ((ArrayNode) parentNode).set(Integer.parseInt(fieldNameOrIndex), objectMapper.convertValue(newValue, JsonNode.class));
            } else if (parentNode.isObject()) {
                ((ObjectNode) parentNode).put(fieldNameOrIndex, newValue);
            }

            // ������ JSON ���ڿ� ��ȯ
            if(isPretty)
            	return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            else 
            	return objectMapper.writeValueAsString(rootNode);
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

        System.out.println("Source JSON: \n" + jsonString);
        
        // ��θ� ����Ͽ� �� �������� (�ݺ��Ǵ� �׷� ����)
        String getPath = "/person/groups/1/name";
        String secondGroupName = getValueAtPath(jsonString, getPath);
        System.out.println("getPath: " + getPath + " -> " + secondGroupName);

        // ��θ� ����Ͽ� �� �����ϱ� (�ݺ��Ǵ� �׷� ����) �� Pretty ���
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
