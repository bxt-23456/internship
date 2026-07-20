package com.whlg.hospital.controller;

import com.whlg.hospital.entity.User;
import com.whlg.hospital.service.UserService;
import com.whlg.hospital.util.JwtUtil;
import com.whlg.hospital.util.R;
import com.whlg.hospital.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户控制器
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 发送短信验证码
     */
    @PostMapping("/sendCode")
    public R<String> sendCode(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        if (phone == null || phone.isEmpty()) {
            return R.createError("手机号不能为空");
        }

        Map<String, Object> result = userService.sendVerifyCode(phone);
        if ((Boolean) result.get("success")) {
            return R.createSuccess((String) result.get("message"));
        } else {
            return R.createError((String) result.get("message"));
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public R<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");
        String username = params.get("username");
        String code = params.get("code");

        if (phone == null || phone.isEmpty()) {
            return R.createError("手机号不能为空");
        }
        if (password == null || password.isEmpty()) {
            return R.createError("密码不能为空");
        }
        if (code == null || code.isEmpty()) {
            return R.createError("验证码不能为空");
        }

        Map<String, Object> result = userService.register(phone, password, username, code);
        if ((Boolean) result.get("success")) {
            return R.createSuccess(result);
        } else {
            return R.createError((String) result.get("message"));
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");

        if (phone == null || phone.isEmpty()) {
            return R.createError("手机号不能为空");
        }
        if (password == null || password.isEmpty()) {
            return R.createError("密码不能为空");
        }

        Map<String, Object> result = userService.login(phone, password);
        if ((Boolean) result.get("success")) {
            return R.createSuccess(result);
        } else {
            return R.createError((String) result.get("message"));
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public R<User> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createError(20001, "请先登录");
        }
        // 从数据库查询完整用户信息
        User user = userService.getUserInfo(userId);
        return R.createSuccess(user);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId != null) {
            userService.logout(userId);
        }
        return R.createSuccess("退出成功");
    }

    /**
     * 更新用户信息（根据phone查询id，再根据id修改）
     */
    @PutMapping("/info")
    public R<Boolean> updateUser(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createError(20001, "请先登录");
        }

        // 根据用户id查询完整用户信息（含phone）
        User dbUser = userService.getUserInfo(userId);
        if (dbUser == null || dbUser.getPhone() == null) {
            return R.createError(20001, "用户信息异常，请重新登录");
        }

        // 根据phone查询id
        String phone = dbUser.getPhone();
        user.setPhone(phone);

        String result = userService.updateUserInfo(user);
        if ("更新成功".equals(result)) {
            return R.createSuccess(true);
        } else {
            return R.createError(result);
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/changePassword")
    public R<String> changePassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createError(20001, "请先登录");
        }

        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");

        String result = userService.changePassword(userId, oldPassword, newPassword);
        if ("密码修改成功".equals(result)) {
            return R.createSuccess(result);
        } else {
            return R.createError(result);
        }
    }
}