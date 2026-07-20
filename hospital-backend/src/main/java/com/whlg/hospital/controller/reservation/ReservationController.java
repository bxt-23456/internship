package com.whlg.hospital.controller.reservation;

import com.whlg.hospital.entity.FamilyMember;
import com.whlg.hospital.entity.Schedule;
import com.whlg.hospital.entity.User;
import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.service.FamilyMemberService;
import com.whlg.hospital.service.ScheduleService;
import com.whlg.hospital.service.UserService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.FamilyMemberVo;
import com.whlg.hospital.vo.reservation.ReservationDoctorVo;
import com.whlg.hospital.vo.reservation.ReservationSlotVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping()
    public R<ReservationDoctorVo> getDoctorInfo(@RequestParam Long doctorId) {
        return R.createSuccess(doctorService.getReservationDoctorInfo(doctorId));
    }

    @GetMapping("/getSlots")
    public R<List<ReservationSlotVo>> getReservationSlots(@RequestParam Long doctorId,
                                                          @RequestParam String scheduleDate) {
        return R.createSuccess(doctorService.getReservationSlots(doctorId, scheduleDate));
    }

    @GetMapping("/getFamilyMember")
    public R<List<FamilyMemberVo>> getFamilyMembers(@RequestParam Long userId) {
        List<FamilyMemberVo> result = new ArrayList<>();

        List<FamilyMember> familyMembers = familyMemberService.listFamilyMembersByUserId(userId);
        for (FamilyMember familyMember : familyMembers) {
            FamilyMemberVo item = new FamilyMemberVo();
            item.setId(familyMember.getId());
            item.setUserId(familyMember.getUserId());
            item.setName(familyMember.getName());
            item.setGender(familyMember.getGender());
            item.setBirthday(familyMember.getBirthday());
            item.setPhone(familyMember.getPhone());
            item.setIdCard(familyMember.getIdCard());
            item.setRelation(familyMember.getRelation());
            item.setIsDefault(familyMember.getIsDefault());
            result.add(item);
        }

        return R.createSuccess(result);
    }

    /**
     * 获取医生未来7天排班汇总（按日期聚合剩余号源）
     */
    @GetMapping("/scheduleSummary")
    public R<List<Map<String, Object>>> getScheduleSummary(@RequestParam Long doctorId) {
        List<Schedule> schedules = scheduleService.listByDoctorId(doctorId, 7);

        // 按日期聚合：每天的总剩余号源
        Map<LocalDate, Integer> remainMap = new LinkedHashMap<>();
        for (Schedule s : schedules) {
            remainMap.merge(s.getScheduleDate(), s.getRemainCount(), Integer::sum);
        }

        String[] weekdays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            int remain = remainMap.getOrDefault(date, 0);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("date", date.toString());
            item.put("dateStr", String.format("%d-%02d-%02d", date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
            item.put("weekday", weekdays[date.getDayOfWeek().getValue() - 1]);
            item.put("remainCount", remain);
            item.put("available", remain > 0);
            result.add(item);
        }

        return R.createSuccess(result);
    }

}
