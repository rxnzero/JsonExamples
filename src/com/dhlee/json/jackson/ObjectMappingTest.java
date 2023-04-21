package com.dhlee.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMappingTest {

	public ObjectMappingTest() {
		// TODO Auto-generated constructor stub
	}
	
	private static String toJson(SimpleUser user) {
		String jsonString = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonString = mapper.writeValueAsString(user);
			System.out.println(jsonString);
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
            System.out.println(jsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
	private static SimpleUser toUser(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		SimpleUser user = null;
		try {
			user = mapper.readValue(jsonString, SimpleUser.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        System.out.println(user);
        return user;
	}
	public static void main(String[] args) {
		SimpleUser user = new SimpleUser();
		user.setName("홍길동");
		user.setAge(20);
		
		Contact contact = new Contact();
		contact.setType("Phone");
		contact.setInfo("02-123-1234");
		user.setContact(contact);
		user.addContact(contact);
		
		contact = new Contact();
		contact.setType("핸드폰");
		contact.setInfo("010-2233-4455");
		user.addContact(contact);
		
		
		System.out.println(user);
		String jsonStr = toJson(user);
		System.out.println(jsonStr);
		SimpleUser user1 = toUser(jsonStr);
		System.out.println(user1);

	}

}
