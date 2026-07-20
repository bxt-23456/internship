package com.whlg.hospital.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {

    IPage<Article> listArticles(int page, int size, Long departmentId, String keyword);

    Article getArticleDetail(Long id);

    List<Article> getTopArticlesByViews(int limit);

    List<Article> getRelatedArticles(Long departmentId, Long excludeId, int limit);
}