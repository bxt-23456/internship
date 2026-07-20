package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.dto.CreateReviewDto;
import com.whlg.hospital.entity.Appointment;
import com.whlg.hospital.entity.Consult;
import com.whlg.hospital.entity.Review;
import com.whlg.hospital.mapper.ReviewMapper;
import com.whlg.hospital.service.AppointmentService;
import com.whlg.hospital.service.ConsultService;
import com.whlg.hospital.service.ReviewService;
import com.whlg.hospital.vo.ReviewVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public List<ReviewVo> listByDoctorId(Long doctorId) {
        return reviewMapper.selectReviewVoByDoctorId(doctorId);
    }

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ConsultService consultService;

    @Override
    @Transactional
    public boolean submitReview(CreateReviewDto dto) {
        if (dto.getOrderType() == null || dto.getUserId() == null) {
            throw new RuntimeException("缺少评价必要参数");
        }
        if (dto.getOrderId() == null && (dto.getOrderNo() == null || dto.getOrderNo().trim().isEmpty())) {
            throw new RuntimeException("缺少订单标识");
        }
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new RuntimeException("评分范围必须在1到5之间");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new RuntimeException("评价内容不能为空");
        }

        Long doctorId = dto.getDoctorId();
        Long actualOrderId;
        if (dto.getOrderType() == 1) {
            Appointment appointment = null;
            if (dto.getOrderId() != null) {
                appointment = appointmentService.getById(dto.getOrderId());
            }
            if (appointment == null && dto.getOrderNo() != null && !dto.getOrderNo().trim().isEmpty()) {
                appointment = appointmentService.lambdaQuery()
                        .eq(Appointment::getOrderNo, dto.getOrderNo().trim())
                        .one();
            }
            if (appointment == null || !dto.getUserId().equals(appointment.getUserId())) {
                throw new RuntimeException("挂号订单不存在");
            }
            if (appointment.getStatus() == null || (appointment.getStatus() != 2 && appointment.getStatus() != 3)) {
                throw new RuntimeException("仅已支付或已完成挂号订单可评价");
            }
            actualOrderId = appointment.getId();
            doctorId = appointment.getDoctorId();
        } else if (dto.getOrderType() == 2) {
            Consult consult = null;
            if (dto.getOrderId() != null) {
                consult = consultService.getById(dto.getOrderId());
            }
            if (consult == null && dto.getOrderNo() != null && !dto.getOrderNo().trim().isEmpty()) {
                consult = consultService.lambdaQuery()
                        .eq(Consult::getOrderNo, dto.getOrderNo().trim())
                        .one();
            }
            if (consult == null || !dto.getUserId().equals(consult.getUserId())) {
                throw new RuntimeException("咨询订单不存在");
            }
            if (consult.getStatus() == null || (consult.getStatus() != 2 && consult.getStatus() != 4)) {
                throw new RuntimeException("仅已支付或已完成咨询订单可评价");
            }
            actualOrderId = consult.getId();
            doctorId = consult.getDoctorId();
        } else {
            throw new RuntimeException("不支持的评价类型");
        }

        Review existed = this.lambdaQuery()
                .eq(Review::getOrderType, dto.getOrderType())
                .eq(Review::getOrderId, actualOrderId)
                .eq(Review::getUserId, dto.getUserId())
                .one();
        if (existed != null) {
            throw new RuntimeException("该订单已评价，请勿重复提交");
        }

        Review review = new Review();
        review.setOrderType(dto.getOrderType());
        review.setOrderId(actualOrderId);
        review.setUserId(dto.getUserId());
        review.setDoctorId(doctorId);
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setCreateTime(LocalDateTime.now());
        this.save(review);
        return true;
    }

    @Override
    public List<ReviewVo> listByUserId(Long userId) {
        return reviewMapper.selectReviewList(userId);
    }
}
