package me.batizhao.dp.service.impl;

import cn.hutool.core.io.IoUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.dp.domain.GenConfig;
import me.batizhao.dp.mapper.CodeMapper;
import me.batizhao.dp.service.CodeService;
import me.batizhao.dp.util.CodeGenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.zip.ZipOutputStream;

/**
 * @author batizhao
 * @date 2020/10/10
 */
@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    CodeMapper codeMapper;

    @Override
    @DS("#last")
    public IPage<Map<String, String>> findTables(Page<Map<String, String>> page, String tableName, String dsName) {
        IPage<Map<String, String>> p = codeMapper.selectTableByDs(page, tableName);
        List<Map<String, String>> l = p.getRecords();

        List<Map<String, String>> newL = new ArrayList<>();
        for (Map<String, String> m : l) {
            Map<String, String> newM = new HashMap<>(m);
            newM.put("dsName", dsName);
            newL.add(newM);
        }

        p.setRecords(newL);

        return p;
    }

    @Override
    public byte[] generateCode(GenConfig genConfig) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        String tableName = genConfig.getTableName();
        Map<String, String> table = codeMapper.selectMetaByTableName(tableName, genConfig.getDsName());
        List<Map<String, String>> columns = codeMapper.selectColumnsByTableName(tableName, genConfig.getDsName());
        CodeGenUtils.generatorCode(genConfig, table, columns, zip);

        IoUtil.close(zip);
        return outputStream.toByteArray();
    }
}
