package com.dhlee.json.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTest {

	public JacksonTest() {

	}
	
	public static void main(String[] args) {
		test(false);
		test(true);
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
	
}
