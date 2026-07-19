package com.whlg.hospital.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whlg.hospital.entity.Article;
import com.whlg.hospital.entity.Department;
import com.whlg.hospital.mapper.DepartmentMapper;
import com.whlg.hospital.service.ArticleService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private DepartmentMapper departmentMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATTER_WITH_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/list")
    public R<Map<String, Object>> listArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String keyword) {
        
        IPage<Article> articlePage = articleService.listArticles(page, size, departmentId, keyword);
        
        List<ArticleVo> articleVos = new ArrayList<>();
        for (Article article : articlePage.getRecords()) {
            ArticleVo vo = new ArticleVo();
            vo.setId(article.getId());
            vo.setTitle(article.getTitle());
            vo.setSummary(article.getSummary());
            vo.setImage(article.getImage());
            vo.setViews(article.getViews());
            vo.setDepartmentId(article.getDepartmentId());
            
            String subCategory = getDepartmentName(article.getDepartmentId());
            vo.setSubCategory(subCategory != null ? subCategory : "未知科室");
            
            if (article.getPublishTime() != null) {
                vo.setDate(article.getPublishTime().format(FORMATTER));
            } else if (article.getCreateTime() != null) {
                vo.setDate(article.getCreateTime().format(FORMATTER));
            }
            
            articleVos.add(vo);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", articleVos);
        result.put("total", articlePage.getTotal());
        result.put("pages", articlePage.getPages());
        result.put("current", articlePage.getCurrent());
        result.put("size", articlePage.getSize());
        
        return R.createSuccess(result);
    }

    @GetMapping("/detail/{id}")
    public R<Map<String, Object>> getArticleDetail(@PathVariable Long id) {
        Article article = articleService.getArticleDetail(id);
        if (article == null) {
            return R.createError("文章不存在或已下架");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", article.getId());
        result.put("title", article.getTitle());
        result.put("summary", article.getSummary());
        result.put("content", article.getContent());
        result.put("image", article.getImage());
        result.put("views", article.getViews());
        result.put("departmentId", article.getDepartmentId());
        result.put("author", article.getAuthor());
        
        String subCategory = getDepartmentName(article.getDepartmentId());
        result.put("subCategory", subCategory != null ? subCategory : "未知科室");
        result.put("category", "健康科普");
        
        if (article.getPublishTime() != null) {
            result.put("date", article.getPublishTime().format(FORMATTER_WITH_TIME));
        } else if (article.getCreateTime() != null) {
            result.put("date", article.getCreateTime().format(FORMATTER_WITH_TIME));
        }
        
        List<Article> relatedArticles = articleService.getRelatedArticles(article.getDepartmentId(), article.getId(), 3);
        List<Map<String, Object>> relatedVos = new ArrayList<>();
        for (Article related : relatedArticles) {
            Map<String, Object> vo = new HashMap<>();
            vo.put("id", related.getId());
            vo.put("title", related.getTitle());
            vo.put("image", related.getImage());
            if (related.getPublishTime() != null) {
                vo.put("date", related.getPublishTime().format(FORMATTER));
            }
            relatedVos.add(vo);
        }
        result.put("relatedArticles", relatedVos);
        
        return R.createSuccess(result);
    }

    @GetMapping("/topN/{limit}")
    public R<List<Map<String, Object>>> getTopArticles(@PathVariable int limit) {
        List<Article> articles = articleService.getTopArticlesByViews(limit);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Article article : articles) {
            Map<String, Object> vo = new HashMap<>();
            vo.put("id", article.getId());
            vo.put("title", article.getTitle());
            vo.put("summary", article.getSummary());
            vo.put("image", article.getImage());
            vo.put("views", article.getViews());
            vo.put("departmentId", article.getDepartmentId());
            
            String subCategory = getDepartmentName(article.getDepartmentId());
            vo.put("category", subCategory != null ? subCategory : "未知科室");
            
            if (article.getPublishTime() != null) {
                vo.put("date", article.getPublishTime().format(FORMATTER_WITH_TIME));
            } else if (article.getCreateTime() != null) {
                vo.put("date", article.getCreateTime().format(FORMATTER_WITH_TIME));
            }
            
            result.add(vo);
        }
        
        return R.createSuccess(result);
    }

    private String getDepartmentName(Long departmentId) {
        if (departmentId == null) {
            return null;
        }
        Department department = departmentMapper.selectById(departmentId);
        return department != null ? department.getName() : null;
    }
}