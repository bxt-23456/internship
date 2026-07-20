package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.dto.CreateConsultDto;
import com.whlg.hospital.entity.Consult;
import com.whlg.hospital.vo.ConsultVo;

import java.util.List;

public interface ConsultService extends IService<Consult> {

    String createConsult(CreateConsultDto dto);

    boolean paySuccess(String orderNo, String tradeNo, String callbackContent);

    List<ConsultVo> listByUserId(Long userId, Integer status);

    ConsultVo getDetail(String orderNo);
}
