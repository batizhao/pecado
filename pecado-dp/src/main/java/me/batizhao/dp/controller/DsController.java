package me.batizhao.dp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.dp.domain.Ds;
import me.batizhao.dp.service.DsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 数据源
 *
 * @module pecado-ims
 *
 * @author batizhao
 * @since 2020-10-19
 */
@Api(tags = "数据源管理")
@RestController
@Slf4j
@Validated
public class DsController {

    @Autowired
    private DsService dsService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param ds 数据源
     * @return ResponseInfo
     */
    @ApiOperation(value = "分页查询")
    @GetMapping("/dss")
    public ResponseInfo<IPage<Ds>> handleDss(Page<Ds> page, Ds ds) {
        return ResponseInfo.ok(dsService.findDss(page, ds));
    }

    /**
     * 查询所有
     * @return ResponseInfo
     */
    @ApiOperation(value = "查询所有")
    @GetMapping("/ds")
    public ResponseInfo<List<Ds>> handleDss() {
        return ResponseInfo.ok(dsService.list());
    }

    /**
     * 通过id查询数据源
     * @param id id
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id查询")
    @GetMapping("/ds/{id}")
    public ResponseInfo<Ds> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Integer id) {
        return ResponseInfo.ok(dsService.findById(id));
    }

    /**
     * 添加或修改数据源
     * @param ds 数据源
     * @return ResponseInfo
     */
    @ApiOperation(value = "添加或修改数据源")
    @PostMapping("/ds")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfo<Ds> handleSaveOrUpdate(@Valid @ApiParam(value = "数据源" , required = true) @RequestBody Ds ds) {
        return ResponseInfo.ok(dsService.saveOrUpdateDs(ds));
    }

    /**
     * 通过id删除数据源
     * @param ids ID串
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id删除数据源")
    @DeleteMapping("/ds")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfo<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return ResponseInfo.ok(dsService.removeByIds(ids));
    }

}
