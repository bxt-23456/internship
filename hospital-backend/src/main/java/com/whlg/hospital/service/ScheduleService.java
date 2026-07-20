package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.Schedule;

import java.util.List;

public interface ScheduleService extends IService<Schedule> {

    /**
     * 查询指定医生未来N天的排班及剩余号源
     * @param doctorId 医生ID
     * @param days 天数
     * @return 排班列表
     */
    List<Schedule> listByDoctorId(Long doctorId, int days);
}