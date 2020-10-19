package me.batizhao.dp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.dp.domain.Ds;
import me.batizhao.dp.mapper.DsMapper;
import me.batizhao.dp.service.DsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 数据源
 *
 * @author batizhao
 * @since 2020-10-19
 */
@Service
public class DsServiceImpl extends ServiceImpl<DsMapper, Ds> implements DsService {

    @Autowired
    private DsMapper dsMapper;

    @Override
    public IPage<Ds> findDss(Page<Ds> page, Ds ds) {
        return dsMapper.selectDsPage(page, ds);
    }

    @Override
    public Ds findById(Integer id) {
        Ds ds = dsMapper.selectById(id);

        if(ds == null) {
            throw new NotFoundException(String.format("没有该记录 '%s'。", id));
        }

        return ds;
    }

    @Override
    public Ds saveOrUpdateDs(Ds ds) {
        if (ds.getId() == null) {
            ds.setCreatedTime(LocalDateTime.now());
            ds.setUpdatedTime(LocalDateTime.now());
            dsMapper.insert(ds);
        } else {
            ds.setUpdatedTime(LocalDateTime.now());
            dsMapper.updateById(ds);
        }

        return ds;
    }

}
