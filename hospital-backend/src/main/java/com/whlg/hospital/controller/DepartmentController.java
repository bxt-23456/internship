package com.whlg.hospital.controller;

import com.whlg.hospital.entity.Department;
import com.whlg.hospital.service.DepartmentService;
import com.whlg.hospital.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 绉戝鎺у埗鍣? */

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 鏍规嵁ID鍒楄〃鎵归噺鏌ヨ绉戝
     * @param ids 绉戝ID鍒楄〃
     * @return 绉戝鍒楄〃
     */
    @GetMapping("/listByIds")
    public R<List<Department>> listByIds(@RequestParam List<Long> ids) {
        List<Department> departments = departmentService.listByIds(ids);
        return R.createSuccess(departments);
    }
}

