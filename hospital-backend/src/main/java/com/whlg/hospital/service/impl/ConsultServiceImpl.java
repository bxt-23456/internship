package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.dto.CreateConsultDto;
import com.whlg.hospital.entity.Consult;
import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.mapper.ConsultMapper;
import com.whlg.hospital.service.ConsultService;
import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.service.PaymentFlowService;
import com.whlg.hospital.vo.ConsultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ConsultServiceImpl extends ServiceImpl<ConsultMapper, Consult> implements ConsultService {

    @Autowired
    private ConsultMapper consultMapper;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PaymentFlowService paymentFlowService;

    @Override
    @Transactional
    public String createConsult(CreateConsultDto dto) {
        if (dto.getUserId() == null || dto.getDoctorId() == null) {
            throw new RuntimeException("缺少必要下单参数");
        }
        if (dto.getPatientName() == null || dto.getPatientName().trim().isEmpty()) {
            throw new RuntimeException("咨询人姓名不能为空");
        }
        if (dto.getDiseaseDesc() == null || dto.getDiseaseDesc().trim().isEmpty()) {
            throw new RuntimeException("请填写病情描述");
        }

        Doctor doctor = doctorService.getById(dto.getDoctorId());
        if (doctor == null || doctor.getStatus() == null || doctor.getStatus() != 1) {
            throw new RuntimeException("医生不存在或不可咨询");
        }

        Consult consult = new Consult();
        consult.setOrderNo(generateOrderNo());
        consult.setUserId(dto.getUserId());
        consult.setDoctorId(dto.getDoctorId());
        consult.setPatientName(dto.getPatientName());
        consult.setPatientPhone(dto.getPatientPhone());
        consult.setDiseaseDesc(dto.getDiseaseDesc());
        consult.setAppointmentTime(LocalDateTime.now());
        consult.setDuration(30);
        consult.setAmount(doctor.getPrice());
        consult.setStatus(1);
        consult.setCreateTime(LocalDateTime.now());
        consult.setUpdateTime(LocalDateTime.now());
        this.save(consult);
        return consult.getOrderNo();
    }

    @Override
    @Transactional
    public boolean paySuccess(String orderNo, String tradeNo, String callbackContent) {
        Consult consult = this.lambdaQuery()
                .eq(Consult::getOrderNo, orderNo)
                .one();
        if (consult == null) {
            return false;
        }
        if (consult.getStatus() != null && consult.getStatus() == 2) {
            return true;
        }
        if (consult.getStatus() == null || consult.getStatus() != 1) {
            return false;
        }

        consult.setStatus(2);
        consult.setPayTime(LocalDateTime.now());
        consult.setUpdateTime(LocalDateTime.now());
        this.updateById(consult);

        paymentFlowService.recordPaymentSuccess(orderNo, 2, consult.getAmount(), tradeNo, callbackContent);
        return true;
    }

    @Override
    public List<ConsultVo> listByUserId(Long userId, Integer status) {
        return consultMapper.selectConsultList(userId, status);
    }

    @Override
    public ConsultVo getDetail(String orderNo) {
        return consultMapper.selectConsultDetailByOrderNo(orderNo);
    }

    private String generateOrderNo() {
        return "CONSULT" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }
}
