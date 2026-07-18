package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 消息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageVo {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Integer isRead;
    private LocalDateTime createTime;
}
