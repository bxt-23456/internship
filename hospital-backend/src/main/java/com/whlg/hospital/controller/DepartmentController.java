package com.whlg.hospital.controller;

import com.whlg.hospital.entity.Department;
import com.whlg.hospital.service.DepartmentService;
import com.whlg.hospital.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 科室控制器
 */

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 根据ID列表批量查询科室
     * @param ids 科室ID列表
     * @return 科室列表
     */
    @GetMapping("/listByIds")
    public R<List<Department>> listByIds(@RequestParam List<Long> ids) {
        List<Department> departments = departmentService.listByIds(ids);
        return R.createSuccess(departments);
    }
}
