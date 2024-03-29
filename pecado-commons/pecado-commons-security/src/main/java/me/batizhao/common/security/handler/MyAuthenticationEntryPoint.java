package me.batizhao.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.util.R;
import me.batizhao.common.core.util.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义 401 返回数据格式
 *
 * 如果异常是 AuthenticationException，使用 AuthenticationEntryPoint 处理
 * 如果异常是 AccessDeniedException 且用户是匿名用户，使用 AuthenticationEntryPoint 处理
 *
 * 在 OAuth 开启后，如果用户携带一个过期 Token，使用 AuthenticationEntryPoint 处理
 *
 * 1. InvalidTokenException: Access token expired 测试用例：givenExpiredToken_whenGetSecureRequest_thenUnauthorized
 * 2. givenNoToken_whenGetSecureRequest_thenUnauthorized
 * 3. givenInvalidToken_whenGetSecureRequest_thenUnauthorized
 *
 * @author batizhao
 * @since 2020-03-02
 **/
@Component
@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        R<String> message = new R<String>(ResultEnum.PERMISSION_UNAUTHORIZED_ERROR.getCode())
                .setData(authException.getMessage());

        if(authException instanceof InsufficientAuthenticationException) {
            message = new R<String>(ResultEnum.OAUTH2_TOKEN_INVALID.getCode())
                    .setData(authException.getMessage());
        }

        log.error("Authentication Exception Handler for 401.", authException);
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }
}
