package com.whlg.hospital.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whlg.hospital.service.DiseaseService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.DiseaseRecommendVo;
import com.whlg.hospital.vo.DiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 疾病控制器
 */
@RestController
@RequestMapping("/disease")
public class DiseaseController {

    @Autowired
    private DiseaseService diseaseService;

    /**
     * 分页查询疾病列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param departmentId 科室ID（可选）
     * @return 疾病分页数据
     */
    @GetMapping("/page")
    public R<IPage<DiseaseVo>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long departmentId) {
        IPage<DiseaseVo> page = diseaseService.pageDisease(pageNum, pageSize, departmentId);
        return R.createSuccess(page);
    }

    /**
     * 获取疾病详情
     * @param id 疾病ID
     * @return 疾病详情
     */
    @GetMapping("/{id}")
    public R<DiseaseVo> detail(@PathVariable Long id) {
        DiseaseVo disease = diseaseService.getDiseaseDetail(id);
        if (disease == null) {
            return R.createError("疾病不存在");
        }
        return R.createSuccess(disease);
    }

    /**
     * 获取疾病关联推荐（相关科室 + 推荐医生）
     * @param id 疾病ID
     * @return 推荐数据
     */
    @GetMapping("/{id}/recommend")
    public R<DiseaseRecommendVo> recommend(@PathVariable Long id) {
        DiseaseRecommendVo recommend = diseaseService.getDiseaseRecommend(id);
        return R.createSuccess(recommend);
    }
}
