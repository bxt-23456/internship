package com.whlg.hospital.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whlg.hospital.util.JwtUtil;
import com.whlg.hospital.util.R;
import com.whlg.hospital.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT拦截器 - 验证token有效性
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

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

        // 验证token
        if (!jwtUtil.validateToken(token)) {
            writeError(response, StatusCode.TOKEN_EXPIRE, "登录已过期，请重新登录");
            return false;
        }

        // 将用户ID存入request attribute，方便后续Controller使用
        Long userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);

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
