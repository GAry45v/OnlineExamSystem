package cn.edu.zjut.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("JwtAuthenticationFilter started...");

        String requestURI = request.getRequestURI();
        if ("/api/user/login".equals(requestURI) || "/api/user/register".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        // 如果 token 为空，返回相应的消息
        if (token == null || !token.startsWith("Bearer ")) {
            logger.info("Token is missing or malformed...");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 设置为 400 错误
            response.getWriter().write("Missing or malformed token");
            return;  // 终止请求处理
        }

        // 去掉 "Bearer " 部分
        token = token.substring(7);

        try {
            logger.info("Parsing JWT: " + token);
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey("javaxcryptospecSecretKeySpecfa779f14javaxcryptospecSecretKeySpecfa779f14javaxcryptospecSecretKeySpecfa779f14")  // 密钥
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            String userNumber = claims.getSubject();
            Integer userId = (Integer) claims.get("userId");
            Integer roleId = (Integer) claims.get("roleId");

            logger.info("JWT parsed successfully: userId=" + userId + ", roleId=" + roleId);

            JwtAuthenticationToken authentication = new JwtAuthenticationToken(userNumber, null, userId, roleId);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            logger.info("JWT parsing failed: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 返回 401 错误
            response.getWriter().write("Invalid or expired token");
            return;  // 终止请求处理
        }

        filterChain.doFilter(request, response);  // 继续执行请求
    }
}
