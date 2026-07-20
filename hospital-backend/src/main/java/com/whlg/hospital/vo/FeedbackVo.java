package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private String typeText;
    private String content;
    private String images;
    private List<String> imagesList;
    private Integer status;
    private String statusText;
    private String replyContent;
    private String reply;
    private LocalDateTime replyTime;
    private LocalDateTime createTime;
    private String createTimeText;
    private String replyTimeText;
}
