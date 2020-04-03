package me.batizhao.system.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.security.annotation.Inner;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.domain.Log;
import me.batizhao.system.service.LogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 日志管理
 * 使用 AOP 实现 API 日志记录
 *
 * @module pecado-system
 *
 * @author batizhao
 * @since 2020-03-24
 **/
@Api(tags = "日志管理")
@RestController
@RequestMapping("log")
@Validated
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 插入日志
     *
     * @param logDto 日志实体
     * @return true/false
     */
    @ApiOperation(value = "插入日志")
    @PostMapping
    @Inner
    public ResponseInfo<Boolean> save(@Valid @RequestBody LogDTO logDto) {
        Log log = new Log();
        BeanUtils.copyProperties(logDto, log);
        return ResponseInfo.ok(logService.save(log));
    }
}
