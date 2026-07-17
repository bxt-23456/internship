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
@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户注册
     * @param params 包含phone, password, username(可选)
     * @return 注册结果
     */
    @PostMapping("/register")
    public R<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");
        String username = params.get("username");

        if (phone == null || phone.isEmpty()) {
            return R.createError("手机号不能为空");
        }
        if (password == null || password.isEmpty()) {
            return R.createError("密码不能为空");
        }

        Map<String, Object> result = userService.register(phone, password, username);
        if ((Boolean) result.get("success")) {
            return R.createSuccess(result);
        } else {
            return R.createError((String) result.get("message"));
        }
    }

    /**
     * 用户登录
     * @param params 包含phone, password
     * @return 登录结果（包含token和用户信息）
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
     * @param request HTTP请求（包含token）
     * @return 用户信息
     */
    @GetMapping("/info")
    public R<User> getUserInfo(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return R.createError(StatusCode.TOKEN_EXPIRE, "未登录或登录已过期");
        }

        User user = userService.getUserInfo(userId);
        if (user == null) {
            return R.createError("用户不存在");
        }
        return R.createSuccess(user);
    }

    /**
     * 更新用户个人信息
     * @param user 用户信息
     * @param request HTTP请求（包含token）
     * @return 更新结果
     */
    @PutMapping("/info")
    public R<String> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return R.createError(StatusCode.TOKEN_EXPIRE, "未登录或登录已过期");
        }

        user.setId(userId); // 确保只能修改自己的信息
        boolean success = userService.updateUserInfo(user);
        if (success) {
            return R.createSuccess("更新成功");
        } else {
            return R.createError("更新失败");
        }
    }

    /**
     * 修改密码
     * @param params 包含oldPassword, newPassword
     * @param request HTTP请求（包含token）
     * @return 修改结果
     */
    @PutMapping("/password")
    public R<String> changePassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return R.createError(StatusCode.TOKEN_EXPIRE, "未登录或登录已过期");
        }

        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");

        if (oldPassword == null || oldPassword.isEmpty()) {
            return R.createError("请输入原密码");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            return R.createError("请输入新密码");
        }

        String result = userService.changePassword(userId, oldPassword, newPassword);
        if ("密码修改成功".equals(result)) {
            return R.createSuccess(result);
        } else {
            return R.createError(result);
        }
    }

    /**
     * 从请求中获取用户ID
     * @param request HTTP请求
     * @return 用户ID，无效则返回null
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            return null;
        }
        try {
            if (jwtUtil.validateToken(token)) {
                return jwtUtil.getUserIdFromToken(token);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
