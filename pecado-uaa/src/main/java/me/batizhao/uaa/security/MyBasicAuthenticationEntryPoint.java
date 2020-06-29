package me.batizhao.uaa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.core.util.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * curl -i -X POST --user 'client_app:123456xx' localhost:4000/oauth/token\?grant_type=password\&username=admin\&password=123456
 * 当提交一个错误的 basic header 时，会触发这个异常
 *
 * @author batizhao
 * @since 2020-03-19
 **/
@Component
@Slf4j
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ResponseInfo<String> message = new ResponseInfo<String>().setCode(ResultEnum.PERMISSION_UNAUTHORIZED_ERROR.getCode())
                .setMessage(ResultEnum.PERMISSION_UNAUTHORIZED_ERROR.getMessage())
                .setData(authException.getMessage());

        log.error("Basic Authentication Exception Handler for 401.", authException);
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName(SecurityConstants.REALM_NAME);
        super.afterPropertiesSet();
    }
}
