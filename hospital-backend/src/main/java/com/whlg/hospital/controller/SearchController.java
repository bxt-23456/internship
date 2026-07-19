package com.whlg.hospital.controller;

import com.whlg.hospital.entity.Article;
import com.whlg.hospital.entity.Department;
import com.whlg.hospital.entity.Disease;
import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.entity.Hospital;
import com.whlg.hospital.mapper.DepartmentMapper;
import com.whlg.hospital.service.ArticleService;
import com.whlg.hospital.service.DiseaseService;
import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.service.HospitalService;
import com.whlg.hospital.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DiseaseService diseaseService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private DepartmentMapper departmentMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("/all")
    public R<Map<String, Object>> searchAll(@RequestParam String keyword) {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> hospitals = new ArrayList<>();
        for (Hospital hospital : hospitalService.searchHospitals(keyword)) {
            Map<String, Object> vo = new HashMap<>();
            vo.put("id", hospital.getId());
            vo.put("name", hospital.getName());
            vo.put("level", hospital.getLevel());
            vo.put("image", hospital.getImage());
            vo.put("address", hospital.getAddress());
            hospitals.add(vo);
        }
        result.put("hospitals", hospitals);

        List<Map<String, Object>> doctors = new ArrayList<>();
        for (Doctor doctor : doctorService.searchDoctors(keyword)) {
            Map<String, Object> vo = new HashMap<>();
            vo.put("id", doctor.getId());
            vo.put("name", doctor.getName());
            vo.put("department", getDepartmentName(doctor.getDepartmentId()));
            vo.put("avatar", doctor.getAvatar());
            vo.put("title", doctor.getTitle());
            vo.put("online", doctor.getOnlineStatus() != null && doctor.getOnlineStatus() == 1);
            doctors.add(vo);
        }
        result.put("doctors", doctors);

        List<Map<String, Object>> diseases = new ArrayList<>();
        for (Disease disease : diseaseService.searchDiseases(keyword)) {
            Map<String, Object> vo = new HashMap<>();
            vo.put("id", disease.getId());
            vo.put("name", disease.getName());
            vo.put("description", disease.getDescription());
            diseases.add(vo);
        }
        result.put("diseases", diseases);

        List<Map<String, Object>> articles = new ArrayList<>();
        for (Article article : articleService.listArticles(1, 10, null, keyword).getRecords()) {
            Map<String, Object> vo = new HashMap<>();
            vo.put("id", article.getId());
            vo.put("title", article.getTitle());
            vo.put("summary", article.getSummary());
            vo.put("image", article.getImage());
            String category = getDepartmentName(article.getDepartmentId());
            vo.put("category", category != null ? category : "健康科普");
            if (article.getPublishTime() != null) {
                vo.put("createTime", article.getPublishTime().format(FORMATTER));
            } else if (article.getCreateTime() != null) {
                vo.put("createTime", article.getCreateTime().format(FORMATTER));
            }
            articles.add(vo);
        }
        result.put("articles", articles);

        return R.createSuccess(result);
    }

    private String getDepartmentName(Long departmentId) {
        if (departmentId == null) {
            return "未知科室";
        }
        Department department = departmentMapper.selectById(departmentId);
        return department != null ? department.getName() : "未知科室";
    }
}