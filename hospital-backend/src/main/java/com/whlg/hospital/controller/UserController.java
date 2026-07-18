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
 * 鐢ㄦ埛鎺у埗鍣? */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 鐢ㄦ埛娉ㄥ唽
     */
    @PostMapping("/register")
    public R<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");
        String username = params.get("username");

        if (phone == null || phone.isEmpty()) {
            return R.createError("鎵嬫満鍙蜂笉鑳戒负绌?");
        }
        if (password == null || password.isEmpty()) {
            return R.createError("瀵嗙爜涓嶈兘涓虹┖");
        }

        Map<String, Object> result = userService.register(phone, password, username);
        if ((Boolean) result.get("success")) {
            return R.createSuccess(result);
        } else {
            return R.createError((String) result.get("message"));
        }
    }

    /**
     * 鐢ㄦ埛鐧诲綍
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String phone = params.get("phone");
        String password = params.get("password");

        if (phone == null || phone.isEmpty()) {
            return R.createError("鎵嬫満鍙蜂笉鑳戒负绌?");
        }
        if (password == null || password.isEmpty()) {
            return R.createError("瀵嗙爜涓嶈兘涓虹┖");
        }

        Map<String, Object> result = userService.login(phone, password);
        if ((Boolean) result.get("success")) {
            // 灏嗙敤鎴蜂俊鎭瓨鍌ㄥ埌Session涓?            @SuppressWarnings("unchecked")
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
     * 鑾峰彇褰撳墠鐧诲綍鐢ㄦ埛淇℃伅
     */
    @GetMapping("/info")
    public R<User> getUserInfo(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return R.createError(20001, "璇峰厛鐧诲綍");
        }
        return R.createSuccess(user);
    }

    /**
     * 閫€鍑虹櫥褰?     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.createSuccess("閫€鍑烘垚鍔?");
    }

    /**
     * 鏇存柊鐢ㄦ埛淇℃伅
     */
    @PutMapping("/update")
    public R<Boolean> updateUser(@RequestBody User user, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            return R.createError(20001, "璇峰厛鐧诲綍");
        }
        
        user.setId(currentUser.getId());
        boolean success = userService.updateUserInfo(user);
        if (success) {
            // 鏇存柊Session涓殑鐢ㄦ埛淇℃伅
            if (user.getUsername() != null) {
                currentUser.setUsername(user.getUsername());
            }
            if (user.getAvatar() != null) {
                currentUser.setAvatar(user.getAvatar());
            }
            request.getSession().setAttribute("user", currentUser);
            return R.createSuccess(true);
        } else {
            return R.createError("鏇存柊澶辫触");
        }
    }

    /**
     * 淇敼瀵嗙爜
     */
    @PostMapping("/changePassword")
    public R<String> changePassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            return R.createError(20001, "璇峰厛鐧诲綍");
        }
        
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        
        String result = userService.changePassword(currentUser.getId(), oldPassword, newPassword);
        if ("瀵嗙爜淇敼鎴愬姛".equals(result)) {
            return R.createSuccess(result);
        } else {
            return R.createError(result);
        }
    }
}

