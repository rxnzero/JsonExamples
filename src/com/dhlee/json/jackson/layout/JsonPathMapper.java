package com.dhlee.json.jackson.layout;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.util.*;

public class JsonPathMapper {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String inputJson = 
        		"{"
        				+"	\"dataPtrnVl\":\"application/json\","
        				+"	\"apiClotDsncVl\":\"1234567890123456789012345678901234567890\","
        				+"	\"acsTknVl\":\"1234567890123456789012345678901234567890\","
        				+"	\"mydtTmUqn\":\"1234567890123456789012345678901234567890\","
        				+"	\"oapiClntId\":\"1234567890123456789012345678901234567890\","
        				+"	\"oapiClntPwd\":\"1234567890123456789012345678901234567890\","
        				+"	\"mydtSvcUrlCon\":\"http://localhost:38080/api/bigdata\","
        				+"	\"sttcInfoDcd\":\"Q\","
        				+"	\"mydtClntId\":\"Client0001Client0002Client0003Client0004Client0005\","
        				+"	\"trmsYmd\":\"20250410\","
        				+"	\"statDateListRowcount\":1,"
        				+"	\"statDateList\":["
        				+"	{ "
        				+"		\"sttcYmd\":\"20250410\","
        				+"		\"mydtCosnCusCnt\":1000,"
        				+"		\"mydtAstLnknCusCnt\":100,"
        				+"		\"orgListRowcount\":1,"
        				+"		\"orgList\":["
        				+"		{"
        				+"			\"mydtSvcOfricd\":\"0000000001\","
        				+"			\"newTrmsDmanCnt\":100,"
        				+"			\"rclTrmsDmanCnt\":200,"
        				+"			\"lastValdTrmsDmanCnt\":0,"
        				+"			\"apiTypeListRowcount\":1,"
        				+"			\"apiTypeList\":["
        				+"			{"
        				+"				\"mydtApiDcd\":\"0001\","
        				+"				\"tmSlotListRowcount\":1,"
        				+"				\"tmSlotList\":["
        				+"				{"
        				+"					\"mydtSttcTimBdCd\":\"00\","
        				+"					\"avvlRspnTim\":1,"
        				+"					\"allRspnTim\":2,"
        				+"					\"stdeRspnTim\":3,"
        				+"					\"apiClotScsNbt\":4,"
        				+"					\"apiClotFirNbt\":5,"
        				+"					\"confSyblsncErrNbt4001\":4001,"
        				+"					\"confSyblsncErrNbt4051\":1000000000,"
        				+"					\"confSyblsncErrNbt3021\":1000000000,"
        				+"					\"confSyblsncErrNbt3022\":1000000000,"
        				+"					\"confSyblsncErrNbt3023\":1000000000,"
        				+"					\"confSyblsncErrNbt3024\":1000000000,"
        				+"					\"confSyblsncErrNbt3025\":1000000000,"
        				+"					\"confSyblsncErrNbt3026\":1000000000,"
        				+"					\"confSyblsncErrNbt3027\":1000000000,"
        				+"					\"acsTknlsncErrNbt4001\":1000000000,"
        				+"					\"acsTknlsncErrNbt4002\":1000000000,"
        				+"					\"acsTknlsncErrNbt4003\":1000000000,"
        				+"					\"acsTknlsncErrNbt4004\":1000000000,"
        				+"					\"acsTknlsncErrNbt4005\":1000000000,"
        				+"					\"acsTknlsncErrNbt4006\":1000000000,"
        				+"					\"acsTknlsncErrNbt4007\":1000000000,"
        				+"					\"acsTknlsncErrNbt4051\":1000000000,"
        				+"					\"acsTknlsncErrNbt5001\":1000000000,"
        				+"					\"acsTknlsncErrNbt5031\":1000000000,"
        				+"					\"bshtApiErrNbt2000\":1000000000,"
        				+"					\"bshtApiErrNbt2001\":1000000000,"
        				+"					\"bshtApiErrNbt4001\":1000000000,"
        				+"					\"bshtApiErrNbt4002\":1000000000,"
        				+"					\"bshtApiErrNbt4003\":1000000000,"
        				+"					\"bshtApiErrNbt4004\":1000000000,"
        				+"					\"bshtApiErrNbt4011\":1000000000,"
        				+"					\"bshtApiErrNbt4012\":1000000000,"
        				+"					\"bshtApiErrNbt4013\":1000000000,"
        				+"					\"bshtApiErrNbt4014\":1000000000,"
        				+"					\"bshtApiErrNbt4015\":1000000000,"
        				+"					\"bshtApiErrNbt4016\":1000000000,"
        				+"					\"bshtApiErrNbt4031\":1000000000,"
        				+"					\"bshtApiErrNbt4032\":1000000000,"
        				+"					\"bshtApiErrNbt4033\":1000000000,"
        				+"					\"bshtApiErrNbt4034\":1000000000,"
        				+"					\"bshtApiErrNbt4035\":1000000000,"
        				+"					\"bshtApiErrNbt4041\":1000000000,"
        				+"					\"bshtApiErrNbt4042\":1000000000,"
        				+"					\"bshtApiErrNbt4043\":1000000000,"
        				+"					\"bshtApiErrNbt4051\":1000000000,"
        				+"					\"bshtApiErrNbt4291\":1000000000,"
        				+"					\"bshtApiErrNbt5001\":1000000000,"
        				+"					\"bshtApiErrNbt5002\":1000000000,"
        				+"					\"bshtApiErrNbt5003\":1000000000,"
        				+"					\"bshtApiErrNbt5004\":1000000000,"
        				+"					\"bshtApiErrNbt5005\":1000000000,"
        				+"					\"bshtApiErrNbt5006\":1000000000,"
        				+"					\"bshtApiErrNbt5007\":1000000000,"
        				+"					\"bshtApiErrNbt5008\":1000000000,"
        				+"					\"bshtApiErrNbt5009\":1000000000,"
        				+"					\"bshtApiErrNbt5000\":1000000000"
        				+"				}"
        				+"				]"
        				+"			}"
        				+"			]"
        				+"		}"
        				+"		]"
        				+"	}"
        				+"	]"
        				+"}";

		Map<String, String> pathMap = new LinkedHashMap<>();
		pathMap.put("httpHeaderGroup.Content-Type", removeLayoutName("MDG_APBO00077741S01_TGTS.dataPtrnVl"));
		pathMap.put("httpHeaderGroup.x-api-type", "apiClotDsncVl");
		pathMap.put("httpHeaderGroup.baseurl", "mydtSvcUrlCon");
		pathMap.put("sttcInfoDcd", "sttcInfoDcd");
		pathMap.put("mydtClntId", "mydtClntId");
		pathMap.put("trmsYmd", "trmsYmd");
		pathMap.put("statDateListRowcount", "statDateListRowcount");
		pathMap.put("statDateList[*].sttcYmd", "statDateList[*].sttcYmd");
		pathMap.put("statDateList[*].orgList[*].mydtSvcOfricd", "statDateList[*].orgList[*].mydtSvcOfricd");
		pathMap.put("statDateList[*].orgList[*].apiTypeList[*].mydtApiDcd",
				"statDateList[*].orgList[*].apiTypeList[*].mydtApiDcd");
		pathMap.put("statDateList[*].orgList[*].apiTypeList[*].tmSlotListRowcount",
				"statDateList[*].orgList[*].apiTypeList[*].tmSlotListRowcount");

		pathMap.put("statDateList[*].orgList[*].apiTypeList[*].tmSlotList[*].mydtSttcTimBdCdNew",
				"statDateList[*].orgList[*].apiTypeList[*].tmSlotList[*].mydtSttcTimBdCd");
		pathMap.put("statDateList[*].orgList[*].apiTypeList[*].tmSlotList[*].confSyblsncErrNbt4001What",
				"statDateList[*].orgList[*].apiTypeList[*].tmSlotList[*].confSyblsncErrNbt4001");

        JsonNode inputNode = mapper.readTree(inputJson);
        ObjectNode outputNode = mapper.createObjectNode();

        for (Map.Entry<String, String> entry : pathMap.entrySet()) {
            applyPathMapping(inputNode, outputNode, entry.getKey(), entry.getValue());
        }
        System.out.println("origin:\n" +inputJson);
        System.out.println("converted:\n");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(outputNode));
    }

    private static void applyPathMapping(JsonNode inputNode, ObjectNode outputNode, String targetPath, String sourcePath) {
        String[] targetTokens = targetPath.split("\\.");
        String[] sourceTokens = sourcePath.split("\\.");

        List<JsonNode> sourceValues = extractValuesByPath(inputNode, Arrays.asList(sourceTokens), 0);
        injectValuesByPath(outputNode, Arrays.asList(targetTokens), 0, sourceValues);
    }

    private static List<JsonNode> extractValuesByPath(JsonNode current, List<String> tokens, int index) {
        if (index >= tokens.size()) return Collections.singletonList(current);

        String token = tokens.get(index);
        boolean isArray = token.endsWith("[*]");
        String cleanKey = isArray ? token.substring(0, token.length() - 3) : token;

        JsonNode nextNode = current.get(cleanKey);
        List<JsonNode> results = new ArrayList<>();

        if (nextNode == null) return results;

        if (isArray && nextNode.isArray()) {
            for (JsonNode item : nextNode) {
                results.addAll(extractValuesByPath(item, tokens, index + 1));
            }
        } else {
            results.addAll(extractValuesByPath(nextNode, tokens, index + 1));
        }

        return results;
    }

    private static void injectValuesByPath(ObjectNode root, List<String> tokens, int index, List<JsonNode> values) {
        if (index >= tokens.size()) return;

        String token = tokens.get(index);
        boolean isArray = token.endsWith("[*]");
        String cleanKey = isArray ? token.substring(0, token.length() - 3) : token;

        if (index == tokens.size() - 1) {
            // 마지막 토큰 - 값 주입
            if (isArray) {
                ArrayNode arrayNode = root.withArray(cleanKey);
                for (JsonNode value : values) {
                    arrayNode.add(value);
                }
            } else {
                if (!values.isEmpty()) {
                    JsonNode first = values.get(0);
                    if (first.isTextual()) {
                        root.put(cleanKey, first.asText());
                    } else if (first.isNumber()) {
                    	if (first.isInt()) {
                            root.put(cleanKey, first.intValue());
                        } else if (first.isLong()) {
                            root.put(cleanKey, first.longValue());
                        } else if (first.isFloat()) {
                            root.put(cleanKey, first.floatValue());
                        } else if (first.isDouble()) {
                            root.put(cleanKey, first.doubleValue());
                        } else if (first.isBigDecimal()) {
                            root.put(cleanKey, first.decimalValue());
                        } else if (first.isBigInteger()) {
                            root.put(cleanKey, first.bigIntegerValue());
                        } else {
                            root.put(cleanKey, first.numberValue().toString()); // fallback
                        }
                    } else {
                        root.set(cleanKey, first); // fallback for objects/arrays
                    }
                }
            }
        } else {
            if (isArray) {
                ArrayNode nextArray = root.withArray(cleanKey);
                while (nextArray.size() < values.size()) {
                    nextArray.add(JsonNodeFactory.instance.objectNode());
                }
                for (int i = 0; i < values.size(); i++) {
                    if (nextArray.get(i).isObject()) {
                        injectValuesByPath((ObjectNode) nextArray.get(i), tokens, index + 1, Collections.singletonList(values.get(i)));
                    }
                }
            } else {
                ObjectNode nextObj = root.with(cleanKey);
                if (!values.isEmpty()) {
                    injectValuesByPath(nextObj, tokens, index + 1, values);
                }
            }
        }
    }
    
    public static String removeLayoutName(String originalString) {
        int firstDotIndex = originalString.indexOf('.');
        if (firstDotIndex != -1) {
            return originalString.substring(firstDotIndex + 1);
        }
        return originalString;
    }
}

