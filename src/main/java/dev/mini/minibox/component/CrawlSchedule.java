package dev.mini.minibox.component;

import dev.mini.minibox.entities.TheaterDetailEntity;
import dev.mini.minibox.services.ScheduleService;
import dev.mini.minibox.services.TheaterDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;


// 크롤링 로직
@Slf4j
@Component
public class CrawlSchedule {

    private final ScheduleService scheduleService;
    private final TheaterDetailService theaterDetailService;

    @Autowired
    public CrawlSchedule(ScheduleService scheduleService, TheaterDetailService theaterDetailService) {
        this.scheduleService = scheduleService;
        this.theaterDetailService = theaterDetailService;
    }

    //    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    //    @Scheduled(cron = "0 0 12 * * *") // 매일 12시마다
    @Scheduled(cron = "0 0 5 * * *") // 매일 5시마다
    //    @Scheduled(cron = "0 */58 * * * *") //
    //@Scheduled(cron = "0 */24 * * * *") //
    public void doCrawl() throws URISyntaxException, IOException, InterruptedException {

        // 현재 날짜부터 7일간의 스케줄을 크롤링 하는 코드
        // 시작 날짜: 현재 날짜 (LocalDate.now())
        LocalDate startDate = LocalDate.now();

        // 끝 날짜: 현재 날짜에서 7일 후
        LocalDate endDate = startDate.plusDays(2);  // 예: 7일간의 스케줄을 크롤링

        // 상영관 정보 가져오기
        TheaterDetailEntity[] theaterDetails = theaterDetailService.getTheaterDetails();
        log.info("Theater details fetched: {}", theaterDetails.length);

        try {
            // 지정된 기간 동안 날짜별로 크롤링 수행
            for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
                log.info("Crawling for date: {}", currentDate);  // 현재 날짜 로그 출력

                // 각 영화관에 대해 상영 스케줄 크롤링
                for (TheaterDetailEntity theaterDetail : theaterDetails) {
                    log.info("Crawling schedule for theater branch: {}", theaterDetail.getBrchNo());
                    scheduleService.crawlAndInsertSchedule(currentDate, theaterDetail.getBrchNo());
                    // 1초 대기 (서버에 부담을 줄이기 위해)
                    Thread.sleep(1000);
                }
            }
            log.info("Crawl and insert schedule completed for dates: {} to {}", startDate, endDate);
        } catch (Exception e) {
            log.error("Error occurred during crawl: {}", e.getMessage(), e);
        }
    }
}



