package com.demo.xf.filters;

import com.demo.xf.config.WebSecurityConfig;
import com.demo.xf.exception.BizException;
import com.demo.xf.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Author: xiongfeng
 * Date: 2019/4/4 09:47
 * Description:
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String LOGIN = "/login";
    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(LOGIN, HttpMethod.POST.name()));
        this.authenticationManager = authenticationManager;
        this.setAuthenticationSuccessHandler(new AjaxAuthenticationSuccessHandler());
        this.setAuthenticationFailureHandler(new AjaxAuthenticationFailureHandler());
    }

    /**
     * 在attemptAuthentication方法中，
     * 主要是进行username和password请求值的获取，
     * 然后再生成一个UsernamePasswordAuthenticationToken 对象，
     * 进行进一步的验证。
     */
    // 登录请求认证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        // 从request中获取参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // 生成Token
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        // 进一步验证
        return authenticationManager.authenticate(authenticationToken);
    }

    static class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {
            Claims claims = Jwts.claims();
            claims.put("role", authentication.getAuthorities().stream().map(item ->
                    item.getAuthority()).collect(Collectors.toList()));
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(authentication.getName())
                    .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*24))
                    .signWith(SignatureAlgorithm.HS512, "MyJwtSecret11").compact();
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            String string = "{\"token\":\""+token+"\"}";
            PrintWriter writer;
            try {
                writer = response.getWriter();
                writer.print(string);
                writer.flush();
                writer.close();
            }catch (IOException e){
                throw new BizException(500, "回送授权码失败");
            }
        }
    }

    static class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler{

        @Override
        public void onAuthenticationFailure(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException authentication)
                throws IOException, ServletException {
            ObjectMapper objectMapper = new ObjectMapper();
            SecurityContextHolder.clearContext();
            //设置状态码
            response.setStatus(500);
            Result result = new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), authentication.getMessage());
            WebSecurityConfig.output(result, response);
        }
    }
   /* @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) {
        Claims claims = Jwts.claims();
        claims.put("role", authentication.getAuthorities().stream().map(item ->
            item.getAuthority()).collect(Collectors.toList()));
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(authentication.getName())
                .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*24))
                .signWith(SignatureAlgorithm.HS512, "MyJwtSecret11").compact();
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String string = "{\"token\":\""+token+"\"}";
        PrintWriter writer;
        try {
            writer = response.getWriter();
            writer.print(string);
            writer.flush();
            writer.close();
        }catch (IOException e){
            throw new BizException(500, "回送授权码失败");
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        SecurityContextHolder.clearContext();
        //设置状态码
        response.setStatus(500);
        Result result = new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), failed.getMessage());
        WebSecurityConfig.output(result, response);
    }
*/
}
