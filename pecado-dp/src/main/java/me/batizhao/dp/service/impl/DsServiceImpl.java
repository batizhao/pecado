package me.batizhao.dp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.exception.DataSourceException;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.util.SpringContextHolder;
import me.batizhao.common.datasource.component.DataSourceConstants;
import me.batizhao.dp.domain.Ds;
import me.batizhao.dp.mapper.DsMapper;
import me.batizhao.dp.service.DsService;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * 数据源
 *
 * @author batizhao
 * @since 2020-10-19
 */
@Service
@Slf4j
public class DsServiceImpl extends ServiceImpl<DsMapper, Ds> implements DsService {

    @Autowired
    private DsMapper dsMapper;

    @Autowired
    private StringEncryptor stringEncryptor;

    @Autowired
    private DataSourceCreator dataSourceCreator;

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
    @Transactional
    public Ds saveOrUpdateDs(Ds ds) {
        checkDataSource(ds);

        if (ds.getId() == null) {
            // 添加动态数据源
            addDynamicDataSource(ds);

            ds.setCreatedTime(LocalDateTime.now());
            ds.setUpdatedTime(LocalDateTime.now());
            ds.setPassword(stringEncryptor.encrypt(ds.getPassword()));
            dsMapper.insert(ds);
        } else {
            // 先移除
            SpringContextHolder.getBean(DynamicRoutingDataSource.class)
                    .removeDataSource(findById(ds.getId()).getName());

            // 再添加
            addDynamicDataSource(ds);

            ds.setUpdatedTime(LocalDateTime.now());
            if (StrUtil.isNotBlank(ds.getPassword())) {
                ds.setPassword(stringEncryptor.encrypt(ds.getPassword()));
            }
            dsMapper.updateById(ds);
        }

        return ds;
    }

    public void addDynamicDataSource(Ds ds) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setPoolName(ds.getName());
        dataSourceProperty.setUrl(ds.getUrl());
        dataSourceProperty.setUsername(ds.getUsername());
        dataSourceProperty.setPassword(ds.getPassword());
        dataSourceProperty.setDriverClassName(DataSourceConstants.DS_DRIVER);
        DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        SpringContextHolder.getBean(DynamicRoutingDataSource.class).addDataSource(dataSourceProperty.getPoolName(),
                dataSource);
    }

    public Boolean checkDataSource(Ds ds) {
        try {
            DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
        }
        catch (SQLException e) {
            log.error("数据源配置 {} , 获取链接失败", ds.getName(), e);
            throw new DataSourceException("获取链接失败", e);
        }
        return Boolean.TRUE;
    }

}
