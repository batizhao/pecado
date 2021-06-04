package me.batizhao.ims.api.feign;

import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.R;
import me.batizhao.ims.api.domain.Role;
import me.batizhao.ims.api.feign.factory.ImsServiceFallbackFactory;
import me.batizhao.ims.api.vo.UserInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-13
 **/
@FeignClient(value = "ims", fallbackFactory = ImsServiceFallbackFactory.class)
public interface ImsFeignService {

    @GetMapping(value = "/user", params = "username")
    R<UserInfoVO> loadUserByUsername(@RequestParam("username") String username,
                                     @RequestHeader(SecurityConstants.FROM) String from);

    @GetMapping(value = "/role", params = "userId")
    R<List<Role>> findRolesByUserId(@RequestParam("userId") Long userId);

}
