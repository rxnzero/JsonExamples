package com.dhlee.json.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

public class UserDeserializer extends StdDeserializer<SimpleUser>{

	protected UserDeserializer() {
		this(null);
	}
	
	protected UserDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public SimpleUser deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
		
		JsonNode node = jp.getCodec().readTree(jp);
        int age = (Integer) ((IntNode) node.get("age")).numberValue();
        String name = node.get("name").asText();
        String type = node.get("type").asText();
        String info = node.get("info").asText();
        
        return new SimpleUser(name, age, new Contact(type, info));
	}

}
