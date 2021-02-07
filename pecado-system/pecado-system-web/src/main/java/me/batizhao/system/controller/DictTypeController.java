package me.batizhao.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.system.domain.DictType;
import me.batizhao.system.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 字典类型 API
 *
 * @module system
 *
 * @author batizhao
 * @since 2021-02-07
 */
@Api(tags = "字典类型管理")
@RestController
@Slf4j
@Validated
public class DictTypeController {

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param dictType 字典类型
     * @return ResponseInfo
     */
    @ApiOperation(value = "分页查询")
    @GetMapping("/dict/types")
    public ResponseInfo<IPage<DictType>> handleDictTypes(Page<DictType> page, DictType dictType) {
        return ResponseInfo.ok(dictTypeService.findDictTypes(page, dictType));
    }


    /**
     * 通过id查询字典类型
     * @param id id
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id查询")
    @GetMapping("/dict/type/{id}")
    public ResponseInfo<DictType> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return ResponseInfo.ok(dictTypeService.findById(id));
    }

    /**
     * 添加或修改字典类型
     * @param dictType 字典类型
     * @return ResponseInfo
     */
    @ApiOperation(value = "添加或修改字典类型")
    @PostMapping("/dict/type")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfo<DictType> handleSaveOrUpdate(@Valid @ApiParam(value = "字典类型" , required = true) @RequestBody DictType dictType) {
        return ResponseInfo.ok(dictTypeService.saveOrUpdateDictType(dictType));
    }

    /**
     * 通过id删除字典类型
     * @param ids ID串
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id删除字典类型")
    @DeleteMapping("/dict/type")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfo<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return ResponseInfo.ok(dictTypeService.removeByIds(ids));
    }

}
