package me.batizhao.dp.service.impl;

import cn.hutool.core.io.IoUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.dp.domain.Code;
import me.batizhao.dp.domain.CodeMeta;
import me.batizhao.dp.mapper.CodeMapper;
import me.batizhao.dp.service.CodeMetaService;
import me.batizhao.dp.service.CodeService;
import me.batizhao.dp.util.CodeGenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * @author batizhao
 * @date 2020/10/10
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements CodeService {

    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private CodeMetaService codeMetaService;

    @Override
    public IPage<Code> findCodes(Page<Code> page, Code code) {
        LambdaQueryWrapper<Code> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(code.getTableName())) {
            wrapper.like(Code::getTableName, code.getTableName());
        }
        return codeMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Code> findCodes() {
        return codeMapper.selectList(null);
    }

    @Override
    public Code findById(Long id) {
        Code code = codeMapper.selectById(id);

        if (code == null) {
            throw new NotFoundException(String.format("没有该记录 '%s'。" , id));
        }

        return code;
    }

    @Override
    @Transactional
    public Code saveOrUpdateCode(Code code) {
        if (code.getId() == null) {
            code.setCreateTime(LocalDateTime.now());
            code.setUpdateTime(LocalDateTime.now());
            codeMapper.insert(code);
        } else {
            code.setUpdateTime(LocalDateTime.now());
            codeMapper.updateById(code);
        }

        return code;
    }

    @Override
    public Boolean deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
        ids.forEach(i -> codeMetaService.remove(Wrappers.<CodeMeta>lambdaQuery().eq(CodeMeta::getCodeId, i)));
        return true;
    }

    @Override
    @DS("#last")
    public IPage<Code> findTables(Page<Code> page, Code code, String dsName) {
        IPage<Code> p = codeMapper.selectTablePageByDs(page, code);
        List<Code> c = p.getRecords();

        if (StringUtils.isBlank(dsName)) {
            dsName = "master";
        }

        String finalDsName = dsName;
        c.forEach(ll -> ll.setDsName(finalDsName));

        return p;
    }

    @Override
    @Transactional
    public Boolean importTables(List<Code> codes) {
        if (codes == null) return true;
        for (Code c : codes) {
            CodeGenUtils.initData(c);
            Code code = saveOrUpdateCode(c);
            List<CodeMeta> codeMetas = codeMetaService.findColumnsByTableName(c.getTableName(), c.getDsName());
            codeMetas.forEach(p -> {
                p.setCodeId(code.getId());
                CodeGenUtils.initColumnField(p);
            });
            codeMetaService.saveBatch(codeMetas);
        }
        return true;
    }

    @Override
    public byte[] generateCode(List<Long> ids) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for(Long i : ids) {
            Code code = this.findById(i);
            List<CodeMeta> codeMetas = codeMetaService.findByCodeId(code.getId());
            CodeGenUtils.generatorCode(code, codeMetas, zip);
        }

        IoUtil.close(zip);
        return outputStream.toByteArray();
    }
}
