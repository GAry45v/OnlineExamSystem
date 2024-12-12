package cn.edu.zjut.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        logger.debug("Request URI: {}"+requestURI);

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // 去掉 "Bearer " 部分

            try {
                Jws<Claims> claimsJws = Jwts.parser()
                        .setSigningKey("javaxcryptospecSecretKeySpecfa779f14javaxcryptospecSecretKeySpecfa779f14javaxcryptospecSecretKeySpecfa779f14")  // 密钥
                        .parseClaimsJws(token);

                Claims claims = claimsJws.getBody();
                String userNumber = claims.getSubject();
                Integer userId = (Integer) claims.get("userId");
                Integer roleId = (Integer) claims.get("roleId");

                // 创建 JwtAuthenticationToken 对象
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(userNumber, null, userId, roleId);

                // 存入 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        }

        filterChain.doFilter(request, response);  // 继续执行请求
    }
}
