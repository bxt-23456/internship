package com.whlg.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.whlg.hospital.entity.FamilyMember;
import com.whlg.hospital.mapper.FamilyMemberMapper;
import com.whlg.hospital.service.FamilyMemberService;
import com.whlg.hospital.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 就诊成员控制器
 */
@RestController
@RequestMapping("/familyMember")
public class FamilyMemberController {

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    /**
     * 查询当前用户的就诊成员列表
     */
    @GetMapping("/list")
    public R<List<FamilyMember>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createError(20001, "请先登录");
        }
        List<FamilyMember> members = familyMemberService.listFamilyMembersByUserId(userId);
        return R.createSuccess(members);
    }

    /**
     * 添加就诊成员
     */
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody FamilyMember member, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createError(20001, "请先登录");
        }
        member.setUserId(userId);
        member.setCreateTime(LocalDateTime.now());
        member.setUpdateTime(LocalDateTime.now());
        // 如果是第一个成员，设为默认
        List<FamilyMember> existing = familyMemberService.listFamilyMembersByUserId(userId);
        if (existing.isEmpty()) {
            member.setIsDefault(1);
        } else if (member.getIsDefault() == null) {
            member.setIsDefault(0);
        }
        boolean success = familyMemberService.save(member);
        return success ? R.createSuccess(true) : R.createError("添加失败");
    }

    /**
     * 根据id和userId查询成员（同时校验归属）
     */
    private FamilyMember getMemberByIdAndUserId(Long id, Long userId) {
        return familyMemberMapper.selectByIdAndUserId(id, userId);
    }

    /**
     * 修改就诊成员
     */
    @PutMapping("/update")
    public R<Boolean> update(@RequestBody FamilyMember member, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createError(20001, "请先登录");
        }
        if (member.getId() == null) {
            return R.createError("成员ID不能为空");
        }
        // 校验成员存在且属于当前用户
        FamilyMember existing = getMemberByIdAndUserId(member.getId(), userId);
        if (existing == null) {
            return R.createError("成员不存在");
        }
        // 使用 UpdateWrapper 直接更新，避免 updateById 的映射问题
        UpdateWrapper<FamilyMember> uw = new UpdateWrapper<>();
        uw.eq("id", existing.getId()).eq("user_id", userId)
          .set("name", member.getName())
          .set("gender", member.getGender())
          .set("birthday", member.getBirthday())
          .set("relation", member.getRelation())
          .set("phone", member.getPhone())
          .set("id_card", member.getIdCard())
          .set("update_time", LocalDateTime.now());
        boolean success = familyMemberService.update(uw);
        return success ? R.createSuccess(true) : R.createError("修改失败");
    }

    /**
     * 删除就诊成员
     */
    @DeleteMapping("/delete")
    public R<Boolean> delete(@RequestParam Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createError(20001, "请先登录");
        }
        // 校验成员存在且属于当前用户
        FamilyMember existing = getMemberByIdAndUserId(id, userId);
        if (existing == null) {
            return R.createError("成员不存在");
        }
        // 使用 QueryWrapper 直接删除
        QueryWrapper<FamilyMember> qw = new QueryWrapper<>();
        qw.eq("id", existing.getId()).eq("user_id", userId);
        boolean success = familyMemberService.remove(qw);
        return success ? R.createSuccess(true) : R.createError("删除失败");
    }

    /**
     * 设为默认就诊成员
     */
    @PutMapping("/setDefault")
    public R<Boolean> setDefault(@RequestParam Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createError(20001, "请先登录");
        }
        FamilyMember existing = getMemberByIdAndUserId(id, userId);
        if (existing == null) {
            return R.createError("成员不存在");
        }
        // 将该用户所有成员的 isDefault 置为 0
        UpdateWrapper<FamilyMember> clearUw = new UpdateWrapper<>();
        clearUw.eq("user_id", userId).eq("is_default", 1)
               .set("is_default", 0)
               .set("update_time", LocalDateTime.now());
        familyMemberService.update(clearUw);
        // 将目标成员设为默认
        UpdateWrapper<FamilyMember> setUw = new UpdateWrapper<>();
        setUw.eq("id", existing.getId()).eq("user_id", userId)
             .set("is_default", 1)
             .set("update_time", LocalDateTime.now());
        boolean success = familyMemberService.update(setUw);
        return success ? R.createSuccess(true) : R.createError("设置失败");
    }
}