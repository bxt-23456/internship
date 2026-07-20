package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Article;
import com.whlg.hospital.mapper.ArticleMapper;
import com.whlg.hospital.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public IPage<Article> listArticles(int page, int size, Long departmentId, String keyword) {
        Page<Article> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(Article::getStatus, 1);
        
        if (departmentId != null && departmentId > 0) {
            wrapper.eq(Article::getDepartmentId, departmentId);
        }
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(Article::getTitle, keyword.trim());
        }
        
        wrapper.orderByDesc(Article::getPublishTime);
        
        return this.page(pageParam, wrapper);
    }

    @Override
    public Article getArticleDetail(Long id) {
        Article article = this.getById(id);
        if (article != null && Integer.valueOf(1).equals(article.getStatus())) {
            this.update(Wrappers.<Article>lambdaUpdate()
                .setSql("views = views + 1")
                .eq(Article::getId, id));
            Article updatedArticle = this.getById(id);
            return updatedArticle;
        }
        return null;
    }

    @Override
    public List<Article> getTopArticlesByViews(int limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, 1)
               .orderByDesc(Article::getViews)
               .last("LIMIT " + limit);
        return this.list(wrapper);
    }

    @Override
    public List<Article> getRelatedArticles(Long departmentId, Long excludeId, int limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, 1)
               .eq(Article::getDepartmentId, departmentId)
               .ne(Article::getId, excludeId)
               .orderByDesc(Article::getViews)
               .last("LIMIT " + limit);
        return this.list(wrapper);
    }
}