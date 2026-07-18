package com.whlg.hospital.controller;

import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.DoctorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生控制器
 */

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取医院的医生列表（支持科室筛选）
     * @param hospitalId 医院ID
     * @param departmentId 科室ID（可选）
     * @return 医生列表
     */
    @GetMapping("/list")
    public R<List<DoctorVo>> listDoctors(
            @RequestParam Long hospitalId,
            @RequestParam(required = false) Long departmentId) {
        List<DoctorVo> doctors = doctorService.listDoctorsByHospital(hospitalId, departmentId);
        return R.createSuccess(doctors);
    }
}
