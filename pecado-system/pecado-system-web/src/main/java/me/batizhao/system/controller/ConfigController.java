package me.batizhao.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
import me.batizhao.system.api.domain.Config;
import me.batizhao.system.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 系统配置 API
 *
 * @module system
 *
 * @author batizhao
 * @since 2022-05-16
 */
@Tag(name = "系统配置管理")
@RestController
@Slf4j
@Validated
public class ConfigController {

    @Autowired
    private ConfigService configService;

    /**
     * 分页查询系统配置
     * @param page 分页对象
     * @param config 系统配置
     * @return R
     * @real_return R<Page<Config>>
     */
    @Operation(description = "分页查询系统配置")
    @GetMapping("/configs")
    @PreAuthorize("@pms.hasPermission('system:config:admin')")
    public R<IPage<Config>> handleConfigs(Page<Config> page, Config config) {
        return R.ok(configService.findConfigs(page, config));
    }

    /**
     * 查询系统配置
     * @return R<List<Config>>
     */
    @Operation(description = "查询系统配置")
    @GetMapping("/config")
    public R<List<Config>> handleConfigs(Config config) {
        return R.ok(configService.findConfigs(config));
    }

    /**
     * 通过id查询系统配置
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询系统配置")
    @GetMapping("/config/{id}")
    @PreAuthorize("@pms.hasPermission('system:config:admin')")
    public R<Config> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Integer id) {
        return R.ok(configService.findById(id));
    }

    /**
     * 添加或编辑系统配置
     * @param config 系统配置
     * @return R
     */
    @Operation(description = "添加或编辑系统配置")
    @PostMapping("/config")
    @PreAuthorize("@pms.hasPermission('system:config:admin')")
    public R<Config> handleSaveOrUpdate(@Valid @Parameter(name = "系统配置" , required = true) @RequestBody Config config) {
        return R.ok(configService.saveOrUpdateConfig(config));
    }

    /**
     * 通过id删除系统配置
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除系统配置")
    @DeleteMapping("/config")
    @PreAuthorize("@pms.hasPermission('system:config:admin')")
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(configService.removeByIds(ids));
    }

    /**
     * 更新系统配置状态
     *
     * @param config 系统配置
     * @return R
     */
    @Operation(description = "更新系统配置状态")
    @PostMapping("/config/status")
    @PreAuthorize("@pms.hasPermission('system:config:admin')")
    public R<Boolean> handleUpdateStatus(@Parameter(name = "系统配置" , required = true) @RequestBody Config config) {
        return R.ok(configService.updateStatus(config));
    }

}
