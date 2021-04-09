package me.batizhao.system.api.feign;

import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.R;
import me.batizhao.system.api.domain.Log;
import me.batizhao.system.api.feign.factory.SystemLogServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@FeignClient(value = "system", fallbackFactory = SystemLogServiceFallbackFactory.class)
public interface SystemLogFeignService {

    @PostMapping(value = "/log")
    R<Boolean> saveLog(Log log,
                       @RequestHeader(SecurityConstants.FROM) String from);

}
