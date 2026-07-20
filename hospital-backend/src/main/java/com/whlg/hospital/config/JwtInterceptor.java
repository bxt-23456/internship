package com.whlg.hospital.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whlg.hospital.entity.User;
import com.whlg.hospital.util.JwtUtil;
import com.whlg.hospital.util.R;
import com.whlg.hospital.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * JWT拦截器 - 验证token有效性并检查Redis中的登录状态
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    /** Redis中用户token的key前缀 */
    private static final String USER_TOKEN_PREFIX = "user:token:";

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${user.token.expiration:60}")
    private Long tokenExpiration;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 获取token
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            writeError(response, StatusCode.TOKEN_EXPIRE, "未登录，请先登录");
            return false;
        }

        // 验证JWT token
        if (!jwtUtil.validateToken(token)) {
            writeError(response, StatusCode.TOKEN_EXPIRE, "登录已过期，请重新登录");
            return false;
        }

        // 从JWT中获取用户ID
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 检查Redis中的登录状态
        String redisKey = USER_TOKEN_PREFIX + userId;
        Object cachedToken = redisTemplate.opsForValue().get(redisKey);

        if (cachedToken == null) {
            // Redis中无状态，登录已过期
            writeError(response, StatusCode.TOKEN_EXPIRE, "登录状态已过期，请重新登录");
            return false;
        }

        // 比对token是否一致（防止旧token生效）
        if (!token.equals(cachedToken.toString())) {
            writeError(response, StatusCode.TOKEN_EXPIRE, "登录状态已失效，请重新登录");
            return false;
        }

        // 验证通过，重置Redis过期时间
        redisTemplate.expire(redisKey, tokenExpiration, TimeUnit.SECONDS);

        // 将用户ID存入request attribute，方便后续Controller使用
        request.setAttribute("userId", userId);

        // 兼容仍然依赖 Session 取登录用户的旧接口。
        // 前端现在主要通过 localStorage 携带 token，请求可能拿不到历史 JSESSIONID，
        // 因此这里在 token 验证通过后重建当前请求所需的用户会话信息。
        User currentUser = new User();
        currentUser.setId(userId);
        currentUser.setUsername(jwtUtil.getUsernameFromToken(token));
        request.getSession().setAttribute("user", currentUser);

        return true;
    }

    /**
     * 写入错误响应
     */
    private void writeError(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        R<String> result = R.createError(code, message);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(result));
    }
}
