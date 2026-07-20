package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.Appointment;
import com.whlg.hospital.vo.AppointmentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppointmentMapper extends BaseMapper<Appointment> {

    List<AppointmentVo> selectAppointmentList(@Param("userId") Long userId, @Param("status") Integer status);

    AppointmentVo selectAppointmentDetailByOrderNo(@Param("orderNo") String orderNo);
}
