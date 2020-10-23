package me.batizhao.dp.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.dp.domain.GenConfig;
import me.batizhao.dp.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 生成代码
 * 调用这个类根据 table 生成前后端代码
 *
 * @author batizhao
 * @date 2020/10/10
 */
@Api(tags = "生成代码")
@RestController
public class CodeController {

    @Autowired
    CodeService codeService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param dsName 数据源
     * @return ResponseInfo
     */
    @ApiOperation(value = "分页查询")
    @GetMapping("/codes")
    public ResponseInfo handleCodes(Page page, String tableName, String dsName) {
        return ResponseInfo.ok(codeService.findTables(page, tableName, dsName));
    }

    @SneakyThrows
    @ApiOperation(value = "生成代码")
    @PostMapping(value = "code")
    public void handleGenerateCode(@RequestBody GenConfig genConfig, HttpServletResponse response) {
        byte[] data = codeService.generateCode(genConfig);
        response.reset();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=%s.zip", genConfig.getTableName()));
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), true, data);
    }
}
