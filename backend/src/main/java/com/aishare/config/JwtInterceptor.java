package com.aishare.config;

import com.aishare.common.BusinessException;
import com.aishare.util.JwtUtil;
import com.aishare.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            // GET 请求允许不登录，POST/PUT/DELETE 必须登录
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                return true;
            }
            throw new BusinessException(401, "未登录，请先登录");
        }
        try {
            Long userId = jwtUtil.getUserId(token.substring(7));
            UserContext.set(userId);
            return true;
        } catch (Exception e) {
            throw new BusinessException(401, "token无效或已过期");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
