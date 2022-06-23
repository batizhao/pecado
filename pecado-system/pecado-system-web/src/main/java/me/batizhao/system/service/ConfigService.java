package me.batizhao.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.system.api.domain.Config;

import java.util.List;

/**
 * 系统配置接口类
 *
 * @author batizhao
 * @since 2022-05-16
 */
public interface ConfigService extends IService<Config> {

    /**
     * 分页查询系统配置
     * @param page 分页对象
     * @param config 系统配置
     * @return IPage<Config>
     */
    IPage<Config> findConfigs(Page<Config> page, Config config);

    /**
     * 查询系统配置
     * @param config
     * @return List<Config>
     */
    List<Config> findConfigs(Config config);

    /**
     * 通过id查询系统配置
     * @param id id
     * @return Config
     */
    Config findById(Integer id);

    /**
     * 添加或编辑系统配置
     * @param config 系统配置
     * @return Config
     */
    Config saveOrUpdateConfig(Config config);

    /**
     * 更新系统配置状态
     * @param config 系统配置
     * @return Boolean
     */
    Boolean updateStatus(Config config);

}
