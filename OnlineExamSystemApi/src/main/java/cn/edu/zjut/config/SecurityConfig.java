package cn.edu.zjut.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/user/login", "/api/user/register").permitAll()  // 登录和注册接口可以不验证
                .anyRequest().authenticated()  // 其他请求需要认证
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");  // 未认证的请求返回 401
                });
//        http
//                .authorizeRequests()
//                .antMatchers("/api/user/login", "/api/user/register").permitAll()  // 登录和注册无需认证
//                .anyRequest().authenticated()  // 其他请求需要认证
//                .and();
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // 在用户名密码过滤器之后添加 JWT 过滤器
    }
}

//public ResponseResult<Map<String, Object>> getProfile(@AuthenticationPrincipal JwtAuthenticationToken authentication) {
//    // 从 authentication 中获取 userId 和 roleId
//    Integer userId = authentication.getUserId();
//    Integer roleId = authentication.getRoleId();

//JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//
//    Integer userId = authentication.getUserId();
//    Integer roleId = authentication.getRoleId();