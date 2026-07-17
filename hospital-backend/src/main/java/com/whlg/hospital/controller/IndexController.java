package com.whlg.hospital.controller;

import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.DoctorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/listTopDoctorVo")
    public R<List<DoctorVo>> listTopDoctorVo(int limit){
        List<DoctorVo> doctorVos = doctorService.listTopDoctorVo(limit);
        return R.createSuccess(doctorVos);
    }

}
