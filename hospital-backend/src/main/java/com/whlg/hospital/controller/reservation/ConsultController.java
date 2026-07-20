package com.whlg.hospital.controller.reservation;

import com.whlg.hospital.dto.CreateConsultDto;
import com.whlg.hospital.service.ConsultService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.ConsultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consult")
public class ConsultController {

    @Autowired
    private ConsultService consultService;

    @PostMapping("/create")
    public R<String> create(@RequestBody CreateConsultDto dto) {
        try {
            return R.createSuccess(consultService.createConsult(dto));
        } catch (RuntimeException e) {
            return R.createError(e.getMessage());
        }
    }

    @GetMapping("/list")
    public R<List<ConsultVo>> list(@RequestParam Long userId,
                                   @RequestParam(required = false) Integer status) {
        return R.createSuccess(consultService.listByUserId(userId, status));
    }

    @GetMapping("/detail")
    public R<ConsultVo> detail(@RequestParam String orderNo) {
        ConsultVo detail = consultService.getDetail(orderNo);
        if (detail == null) {
            return R.createError("咨询订单不存在");
        }
        return R.createSuccess(detail);
    }
}
