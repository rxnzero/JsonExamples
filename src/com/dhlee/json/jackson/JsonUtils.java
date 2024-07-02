package com.dhlee.json.jackson;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	public JsonUtils() {

	}

	public static boolean isJSONValid(String jsonInString) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(jsonInString);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static void main(String[] argv) {
		String jsonString = "123";
		System.out.println(jsonString + "\nisJSONValid=" +isJSONValid(jsonString));
		jsonString = "{\"test\": \"code\"}";
		System.out.println(jsonString + "\nisJSONValid=" +isJSONValid(jsonString));
	}
}
