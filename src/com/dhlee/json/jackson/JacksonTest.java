package com.dhlee.json.jackson;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonTest {

	public JacksonTest() {

	}
	
	public static void main(String[] args) {
//		test(false);
//		test(true);
		testPathFind();
		testSetValues();
	}
	
	public static void testSetValues() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		ObjectNode root = mapper.createObjectNode();
		
		ObjectNode contact = mapper.createObjectNode();       
		contact.put("tel", "010-1111-2222");        
		contact.put("email", "dhlee@mail.com");         
		root.set("contact", contact);        
		
		ArrayNode arr = mapper.createArrayNode();
		arr.add("a");
		arr.add("b");        
		arr.add("c");
		arr.add("d");
		root.set("alphabet", arr);
		
		ObjectNode element1 = mapper.createObjectNode();
		element1.put("id", 1);
		element1.put("name", "Anna");
		
		ObjectNode element2 = mapper.createObjectNode();
		element2.put("id", 2);
		element2.put("name", "Brian");
		
		ObjectNode element3 = mapper.createObjectNode();
		element3.put("id", 3);        
		element3.put("name", "Sam");         
		
		ArrayNode students = mapper.createArrayNode();        
		students.add(element1);        
		students.addAll(Arrays.asList(element2, element3));         
		root.set("students", students);
			
//		System.out.println(root.toString());
		System.out.println(root.toPrettyString());
	}

	public static void testPathFind() {
		String jsonString = null;
		jsonString ="{"
				+"\n  \"id\": 1,"
				+"\n  \"name\": {"
				+"\n    \"first\": \"DongHoon\","
				+"\n    \"last\": \"Lee\""
				+"\n  },"
				+"\n  \"contact\": ["
				+"\n    {"
				+"\n      \"type\": \"phone/home\","
				+"\n      \"ref\": \"111-111-1234\""
				+"\n    },"
				+"\n    {"
				+"\n      \"type\": \"phone/work\","
				+"\n      \"ref\": \"222-222-2222\""
				+"\n    }"
				+"\n  ]"
				+"\n}";
//		jsonString = "{\"id\":\"1234\",\"name\":\"dhlee\",\"amount\":1234567890.01234567890123456789}";
		System.out.println(jsonString);
		
		traverse(jsonString);
		
		String path = "name1";
		System.out.println(path +"=" + testPath(jsonString, path));
		
		path = "name";
		System.out.println(path +"=" + testPath(jsonString, path));
		
		path = "name/first";
		System.out.println(path +"=" + testPath(jsonString, path));
		
		path = "contact/type";
		System.out.println(path +"=" + testPath(jsonString, path));
	}
	
	public static void test(boolean isBigDecimal) {
		try {
		    // create a reader
		    String data = "{\"id\":\"1234\", \"name\":\"dhlee\", \"amount\":1234567890.01234567890123456789}";
		    
		    System.out.println("Json String : " + data);
		    
		    ObjectMapper mapper = new ObjectMapper();
		    
		    if(isBigDecimal) mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		    
		    //read customer.json file into tree model
		    JsonFactory factory = mapper.getFactory();
		    JsonParser parser = factory.createParser(data);
		    JsonNode actualObj = mapper.readTree(parser);
		    
		    // read customer details
		    System.out.println(actualObj.path("id").asText());
		    System.out.println(actualObj.path("name").asText());
		    System.out.println("asText = " + actualObj.path("amount").asText());
		    System.out.println("decimalValue = " + actualObj.path("amount").decimalValue());
		    System.out.println("floatValue = " + actualObj.path("amount").floatValue());
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	
	public static String testPath(String jsonString, String path) {
		try {
//		    System.out.println("Json String : " + jsonString);
		    ObjectMapper mapper = new ObjectMapper();
		    mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		    //read customer.json file into tree model
		    JsonFactory factory = mapper.getFactory();
		    JsonParser parser = factory.createParser(jsonString);
		    JsonNode root = mapper.readTree(parser);
		    String[] paths = path.split("/");
		    JsonNode node = null;
		    boolean isFirst = true;
		    int i = 0;
		    for(String p : paths) {
		    	if(isFirst) {
		    		node = root.path(p);
		    		isFirst = false;
		    		System.out.println(i++ +" : " + node.isMissingNode());
		    		if(node.isMissingNode()) break;
		    	}
		    	else {
		    		node = node.path(p);
		    		System.out.println(i++ +" : " + node.isMissingNode());
		    		if(node.isMissingNode()) break;
		    	}
		    }
		    String findValue = null;
		    if(!node.isMissingNode()) {
		    	findValue = node.asText();
		    }
		    return findValue;
		} catch (Exception ex) {
		    ex.printStackTrace();
		    return null;
		}
	}
	
	public static void traverse(String jsonString) {
		try {
			ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(new File("c:\\projects\\user.json"));
		    JsonFactory factory = mapper.getFactory();
		    mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		    JsonParser parser = factory.createParser(jsonString);
		    
		    JsonNode root = mapper.readTree(parser);
		    
		    traverse(root, "");
            // Get id
            long id = root.path("id").asLong();
            System.out.println("id : " + id);

            // Get Name
            JsonNode nameNode = root.path("name");
            if (!nameNode.isMissingNode()) {        // if "name" node is exist
                System.out.println("firstName : " + nameNode.path("first").asText());
                System.out.println("middleName : " + nameNode.path("middle").asText());
                System.out.println("lastName : " + nameNode.path("last").asText());
            }

            // Get Contact
            JsonNode contactNode = root.path("contact");
            if (contactNode.isArray()) {
                System.out.println("isArray = " + contactNode.isArray());
                for (JsonNode node : contactNode) {
                    String type = node.path("type").asText();
                    String number = node.path("number").asText();
                    System.out.println("type : " + type);
                    System.out.println("number : " + number);
                }
            }
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static void traverse(JsonNode root, String parentName){
	    if(root.isObject()){
	        Iterator<String> fieldNames = root.fieldNames();
	        while(fieldNames.hasNext()) {
	            String fieldName = fieldNames.next();
	            JsonNode fieldValue = root.get(fieldName);
	            fieldName = genPath(parentName, fieldName);
	            traverse(fieldValue, fieldName);
	        }
	    } else if(root.isArray()){
	        ArrayNode arrayNode = (ArrayNode) root;
	        for(int i = 0; i < arrayNode.size(); i++) {
	            JsonNode arrayElement = arrayNode.get(i);
	            traverse(arrayElement, parentName);
	        }
	    } else {
	        System.out.println("@" + parentName + "=" +root.asText());
	    }
	}

	public static String genPath(String parent, String nodeName) {
		return parent +"." + nodeName;
	}
}

