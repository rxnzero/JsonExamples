package com.dhlee.json.simple;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.eactive.json.simple.JSONObject;
import com.eactive.json.simple.JSONValue;

public class JsonTest {

	public JsonTest() {
	}

	public static void main(String[] args) {
//		double d = 0.0123456789012345678901234567890123456789;
//		d = Double.MAX_VALUE;
//		format(d);
//		
		jsonTest();
		
//		testMax();
		
//		testSetUrl();
	}
	
	public static void testSetUrl() {
		
//		JSONValue.escapeForwardSlash = false;
		// JSON 으로 파싱할 문자열
        String str = "{\"name\" : \"apple\", \"id\" : 1, \"price\" : 1000, \"url-org\":\"http://www.testcode.com\"}";
 
        // JSONParser로 JSONObject로 변환
        JSONObject jsonObject = (JSONObject) JSONValue.parse(str);
 
        // 추가
        jsonObject.put("url", "http://www.google.co.kr?code=1");
        jsonObject.put("code", "{\"sample\":\"json-code\"}");
        
        // 변경
        jsonObject.put("id", 2);
        jsonObject.replace("name", "banana");
 
        // 삭제
        jsonObject.remove("price");
        // 결과 출력
        System.out.println(jsonObject.toJSONString());        
    }
	
	public static void jsonTest() {
		String data = "{\"id\":\"1234\",\"name\":\"dhlee\",\"amount\":1234567890.01234567890123456789}";
		test(data);
		
		data = "{\"id\":\"1234\",\"name\":\"dhlee\",\"amount\":12345678901234.012}"; // 17자리까지 정상
		test(data);
		
		data = "{\"id\":\"1234\",\"name\":\"dhlee\",\"amount\":1.2}";
		test(data);
		
		data = "{\"id\":\"1234\",\"name\":\"dhlee\",\"amount\":1}";
		test(data);
	}
	
	
	public static void test(String jsonText) {
		try {
		    // create a reader
		    
		    
		    System.out.println("\nJson String : " + jsonText);
		    
		    // create parser
		    JSONObject  parser = (JSONObject)JSONValue.parse(jsonText);
		    
		    String xml = "<root><name>xml</name></root>";
		    parser.put("xml", xml);
		    
		    System.out.println("Json String : " + parser.toJSONString());
		    // read customer details
//		    String id = (String) parser.get("id");
//		    String name = (String) parser.get("name");
//		    Object amount = parser.get("amount");
//		    
		    
//		    System.out.println(id);
//		    System.out.println(name);
//		    System.out.println("amount = " + amount);
//		    if(amount instanceof Short)
//		    	System.out.println("Short BigDecimal = " + toBD((Short)amount) ) ;
//		    else if(amount instanceof Integer)
//		    	System.out.println("Integer BigDecimal = " + toBD((Integer)amount) ) ;
//		    else if(amount instanceof Long)
//		    	System.out.println("Long BigDecimal = " + toBD((Long)amount) ) ;
//		    else if(amount instanceof Float)
//		    	System.out.println("Float BigDecimal = " + toBD((Float)amount) ) ;
//		    else if(amount instanceof Double)
//		    	System.out.println("Double BigDecimal = " + toBD((Double)amount) ) ;
//		    else 
//		    	System.out.println("ELSE = " + amount ) ;
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	
	public static BigDecimal toBD(Object n) {
		if(n instanceof Short)
	    	return BigDecimal.valueOf((Short)n)  ;
	    else if(n instanceof Integer)
	    	return BigDecimal.valueOf((Integer)n)  ;
	    else if(n instanceof Long)
	    	return BigDecimal.valueOf((Long)n)  ;
	    else if(n instanceof Float)
	    	return BigDecimal.valueOf((Float)n)  ;
	    else if(n instanceof Double)
	    	return BigDecimal.valueOf((Double)n)  ;
	    else 
	    	return new BigDecimal((String)n)  ;
		
//		MathContext mc = MathContext.DECIMAL64; // DECIMAL64 16 digits , DECIMAL32 : 7 digits, DECIMAL128 : 34 digits
//		if(n instanceof Short)
//	    	return new BigDecimal((Short)n, mc )  ;
//	    else if(n instanceof Integer)
//	    	return new BigDecimal((Integer)n, mc )  ;
//	    else if(n instanceof Long)
//	    	return new BigDecimal((Long)n, mc )  ;
//	    else if(n instanceof Float)
//	    	return new BigDecimal((Float)n, mc )  ;
//	    else if(n instanceof Double)
//	    	return new BigDecimal((Double)n, mc )  ;
//	    else 
//	    	return new BigDecimal((String)n, mc )  ;		
	}
	
	public static void format(double d) {
		DecimalFormat format = new DecimalFormat("0.####################");
		String str = format.format(d);
		System.out.println("result = " + d  +" -> " + str);
	}
	
	public static void testMax() {
		Double maxLong= new Double(Long.MAX_VALUE);
		Double maxDouble = maxLong +2000;
		
		System.out.println(Long.MAX_VALUE);
		System.out.println(maxLong);
		System.out.println(maxDouble);
		System.out.println(maxDouble -  maxLong);
		if( (maxDouble - maxLong) > 0) {
			System.out.println(">> OverFlow" );
		}
	}
}
