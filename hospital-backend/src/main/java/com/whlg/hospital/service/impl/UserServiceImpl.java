package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.User;
import com.whlg.hospital.mapper.UserMapper;
import com.whlg.hospital.service.UserService;
import com.whlg.hospital.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> register(String phone, String password, String username) {
        Map<String, Object> result = new HashMap<>();

        // 检查手机号是否已注册
        LambdaQueryWrapper<User> phoneQuery = new LambdaQueryWrapper<>();
        phoneQuery.eq(User::getPhone, phone);
        if (this.count(phoneQuery) > 0) {
            result.put("success", false);
            result.put("message", "该手机号已注册");
            return result;
        }

        // 如果提供了用户名，检查用户名是否已存在
        if (username != null && !username.isEmpty()) {
            LambdaQueryWrapper<User> usernameQuery = new LambdaQueryWrapper<>();
            usernameQuery.eq(User::getUsername, username);
            if (this.count(usernameQuery) > 0) {
                result.put("success", false);
                result.put("message", "该用户名已被使用");
                return result;
            }
        }

        // 创建用户
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password); // 实际项目中应使用BCrypt等加密
        user.setUsername(username != null && !username.isEmpty() ? username : phone);
        user.setAvatar("img/default-avatar.png");
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        this.save(user);

        result.put("success", true);
        result.put("message", "注册成功");
        return result;
    }

    @Override
    public Map<String, Object> login(String phone, String password) {
        Map<String, Object> result = new HashMap<>();

        // 根据手机号查询用户
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getPhone, phone);
        User user = this.getOne(query);

        if (user == null) {
            result.put("success", false);
            result.put("message", "手机号或密码错误");
            return result;
        }

        // 验证密码
        if (!user.getPassword().equals(password)) {
            result.put("success", false);
            result.put("message", "手机号或密码错误");
            return result;
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            result.put("success", false);
            result.put("message", "账号已被禁用");
            return result;
        }

        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 构建返回的用户信息（不包含密码）
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("phone", user.getPhone());
        userInfo.put("email", user.getEmail());
        userInfo.put("realName", user.getRealName());
        userInfo.put("gender", user.getGender());
        userInfo.put("birthday", user.getBirthday());
        userInfo.put("avatar", user.getAvatar());

        result.put("success", true);
        result.put("message", "登录成功");
        result.put("token", token);
        result.put("userInfo", userInfo);
        return result;
    }

    @Override
    public User getUserInfo(Long userId) {
        User user = this.getById(userId);
        if (user != null) {
            user.setPassword(null); // 不返回密码
        }
        return user;
    }

    @Override
    public boolean updateUserInfo(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, user.getId());

        // 只更新非空字段
        if (user.getRealName() != null) {
            updateWrapper.set(User::getRealName, user.getRealName());
        }
        if (user.getGender() != null) {
            updateWrapper.set(User::getGender, user.getGender());
        }
        if (user.getBirthday() != null) {
            updateWrapper.set(User::getBirthday, user.getBirthday());
        }
        if (user.getEmail() != null) {
            updateWrapper.set(User::getEmail, user.getEmail());
        }
        if (user.getAvatar() != null) {
            updateWrapper.set(User::getAvatar, user.getAvatar());
        }

        updateWrapper.set(User::getUpdateTime, LocalDateTime.now());

        return this.update(updateWrapper);
    }

    @Override
    public String changePassword(Long userId, String oldPassword, String newPassword) {
        User user = this.getById(userId);
        if (user == null) {
            return "用户不存在";
        }

        // 验证旧密码
        if (!user.getPassword().equals(oldPassword)) {
            return "原密码不正确";
        }

        // 检查新密码是否与旧密码相同
        if (oldPassword.equals(newPassword)) {
            return "新密码不能与原密码相同";
        }

        // 更新密码
        user.setPassword(newPassword);
        user.setUpdateTime(LocalDateTime.now());
        this.updateById(user);

        return "密码修改成功";
    }
}
