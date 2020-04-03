package me.batizhao.system.api.feign;

import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.api.feign.factory.SystemLogServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@FeignClient(value = "system", fallbackFactory = SystemLogServiceFallbackFactory.class)
public interface SystemLogFeignService {

    @PostMapping(value = "/log")
    ResponseInfo<Boolean> saveLog(LogDTO logDTO,
                                  @RequestHeader(SecurityConstants.FROM) String from);

}
