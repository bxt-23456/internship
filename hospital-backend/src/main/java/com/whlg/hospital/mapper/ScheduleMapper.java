package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.Schedule;
import org.apache.ibatis.annotations.Param;

public interface ScheduleMapper extends BaseMapper<Schedule> {

    int deductRemainCount(@Param("id") Long id, @Param("remainCount") Integer remainCount);

    int restoreRemainCount(@Param("id") Long id);
}
