package com.dhlee.json.jackson.layout;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class JsonFieldRenamer {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json =
            "{"
            + "\"dataPtrnVl\":\"1234567890123456789012345678901234567890\","
            + "\"apiClotDsncVl\":\"BIGTEST00001\","
            + "\"mydtSvcUrlCon\":\"http://localhost:38080/api/bigdata\","
            + "\"sttcInfoDcd\":\"Q\","
            + "\"mydtClntId\":\"Client0001Client0002Client0003Client0004Client0005\","
            + "\"trmsYmd\":\"20250410\","
            + "\"statDateListRowcount\":10,"
            + "\"statDateList\":["
            + "{"
            + "\"sttcYmd\":\"20250410\","
            + "\"mydtCosnCusCnt\":1000,"
            + "\"mydtAstLnknCusCnt\":100,"
            + "\"orgListRowcount\":1,"
            + "\"orgList\":["
            + "{"
            + "\"mydtSvcOfricd\":\"0000000001\","
            + "\"newTrmsDmanCnt\":100"
            + "}"
            + "]"
            + "}"
            + "]"
            + "}";

        // 필드명 매핑 정의 (원래이름 -> 새이름)
        Map<String, String> fieldRenameMap = new LinkedHashMap<>();
        fieldRenameMap.put("dataPtrnVl", "Content-Type");
        fieldRenameMap.put("apiClotDsncVl", "x-api-type");
        fieldRenameMap.put("mydtSvcUrlCon", "baseurl");
        fieldRenameMap.put("sttcInfoDcd", "sttcCode");
        fieldRenameMap.put("mydtClntId", "clientId");
        fieldRenameMap.put("trmsYmd", "transDate");
        fieldRenameMap.put("statDateListRowcount", "statRowCount");
        fieldRenameMap.put("statDateList", "statList");
        fieldRenameMap.put("sttcYmd", "statDate");
        fieldRenameMap.put("mydtCosnCusCnt", "consentCustomerCount");
        fieldRenameMap.put("mydtAstLnknCusCnt", "assetLinkedCustomerCount");
        fieldRenameMap.put("orgListRowcount", "orgRowCount");
        fieldRenameMap.put("orgList", "organizationList");
        fieldRenameMap.put("mydtSvcOfricd", "organizationCode");
        // 지정하지 않은 경우 그대로 전달
//        fieldRenameMap.put("newTrmsDmanCnt", "newDemandCount");

        // JSON 파싱
        Map<String, Object> original = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> result = new LinkedHashMap<>();

        // httpHeaderGroup 생성 및 제거
        Map<String, Object> httpHeaderGroup = new LinkedHashMap<>();
        httpHeaderGroup.put("Content-Type", original.remove("dataPtrnVl"));
        httpHeaderGroup.put("x-api-type", original.remove("apiClotDsncVl"));
        httpHeaderGroup.put("baseurl", original.remove("mydtSvcUrlCon"));
        result.put("httpHeaderGroup", httpHeaderGroup);

        // 나머지 필드 재귀적으로 이름 변경
        for (Map.Entry<String, Object> entry : original.entrySet()) {
            String newKey = fieldRenameMap.getOrDefault(entry.getKey(), entry.getKey());
            Object newValue = renameRecursively(entry.getValue(), fieldRenameMap);
            result.put(newKey, newValue);
        }

        // 결과 출력
        String outputJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        System.out.println(outputJson);
    }

    // 재귀적으로 Map/List 내부 필드명 변경
    private static Object renameRecursively(Object value, Map<String, String> renameMap) {
        if (value instanceof Map) {
            Map<String, Object> newMap = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                String oldKey = String.valueOf(entry.getKey());
                String newKey = renameMap.getOrDefault(oldKey, oldKey);
                newMap.put(newKey, renameRecursively(entry.getValue(), renameMap));
            }
            return newMap;
        } else if (value instanceof List) {
            List<Object> newList = new ArrayList<>();
            for (Object item : (List<?>) value) {
                newList.add(renameRecursively(item, renameMap));
            }
            return newList;
        } else {
            return value;
        }
    }
}
