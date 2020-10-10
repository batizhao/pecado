package me.batizhao.codegen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.batizhao.codegen.service.GeneratorService;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.security.annotation.Inner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;

/**
 * 生成代码
 * 调用这个类根据 table 生成前后端代码
 *
 * @author batizhao
 * @date 2020/10/10
 */
@Api(tags = "生成代码")
@RestController
public class GeneratorController {

    @Autowired
    GeneratorService generatorService;

    @ApiOperation(value = "生成代码")
    @PostMapping(value = "generator", params = "name")
    @Inner
    public ResponseInfo<Boolean> handleUsername(@ApiParam(value = "用户名", required = true) @RequestParam @Size(min = 3) String name) {
        generatorService.generateCode(name);

        return ResponseInfo.ok(true);
    }
}
