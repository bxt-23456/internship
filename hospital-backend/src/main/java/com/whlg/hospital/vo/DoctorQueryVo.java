package com.whlg.hospital.vo;

import lombok.Data;

import java.util.List;

/**
 * 医生分页查询参数
 */
@Data
public class DoctorQueryVo {

    /**
     * 当前页码，默认第1页
     */
    private Integer pageNum = 1;

    /**
     * 每页条数，默认10条
     */
    private Integer pageSize = 10;

    /**
     * 医院ID（可选）
     */
    private Long hospitalId;

    /**
     * 科室ID列表（可选，支持多科室筛选）
     */
    private List<Long> departmentIds;

    /**
     * 在线状态: 1在线 2离线（可选）
     */
    private Integer onlineStatus;

    /**
     * 擅长领域关键词（可选，模糊匹配）
     */
    private String expertise;
}