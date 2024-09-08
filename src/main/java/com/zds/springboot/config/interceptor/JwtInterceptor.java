package com.zds.springboot.config.interceptor;

import com.zds.springboot.service.IJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    @Qualifier("jJwtUtil")
    IJwtUtil iJwtUtil;

    List<String> whiteList = Arrays.asList(
            "/auth/login",
            "/user/login",
            "/error",
            // 记得删除（开发测试白名单）
            "/point/getTest",
            "/point/endpoint",
            "/point/get_messages",
            "/point/get_messagesById",
            "/point/*"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //放过不需要验证的页面。
        String uri = request.getRequestURI();
        if (whiteList.contains(uri)) {
            return true;
        }

        // 头部和参数都查看一下是否有token
        String token = request.getHeader("token");
        System.out.println(token);

        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
            if (StringUtils.isEmpty(token)) {
                response.setStatus(401);
                System.out.println("Status: 401");
                System.out.println("token 空的：" + token);
//                throw new RuntimeException("token是空的");
                return false;
            }
        }

        if (!iJwtUtil.verifyToken(token)) {
            response.setStatus(401);
            System.out.println("Status: 401 token 无效");
            System.out.println("token 无效：" + token);
            log.error("token无效");
            return false;
        }

        String userId = iJwtUtil.getUserIdByToken(token);
        log.info("userId:" + userId);
        String userName = iJwtUtil.getUserNameByToken(token);
        log.info("userName:" + userName);
        return true;
    }
}
