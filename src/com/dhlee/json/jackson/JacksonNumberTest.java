package com.dhlee.json.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class JacksonNumberTest {

    public static void testOptions(ObjectMapper mapper, String testJsonString) throws JsonProcessingException {
        JsonNode jsonNode =  mapper.readTree(testJsonString);
        System.out.println(testJsonString + " => " + jsonNode);
    }

    public static void test(ObjectMapper mapper) throws JsonProcessingException {
        testOptions(mapper, "{\"case1\": 100.00}");
        testOptions(mapper, "{\"case2\": 1000000000000.01}");
    }

    public static void main(String[] args) throws JsonProcessingException {
        System.out.println("ONLY DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        test(mapper);


        System.out.println("ONLY JsonNodeFactory.withExactBigDecimals(true)");
        mapper = new ObjectMapper();
        mapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
        test(mapper);

        System.out.println("DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS and JsonNodeFactory.withExactBigDecimals(true)");
        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        mapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
        test(mapper);
    }
}
