package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.User;

import java.util.Map;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {

    /**
     * 发送短信验证码
     * @param phone 手机号
     * @return 发送结果
     */
    Map<String, Object> sendVerifyCode(String phone);

    /**
     * 发送短信验证码（用于忘记密码）
     * @param phone 手机号
     * @return 发送结果
     */
    Map<String, Object> sendResetPasswordCode(String phone);

    /**
     * 用户注册
     * @param phone 手机号
     * @param password 密码
     * @param username 用户名（可选，默认使用手机号）
     * @param code 短信验证码
     * @return 注册结果
     */
    Map<String, Object> register(String phone, String password, String username, String code);

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
     * 更新用户个人信息（根据phone查询id，再根据id修改）
     * @param user 用户信息（需包含phone）
     * @return 更新结果消息，成功返回"更新成功"，失败返回原因
     */
    String updateUserInfo(User user);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果消息
     */
    String changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 退出登录，清除Redis中的登录状态
     * @param userId 用户ID
     */
    void logout(Long userId);

    /**
     * 重置密码（忘记密码功能）
     * @param phone 手机号
     * @param code 短信验证码
     * @param newPassword 新密码
     * @return 操作结果
     */
    Map<String, Object> resetPassword(String phone, String code, String newPassword);
}
