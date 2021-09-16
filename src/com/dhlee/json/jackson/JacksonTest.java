package com.dhlee.json.jackson;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTest {

	public JacksonTest() {

	}
	
	public static void main(String[] args) {
//		test(false);
//		test(true);
		testPathFind();
	}
	public static void testPathFind() {
		String jsonString ="{"
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
		    JsonParser parser = factory.createParser(jsonString);
		    JsonNode root = mapper.readTree(parser);
		    
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
	
}
