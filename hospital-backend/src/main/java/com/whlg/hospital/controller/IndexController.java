package com.whlg.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whlg.hospital.entity.Article;
import com.whlg.hospital.entity.Disease;
import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.service.HospitalService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.DoctorVo;
import com.whlg.hospital.vo.HospitalVo;
import com.whlg.hospital.mapper.ArticleMapper;
import com.whlg.hospital.mapper.DiseaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 获取首页推荐医生
     */
    @GetMapping("/listTopDoctorVo")
    public R<List<DoctorVo>> listTopDoctorVo(@RequestParam(defaultValue = "5") int limit) {
        List<DoctorVo> doctorVos = doctorService.listTopDoctorVo(limit);
        return R.createSuccess(doctorVos);
    }

    /**
     * 获取首页推荐医院
     */
    @GetMapping("/listTopHospitalVo")
    public R<List<HospitalVo>> listTopHospitalVo(@RequestParam(defaultValue = "4") int limit) {
        List<HospitalVo> hospitals = hospitalService.listTopHospitals(limit);
        return R.createSuccess(hospitals);
    }

    /**
     * 获取热门疾病
     */
    @GetMapping("/listTopDisease")
    public R<List<Disease>> listTopDisease(@RequestParam(defaultValue = "8") int limit) {
        QueryWrapper<Disease> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("follow_count").last("LIMIT " + limit);
        List<Disease> diseases = diseaseMapper.selectList(queryWrapper);
        return R.createSuccess(diseases);
    }

    /**
     * 获取最新文章
     */
    @GetMapping("/listTopArticle")
    public R<List<Article>> listTopArticle(@RequestParam(defaultValue = "4") int limit) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1).orderByDesc("views").last("LIMIT " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return R.createSuccess(articles);
    }
}
