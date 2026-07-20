package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.User;
import com.whlg.hospital.mapper.UserMapper;
import com.whlg.hospital.service.UserService;
import com.whlg.hospital.util.AliyunSmsUtil;
import com.whlg.hospital.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AliyunSmsUtil aliyunSmsUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /** Redis中验证码的key前缀 */
    private static final String SMS_CODE_PREFIX = "sms:code:";

    /** Redis中用户token的key前缀 */
    private static final String USER_TOKEN_PREFIX = "user:token:";

    @Value("${user.token.expiration:60}")
    private Long tokenExpiration;

    /** 短信签名 */
    private static final String SMS_SIGN_NAME = "速通互联验证码";

    /** 短信模板CODE */
    private static final String SMS_TEMPLATE_CODE = "100001";

    @Override
    public Map<String, Object> sendVerifyCode(String phone) {
        Map<String, Object> result = new HashMap<>();

        // 检查手机号是否已注册
        LambdaQueryWrapper<User> phoneQuery = new LambdaQueryWrapper<>();
        phoneQuery.eq(User::getPhone, phone);
        if (this.count(phoneQuery) > 0) {
            result.put("success", false);
            result.put("message", "该手机号已注册");
            return result;
        }

        try {
            // 生成6位验证码
            String code = AliyunSmsUtil.generateVerificationCode();

            // 存入Redis，5分钟过期（先存储，确保验证码可用）
            String redisKey = SMS_CODE_PREFIX + phone;
            redisTemplate.opsForValue().set(redisKey, code, 5, TimeUnit.MINUTES);

            // 发送短信
            try {
                String templateParam = "{\"code\":\"" + code + "\",\"min\":\"5\"}";
                aliyunSmsUtil.sendSmsVerifyCode(phone, SMS_SIGN_NAME, SMS_TEMPLATE_CODE, templateParam);
            } catch (Exception smsException) {
                // 短信发送失败不影响验证码存储，打印日志继续
                System.err.println("短信发送异常（验证码已存入Redis）: " + smsException.getMessage());
            }

            result.put("success", true);
            result.put("message", "验证码已发送到手机，请注意查收");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "验证码发送失败，请稍后重试");
        }

        return result;
    }

    @Override
    public Map<String, Object> register(String phone, String password, String username, String code) {
        Map<String, Object> result = new HashMap<>();

        // 校验验证码
        String redisKey = SMS_CODE_PREFIX + phone;
        Object cachedCode = redisTemplate.opsForValue().get(redisKey);
        if (cachedCode == null) {
            result.put("success", false);
            result.put("message", "验证码已过期，请重新获取");
            return result;
        }
        if (!cachedCode.toString().equals(code)) {
            result.put("success", false);
            result.put("message", "验证码错误");
            return result;
        }

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

        // 注册成功，删除已使用的验证码
        redisTemplate.delete(redisKey);

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

        // 将token存入Redis，记录用户登录状态
        String redisKey = USER_TOKEN_PREFIX + user.getId();
        redisTemplate.opsForValue().set(redisKey, token, tokenExpiration, TimeUnit.SECONDS);

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
    public String updateUserInfo(User user) {
        // 根据phone查询id
        String phone = user.getPhone();
        if (phone == null || phone.isEmpty()) {
            return "手机号不能为空";
        }

        LambdaQueryWrapper<User> phoneQuery = new LambdaQueryWrapper<>();
        phoneQuery.eq(User::getPhone, phone);
        User dbUser = this.getOne(phoneQuery);
        if (dbUser == null) {
            return "用户不存在";
        }

        Long userId = dbUser.getId();

        // 如果要修改用户名，检查新用户名是否已被其他用户使用
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            if (!user.getUsername().equals(dbUser.getUsername())) {
                LambdaQueryWrapper<User> usernameQuery = new LambdaQueryWrapper<>();
                usernameQuery.eq(User::getUsername, user.getUsername());
                usernameQuery.ne(User::getId, userId);
                if (this.count(usernameQuery) > 0) {
                    return "该用户名已被使用";
                }
            }
        }

        // 根据id更新非空字段
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId);

        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            updateWrapper.set(User::getUsername, user.getUsername());
        }
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

        updateWrapper.set(User::getUpdateTime, LocalDateTime.now());

        boolean success = this.update(updateWrapper);
        return success ? "更新成功" : "更新失败";
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

    @Override
    public void logout(Long userId) {
        // 删除Redis中的登录状态
        String redisKey = USER_TOKEN_PREFIX + userId;
        redisTemplate.delete(redisKey);
    }
}
