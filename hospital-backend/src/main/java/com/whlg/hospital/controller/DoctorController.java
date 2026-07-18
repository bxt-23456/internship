package com.whlg.hospital.controller;

import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.DoctorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 鍖荤敓鎺у埗鍣? */

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 鑾峰彇鍖婚櫌鐨勫尰鐢熷垪琛紙鏀寔绉戝绛涢€夛級
     * @param hospitalId 鍖婚櫌ID
     * @param departmentId 绉戝ID锛堝彲閫夛級
     * @return 鍖荤敓鍒楄〃
     */
    @GetMapping("/list")
    public R<List<DoctorVo>> listDoctors(
            @RequestParam Long hospitalId,
            @RequestParam(required = false) Long departmentId) {
        List<DoctorVo> doctors = doctorService.listDoctorsByHospital(hospitalId, departmentId);
        return R.createSuccess(doctors);
    }
}

