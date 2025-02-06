package dev.mini.minibox.services;

import dev.mini.minibox.dtos.ReturnDto;
import dev.mini.minibox.entities.AreaEntity;
import dev.mini.minibox.entities.TheaterEntity;
import dev.mini.minibox.results.CommonResult;
import dev.mini.minibox.results.theater.CrawlResult;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BranchService {
    public ReturnDto<Map<AreaEntity, List<TheaterEntity>>> crawl() throws URISyntaxException, IOException, InterruptedException {
        JSONObject requestBody = new JSONObject() {{
            put("brchLat", "");
            put("brchLon", "");
            put("deviceInfo", "");
            put("menuId", "M-RE-TH-01");
            put("sellChnlCd", "MOBILEWEB");
        }};
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://m.megabox.co.kr/on/oh/ohb/SimpleBooking/selectBokdList.do"))
                .header("accept", "application/json, text/javascript, */*; q=0.01")
                .header("accept-encoding", "gzip, deflate, br, zstd")
                .header("accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("content-type", "application/json;charset=UTF-8")
                .header("dnt", "1")
                .header("origin", "https://m.megabox.co.kr")
                .header("referer", "https://m.megabox.co.kr/booking/theater")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
                .header("x-requested-with", "XMLHttpRequest")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString(), StandardCharsets.UTF_8))
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            return ReturnDto.<Map<AreaEntity, List<TheaterEntity>>>builder().result(CrawlResult.FAILURE_NETWORK).build();
        }
        JSONArray branchArray;
        try {
            JSONObject responseObject = new JSONObject(response.body());
            branchArray = responseObject.getJSONArray("branchList");
        } catch (JSONException ignored) {
            return ReturnDto.<Map<AreaEntity, List<TheaterEntity>>>builder().result(CrawlResult.FAILURE_RESPONSE_TYPE).build();
        }
        Map<AreaEntity, List<TheaterEntity>> areaBranchMap = new HashMap<>();
        for (int i = 0; i < branchArray.length(); i++) {
            JSONObject branchObject = branchArray.getJSONObject(i);
            if (!branchObject.has("brchNo") ||
                    !branchObject.has("brchNm") ||
                    !branchObject.has("areaCd") ||
                    !branchObject.has("areaCdNm")) {
                continue;
            }
            String areaCode = branchObject.getString("areaCd");
            String areaName = StringEscapeUtils.unescapeHtml4(branchObject.getString("areaCdNm"));
            AreaEntity area = AreaEntity.builder()
                    .code(areaCode)
                    .name(areaName)
                    .build();
            if (!areaBranchMap.containsKey(area)) {
                areaBranchMap.put(area, new LinkedList<>());
            }
            areaBranchMap.get(area).add(TheaterEntity.builder()
                    .brchNo(branchObject.getString("brchNo"))
                    .name(StringEscapeUtils.unescapeHtml4(branchObject.getString("brchNm")))
                    .areaCode(areaCode)
                    .build());
        }
        return ReturnDto.<Map<AreaEntity, List<TheaterEntity>>>builder()
                .result(CommonResult.SUCCESS)
                .payload(areaBranchMap)
                .build();
    }
}
