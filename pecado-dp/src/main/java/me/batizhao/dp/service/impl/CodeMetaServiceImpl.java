package me.batizhao.dp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.dp.domain.CodeMeta;
import me.batizhao.dp.mapper.CodeMetaMapper;
import me.batizhao.dp.service.CodeMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 生成代码元数据表
 *
 * @author batizhao
 * @since 2021-02-01
 */
@Service
public class CodeMetaServiceImpl extends ServiceImpl<CodeMetaMapper, CodeMeta> implements CodeMetaService {

    @Autowired
    private CodeMetaMapper codeMetaMapper;

//    @Override
//    public IPage<CodeMeta> findCodeMetas(Page<CodeMeta> page, CodeMeta codeMeta) {
//        return codeMetaMapper.selectCodeMetaPage(page, codeMeta);
//    }

//    @Override
//    public CodeMeta findById(Long id) {
//        CodeMeta codeMeta = codeMetaMapper.selectById(id);
//
//        if(codeMeta == null) {
//            throw new NotFoundException(String.format("没有该记录 '%s'。", id));
//        }
//
//        return codeMeta;
//    }

    @Override
    public List<CodeMeta> findByCodeId(Long codeId) {
        return codeMetaMapper.selectList(Wrappers.<CodeMeta>query().lambda().eq(CodeMeta::getCodeId, codeId));
    }

//    @Override
//    @Transactional
//    public CodeMeta saveOrUpdateCodeMeta(CodeMeta codeMeta) {
//        if (codeMeta.getId() == null) {
//            codeMeta.setCreateTime(LocalDateTime.now());
//            codeMeta.setUpdateTime(LocalDateTime.now());
//            codeMetaMapper.insert(codeMeta);
//        } else {
//            codeMeta.setUpdateTime(LocalDateTime.now());
//            codeMetaMapper.updateById(codeMeta);
//        }
//
//        return codeMeta;
//    }

    @Override
    public List<CodeMeta> findColumnsByTableName(String tableName, String dsName) {
        return codeMetaMapper.selectColumnsByTableName(tableName, dsName);
    }
}
