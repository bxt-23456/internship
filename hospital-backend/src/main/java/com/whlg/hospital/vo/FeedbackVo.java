package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 反馈VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackVo {

    private Long id;
    private Long userId;
    private String userName;
    private Integer feedbackType;
    private String content;
    private String images;
    private Integer status;
    private String replyContent;
    private LocalDateTime replyTime;
    private LocalDateTime createTime;
}
