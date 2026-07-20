package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 健康科普文章VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVo {

    private Long id;
    private String title;
    private String summary;
    private String content;
    private Long departmentId;
    private String departmentName;
    private String author;
    private String image;
    private Integer views;
    private Integer status;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private String subCategory;
    private String date;
}
