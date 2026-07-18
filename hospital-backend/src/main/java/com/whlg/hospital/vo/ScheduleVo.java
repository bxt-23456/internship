package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 医生排班VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleVo {

    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long hospitalId;
    private String hospitalName;
    private Long departmentId;
    private String departmentName;
    private LocalDate scheduleDate;
    private String timeSlot;
    private Integer totalCount;
    private Integer remainCount;
    private Integer status;
}
