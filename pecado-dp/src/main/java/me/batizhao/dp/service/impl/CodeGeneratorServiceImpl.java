package me.batizhao.dp.service.impl;

import cn.hutool.core.io.IoUtil;
import me.batizhao.dp.domain.GenConfig;
import me.batizhao.dp.mapper.CodeGeneratorMapper;
import me.batizhao.dp.service.CodeGeneratorService;
import me.batizhao.dp.util.CodeGenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author batizhao
 * @date 2020/10/10
 */
@Service
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

    @Autowired
    CodeGeneratorMapper codeGeneratorMapper;

    @Override
    public byte[] generateCode(GenConfig genConfig) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        String tableName = genConfig.getTableName();
        Map<String, String> table = codeGeneratorMapper.selectTable(tableName);
        List<Map<String, String>> columns = codeGeneratorMapper.selectColumns(tableName);
        CodeGenUtils.generatorCode(genConfig, table, columns, zip);

        IoUtil.close(zip);
        return outputStream.toByteArray();
    }
}