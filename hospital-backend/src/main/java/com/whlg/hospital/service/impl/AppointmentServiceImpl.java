package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.dto.CreateAppointmentDto;
import com.whlg.hospital.entity.Appointment;
import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.entity.Schedule;
import com.whlg.hospital.mapper.AppointmentMapper;
import com.whlg.hospital.mapper.ScheduleMapper;
import com.whlg.hospital.service.AppointmentService;
import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.service.PaymentFlowService;
import com.whlg.hospital.vo.AppointmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private PaymentFlowService paymentFlowService;

    @Override
    @Transactional
    public String createAppointment(CreateAppointmentDto dto) {
        if (dto.getUserId() == null || dto.getDoctorId() == null) {
            throw new RuntimeException("缺少必要下单参数");
        }
        if (dto.getPatientName() == null || dto.getPatientName().trim().isEmpty()) {
            throw new RuntimeException("就诊人姓名不能为空");
        }
        if (dto.getPatientIdCard() == null || dto.getPatientIdCard().trim().isEmpty()) {
            throw new RuntimeException("就诊人身份证号不能为空");
        }
        if (dto.getAppointmentDate() == null || dto.getAppointmentTime() == null) {
            throw new RuntimeException("请选择预约日期和时间段");
        }

        Doctor doctor = doctorService.getById(dto.getDoctorId());
        if (doctor == null || doctor.getStatus() == null || doctor.getStatus() != 1) {
            throw new RuntimeException("医生不存在或不可预约");
        }

        LocalDate appointmentDate = LocalDate.parse(dto.getAppointmentDate());
        Schedule schedule = scheduleMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Schedule>()
                        .lambda()
                        .eq(Schedule::getDoctorId, dto.getDoctorId())
                        .eq(Schedule::getScheduleDate, appointmentDate)
                        .eq(Schedule::getTimeSlot, dto.getAppointmentTime())
        );
        if (schedule == null) {
            throw new RuntimeException("当前时间段暂无号源");
        }
        if (schedule.getRemainCount() == null || schedule.getRemainCount() <= 0 || schedule.getStatus() == null || schedule.getStatus() != 1) {
            throw new RuntimeException("当前时间段已约满");
        }

        int updated = scheduleMapper.deductRemainCount(schedule.getId(), schedule.getRemainCount());
        if (updated == 0) {
            throw new RuntimeException("当前号源已被抢完，请刷新后重试");
        }

        Appointment appointment = new Appointment();
        appointment.setOrderNo(generateOrderNo());
        appointment.setUserId(dto.getUserId());
        appointment.setDoctorId(dto.getDoctorId());
        appointment.setHospitalId(doctor.getHospitalId());
        appointment.setPatientName(dto.getPatientName());
        appointment.setPatientPhone(dto.getPatientPhone());
        appointment.setPatientIdCard(dto.getPatientIdCard());
        appointment.setPatientGender(dto.getPatientGender());
        appointment.setPatientAge(calculateAge(dto.getPatientBirthday()));
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setDiseaseDesc(dto.getDiseaseDesc());
        appointment.setAmount(doctor.getRegistrationPrice());
        appointment.setStatus(1);
        appointment.setCreateTime(LocalDateTime.now());
        appointment.setUpdateTime(LocalDateTime.now());
        this.save(appointment);
        return appointment.getOrderNo();
    }

    @Override
    @Transactional
    public boolean paySuccess(String orderNo, String tradeNo, String callbackContent) {
        Appointment appointment = this.lambdaQuery()
                .eq(Appointment::getOrderNo, orderNo)
                .one();
        if (appointment == null) {
            return false;
        }
        if (appointment.getStatus() != null && appointment.getStatus() == 2) {
            return true;
        }
        if (appointment.getStatus() == null || appointment.getStatus() != 1) {
            return false;
        }

        appointment.setStatus(2);
        appointment.setPayTime(LocalDateTime.now());
        appointment.setUpdateTime(LocalDateTime.now());
        this.updateById(appointment);

        paymentFlowService.recordPaymentSuccess(orderNo, 1, appointment.getAmount(), tradeNo, callbackContent);
        return true;
    }

    @Override
    public List<AppointmentVo> listByUserId(Long userId, Integer status) {
        return appointmentMapper.selectAppointmentList(userId, status);
    }

    @Override
    public AppointmentVo getDetail(String orderNo) {
        return appointmentMapper.selectAppointmentDetailByOrderNo(orderNo);
    }

    @Override
    @Transactional
    public boolean cancelAppointment(Long userId, String orderNo) {
        Appointment appointment = this.lambdaQuery()
                .eq(Appointment::getUserId, userId)
                .eq(Appointment::getOrderNo, orderNo)
                .one();
        if (appointment == null) {
            throw new RuntimeException("挂号订单不存在");
        }
        if (appointment.getStatus() == null || appointment.getStatus() != 1) {
            throw new RuntimeException("仅待支付订单允许取消");
        }

        appointment.setStatus(4);
        appointment.setUpdateTime(LocalDateTime.now());
        this.updateById(appointment);

        Schedule schedule = scheduleMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Schedule>()
                        .lambda()
                        .eq(Schedule::getDoctorId, appointment.getDoctorId())
                        .eq(Schedule::getScheduleDate, appointment.getAppointmentDate())
                        .eq(Schedule::getTimeSlot, appointment.getAppointmentTime())
        );
        if (schedule != null) {
            scheduleMapper.restoreRemainCount(schedule.getId());
        }
        return true;
    }

    private String generateOrderNo() {
        return "APPOINTMENT" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }

    private Integer calculateAge(String birthdayText) {
        if (birthdayText == null || birthdayText.trim().isEmpty()) {
            return null;
        }
        LocalDate birthday = LocalDate.parse(birthdayText);
        return Period.between(birthday, LocalDate.now()).getYears();
    }
}
