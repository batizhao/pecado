package me.batizhao.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
import me.batizhao.system.api.annotation.SystemLog;
import me.batizhao.system.api.domain.DictType;
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
     * @return R
     */
    @ApiOperation(value = "分页查询字典类型")
    @GetMapping("/dict/types")
    @PreAuthorize("@pms.hasPermission('system:dict:admin')")
    @SystemLog
    public R<IPage<DictType>> handleDictTypes(Page<DictType> page, DictType dictType) {
        return R.ok(dictTypeService.findDictTypes(page, dictType));
    }

    /**
     * 查询所有
     * @return R
     */
    @ApiOperation(value = "查询所有字典类型")
    @GetMapping("/dict/type")
    @SystemLog
    public R<List<DictType>> handleDictType() {
        return R.ok(dictTypeService.list());
    }

    /**
     * 通过id查询字典类型
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询字典类型")
    @GetMapping("/dict/type/{id}")
    @SystemLog
    public R<DictType> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        DictType dictType = dictTypeService.findById(id);
//        dictType.setDictDataList(dictDataService.list(Wrappers.<DictData>lambdaQuery().eq(DictData::getCode, dictType.getCode())));
        return R.ok(dictType);
    }

    /**
     * 添加或修改字典类型
     * @param dictType 字典类型
     * @return R
     */
    @ApiOperation(value = "添加或修改字典类型")
    @PostMapping("/dict/type")
    @PreAuthorize("@pms.hasPermission('system:dict:add') or @pms.hasPermission('system:dict:edit')")
    @SystemLog
    public R<DictType> handleSaveOrUpdate(@Valid @ApiParam(value = "字典类型" , required = true) @RequestBody DictType dictType) {
        return R.ok(dictTypeService.saveOrUpdateDictType(dictType));
    }

    /**
     * 通过id删除字典类型
     * @param codes code串
     * @return R
     */
    @ApiOperation(value = "通过id删除字典类型")
    @DeleteMapping("/dict/type")
    @PreAuthorize("@pms.hasPermission('system:dict:delete')")
    @SystemLog
    public R<Boolean> handleDelete(@ApiParam(value = "code串" , required = true) @RequestParam List<String> codes) {
        return R.ok(dictTypeService.deleteByIds(codes));
    }

    /**
     * 更新字典类型状态
     *
     * @param dictType 字典类型
     * @return R
     */
    @ApiOperation(value = "更新字典类型状态")
    @PostMapping("/dict/type/status")
    @PreAuthorize("@pms.hasPermission('system:dict:admin')")
    @SystemLog
    public R<Boolean> handleUpdateStatus(@ApiParam(value = "字典类型" , required = true) @RequestBody DictType dictType) {
        return R.ok(dictTypeService.updateStatus(dictType));
    }

}
