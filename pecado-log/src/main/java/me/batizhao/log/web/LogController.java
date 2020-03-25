package me.batizhao.log.web;

import io.swagger.annotations.ApiParam;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.feign.UserFeignService;
import me.batizhao.ims.api.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-24
 **/
@RestController
@RequestMapping("xx")
public class LogController {

    @Autowired
    private UserFeignService userFeignService;

    @GetMapping
    public ResponseInfo<List<RoleVO>> findRolesByUserId() {
        ResponseInfo<List<RoleVO>> roles = userFeignService.findRolesByUserId(1L);
        return roles;
    }
}
