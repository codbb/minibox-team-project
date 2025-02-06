package dev.mini.minibox.controllers;

import dev.mini.minibox.entities.MovieBoxOffice;
import dev.mini.minibox.entities.ScheduleEntity;
import dev.mini.minibox.entities.UserEntity;
import dev.mini.minibox.results.CommonResult;
import dev.mini.minibox.results.Result;
import dev.mini.minibox.services.AreaService;
import dev.mini.minibox.services.BookingService;
import dev.mini.minibox.services.MovieService;
import dev.mini.minibox.vos.SeatVo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/booking")
public class BookingController {
    private final MovieService movieService;
    private final AreaService areaService;
    private final BookingService bookingService;
//    private final CrawlService crawlService;

    @Autowired
    public BookingController(MovieService movieService, AreaService areaService, BookingService bookingService) {
        this.movieService = movieService;
        this.areaService = areaService;
        this.bookingService = bookingService;
//        this.crawlService = crawlService;
    }

    // 예약, 좌석, 결제를 보여주는 페이지
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView();
        MovieBoxOffice[] movieBoxOffices = this.movieService.getMovieBoxOffices(false);
        modelAndView.addObject("areas", this.areaService.selectAreas());
        modelAndView.addObject("theaters", this.areaService.selectTheaters());
        modelAndView.addObject("movieBoxOffices", movieBoxOffices);
        modelAndView.addObject("gradeMap", this.movieService.getGrade());
        modelAndView.addObject("schedules", this.bookingService.getSchedules());
        modelAndView.addObject("seats", this.bookingService.getSeats());
        modelAndView.setViewName("booking/index");
        return modelAndView;
    }

    // 스케줄 가져오기
    @RequestMapping(value = "/schedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> getSchedule(@RequestParam("date") String date,
                                           @RequestParam("areaNo") String areaNo,
                                           @RequestParam("brchNo") String brchNo,
                                           @RequestParam(value = "movieNm", required = false, defaultValue = "") String movieNm) {
//        List<ScheduleEntity> schedule = this.bookingService.getSchedule(areaNo, brchNo);
//        Map<String, Object> result = new HashMap<>();
//        result.put("schedules", schedule);
//        return result;

        Map<String, Object> result = new HashMap<>();
        try {
            // 지역과 극장이 유효한지 확인
            if (areaNo == null || brchNo == null) {
                throw new IllegalArgumentException("지역 번호와 극장 번호는 필수입니다.");
            }
            List<ScheduleEntity> schedule = this.bookingService.getSchedule(date, areaNo, brchNo, movieNm);
            result.put("schedules", schedule);
        } catch (Exception e) {
            result.put("error", "스케줄을 가져오는 데 오류가 발생했습니다.");
            log.error("Schedule retrieval error: ", e);
        }
        return result;
    }

    // 여러 좌석 예매
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String post(@SessionAttribute(value = "user", required = false) UserEntity user,
                       @RequestParam(value = "scheduleId", required = false) int scheduleId,
                       @RequestParam(value = "seatIds", required = false) String seatIds,
                       @RequestParam(value = "charge", required = false) int charge) {

        // String으로 받은 seatIds를 쉼표로 분리하여 List<Integer>로 변환
        List<Integer> seatList = new ArrayList<>();
        if (seatIds != null && !seatIds.isEmpty()) {
            String[] seatArray = seatIds.split(",");
            for (String seatId : seatArray) {
                try {
                    seatList.add(Integer.parseInt(seatId.trim()));
                } catch (NumberFormatException e) {
                    // 예외 처리: 잘못된 좌석 ID 처리
                    log.error("Invalid seat ID: " + seatId);
                }
            }
        }

        String userEmail = (user != null) ? user.getEmail() : null;
        Result result = this.bookingService.reservation(userEmail, scheduleId, seatList, charge);

        JSONObject response = new JSONObject();
        response.put(CommonResult.NAME, result.name().toLowerCase());
        return response.toString();
    }

    @GetMapping(value = "/seats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<SeatVo[]> getSeats(@RequestParam(value = "scheduleId") int scheduleId) {
        return ResponseEntity.ok(this.bookingService.getSeatVos(scheduleId));
    }

    // 결제, 예매 취소하기
    @RequestMapping(value = "/cancel-payment", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteCancelPayment(@SessionAttribute(value = "user", required = false) UserEntity user,
                                      @RequestParam(value = "paymentId", required = false) int paymentId) {
        String userEmail = (user != null) ? user.getEmail() : null;
        Result result = this.bookingService.deletePayment(userEmail, paymentId);
        JSONObject response = new JSONObject();
        response.put(CommonResult.NAME, result.name().toLowerCase());
        return response.toString();
    }
}
