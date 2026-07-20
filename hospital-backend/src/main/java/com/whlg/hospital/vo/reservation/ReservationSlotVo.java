package com.whlg.hospital.vo.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSlotVo {
    private String timeSlot;
    private Integer remainCount;
    private Integer totalCount;
    private Integer status;
}