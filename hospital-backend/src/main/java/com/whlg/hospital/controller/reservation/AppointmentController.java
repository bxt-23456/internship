package com.whlg.hospital.controller.reservation;

import com.whlg.hospital.dto.CreateAppointmentDto;
import com.whlg.hospital.service.AppointmentService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.AppointmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/create")
    public R<String> create(@RequestBody CreateAppointmentDto dto) {
        try {
            return R.createSuccess(appointmentService.createAppointment(dto));
        } catch (RuntimeException e) {
            return R.createError(e.getMessage());
        }
    }

    @GetMapping("/list")
    public R<List<AppointmentVo>> list(@RequestParam Long userId,
                                       @RequestParam(required = false) Integer status) {
        return R.createSuccess(appointmentService.listByUserId(userId, status));
    }

    @GetMapping("/detail")
    public R<AppointmentVo> detail(@RequestParam String orderNo) {
        AppointmentVo detail = appointmentService.getDetail(orderNo);
        if (detail == null) {
            return R.createError("挂号订单不存在");
        }
        return R.createSuccess(detail);
    }

    @PostMapping("/cancel")
    public R<String> cancel(@RequestBody Map<String, String> params) {
        try {
            Long userId = Long.valueOf(params.get("userId"));
            String orderNo = params.get("orderNo");
            appointmentService.cancelAppointment(userId, orderNo);
            return R.createSuccess("取消成功");
        } catch (RuntimeException e) {
            return R.createError(e.getMessage());
        }
    }
}
