package me.batizhao.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.app.domain.AppCommon;
import me.batizhao.app.service.AppCommonService;
import me.batizhao.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * <p> 常用应用控制层 </p>
 *
 * @author wws
 * @since 2022-05-16 19:05
 */
@Tag(name = "常用应用")
@RestController
@Slf4j
@Validated
public class AppCommonController {

    @Autowired
    private AppCommonService appCommonService;

    /**
     * 分页查询常用应用表
     * @param page 分页对象
     * @param appCommon 常用应用表
     * @return R
     * @real_return R<Page<AppCommon>>
     */
    @Operation(description = "分页查询常用应用表")
    @GetMapping("/commons")
    @PreAuthorize("isAuthenticated()")
    public R<IPage<AppCommon>> handleAppCommon(Page<AppCommon> page, AppCommon appCommon) {
        return R.ok(appCommonService.findAppCommon(page, appCommon));
    }

    /**
     * 常用应用表
     * @return R<List<AppCommon>>
     */
    @Operation(description = "查询常用应用表")
    @GetMapping("/common")
    @PreAuthorize("isAuthenticated()")
    public R<List<AppCommon>> handleAppCommon(AppCommon appCommon) {
        return R.ok(appCommonService.findAppCommon(appCommon));
    }

    /**
     * 通过id查询常用应用表
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询常用应用表")
    @GetMapping("/common/{id}")
    @PreAuthorize("isAuthenticated()")
    public R<AppCommon> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(appCommonService.getById(id));
    }

    /**
     * 添加或编辑常用应用表
     * @param appCommon 常用应用表
     * @return R
     */
    @Operation(description = "添加或编辑常用应用表")
    @PostMapping("/common")
    @PreAuthorize("isAuthenticated()")
    public R<AppCommon> handleSaveOrUpdate(@Valid @Parameter(name = "常用应用表" , required = true) @RequestBody AppCommon appCommon) {
        return R.ok(appCommonService.saveOrUpdateAppCommon(appCommon));
    }

    /**
     * 通过id删除常用应用表
     * @param id 常用应用Id
     * @return R
     */
    @Operation(description = "通过id删除常用应用表")
    @DeleteMapping("/common")
    @PreAuthorize("isAuthenticated()")
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam Long id) {
        return R.ok(appCommonService.removeById(id));
    }
}
