package com.whlg.hospital.task;

import com.whlg.hospital.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ScheduleInitRunner implements ApplicationRunner {

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void run(ApplicationArguments args) {
        scheduleService.initNext7DaysSchedule();
    }
}