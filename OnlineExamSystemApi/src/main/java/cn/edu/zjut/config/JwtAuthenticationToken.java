package cn.edu.zjut.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Object credentials;
    private final Integer userId;
    private final Integer roleId;

    public JwtAuthenticationToken(Object principal, Object credentials, Integer userId, Integer roleId) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.userId = userId;
        this.roleId = roleId;
        setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getRoleId() {
        return this.roleId;
    }
}

