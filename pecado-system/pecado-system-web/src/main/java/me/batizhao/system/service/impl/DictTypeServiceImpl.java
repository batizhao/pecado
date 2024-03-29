package me.batizhao.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.system.api.domain.DictData;
import me.batizhao.system.api.domain.DictType;
import me.batizhao.system.mapper.DictTypeMapper;
import me.batizhao.system.service.DictDataService;
import me.batizhao.system.service.DictTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典类型接口实现类
 *
 * @author batizhao
 * @since 2021-02-07
 */
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    @Autowired
    private DictTypeMapper dictTypeMapper;
    @Autowired
    private DictDataService dictDataService;

    @Override
    public IPage<DictType> findDictTypes(Page<DictType> page, DictType dictType) {
        LambdaQueryWrapper<DictType> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(dictType.getName())) {
            wrapper.like(DictType::getName, dictType.getName());
        }
        return dictTypeMapper.selectPage(page, wrapper);
    }

    @Override
    public DictType findById(Long id) {
        DictType dictType = dictTypeMapper.selectById(id);

        if (dictType == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return dictType;
    }

    @Override
    @Transactional
    public DictType saveOrUpdateDictType(DictType dictType) {
        if (dictType.getId() == null) {
            dictType.setCreateTime(LocalDateTime.now());
            dictType.setUpdateTime(LocalDateTime.now());
            dictTypeMapper.insert(dictType);
        } else {
            dictType.setUpdateTime(LocalDateTime.now());
            dictTypeMapper.updateById(dictType);
        }

        return dictType;
    }

    @Override
    @Transactional
    public Boolean deleteByIds(List<String> codes) {
        codes.forEach(i -> {
            this.remove(Wrappers.<DictType>lambdaQuery().eq(DictType::getCode, i));
            dictDataService.remove(Wrappers.<DictData>lambdaQuery().eq(DictData::getCode, i));
        });
        return true;
    }

    @Override
    @Transactional
    public Boolean updateStatus(DictType dictType) {
        LambdaUpdateWrapper<DictType> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(DictType::getId, dictType.getId()).set(DictType::getStatus, dictType.getStatus());
        return dictTypeMapper.update(null, wrapper) == 1;
    }
}
