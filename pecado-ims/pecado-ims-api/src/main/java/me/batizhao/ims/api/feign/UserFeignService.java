package me.batizhao.ims.api.feign;

import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.feign.factory.UserServiceFallbackFactory;
import me.batizhao.ims.api.vo.UserInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author batizhao
 * @since 2020-03-13
 **/
@FeignClient(value = "ims", fallbackFactory = UserServiceFallbackFactory.class)
public interface UserFeignService {

    @GetMapping(value = "/user", params = "username")
    ResponseInfo<UserInfoVO> loadUserByUsername(@RequestParam("username") String username,
                                                @RequestHeader(SecurityConstants.FROM) String from);

//    @GetMapping(value = "/role", params = "userId")
//    ResponseInfo<List<Role>> findRolesByUserId(@RequestParam("userId") Long userId);

}
