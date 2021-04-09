package me.batizhao.common.sentinel.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
import me.batizhao.common.core.util.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义 Sentinel 异常处理
 *
 * @author batizhao
 * @since 2020-04-12
 **/
@Slf4j
public class PecadoBlockExceptionHandler implements BlockExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

        R<String> message = new R<String>().setCode(ResultEnum.TOO_MANY_REQUEST.getCode())
                .setMessage(ResultEnum.TOO_MANY_REQUEST.getMessage())
                .setData(e.getMessage());

        log.error("Sentinel Block Exception", e);
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }

}
