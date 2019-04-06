package com.demo.xf.filters;

import com.demo.xf.exception.BizException;
import com.demo.xf.exception.CustomAccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: xiongfeng
 * Date: 2019/4/4 10:09
 * Description: 验证用户是否能访问指定资源的过滤器
 */
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    /**
     * 在拦截器中获取token并解析，拿到用户信息，
     * 放置到SecurityContextHolder，这样便完成了springsecurity和jwt的整合。
     */
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken token = getAuthentication(request);
        // 用户能全局访问的资源
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            Claims claims;
            try {
                 claims = Jwts.parser().
                        setSigningKey("MyJwtSecret11").
                        parseClaimsJws(token.replace("Bearer ", ""))
                        .getBody();
            }catch (MalformedJwtException e){
                throw  new CustomAccessDeniedException("授权失败!");
            }

            String user  = claims.getSubject();
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("role", List.class);
            List<SimpleGrantedAuthority> auth = roles.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, auth);
            }
            return null;
        }
        return null;
    }

}
