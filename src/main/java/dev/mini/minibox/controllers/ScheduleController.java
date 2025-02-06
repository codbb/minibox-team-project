package dev.mini.minibox.controllers;

import dev.mini.minibox.dtos.ReturnDto;
import dev.mini.minibox.dtos.ScheduleRequest;
import dev.mini.minibox.results.CommonResult;
import dev.mini.minibox.services.ScheduleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/fetchSchedule")
    public ReturnDto<String> fetchSchedule(@RequestBody ScheduleRequest request) {
        try {
            // 크롤링을 수행하고 DB에 저장
            scheduleService.crawlAndInsertSchedule(request.getPlayDate(), request.getBrchNo());
            return ReturnDto.<String>builder().result(CommonResult.SUCCESS).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnDto.<String>builder().result(CommonResult.FAILURE).build();
        }
    }
}

