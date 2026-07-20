package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.entity.Schedule;
import com.whlg.hospital.mapper.ScheduleMapper;
import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

    @Override
    public List<Schedule> listByDoctorId(Long doctorId, int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);

        LambdaQueryWrapper<Schedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Schedule::getDoctorId, doctorId)
                .ge(Schedule::getScheduleDate, startDate)
                .lt(Schedule::getScheduleDate, endDate)
                .orderByAsc(Schedule::getScheduleDate)
                .orderByAsc(Schedule::getTimeSlot);

        return list(wrapper);
    }

    private static final List<String> TIME_SLOTS = Arrays.asList(
            "08:00-09:00",
            "09:00-10:00",
            "10:00-11:00",
            "14:00-15:00",
            "15:00-16:00",
            "16:00-17:00"
    );

    private static final int DEFAULT_TOTAL_COUNT = 20;

    @Autowired
    private DoctorService doctorService;

    /**
     * 项目启动时初始化今天起往后7天的排班
     */
    @Override
    @Transactional
    public void initNext7DaysSchedule() {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            generateScheduleForDate(today.plusDays(i));
        }
    }

    /**
     * 每天0点滚动更新：
     * 1. 删除昨天的排班
     * 2. 新增第7天后的那一天排班
     *
     * 例如：
     * 7/17 0:00 执行时：
     * - 删除 7/16
     * - 新增 7/23
     * - 7/17 ~ 7/22 不动
     */
    @Override
    @Transactional
    public void refreshNext7DaysSchedule() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate targetDate = today.plusDays(6);

        // 只删除昨天的数据
        this.lambdaUpdate()
                .eq(Schedule::getScheduleDate, yesterday)
                .remove();

        // 只新增第7天的数据
        generateScheduleForDate(targetDate);
    }

    /**
     * 为指定日期生成所有医生的排班
     */
    private void generateScheduleForDate(LocalDate scheduleDate) {
        List<Doctor> doctors = doctorService.lambdaQuery()
                .eq(Doctor::getStatus, 1)
                .list();

        if (doctors == null || doctors.isEmpty()) {
            return;
        }

        for (Doctor doctor : doctors) {
            for (String timeSlot : TIME_SLOTS) {
                boolean exists = this.lambdaQuery()
                        .eq(Schedule::getDoctorId, doctor.getId())
                        .eq(Schedule::getScheduleDate, scheduleDate)
                        .eq(Schedule::getTimeSlot, timeSlot)
                        .count() > 0;

                if (!exists) {
                    Schedule schedule = new Schedule();
                    schedule.setDoctorId(doctor.getId());
                    schedule.setHospitalId(doctor.getHospitalId());
                    schedule.setDepartmentId(doctor.getDepartmentId());
                    schedule.setScheduleDate(scheduleDate);
                    schedule.setTimeSlot(timeSlot);
                    schedule.setTotalCount(DEFAULT_TOTAL_COUNT);
                    schedule.setRemainCount(DEFAULT_TOTAL_COUNT);
                    schedule.setStatus(1);
                    schedule.setCreateTime(LocalDateTime.now());
                    this.save(schedule);
                }
            }
        }
    }
}
