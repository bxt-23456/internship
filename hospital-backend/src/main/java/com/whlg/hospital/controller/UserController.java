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
     * 用户注册
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
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, String> params, HttpServletRequest request) {
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
            // 将用户信息存储到Session中
            @SuppressWarnings("unchecked")
            Map<String, Object> userInfo = (Map<String, Object>) result.get("userInfo");
            if (userInfo != null) {
                User user = new User();
                user.setId((Long) userInfo.get("id"));
                user.setUsername((String) userInfo.get("username"));
                user.setAvatar((String) userInfo.get("avatar"));
                request.getSession().setAttribute("user", user);
            }
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
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return R.createError(20001, "请先登录");
        }
        return R.createSuccess(user);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.createSuccess("退出成功");
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public R<Boolean> updateUser(@RequestBody User user, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            return R.createError(20001, "请先登录");
        }

        user.setId(currentUser.getId());
        boolean success = userService.updateUserInfo(user);
        if (success) {
            // 更新Session中的用户信息
            if (user.getUsername() != null) {
                currentUser.setUsername(user.getUsername());
            }
            if (user.getAvatar() != null) {
                currentUser.setAvatar(user.getAvatar());
            }
            request.getSession().setAttribute("user", currentUser);
            return R.createSuccess(true);
        } else {
            return R.createError("更新失败");
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/changePassword")
    public R<String> changePassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            return R.createError(20001, "请先登录");
        }

        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");

        String result = userService.changePassword(currentUser.getId(), oldPassword, newPassword);
        if ("密码修改成功".equals(result)) {
            return R.createSuccess(result);
        } else {
            return R.createError(result);
        }
    }
}
