package dev.mini.minibox.services;

import dev.mini.minibox.dtos.ReturnDto;
import dev.mini.minibox.mappers.ScheduleMapper;
import dev.mini.minibox.mappers.SchedulesMapper;
import dev.mini.minibox.results.CommonResult;
import dev.mini.minibox.results.schedule.CrawlResult;
import dev.mini.minibox.vos.SchedulesVo;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final SchedulesMapper schedulesMapper;

    @Autowired
    public ScheduleService(ScheduleMapper scheduleMapper, SchedulesMapper schedulesMapper) {
        this.scheduleMapper = scheduleMapper;
        this.schedulesMapper = schedulesMapper;
    }

    public void crawlAndInsertSchedule(LocalDate inquiryPlayDate, String brchNo) throws InterruptedException, IOException, URISyntaxException {
        // 크롤링 작업을 수행하고 결과를 DB에 삽입
        ReturnDto<List<SchedulesVo>> result = crawl(inquiryPlayDate, brchNo); // 앞서 제공된 crawl 메서드 호출

        if (result.getResult().equals(CommonResult.SUCCESS) && result.getPayload() != null) {
            // 크롤링 결과가 성공적이면 DB에 저장
            List<SchedulesVo> schedules = result.getPayload();
            for (SchedulesVo schedule : schedules) {
                schedulesMapper.insertSchedule(schedule);  // Mapper를 통해 DB에 삽입
            }
        }
    }

    // 지점 번호(brchNo)로 상영 스케줄을 크롤링하는 메서드
    private ReturnDto<List<SchedulesVo>> crawl(LocalDate inquiryPlayDate, String brchNo) throws IOException, InterruptedException, URISyntaxException {
        // 1. 지점 번호(brchNo)로 상영 정보를 크롤링
        List<SchedulesVo> schedules = new LinkedList<>();

        // 요청 본문 생성
        JSONObject requestBody = new JSONObject() {{
            put("playDe", inquiryPlayDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            put("brchNo", brchNo);  // 지점 번호 사용
            put("brchNo1", brchNo);
            put("detailType", "area");
            put("masterType", "brch");
        }};

        // HTTP 요청
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://www.megabox.co.kr/on/oh/ohc/Brch/schedulePage.do"))
                .header("accept", "application/json, text/javascript, */*; q=0.01")
                .header("accept-encoding", "gzip, deflate, br, zstd")
                .header("accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("content-type", "application/json;charset=UTF-8")
                .header("dnt", "1")
                .header("origin", "https://www.megabox.co.kr")
                .header("referer", "https://www.megabox.co.kr/booking/timetable")
                .header("sec-ch-ua", "\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"macOS\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
                .header("x-requested-with", "XMLHttpRequest")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString(), StandardCharsets.UTF_8))
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // 응답 코드 확인
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            return ReturnDto.<List<SchedulesVo>>builder().result(CrawlResult.FAILURE_NETWORK).build();
        }

        // JSON 응답 파싱
        JSONArray scheduleArray;
        try {
            JSONObject responseObject = new JSONObject(response.body());
            scheduleArray = responseObject.getJSONObject("megaMap").getJSONArray("movieFormList");
        } catch (JSONException ignored) {
            return ReturnDto.<List<SchedulesVo>>builder().result(CrawlResult.FAILURE_RESPONSE_TYPE).build();
        }

        // 상영 정보를 추출하여 리스트에 추가
        DateTimeFormatter playDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter playTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (int i = 0; i < scheduleArray.length(); i++) {
            JSONObject scheduleObject = scheduleArray.getJSONObject(i);

            try {
                String playStartTime = scheduleObject.getString("playStartTime");
                int playStartTimeHour = Integer.parseInt(playStartTime.substring(0, 2));
                if (playStartTimeHour >= 24) {
                    playStartTimeHour -= 24;
                    playStartTime = String.format("%02d%s", playStartTimeHour, playStartTime.substring(2));
                }
                String playEndTime = scheduleObject.getString("playEndTime");
                int playEndTimeHour = Integer.parseInt(playEndTime.substring(0, 2));
                if (playEndTimeHour >= 24) {
                    playEndTimeHour -= 24;
                    playEndTime = String.format("%02d%s", playEndTimeHour, playEndTime.substring(2));
                }

                // SchedulesVo 객체 생성 및 설정
                SchedulesVo schedule = new SchedulesVo() {{
                    setAreaCd(StringEscapeUtils.unescapeHtml4(scheduleObject.getString("areaCd")));
                    setAreaCdNm(StringEscapeUtils.unescapeHtml4(scheduleObject.getString("areaCdNm")));
                    setBrchNo(StringEscapeUtils.unescapeHtml4(scheduleObject.getString("brchNo")));
                    setBrchNm(StringEscapeUtils.unescapeHtml4(scheduleObject.getString("brchNm")));
                    setTheabNo(StringEscapeUtils.unescapeHtml4(scheduleObject.getString("theabNo")));
                    setTheabNm(StringEscapeUtils.unescapeHtml4(scheduleObject.getString("theabExpoNm")));
                    setMovieCd(StringEscapeUtils.unescapeHtml4(scheduleObject.getString("movieNo")));
                    setMovieNm(StringEscapeUtils.unescapeHtml4(scheduleObject.getString("movieNm")));
                    setPlayDe(LocalDate.parse(scheduleObject.getString("playDe"), playDateFormatter));
                }};
                schedule.setPlayStartTime(LocalTime.parse(playStartTime, playTimeFormatter));
                schedule.setPlayEndTime(LocalTime.parse(playEndTime, playTimeFormatter));

                schedules.add(schedule);
            } catch (JSONException ignored) {
            }
        }

        // 상영 정보 크롤링 결과 반환
        return ReturnDto.<List<SchedulesVo>>builder()
                .payload(schedules)
                .result(CommonResult.SUCCESS)
                .build();
    }
}
