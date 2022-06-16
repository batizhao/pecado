package me.batizhao.dp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
import me.batizhao.dp.domain.PageComponent;
import me.batizhao.dp.service.PageComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * <p> 组件模板管理 </p>
 *
 * @author wws
 * @since 2022-05-13 11:12
 */
@Tag(name = "组件模板管理")
@RestController
@Slf4j
@Validated
public class PageComponentController {

    @Autowired
    private PageComponentService pageComponentService;

    /**
     * 分页查询组件模板表
     * @param page 分页对象
     * @param pageComponent 组件模板表
     * @return R
     * @real_return R<Page<Components>>
     */
    @Operation(description = "分页查询组件模板表")
    @GetMapping("/page/components")
    @PreAuthorize("isAuthenticated()")
    public R<IPage<PageComponent>> handleAppTables(Page<PageComponent> page, PageComponent pageComponent) {
        return R.ok(pageComponentService.findComponentsTables(page, pageComponent));
    }

    /**
     * 组件模板表
     * @return R<List<Components>>
     */
    @Operation(description = "组件模板表")
    @GetMapping("/page/component")
    @PreAuthorize("isAuthenticated()")
    public R<List<PageComponent>> handleComponentsTable(PageComponent pageComponent) {
        return R.ok(pageComponentService.findComponentsTable(pageComponent));
    }

    /**
     * 通过id组件模板表
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询组件模板表")
    @GetMapping("/page/component/{id}")
    @PreAuthorize("isAuthenticated()")
    public R<PageComponent> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(pageComponentService.getById(id));
    }

    
    /**
     * 添加或编辑组件模板表
     * @param pageComponent 组件模板表
     * @return R
     */
    @Operation(description = "添加或编辑组件模板表")
    @PostMapping("/page/component")
    @PreAuthorize("isAuthenticated()")
    public R<PageComponent> handleSaveOrUpdate(@Valid @Parameter(name = "组件模板表" , required = true) @RequestBody PageComponent pageComponent) {
        return R.ok(pageComponentService.saveOrUpdateComponentsTable(pageComponent));
    }

    /**
     * 通过id删除组件模板表
     * @param id 组件id
     * @return R
     */
    @Operation(description = "通过id删除组件模板表")
    @DeleteMapping("/page/component")
    @PreAuthorize("isAuthenticated()")
    public R<Boolean> handleDelete(@Parameter(name = "id" , required = true) @RequestParam Long id) {
        return R.ok(pageComponentService.removeById(id));
    }
}
