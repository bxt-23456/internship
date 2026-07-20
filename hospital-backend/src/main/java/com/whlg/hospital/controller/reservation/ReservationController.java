package com.whlg.hospital.controller.reservation;

import com.whlg.hospital.entity.FamilyMember;
import com.whlg.hospital.entity.User;
import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.service.FamilyMemberService;
import com.whlg.hospital.service.UserService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.FamilyMemberVo;
import com.whlg.hospital.vo.reservation.ReservationDoctorVo;
import com.whlg.hospital.vo.reservation.ReservationSlotVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private UserService userService;

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

        User user = userService.getUserInfo(userId);
        if (user != null) {
            FamilyMemberVo self = new FamilyMemberVo();
            self.setId(user.getId());
            self.setUserId(user.getId());
            self.setName(user.getRealName() != null && !user.getRealName().isEmpty() ? user.getRealName() : user.getUsername());
            self.setGender(user.getGender());
            self.setBirthday(user.getBirthday());
            self.setPhone(user.getPhone());
            // t_user 当前没有身份证字段，这里先返回空串，避免前端空指针。
            self.setIdCard("");
            self.setRelation("本人");
            self.setIsDefault(1);
            result.add(self);
        }

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

}
