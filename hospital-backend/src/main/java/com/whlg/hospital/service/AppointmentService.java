package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.dto.CreateAppointmentDto;
import com.whlg.hospital.entity.Appointment;
import com.whlg.hospital.vo.AppointmentVo;

import java.util.List;

public interface AppointmentService extends IService<Appointment> {

    String createAppointment(CreateAppointmentDto dto);

    boolean paySuccess(String orderNo, String tradeNo, String callbackContent);

    List<AppointmentVo> listByUserId(Long userId, Integer status);

    AppointmentVo getDetail(String orderNo);

    boolean cancelAppointment(Long userId, String orderNo);
}
