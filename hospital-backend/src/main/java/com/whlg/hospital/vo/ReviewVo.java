package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评价VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewVo {

    private Long id;
    private Integer orderType;
    private Long orderId;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Long doctorId;
    private String doctorName;
    private Integer rating;
    private String content;
    private LocalDateTime createTime;
}
