package com.whlg.hospital.task;

import com.whlg.hospital.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleAutoTask {

    @Autowired
    private ScheduleService scheduleService;

    // 每天 00:00:00 执行
    @Scheduled(cron = "0 0 0 * * ?")
    public void refreshSchedule() {
        scheduleService.refreshNext7DaysSchedule();
    }
}