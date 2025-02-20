package cn.edu.zjut.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
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
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/user/login", "/api/user/register","/demo").permitAll()  // 登录和注册无需认证
                .and()
//                .addFilterBefore(jwtAuthenticationFilter, FilterSecurityInterceptor.class);
                .addFilterBefore(new OptionsRequestFilter(), FilterSecurityInterceptor.class); // 添加 OPTIONS 请求过滤器;
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