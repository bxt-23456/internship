package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.User;

import java.util.Map;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param phone 手机号
     * @param password 密码
     * @param username 用户名（可选，默认使用手机号）
     * @return 注册结果
     */
    Map<String, Object> register(String phone, String password, String username);

    /**
     * 用户登录
     * @param phone 手机号
     * @param password 密码
     * @return 登录结果（包含token和用户信息）
     */
    Map<String, Object> login(String phone, String password);

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserInfo(Long userId);

    /**
     * 更新用户个人信息
     * @param user 用户信息
     * @return 更新结果
     */
    boolean updateUserInfo(User user);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果消息
     */
    String changePassword(Long userId, String oldPassword, String newPassword);
}
